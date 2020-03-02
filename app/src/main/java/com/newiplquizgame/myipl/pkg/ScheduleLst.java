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
    @SerializedName("TeamA")
    @Expose
    private Integer teamA;
    @SerializedName("TeamB")
    @Expose
    private Integer teamB;
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
    @SerializedName("TeamAName")
    @Expose
    private String teamAName;
    @SerializedName("TeamBName")
    @Expose
    private String teamBName;
    @SerializedName("TeamACode")
    @Expose
    private String teamACode;
    @SerializedName("TeamBCode")
    @Expose
    private String teamBCode;
    @SerializedName("TeamAIcon")
    @Expose
    private String teamAIcon;
    @SerializedName("TeamBIcon")
    @Expose
    private String teamBIcon;
    @SerializedName("PredicStartTime")
    @Expose
    private String predicStartTime;
    @SerializedName("PredicEndTime")
    @Expose
    private String predicEndTime;

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

    public Integer getTeamA() {
        return teamA;
    }

    public void setTeamA(Integer teamA) {
        this.teamA = teamA;
    }

    public Integer getTeamB() {
        return teamB;
    }

    public void setTeamB(Integer teamB) {
        this.teamB = teamB;
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

    public String getTeamAName() {
        return teamAName;
    }

    public void setTeamAName(String teamAName) {
        this.teamAName = teamAName;
    }

    public String getTeamBName() {
        return teamBName;
    }

    public void setTeamBName(String teamBName) {
        this.teamBName = teamBName;
    }

    public String getTeamACode() {
        return teamACode;
    }

    public void setTeamACode(String teamACode) {
        this.teamACode = teamACode;
    }

    public String getTeamBCode() {
        return teamBCode;
    }

    public void setTeamBCode(String teamBCode) {
        this.teamBCode = teamBCode;
    }

    public String getTeamAIcon() {
        return teamAIcon;
    }

    public void setTeamAIcon(String teamAIcon) {
        this.teamAIcon = teamAIcon;
    }

    public String getTeamBIcon() {
        return teamBIcon;
    }

    public void setTeamBIcon(String teamBIcon) {
        this.teamBIcon = teamBIcon;
    }
    public String getPredicStartTime() {
        return predicStartTime;
    }

    public void setPredicStartTime(String predicStartTime) {
        this.predicStartTime = predicStartTime;
    }

    public String getPredicEndTime() {
        return predicEndTime;
    }

    public void setPredicEndTime(String predicEndTime) {
        this.predicEndTime = predicEndTime;
    }

}
