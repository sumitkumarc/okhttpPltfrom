package com.newiplquizgame.myipl.pkg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupDatum {

    @SerializedName("GroupId")
    @Expose
    private Integer groupId;
    @SerializedName("GroupName")
    @Expose
    private String groupName;
    @SerializedName("Icon")
    @Expose
    private String icon;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("CreatedBy")
    @Expose
    private Integer createdBy;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("UserMaster")
    @Expose
    private Object userMaster;
    @SerializedName("QuestionGroupMasters")
    @Expose
    private List<Object> questionGroupMasters = null;
    @SerializedName("GroupUsers")
    @Expose
    private List<Object> groupUsers = null;
    @SerializedName("ImageFile")
    @Expose
    private Object imageFile;
    @SerializedName("IsAdmin")
    @Expose
    private Integer isAdmin;
    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Object getUserMaster() {
        return userMaster;
    }

    public void setUserMaster(Object userMaster) {
        this.userMaster = userMaster;
    }

    public List<Object> getQuestionGroupMasters() {
        return questionGroupMasters;
    }

    public void setQuestionGroupMasters(List<Object> questionGroupMasters) {
        this.questionGroupMasters = questionGroupMasters;
    }

    public List<Object> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(List<Object> groupUsers) {
        this.groupUsers = groupUsers;
    }

    public Object getImageFile() {
        return imageFile;
    }

    public void setImageFile(Object imageFile) {
        this.imageFile = imageFile;
    }


    // User Info
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("RoleId")
    @Expose
    private Integer roleId;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("OneSignalToken")
    @Expose
    private String oneSignalToken;
    @SerializedName("PPhoto")
    @Expose
    private String pPhoto;

    public String getPPhoto() {
        return pPhoto;
    }

    public void setPPhoto(String pPhoto) {
        this.pPhoto = pPhoto;
    }

    public String getOneSignalToken() {
        return oneSignalToken;
    }

    public void setOneSignalToken(String oneSignalToken) {
        this.oneSignalToken = oneSignalToken;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    @SerializedName("GroupUserId")
    @Expose
    private Integer groupUserId;
    @SerializedName("TotalPoint")
    @Expose
    private Double totalPoint;
    @SerializedName("IsApproved")
    @Expose
    private Integer isApproved;
    @SerializedName("ApprovedDate")
    @Expose
    private Object approvedDate;
    @SerializedName("SendDate")
    @Expose
    private String sendDate;
    @SerializedName("GroupMaster")
    @Expose
    private Object groupMaster;


    public Integer getGroupUserId() {
        return groupUserId;
    }

    public void setGroupUserId(Integer groupUserId) {
        this.groupUserId = groupUserId;
    }

    public Double getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Double totalPoint) {
        this.totalPoint = totalPoint;
    }

    public Integer getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Integer isApproved) {
        this.isApproved = isApproved;
    }

    public Object getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Object approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public Object getGroupMaster() {
        return groupMaster;
    }

    public void setGroupMaster(Object groupMaster) {
        this.groupMaster = groupMaster;
    }


    @SerializedName("QueId")
    @Expose
    private Integer queId;
    @SerializedName("QueGrpId")
    @Expose
    private Integer queGrpId;
    @SerializedName("Question")
    @Expose
    private String question;
    @SerializedName("Answers")
    @Expose
    private String answers;
    @SerializedName("QueType")
    @Expose
    private Integer queType;
    @SerializedName("CurrAns")
    @Expose
    private String currAns;
    @SerializedName("Point")
    @Expose
    private Double point;
    @SerializedName("AnswerMasters")
    @Expose
    private List<Object> answerMasters = null;
    @SerializedName("QuestionGroupMaster")
    @Expose
    private Object questionGroupMaster;
    @SerializedName("Mode")
    @Expose
    private Integer mode;

    public Integer getQueId() {
        return queId;
    }

    public void setQueId(Integer queId) {
        this.queId = queId;
    }

    public Integer getQueGrpId() {
        return queGrpId;
    }

    public void setQueGrpId(Integer queGrpId) {
        this.queGrpId = queGrpId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public Integer getQueType() {
        return queType;
    }

    public void setQueType(Integer queType) {
        this.queType = queType;
    }

    public String getCurrAns() {
        return currAns;
    }

    public void setCurrAns(String currAns) {
        this.currAns = currAns;
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    public List<Object> getAnswerMasters() {
        return answerMasters;
    }

    public void setAnswerMasters(List<Object> answerMasters) {
        this.answerMasters = answerMasters;
    }

    public Object getQuestionGroupMaster() {
        return questionGroupMaster;
    }

    public void setQuestionGroupMaster(Object questionGroupMaster) {
        this.questionGroupMaster = questionGroupMaster;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    @SerializedName("TournamentID")
    @Expose
    private Integer tournamentID;
    @SerializedName("TournamentName")
    @Expose
    private String tournamentName;
    @SerializedName("SponserName")
    @Expose
    private Object sponserName;
    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("ScheduleLst")
    @Expose
    private List<ScheduleLst> scheduleLst = null;

    public Integer getTournamentID() {
        return tournamentID;
    }

    public void setTournamentID(Integer tournamentID) {
        this.tournamentID = tournamentID;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public Object getSponserName() {
        return sponserName;
    }

    public void setSponserName(Object sponserName) {
        this.sponserName = sponserName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

//    public String getCreatedDate() {
//        return createdDate;
//    }
//
//    public void setCreatedDate(String createdDate) {
//        this.createdDate = createdDate;
//    }
//
//    public Integer getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Integer userId) {
//        this.userId = userId;
//    }

    public List<ScheduleLst> getScheduleLst() {
        return scheduleLst;
    }

    public void setScheduleLst(List<ScheduleLst> scheduleLst) {
        this.scheduleLst = scheduleLst;
    }


    // Video List

    @SerializedName("VidId")
    @Expose
    private Integer vidId;
    @SerializedName("VidLink")
    @Expose
    private String vidLink;
    @SerializedName("EntryDate")
    @Expose
    private String entryDate;

    public Integer getVidId() {
        return vidId;
    }

    public void setVidId(Integer vidId) {
        this.vidId = vidId;
    }

    public String getVidLink() {
        return vidLink;
    }

    public void setVidLink(String vidLink) {
        this.vidLink = vidLink;
    }


    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }


}
