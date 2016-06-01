package com.keenbrace.bean.response;

/**
 * Created by manor on 16/5/26.
 */
public class Result {
    String ResultCode;
    String Msg;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    String ID;
}
