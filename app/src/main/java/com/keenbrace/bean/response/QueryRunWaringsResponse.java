package com.keenbrace.bean.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.keenbrace.greendao.KeenBrace;
import com.keenbrace.greendao.RunWaring;

import java.util.List;

/**
 * Created by manor on 16/5/26.
 */
public class QueryRunWaringsResponse extends Result {
    public List<RunWaring> getRunWarings() {
        return runWarings;
    }

    public void setRunWarings(List<RunWaring> runWarings) {
        this.runWarings = runWarings;
    }

    @Expose
    @SerializedName("runWarings")
    private List<RunWaring> runWarings;
}
