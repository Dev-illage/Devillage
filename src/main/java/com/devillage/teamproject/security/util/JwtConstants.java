package com.devillage.teamproject.security.util;

public class JwtConstants {
    public final static String SECRET_KEY = "antmsEmtdlswlrndrmagkrpTsyd";
    public final static String REFRESH_SECRET_KEY = "dlrjsejejdnrrndrmagkrpTshd";
    public final static Long ACCESS_TOKEN_EXPIRE_COUNT = 60000L*10*3; // 30 minutes
    public final static Long REFRESH_TOKEN_EXPIRE_COUNT = 7 * 24 * 60 * 60 * 1000L; // 7 days
    public final static String BEARER_TYPE = "Bearer ";
    public final static String AUTHORIZATION_HEADER = "Authorization";
    public final static String REFRESH_HEADER = "RefreshToken";
    public final static String ROLES = "roles";
    public final static String SEQUENCE = "sequence";
    public final static String REDIRECT_URL_OAUTH2 = "https://dev-illage.com/login/oauth";
    public final static String NULL_TOKEN = "Bearer null";
}
