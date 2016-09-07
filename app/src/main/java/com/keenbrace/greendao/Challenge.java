package com.keenbrace.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import java.io.Serializable;

/**
 * Entity mapped to table "CHALLENGE".
 */
public class Challenge implements Serializable {

    private Long id;
    private Integer challengeID;
    private Integer rounds;
    private Integer reps;
    private Integer workoutsNumber;
    private byte[] workouts;
    private Integer distance;
    private Integer section;
    private byte[] content;
    private byte[] duration;
    private Integer cadence;
    private Integer speed;
    private Integer totalTime;

    public Challenge() {
    }

    public Challenge(Long id) {
        this.id = id;
    }

    public Challenge(Long id, Integer challengeID, Integer rounds, Integer reps, Integer workoutsNumber, byte[] workouts, Integer distance, Integer section, byte[] content, byte[] duration, Integer cadence, Integer speed, Integer totalTime) {
        this.id = id;
        this.challengeID = challengeID;
        this.rounds = rounds;
        this.reps = reps;
        this.workoutsNumber = workoutsNumber;
        this.workouts = workouts;
        this.distance = distance;
        this.section = section;
        this.content = content;
        this.duration = duration;
        this.cadence = cadence;
        this.speed = speed;
        this.totalTime = totalTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getChallengeID() {
        return challengeID;
    }

    public void setChallengeID(Integer challengeID) {
        this.challengeID = challengeID;
    }

    public Integer getRounds() {
        return rounds;
    }

    public void setRounds(Integer rounds) {
        this.rounds = rounds;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Integer getWorkoutsNumber() {
        return workoutsNumber;
    }

    public void setWorkoutsNumber(Integer workoutsNumber) {
        this.workoutsNumber = workoutsNumber;
    }

    public byte[] getWorkouts() {
        return workouts;
    }

    public void setWorkouts(byte[] workouts) {
        this.workouts = workouts;
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

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

}
