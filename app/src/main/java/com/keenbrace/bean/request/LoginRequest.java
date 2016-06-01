package com.keenbrace.bean.request;

/**
 * Created by zrq on 16/1/21.
 */
public class LoginRequest {
    private String loginName;
    private String password;
    private String uPlatform;
    private int loginType;

    public LoginRequest(String loginName, String password, String uPlatform, int loginType) {
        this.loginName = loginName;
        this.password = password;
        this.uPlatform = uPlatform;
        this.loginType = loginType;
    }
}
