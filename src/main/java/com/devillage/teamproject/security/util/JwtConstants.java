package com.devillage.teamproject.security.util;

public class JwtConstants {
    public final static String SECRET_KEY = "antmsEmtdlswlrndrmagkrpTsyd";
    public final static String REFRESH_SECRET_KEY = "dlrjsejejdnrrndrmagkrpTshd";
    public final static Long ACCESS_TOKEN_EXPIRE_COUNT = 60000L*10; // 10 minutes
    public final static Long REFRESH_TOKEN_EXPIRE_COUNT = 604800L*10; // 7 days
    public final static String BEARER_TYPE = "Bearer ";
    public final static String AUTHORITIES_KEY = "auth";
    public final static String AUTHORIZATION_HEADER = "Authorization";
    public final static String REFRESH_HEADER = "RefreshToken";
    public final static String TOKEN_EXPIRED = "tokenExpired";
    public final static String TOKEN_OK = "tokenOk";
    public final static String ROLES = "roles";
}
