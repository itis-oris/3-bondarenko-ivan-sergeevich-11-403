package com.itis.cryptotracker.controller.web;

import com.itis.cryptotracker.security.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice(basePackages = "com.itis.cryptotracker.controller.web")
public class CurrentUserAdvice {

    @ModelAttribute("currentUser")
    public UserPrincipal currentUser(@AuthenticationPrincipal UserPrincipal principal) {
        return principal;
    }

    @ModelAttribute("isAuthenticated")
    public boolean isAuthenticated(@AuthenticationPrincipal UserPrincipal principal) {
        return principal != null;
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) return false;
        return principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
    }

    @ModelAttribute("currentRoles")
    public Set<String> currentRoles(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) return Set.of();
        return principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toUnmodifiableSet());
    }
}
