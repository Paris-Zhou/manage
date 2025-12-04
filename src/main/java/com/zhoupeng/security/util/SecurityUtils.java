package com.zhoupeng.security.util;
import com.zhoupeng.domain.AdminUserDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
/**
 * @author ZhouPP
 * create_time 2025/12/4:16:13
 */
public class SecurityUtils {
    /**
     * 获取当前认证对象
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前登录用户（AdminUserDetails）
     */
    public static AdminUserDetails getLoginUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof AdminUserDetails) {
            return (AdminUserDetails) principal;
        }
        return null;
    }

    /**
     * 获取当前登录用户ID
     */
    public static Long getLoginUserId() {
        AdminUserDetails user = getLoginUser();
        if (user == null) {
            return null;
        }
        return user.getUserId();
    }

    /**
     * 获取当前登录用户名
     */
    public static String getLoginUsername() {
        AdminUserDetails user = getLoginUser();
        if (user == null) {
            return null;
        }
        return user.getUsername();
    }
}
