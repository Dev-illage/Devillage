package com.devillage.teamproject.util;

/*
*
* 메서드로 빼기는 애매하고 별도 클래스 만들기도 애매한 친구들은
* 여기 상수로 지정해놓고 필요할 때 찾아쓰면 편할 것 같아요
*
* */

import java.util.Collections;
import java.util.List;

public class TestConstants {
    public static String SECRET_KEY = "jwt-tokens-that-should-be-changed-production";
    public static String REFRESH_KEY = "jwt-refresh-tokens-that-should-be-changed-production";
    public final static String BEARER = "Bearer ";
    public final static Long SECRET_EXPIRE = 600000L;
    public final static Long REFRESH_EXPIRE = 604800000L;
    public final static String EMAIL1 = "email@test.com";
    public final static String PASSWORD1 = "testPassword!23";
    public final static String NICKNAME1 = "코딩잘하고싶다1";
    public final static Long ID1 = 1L;
    public final static String EMAIL2 = "email2@test.com";
    public final static String PASSWORD2 = "testPassword!23";
    public final static String NICKNAME2 = "코딩잘하고싶다2";
    public final static Long ID2 = 2L;
    public final static Long WRONG_ID = 50000L;
    public final static List<String> ROLES = Collections.singletonList("ROLE_USER");
}
