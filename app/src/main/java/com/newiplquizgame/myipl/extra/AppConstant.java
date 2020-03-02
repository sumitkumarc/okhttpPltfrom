package com.newiplquizgame.myipl.extra;

public class AppConstant {

    public static final String REQUEST_DEVICE_TYPE = "DeviceType";
    public static final String REQUEST_DEVICE_TOKEN = "DeviceToken";
    public static final String REQUEST_HEADER_TOKEN = "Authorization";
    public static final String CONNECTION_ERROR_MSG = "No connection found. Please connect & try again.";

    public static final String BASE_URL1 = "http://crictipp.havbyte.com/";
    public static final String BASE_URL = BASE_URL1 + "api/";

    public static final String GET_LOGIN = BASE_URL + "Authentication/Login";
    public static final String GET_SING_UP = BASE_URL + "Authentication/Register";
    public static final String GET_UPDATE_PROFILE = BASE_URL + "Authentication/editprofile";

    public static final String GET_GROUP_LIST = BASE_URL + "GetGroupList";
    public static final String GET_GROUP_CREATE = BASE_URL + "ManageGroup";
    public static final String GET_GROUP_USER_LIST = BASE_URL + "getgroupusers";
    public static final String GET_USER_LIST = BASE_URL + "getinviteusers";
    public static final String GET_SEND_INVITATION = BASE_URL + "invitemember";

    public static final String GET_ALL_QUESTION = BASE_URL + "getallquestions";
    public static final String GET_LEAVEGROUP = BASE_URL + "leavegroup";
    public static final String GET_TOURNAMENT = BASE_URL + "gettournament";
    public static final String GET_VIDEO = BASE_URL + "getvideo";
    public static final String GET_ACCEPT_REJECT = BASE_URL + "confirmmember";
    public static final String GET_USER_IMAGE_UPLOAD = BASE_URL + "PostFileUpload";


}
