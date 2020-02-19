package com.newiplquizgame.myipl.pkg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserMaster {
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("RoleId")
    @Expose
    private Integer roleId;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("DisplayName")
    @Expose
    private String displayName;
    @SerializedName("Photo")
    @Expose
    private String photo;
    @SerializedName("DOB")
    @Expose
    private Object dOB;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Object getDOB() {
        return dOB;
    }

    public void setDOB(Object dOB) {
        this.dOB = dOB;
    }
}
