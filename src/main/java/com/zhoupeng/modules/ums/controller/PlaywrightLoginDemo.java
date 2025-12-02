package com.zhoupeng.modules.ums.controller;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;


/**
 * @author ZhouPP
 * create_time 2025/12/1:9:09
 */
public class PlaywrightLoginDemo {
    public static void main(String[] args) {
        // 1. 启动 Playwright
        try (Playwright playwright = Playwright.create()) {
            // 2. 启动浏览器（带界面，方便你看）
            Browser browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions().setHeadless(false));

            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            // 3. 打开登录页
            page.navigate("http://10.126.1.187/#/login?sdlkjLKJASD12USHDss12=asdjk123102");

            // 4. 点击 “账号密码登录” tab
            // 根据你给的 HTML：role="tab"，文本=账号密码登录
            page.getByRole(AriaRole.TAB,
                    new Page.GetByRoleOptions().setName("账号密码登录")
            ).click();

            // 5. 填写用户名和密码
            // 你之前就确认过 id 是 form_item_username / form_item_password
            page.locator("#form_item_username").fill("admin");
            page.locator("#form_item_password").fill("123456");

            // 6. 点击 “登 录” 按钮
            // 你的 HTML 是：<button ... class="ant-btn ant-btn-primary"><span>登 录</span></button>
            // 方式 A：按按钮文本
            page.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("登 录")
            ).click();

            // 也可以用 CSS 定位：
            // page.locator("button.ant-btn.ant-btn-primary").click();

            // 7. 等几秒看登录结果
            page.waitForTimeout(1000);

            page.getByRole(AriaRole.MENUITEM,
                    new Page.GetByRoleOptions().setName("需求管理")
            ).click();

            page.getByText("功能清单").click();

            page.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("新 增")
            ).click();
            createFunction(page);
            page.waitForTimeout(5000);
            browser.close();
        }
    }
    static void createFunction(Page page) {
        // 这里假设“新建”弹窗已经打开（你前面点了“新增”按钮）

        // 1. 功能归属（例如：根功能A / 子功能B）
        // 如果只有一层，就传一个；多级就传多个
        selectFunctionBelong(page, "动力域", "SADASDAD");

        // 2. 功能名称
        page.fill("#form_item_name", "自动化功能003");

        // 3. 功能英文名
        page.fill("#form_item_nameEn", "auto_function_003");

        // 4. 负责人（搜索下拉）
        // keyword 用来过滤列表，optionText 是下拉里真实显示的名字
        selectOwner(page, "宋波", "宋波(songbo)");

        // 5. 描述
        page.fill("#form_item_description", "这是通过 Playwright 自动创建的功能清单");

        // 6. 开发状态（根据你实际下拉里的文字改，比如：开发中 / 已完成 / 冻结 等）
        selectDevStatus(page, "develop");

        // 7. 点击【确 认】按钮提交
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("确 认")).click();

        // 等一下保存完成（可以换成等待某个成功提示）
        page.waitForTimeout(1000);
    }
    // 功能归属（级联 Cascader，下拉里的具体类名可能要视页面再微调）
//    static void selectFunctionBelong(Page page, String... path) {
//        // 点击“功能归属”这一栏的 selector
//        Locator selector = page.locator(
//                "div.ant-form-item:has(label[for='form_item_parentId']) .ant-select-selector"
//        );
//        selector.click();
//
//        // 如果是多级，比如 ["根功能A", "子功能B"]，就一层层点
//        for (String text : path) {
//            page.locator(".ant-cascader-menu-item")
//                    .filter(new Locator.FilterOptions().setHasText(text))
//                    .first()
//                    .click();
//        }
//    }
//    static void selectFunctionBelong(Page page, String... path) {
//        // 精确定位“功能归属”这一栏的 selector
//        Locator trigger = page
//                .locator("div.ant-form-item:has(label[for='form_item_parentId']) .ant-select-selector");
//
//        // 1. 强制点一下，确保不会被“不可点击/透明”拦住
//        trigger.click(new Locator.ClickOptions().setForce(true));
//
//        // 2. 等下拉面板真正挂上 DOM
//        page.waitForSelector(".ant-cascader-dropdown",
//                new Page.WaitForSelectorOptions().setState(ATTACHED));
//
//        // 3. 逐级选择
//        for (String text : path) {
//            Locator option = page
//                    .locator(".ant-cascader-menu-item")
//                    .filter(new Locator.FilterOptions().setHasText(text))
//                    .first();
//            option.click();
//        }
//    }
    static void selectFunctionBelong(Page page, String... path) {
        // 1. 打开功能归属的 cascader
        Locator trigger = page
                .locator("div.ant-form-item:has(label[for='form_item_parentId']) .ant-select-selector");

        trigger.click(new Locator.ClickOptions().setForce(true));
        page.waitForSelector(".ant-cascader-dropdown");

        // 2. 逐级选择：前 n-1 级 hover，最后一级 click
        for (int i = 0; i < path.length; i++) {
            String text = path[i];

            Locator option = page.locator(".ant-cascader-menu-item")
                    .filter(new Locator.FilterOptions().setHasText(text))
                    .first();

            if (i < path.length - 1) {
                // 不是最后一级：只 hover，让右侧子菜单展开
                option.hover();
                // 稍微等一下子菜单渲染（也可以换成等待下一级特定文本）
                page.waitForTimeout(200);
            } else {
                // 最后一级：真正点击选择
                option.click();
            }
        }
    }

    // 负责人：可搜索的 ant-select（form_item_functionOwnerId）
    static void selectOwner(Page page, String keyword, String optionText) {
        // 打开下拉
        Locator selector = page.locator(
                "div.ant-form-item:has(label[for='form_item_functionOwnerId']) .ant-select-selector"
        );
        selector.click();

        // 在搜索框里输入关键字
        page.locator("#form_item_functionOwnerId").fill(keyword);

        // 选择下拉中匹配的那一项
        page.locator(".ant-select-item-option-content")
                .filter(new Locator.FilterOptions().setHasText(optionText))
                .first()
                .click();
    }

    // 开发状态：普通 ant-select（form_item_developStatus）
    static void selectDevStatus(Page page, String optionText) {
        // 打开下拉
        Locator selector = page.locator(
                "div.ant-form-item:has(label[for='form_item_developStatus']) .ant-select-selector"
        );
        selector.click();

        // 选中目标选项
        page.locator(".ant-select-item-option-content")
                .filter(new Locator.FilterOptions().setHasText(optionText))
                .first()
                .click();
    }
}
