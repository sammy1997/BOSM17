package com.bitspilanidvm.bosm2017.Modals;

import java.util.Date;

public class FixtureSportsData {

    private String TeamA;
    private String TeamB;
    private String date;
    private String time;
    private String venue;
    private String round;
    private String winner;
    private Date scheduleTime;
    private Date resultTime;
    private String TeamAScore;
    private String TeamBScore;

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getTeamA() {
        return TeamA;
    }

    public void setTeamA(String teamA) {
        TeamA = teamA;
    }

    public String getTeamB() {
        return TeamB;
    }

    public void setTeamB(String teamB) {
        TeamB = teamB;
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

    public String getTeamAScore() {
        return TeamAScore;
    }

    public void setTeamAScore(String teamAScore) {
        TeamAScore = teamAScore;
    }

    public String getTeamBScore() {
        return TeamBScore;
    }

    public void setTeamBScore(String teamBScore) {
        TeamBScore = teamBScore;
    }

    public FixtureSportsData(String teamA, String teamB, String date, String time, String venue, String round, String winner, Date scheduleTime, Date resultTime, String teamAScore, String teamBScore) {
        TeamA = teamA;
        TeamB = teamB;
        this.winner=winner;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.round = round;
        this.scheduleTime = scheduleTime;
        this.resultTime = resultTime;
        this.TeamAScore = teamAScore;
        this.TeamBScore = teamBScore;
    }
}
