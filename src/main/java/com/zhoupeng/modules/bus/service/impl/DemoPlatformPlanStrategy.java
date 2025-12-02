package com.zhoupeng.modules.bus.service.impl;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.zhoupeng.modules.bus.dto.PlatformAccountDTO;
import com.zhoupeng.modules.bus.model.BusProposal;
import com.zhoupeng.modules.bus.service.PlanTemplateStrategy;
import org.springframework.stereotype.Service;

/**
 * @author ZhouPP
 * create_time 2025/12/1:14:24
 */
@Service
public class DemoPlatformPlanStrategy implements PlanTemplateStrategy {

    @Override
    public void login(Page page, PlatformAccountDTO account) {
        page.navigate(account.getLoginUrl());

        // 账号密码登录 tab
        page.getByRole(AriaRole.TAB,
                new Page.GetByRoleOptions().setName("账号密码登录")).click();

        page.locator("#form_item_username").fill(account.getUsername());
        page.locator("#form_item_password").fill(account.getPassword());
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("登 录")).click();

        // 简单等一下（你也可以写显式等待某个元素）
        page.waitForTimeout(500);
    }

    @Override
    public void navigateToNewPlanForm(Page page) {
        // 左侧菜单：需求管理 -> 功能清单
        page.getByRole(AriaRole.MENUITEM,
                new Page.GetByRoleOptions().setName("需求管理")).click();

        page.getByText("功能清单").click();

        // 点击“新增”按钮（根据实际按钮文案修改）
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("新增")).click();

    }

    @Override
    public void createPlan(Page page, BusProposal data) {
        // 这里假设“新建”弹窗已经打开（你前面点了“新增”按钮）

        // 1. 功能归属（例如：根功能A / 子功能B）
        // 如果只有一层，就传一个；多级就传多个
        selectFunctionBelong(page, "动力域", "SADASDAD");

        // 2. 功能名称
        page.fill("#form_item_name", "自动化功能004");

        // 3. 功能英文名
        page.fill("#form_item_nameEn", "auto_function_004");

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