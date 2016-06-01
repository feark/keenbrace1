package com.keenbrace.bean.response;


public class GetVersionInfoResponse {
    private String version;
    private String apkPath;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    @Override
    public String toString() {
        return "GetVersionInfoResponse{" +
                "version='" + version + '\'' +
                ", apkPath='" + apkPath + '\'' +
                '}';
    }
}
