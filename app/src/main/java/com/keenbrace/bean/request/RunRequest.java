package com.keenbrace.bean.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.keenbrace.greendao.KeenBrace;
import com.keenbrace.greendao.RunWaring;

import java.util.List;

/**
 * Created by manor on 16/5/26.
 */
public class RunRequest {
    @Expose
    @SerializedName("keenBrace")
    private KeenBrace keenBrace;
    @Expose
    @SerializedName("runWarings")
    private List<RunWaring> runWarings;

    public List<RunWaring> getRunWarings() {
        return runWarings;
    }

    public void setRunWarings(List<RunWaring> runWarings) {
        this.runWarings = runWarings;
    }

    public KeenBrace getKeenBrace() {
        return keenBrace;
    }

    public void setKeenBrace(KeenBrace keenBrace) {
        this.keenBrace = keenBrace;
    }
}
