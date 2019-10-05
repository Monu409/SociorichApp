package com.sociorich.app.modals;

import java.io.Serializable;

public class CommentModal implements Serializable {
    private String comntStr;
    private String timeDateStr;
    private String imgUrl;
    private String userStr;

    public String getComntStr() {
        return comntStr;
    }

    public void setComntStr(String comntStr) {
        this.comntStr = comntStr;
    }

    public String getTimeDateStr() {
        return timeDateStr;
    }

    public void setTimeDateStr(String timeDateStr) {
        this.timeDateStr = timeDateStr;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUserStr() {
        return userStr;
    }

    public void setUserStr(String userStr) {
        this.userStr = userStr;
    }
}
