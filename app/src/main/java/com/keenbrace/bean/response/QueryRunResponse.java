package com.keenbrace.bean.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.keenbrace.greendao.KeenBrace;

import java.util.List;

/**
 * Created by manor on 16/5/26.
 */
public class QueryRunResponse extends Result{
    public List<KeenBrace> getKeenBraceList() {
        return keenBraceList;
    }

    public void setKeenBraceList(List<KeenBrace> keenBraceList) {
        this.keenBraceList = keenBraceList;
    }

    @Expose
    @SerializedName("keenbraces")
    private List<KeenBrace> keenBraceList;
}
