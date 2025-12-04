package com.zhoupeng.modules.bus.service.impl;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author ZhouPP
 * create_time 2025/12/4:16:48
 */
public class PlaywrightLogin {

    /**
     * 登录并点击“继续”
     */
    public static void loginAndContinue(String username, String password) {
        try (Playwright playwright = Playwright.create()) {
            // 1. 启动浏览器（带界面，方便调试）
            Browser browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions().setHeadless(false));

            // 关键：允许下载
            BrowserContext context = browser.newContext(
                    new Browser.NewContextOptions().setAcceptDownloads(true)
            );
            Page page = context.newPage();

            // 2. 打开登录页
            page.navigate("https://yflifeb2cprdagent.b2clogin.com/yflifeb2cprdagent.onmicrosoft.com/B2C_1A_SIGNIN_USERNAMEOREMAIL/oauth2/v2.0/authorize?client_id=41811dd7-2e50-4dc6-af34-faa5694bc9cc&response_type=code+id_token&redirect_uri=https%3A%2F%2Fproposal.yflife.app%2FsystemSelection&response_mode=fragment&scope=openid&state=b2c_1a_signin_usernameoremail&nonce=1&ui_locales=zh-hant");

            // 3. 等待表单渲染
            page.waitForSelector("#signInName");
            page.waitForSelector("#password");

            // 4. 输入登入編號
            page.locator("#signInName").fill(username);

            // 5. 输入 AES 密碼
            page.locator("#password").fill(password);

            // 6. 点击登录按钮（根据实际按钮选择器调整）
            // 优先用按钮文字
            // page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("登入")).click();
            // 如果你不确定登录按钮文字，可以继续用 submit
            page.locator("button[type='submit']").click();

            // 7. 等待跳转到系统选择页（URL 你可以根据实际情况改一下）
            // 例如：跳到 https://proposal.yflife.app/systemSelection 开头的地址
            page.waitForURL("**/systemSelection*");

            // 8. 等待“继续”按钮出现
            // 方式一：根据按钮文字
            Locator continueBtn = page.getByRole(
                    AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("继续")
            );

            // 等待按钮可见
            continueBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

            // 9. 点击“继续”
            continueBtn.click();

            // 10. 后续如果还有跳转，可以再加等待
            page.waitForLoadState(LoadState.NETWORKIDLE);
            // ===== 点击“建立保单建议书” =====
            Locator createProposalBtn = page.getByRole(
                    AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName("建立保单建议书")
            );
            createProposalBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            createProposalBtn.click();

            page.waitForLoadState(LoadState.NETWORKIDLE);
            // ===== 填写客户资料并点击 =====
            fillCustomerInfoStep(page,
                    "张三受保人", // 受保人姓名
                    30,          // 投保年龄
                    "女",        // 性别： "男" 或 "女"
                    "非吸烟者"   // 吸烟状况： "非吸烟者" 或 "吸烟者"
            );
            // ===== 选择产品 =====
            fillProductSelectionStep(
                    page,
                    "储蓄及年金", // planCategoryText 产品类别
                    "7.8",        // exchangeRate 兑换率
                    "10000"       // annualPremium 每年基本保费
            );
            // 万寿险更改
            change(page);
            //保单建议书摘要
            abstracts(page);
            //保单建议书设定
            establish(page);
            downloadPdf(page);
            // 为了方便观察结果，暂时停留几秒
            page.waitForTimeout(5000);
            browser.close();
        }
    }

    /**
     * 填写「1. 客户资料」页面
     */
    private static void fillCustomerInfoStep(Page page,
                                             String insuredName,
                                             int issueAge,
                                             String genderText,
                                             String smokingText) {
        // 等页面关键元素加载出来
        page.waitForSelector("h1:has-text(\"客户资料\")");
        page.waitForSelector("#insuredName");

        // 1. 填写受保人姓名
        page.fill("#insuredName", insuredName);

        // 如果页面有错误提示（“请输入受保人姓名”），填完就会消失，没关系

        // 2. 勾选「申请人为受保人」
        // 用 checkbox 的 input + check()，保证勾上
        Locator applicantIsInsured = page.locator("input[type='checkbox'][name='checkboxValue']");
        applicantIsInsured.waitFor(); // 确保出现
        if (!applicantIsInsured.isChecked()) {
            applicantIsInsured.check();  // Playwright 自带，确保最终是勾选状态
        }

        // 3. 选择 投保年龄 / 出生日期 —— 我们选「投保年龄」，简单直接填数字
        page.getByRole(
                AriaRole.RADIO,
                new Page.GetByRoleOptions().setName("投保年龄")
        ).click();

        // 等投保年龄输入框从 disabled 变成可用
        page.waitForSelector("#issueAge:not([disabled])");
        page.fill("#issueAge", String.valueOf(issueAge));

        // 4. 选择性别
        // 性别选项是 role="radio"，文案是 “男” / “女”
        page.getByRole(
                AriaRole.RADIO,
                new Page.GetByRoleOptions().setName(genderText)
        ).click();

        // 5. 选择吸烟状况： "非吸烟者" 或 "吸烟者"
        page.getByRole(
                AriaRole.RADIO,
                new Page.GetByRoleOptions().setName(smokingText)
        ).click();

        // 6. 点击底部固定栏的「下一步」按钮
        page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("下一步")
        ).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);

    }

    /**
     * 步骤 2：选择产品 & 填写计划详情，然后点击「下一步」
     *
     * @param page             当前页面
     * @param planCategoryText 产品类别，例如："储蓄及年金" / "严重疾病" / "医疗" / "万用寿险" / ...
     * @param exchangeRate     兑换率，如："7.8"
     * @param annualPremium    每年基本保费，如："10000"
     */
    public static void fillProductSelectionStep(Page page,
                                                String planCategoryText,
                                                String exchangeRate,
                                                String annualPremium) {
        // 1. 等待标题 "2. 选择产品" 出现，说明页面已加载
        page.waitForSelector("h1:has-text(\"2. 选择产品\")");

        // ===== 基本计划 - 产品类别（radiogroup）=====
        // radiogroup id = headlessui-radiogroup-36
        // 里面每个大卡片是一个 div[role='radio']，文字如 "储蓄及年金"、"严重疾病"、"医疗" 等
        Locator planOption = page.locator(
                "#headlessui-radiogroup-36 div[role='radio']:has-text(\"" + planCategoryText + "\")"
        );
        planOption.click();

        // 如果你暂时只用「储蓄及年金」，也可以直接写死：
         page.locator("#headlessui-radiogroup-36 div[role='radio']:has-text(\"储蓄及年金\")").click();

        // ===== 产品、计划下拉（目前已经默认选好 PIS / PISP2，就可以不动它）=====
        // 如需切换产品，可以参考下面这种写法（你之后有需要再开）：
         selectListboxOption(page, "#headlessui-listbox-button-37", "PIS - 富饶千秋储蓄计划");
         selectListboxOption(page, "#headlessui-listbox-button-38", "PISP2 - 富饶千秋储蓄计划 (2年缴付)");

        // ===== 计划详情 - 货币、兑换率、每年基本保费等 =====

        // 2. 货币：默认显示的是 US$，一般可以不动
        // 如果要切换，可以用 selectListboxOption 方法操作 headlessui-listbox-button-42

        // 3. 兑换率 exchangeRate（必填）
//        page.waitForSelector("#exchangeRate");
//        page.fill("#exchangeRate", exchangeRate);

        // 4. 每年基本保费 sumInsured（必填）
//        page.waitForSelector("#sumInsured");
//        page.fill("#sumInsured", annualPremium);

        // 5. 年金权益选择 annOption、保费储备期 premiumDepositPeriod 等
        // 现在页面默认：
        //  - 年金权益选择：不行使年金
        //  - 保费储备期：-- （可能后面校验才会要求）
        // 如果有需要可以类似这样选（只是示例，先注释掉）：
        // selectListboxOption(page, "#headlessui-listbox-button-44", "某个储备期选项");
        // selectListboxOption(page, "#headlessui-listbox-button-45", "不行使年金");

        // 6. 底部固定栏点击「下一步」
        page.locator("button[type='submit']:has-text(\"下一步\")").click();
        page.waitForLoadState(LoadState.NETWORKIDLE);

    }

    /**
     * 通用的 headlessui Listbox 选择工具：
     * 点击按钮 -> 在弹出的下拉中选择指定文本的选项
     */
    private static void selectListboxOption(Page page, String buttonSelector, String optionText) {
        // 点击打开下拉
        Locator button = page.locator(buttonSelector);
        button.click();

        // 等待下拉内容出现（通常是 role="listbox" 的 ul/div，再找里面的 option）
        Locator option = page.locator("[role='option']:has-text(\"" + optionText + "\")");
        option.click();
    }
    public static void change(Page page){
        page.locator("button[type='submit']:has-text(\"下一步\")").click();
        page.waitForLoadState(LoadState.NETWORKIDLE);

    }
    public static void abstracts(Page page){
        page.locator("button[type='submit']:has-text(\"下一步\")").click();
        page.waitForLoadState(LoadState.NETWORKIDLE);

    }
    public static void establish(Page page){
        // 1. 确认已经到了“6. 保单建议书设定”
        page.waitForSelector("h1:has-text(\"6. 保单建议书设定\")");

        // 2. 精确找到「语言」这个下拉按钮（不要用裸的 button:has-text("繁體中文")）
        // 使用 label[for='overrideRPTLocale'] 的兄弟节点里的 button
        Locator langButton = page.locator(
                "xpath=//label[@for='overrideRPTLocale']/following-sibling::div//button"
        );
        langButton.waitFor();

        // 3. 看当前是不是已经是「繁體中文」
        String currentLang = langButton.locator("span.block.truncate").innerText().trim();
        if (!"繁體中文".equals(currentLang)) {
            // 不是的话，点开下拉选单
            langButton.click();

            // headlessui listbox 里的 option 一般是 role="option"
            Locator zhHantOption = page.locator("[role='option']:has-text(\"繁體中文\")").first();
            zhHantOption.click();
        }


        // 4. 勾选「每年预测」
        // 直接对 checkbox input 用 check()，确保最终为选中状态
        Locator everyYearProjection = page.locator("input[type='checkbox'][name='everyYearProjection']");
        everyYearProjection.waitFor();
        if (!everyYearProjection.isChecked()) {
            everyYearProjection.check();
        }

        // 5. 点击底部的「建立」按钮
        page.locator("button[type='submit']:has-text(\"建立\")").click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }
    public static void downloadPdf(Page page){
        // 等标题出现，确保在预览页
        page.waitForSelector("span:has-text(\"保单建议书预览\")");

        // ===== 核心：点击「下载」，并把 PDF 保存到本地 =====

        // 监听下载事件并在回调里点击“下载”
        Download download = page.waitForDownload(() -> {
            // 方式一：根据按钮文字
            page.locator("button:has-text(\"下载\")").click();

            // 方式二（按角色+名字），任选其一
            // page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("下载")).click();
        });
        // 建议用服务器返回的默认文件名
        String suggestedName = download.suggestedFilename();  // 比如 xxx.pdf

        // 自己指定保存目录
        Path downloadDir = Paths.get("D:/proposal-pdf"); // 换成你自己的路径
        try {
            Files.createDirectories(downloadDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Path target = downloadDir.resolve(suggestedName);

        // 真正把文件保存到本地
        download.saveAs(target);

    }
    public static void main(String[] args) {
        // 把这里的账号密码换成你自己的
        loginAndContinue("LH339", "Ty123123@@");
    }
}
