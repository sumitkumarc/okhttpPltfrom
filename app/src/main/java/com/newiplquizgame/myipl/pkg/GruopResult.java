package com.newiplquizgame.myipl.pkg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GruopResult {
    @SerializedName("GroupId")
    @Expose
    private Integer groupId;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("RoleId")
    @Expose
    private Integer roleId;
    @SerializedName("LoginPassword")
    @Expose
    private String loginPassword;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("NickName")
    @Expose
    private String nickName;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("IsDelete")
    @Expose
    private Boolean isDelete;
    @SerializedName("CreatedBy")
    @Expose
    private Integer createdBy;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("ModifyBy")
    @Expose
    private Object modifyBy;
    @SerializedName("ModifyDate")
    @Expose
    private Object modifyDate;
    @SerializedName("DOB")
    @Expose
    private Object dOB;
    @SerializedName("TotalPoints")
    @Expose
    private Double totalPoints;
    @SerializedName("IsVerify")
    @Expose
    private Boolean isVerify;
    @SerializedName("PPhoto")
    @Expose
    private String pPhoto;
    @SerializedName("FBId")
    @Expose
    private String fBId;
    @SerializedName("GMAILId")
    @Expose
    private Object gMAILId;
    @SerializedName("LoginType")
    @Expose
    private Integer loginType;
    @SerializedName("OneSignalToken")
    @Expose
    private Object oneSignalToken;
    @SerializedName("AnswerMasters")
    @Expose
    private List<Object> answerMasters = null;
    @SerializedName("AuditMasters")
    @Expose
    private List<Object> auditMasters = null;
    @SerializedName("GroupMasters")
    @Expose
    private List<Object> groupMasters = null;
    @SerializedName("GroupUsers")
    @Expose
    private List<Object> groupUsers = null;
    @SerializedName("PointHistoryMasters")
    @Expose
    private List<Object> pointHistoryMasters = null;
    @SerializedName("QuestionGroupMasters")
    @Expose
    private List<Object> questionGroupMasters = null;
    @SerializedName("RoleMaster")
    @Expose
    private Object roleMaster;
    @SerializedName("ImageFile")
    @Expose
    private Object imageFile;

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

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
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

    public Object getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(Object modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Object getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Object modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Object getDOB() {
        return dOB;
    }

    public void setDOB(Object dOB) {
        this.dOB = dOB;
    }

    public Double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Double totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Boolean getIsVerify() {
        return isVerify;
    }

    public void setIsVerify(Boolean isVerify) {
        this.isVerify = isVerify;
    }

    public String getPPhoto() {
        return pPhoto;
    }

    public void setPPhoto(String pPhoto) {
        this.pPhoto = pPhoto;
    }

    public String getFBId() {
        return fBId;
    }

    public void setFBId(String fBId) {
        this.fBId = fBId;
    }

    public Object getGMAILId() {
        return gMAILId;
    }

    public void setGMAILId(Object gMAILId) {
        this.gMAILId = gMAILId;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    public Object getOneSignalToken() {
        return oneSignalToken;
    }

    public void setOneSignalToken(Object oneSignalToken) {
        this.oneSignalToken = oneSignalToken;
    }

    public List<Object> getAnswerMasters() {
        return answerMasters;
    }

    public void setAnswerMasters(List<Object> answerMasters) {
        this.answerMasters = answerMasters;
    }

    public List<Object> getAuditMasters() {
        return auditMasters;
    }

    public void setAuditMasters(List<Object> auditMasters) {
        this.auditMasters = auditMasters;
    }

    public List<Object> getGroupMasters() {
        return groupMasters;
    }

    public void setGroupMasters(List<Object> groupMasters) {
        this.groupMasters = groupMasters;
    }

    public List<Object> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(List<Object> groupUsers) {
        this.groupUsers = groupUsers;
    }

    public List<Object> getPointHistoryMasters() {
        return pointHistoryMasters;
    }

    public void setPointHistoryMasters(List<Object> pointHistoryMasters) {
        this.pointHistoryMasters = pointHistoryMasters;
    }

    public List<Object> getQuestionGroupMasters() {
        return questionGroupMasters;
    }

    public void setQuestionGroupMasters(List<Object> questionGroupMasters) {
        this.questionGroupMasters = questionGroupMasters;
    }

    public Object getRoleMaster() {
        return roleMaster;
    }

    public void setRoleMaster(Object roleMaster) {
        this.roleMaster = roleMaster;
    }

    public Object getImageFile() {
        return imageFile;
    }

    public void setImageFile(Object imageFile) {
        this.imageFile = imageFile;
    }

}
