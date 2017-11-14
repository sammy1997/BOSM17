package com.bitspilanidvm.bosm2017.Modals;

import java.util.ArrayList;
import java.util.Date;

public class NonFixtureSportsData {
    private ArrayList <String> categoryName=new ArrayList<>();
    private ArrayList <String> categoryDesc=new ArrayList<>();
    private ArrayList<ArrayList<String>> categoryResult=new ArrayList<>();
    private ArrayList<ArrayList<String>> categoryScore = new ArrayList<>();

    private String date;
    private String time;
    private String venue;
    private String round;
    private Date scheduleTime;
    private Date resultTime;

    public ArrayList<ArrayList<String>> getCategoryResult() {
        return categoryResult;
    }

    public void setCategoryResult(ArrayList<ArrayList<String>> categoryResult) {
        this.categoryResult = categoryResult;
    }

    public ArrayList<String> getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(ArrayList<String> categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<String> getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(ArrayList<String> categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public Date getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public Date getResultTime() {
        return resultTime;
    }

    public void setResultTime(Date resultTime) {
        this.resultTime = resultTime;
    }

    public ArrayList<ArrayList<String>> getCategoryScore() {
        return categoryScore;
    }

    public void setCategoryScore(ArrayList<ArrayList<String>> categoryScore) {
        this.categoryScore = categoryScore;
    }

    public NonFixtureSportsData(ArrayList<String> categoryName, ArrayList<String> categoryDesc, String date, String time, String venue, String round, ArrayList<ArrayList<String>> categoryResult, Date scheduleTime, Date resultTime, ArrayList<ArrayList<String>> categoryScore) {
        this.categoryName = categoryName;
        this.categoryDesc = categoryDesc;
        this.categoryResult=categoryResult;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.round = round;
        this.scheduleTime = scheduleTime;
        this.resultTime = resultTime;
        this.categoryScore = categoryScore;
    }
}
