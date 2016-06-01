package com.keenbrace.storage;

public class BleData implements java.io.Serializable {

    private long id;
    private int type;
    private long startTime;
    private long timelength;
    private int sumscore;// �ܷ�
    private int cadence;// ��Ƶ
    private int stride;// ����
    private int mileage;// ���
    private String fileName;//
    private long endTime;

    String latLngs;//��ʽx,y;x,y

    private int sumwarings;
    private double latitude;
    private double longitude;

    private double endlatitude;
    private double endlongitude;

    public double getEndlatitude() {
        return endlatitude;
    }

    public void setEndlatitude(double endlatitude) {
        this.endlatitude = endlatitude;
    }

    public double getEndlongitude() {
        return endlongitude;
    }

    public void setEndlongitude(double endlongitude) {
        this.endlongitude = endlongitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getSumwarings() {
        return sumwarings;
    }

    public void setSumwarings(int sumwarings) {
        this.sumwarings = sumwarings;
    }

    public String getLatLngs() {
        return latLngs;
    }

    public void setLatLngs(String latLngs) {
        this.latLngs = latLngs;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getTimelength() {
        return timelength;
    }

    public void setTimelength(long timelength) {
        this.timelength = timelength;
    }

    public int getSumscore() {
        return sumscore;
    }

    public void setSumscore(int sumscore) {
        this.sumscore = sumscore;
    }

    public int getCadence() {
        return cadence;
    }

    public void setCadence(int cadence) {
        this.cadence = cadence;
    }

    public int getStride() {
        return stride;
    }

    public void setStride(int stride) {
        this.stride = stride;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }


}
