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
        page.waitForTimeout(2000);
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

        // 等弹出“新建”弹窗
        page.waitForSelector("div.ant-modal-title:has-text('新建')");
    }

    @Override
    public void createPlan(Page page, BusProposal data) {
        // ==== 1. 功能归属：cascader，多级 hover + 最后一级 click ====
        selectFunctionBelong(page, "动力域", "SADASDAD");

        // ==== 2. 文本输入 ====
        page.fill("#form_item_name", "自动化功能004");
        page.fill("#form_item_nameEn","auto_function_004");

        // 负责人：搜索下拉
        selectOwner(page, "宋波", "宋波(songbo)");

        page.fill("#form_item_description", "这是通过 Playwright 自动创建的功能清单");

        // ==== 3. 开发状态下拉 ====
        selectDevStatus(page, "develop");

        // ==== 4. 提交 ====
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("确 认")).click();

        // 等保存完成
        page.waitForTimeout(1000);
    }

    // -------- 以下是给这个策略内部用的小工具方法 --------

    // 功能归属 cascader：前面几级 hover，最后一级 click
    private void selectFunctionBelong(Page page, String... path) {
        Locator trigger = page.locator(
                "div.ant-form-item:has(label[for='form_item_parentId']) .ant-select-selector"
        );

        trigger.click(new Locator.ClickOptions().setForce(true));
        page.waitForSelector(".ant-cascader-dropdown");

        for (int i = 0; i < path.length; i++) {
            String text = path[i];

            Locator option = page.locator(".ant-cascader-menu-item")
                    .filter(new Locator.FilterOptions().setHasText(text))
                    .first();

            if (i < path.length - 1) {
                option.hover();
                page.waitForTimeout(200);
            } else {
                option.click();
            }
        }
    }

    // 负责人下拉（可搜索）
    private void selectOwner(Page page, String keyword, String optionText) {
        Locator trigger = page.locator(
                "div.ant-form-item:has(label[for='form_item_functionOwnerId']) .ant-select-selector"
        );
        trigger.click();

        page.locator("#form_item_functionOwnerId").fill(keyword);

        page.locator(".ant-select-item-option-content")
                .filter(new Locator.FilterOptions().setHasText(optionText))
                .first()
                .click();
    }

    // 开发状态下拉
    private void selectDevStatus(Page page, String optionText) {
        Locator trigger = page.locator(
                "div.ant-form-item:has(label[for='form_item_developStatus']) .ant-select-selector"
        );
        trigger.click();

        page.locator(".ant-select-item-option-content")
                .filter(new Locator.FilterOptions().setHasText(optionText))
                .first()
                .click();
    }
}