package com.newiplquizgame.myipl.pkg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleLst {
    @SerializedName("ScheduleId")
    @Expose
    private Integer scheduleId;
    @SerializedName("TournamentID")
    @Expose
    private Integer tournamentID;
    @SerializedName("MatchID")
    @Expose
    private Integer matchID;
    @SerializedName("AvsB")
    @Expose
    private String avsB;
    @SerializedName("MatchDate")
    @Expose
    private String matchDate;
    @SerializedName("MatchDays")
    @Expose
    private String matchDays;
    @SerializedName("MatchTime")
    @Expose
    private String matchTime;
    @SerializedName("Stadium")
    @Expose
    private String stadium;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("State")
    @Expose
    private String state;

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getTournamentID() {
        return tournamentID;
    }

    public void setTournamentID(Integer tournamentID) {
        this.tournamentID = tournamentID;
    }

    public Integer getMatchID() {
        return matchID;
    }

    public void setMatchID(Integer matchID) {
        this.matchID = matchID;
    }

    public String getAvsB() {
        return avsB;
    }

    public void setAvsB(String avsB) {
        this.avsB = avsB;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getMatchDays() {
        return matchDays;
    }

    public void setMatchDays(String matchDays) {
        this.matchDays = matchDays;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
