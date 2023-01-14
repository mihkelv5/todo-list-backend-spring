package com.todolist.constant;

public class SecurityConstant {
    public static final long ACCESS_EXPIRATION_TIME = 1000  * 60 * 15; // 15 min in milliseconds

    public static final long REGISTRATION_TOKEN_EXPIRATION_TIME = 60 * 60 * 24 * 5; //5 days in seconds
    public static final long REFRESH_EXPIRATION_TIME = 60 * 60 * 24 * 5; //5 days in seconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String REFRESH_TOKEN = "Refresh-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token Cannot Be Verified";
    public static final String TODO = "Placeholder";
    public static final String TODO_ADMINISTRATION = "TODO app";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";

}
