package com.newiplquizgame.myipl.pkg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GruopMaster {
    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("Msg")
    @Expose
    private String msg;
    @SerializedName("Data")
    @Expose
    private List<GroupDatum> data = null;
    @SerializedName("Result")
    @Expose
    private Object result;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<GroupDatum> getData() {
        return data;
    }

    public void setData(List<GroupDatum> data) {
        this.data = data;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
