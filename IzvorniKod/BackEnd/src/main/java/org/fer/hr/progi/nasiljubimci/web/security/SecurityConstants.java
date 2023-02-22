package org.fer.hr.progi.nasiljubimci.web.security;

public class SecurityConstants {

    public static final String SECRET = "SOme rand0m k3Y to generate JWTs";
    public static final long EXPIRATION_TIME = 600_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/register";
}