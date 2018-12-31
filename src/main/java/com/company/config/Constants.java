package com.company.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Constants {

    private static final Logger log = LoggerFactory.getLogger(Constants.class);

    public static final String URI_API_PREFIX = "/api";
    public static final String URI_ADMIN_PREFIX = "/admin";

    public static final String ERROR_UPDATE_PROFILE = "Updating profile doesn't match the current one";
    public static final String ERROR_UPDATE_EMAIL = "E-mail is already used by another person";
    public static final String ERROR_SIGN_UP_EMAIL = ERROR_UPDATE_EMAIL;
    public static final String ERROR_PASSWORD_CONFIRMATION = "Current password is invalid";
}
