package com.company.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Constants {

    private static final Logger log = LoggerFactory.getLogger(Constants.class);

    public static final String URI_API_PREFIX = "/api";
    public static final String URI_ADMIN_PREFIX = "/admin";

    public static final int BUTTONS_TO_SHOW = 3;
    public static final int INITIAL_PAGE = 0;
    public static final int INITIAL_PAGE_SIZE = 5;
    public static final int[] PAGE_SIZES = {5, 10};
}
