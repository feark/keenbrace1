package com.keenbrace.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import java.io.Serializable;

/**
 * Entity mapped to table "SHORT_PLAN".
 */
public class ShortPlan implements Serializable {

    private Long id;
    private String ShortPlanName;
    private Integer totalTime;
    private String pos;
    private Integer intense;
    private Integer status;
    private Integer logo;
    private Integer singleTrainID;
    private Integer warmUpTime;
    private Integer distance;
    private Integer section;
    private byte[] content;
    private byte[] duration;
    private Integer cadence;
    private Integer speed;
    private byte[] type;
    private byte[] set;
    private byte[] reps;
    private Integer restBtwType;
    private byte[] restBtwSet;

    public ShortPlan() {
    }

    public ShortPlan(Long id) {
        this.id = id;
    }

    public ShortPlan(Long id, String ShortPlanName, Integer totalTime, String pos, Integer intense, Integer status, Integer logo, Integer singleTrainID, Integer warmUpTime, Integer distance, Integer section, byte[] content, byte[] duration, Integer cadence, Integer speed, byte[] type, byte[] set, byte[] reps, Integer restBtwType, byte[] restBtwSet) {
        this.id = id;
        this.ShortPlanName = ShortPlanName;
        this.totalTime = totalTime;
        this.pos = pos;
        this.intense = intense;
        this.status = status;
        this.logo = logo;
        this.singleTrainID = singleTrainID;
        this.warmUpTime = warmUpTime;
        this.distance = distance;
        this.section = section;
        this.content = content;
        this.duration = duration;
        this.cadence = cadence;
        this.speed = speed;
        this.type = type;
        this.set = set;
        this.reps = reps;
        this.restBtwType = restBtwType;
        this.restBtwSet = restBtwSet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortPlanName() {
        return ShortPlanName;
    }

    public void setShortPlanName(String ShortPlanName) {
        this.ShortPlanName = ShortPlanName;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public Integer getIntense() {
        return intense;
    }

    public void setIntense(Integer intense) {
        this.intense = intense;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLogo() {
        return logo;
    }

    public void setLogo(Integer logo) {
        this.logo = logo;
    }

    public Integer getSingleTrainID() {
        return singleTrainID;
    }

    public void setSingleTrainID(Integer singleTrainID) {
        this.singleTrainID = singleTrainID;
    }

    public Integer getWarmUpTime() {
        return warmUpTime;
    }

    public void setWarmUpTime(Integer warmUpTime) {
        this.warmUpTime = warmUpTime;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] getDuration() {
        return duration;
    }

    public void setDuration(byte[] duration) {
        this.duration = duration;
    }

    public Integer getCadence() {
        return cadence;
    }

    public void setCadence(Integer cadence) {
        this.cadence = cadence;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public byte[] getType() {
        return type;
    }

    public void setType(byte[] type) {
        this.type = type;
    }

    public byte[] getSet() {
        return set;
    }

    public void setSet(byte[] set) {
        this.set = set;
    }

    public byte[] getReps() {
        return reps;
    }

    public void setReps(byte[] reps) {
        this.reps = reps;
    }

    public Integer getRestBtwType() {
        return restBtwType;
    }

    public void setRestBtwType(Integer restBtwType) {
        this.restBtwType = restBtwType;
    }

    public byte[] getRestBtwSet() {
        return restBtwSet;
    }

    public void setRestBtwSet(byte[] restBtwSet) {
        this.restBtwSet = restBtwSet;
    }

}
