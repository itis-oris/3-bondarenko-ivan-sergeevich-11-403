package com.itis.cryptotracker.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static UserPrincipal currentPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
            return null;
        }
        return principal;
    }

    public static Long currentUserId() {
        UserPrincipal principal = currentPrincipal();
        if (principal == null) {
            throw new IllegalStateException("No authenticated user in security context");
        }
        return principal.getId();
    }
}
