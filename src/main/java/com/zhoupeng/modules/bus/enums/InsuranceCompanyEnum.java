package com.zhoupeng.modules.bus.enums;

/**
 * @author ZhouPP
 * create_time 2025/12/1:11:37
 */
public enum InsuranceCompanyEnum {
    // —— 中国内资头部寿险/综合险 ——
    PING_AN("PING_AN", "中国平安保险（集团）股份有限公司"),
    CHINA_LIFE("CHINA_LIFE", "中国人寿保险股份有限公司"),
    PICC("PICC", "中国人民保险集团股份有限公司"),
    CPIC("CPIC", "中国太平洋保险（集团）股份有限公司"),
    NEW_CHINA_LIFE("NEW_CHINA_LIFE", "新华人寿保险股份有限公司"),
    TAIPING("TAIPING", "中国太平保险集团有限责任公司"),
    TAIKANG("TAIKANG", "泰康保险集团股份有限公司"),
    SUNSHINE("SUNSHINE", "阳光保险集团股份有限公司"),

    // —— 中资/港资在内地较常见的公司 ——
    AIA_CHINA("AIA_CHINA", "友邦人寿保险有限公司（中国）"),
    FWD("FWD", "富卫人寿保险"),
    PRUDENTIAL_CN("PRUDENTIAL_CN", "英国保诚人寿保险（中国）有限公司"),

    // —— 国际知名保险集团（适合面向海外业务的平台） ——
    ALLIANZ("ALLIANZ", "安联保险集团"),
    AXA("AXA", "安盛集团"),
    PRUDENTIAL("PRUDENTIAL", "保德信金融集团（美国 Prudential Financial）"),
    METLIFE("METLIFE", "大都会人寿保险公司"),
    ZURICH("ZURICH", "苏黎世保险集团"),
    MANULIFE("MANULIFE", "宏利金融集团"),
    AIG("AIG", "美国国际集团 AIG");
    private String code;
    private String name;

    InsuranceCompanyEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
    /**
     * 根据 code 反查枚举
     */
    public static InsuranceCompanyEnum fromCode(String code) {
        if (code == null) return null;
        for (InsuranceCompanyEnum e : values()) {
            if (e.code.equalsIgnoreCase(code)) {
                return e;
            }
        }
        return null;
    }
}
