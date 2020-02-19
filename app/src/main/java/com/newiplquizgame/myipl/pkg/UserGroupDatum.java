package com.newiplquizgame.myipl.pkg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserGroupDatum {
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
}
