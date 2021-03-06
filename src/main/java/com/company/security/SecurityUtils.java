package com.company.security;

import com.company.persist.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class SecurityUtils {

    private static final Logger log = LoggerFactory.getLogger(SecurityUtils.class);

    private SecurityUtils() {
    }

    public static User currentProfile() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        final Authentication authentication = securityContext.getAuthentication();
        User profile = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                profile = (User) authentication.getPrincipal();
            }
        }
        return profile;
    }
}
