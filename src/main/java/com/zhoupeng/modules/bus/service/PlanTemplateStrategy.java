package com.zhoupeng.modules.bus.service;
import com.microsoft.playwright.Page;
import com.zhoupeng.modules.bus.dto.PlatformAccountDTO;
import com.zhoupeng.modules.bus.model.BusProposal;

/**
 * @author ZhouPP
 * create_time 2025/12/1:13:54
 */
public interface PlanTemplateStrategy {
    /**
     * 登录平台
     * @param page
     * @param account
     */
    void login(Page page, PlatformAccountDTO account);

    /**
     * 导航到新建计划书页面
     * @param page
     */
    void navigateToNewPlanForm(Page page);

    /**
     * 在当前页面根据 PlanData 填完并提交一条计划书
     */
    void createPlan(Page page, BusProposal data);
}
