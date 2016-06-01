package com.keenbrace.bean.response;

import com.google.gson.annotations.SerializedName;
import com.keenbrace.greendao.User;

/**
 * Created by manor on 16/5/26.
 */
public class LoginResponse extends Result {
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @SerializedName("user")
    User user;
}
