package com.sociorich.app.modals;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class DashModal implements Serializable {
    private String catNameStr;
    private String userNameStr;
    private String dateStr;
    private String postDataStr;
    private String profilePicStr;
    private String likeStr;
    private String verifyStr;
    private String socioCreStr;
    private String commentStr;
    private String postIdStr;
    private List<String> mediaList;
    private String mVeryfyStr;
    private String mLikeStr;
    private String userIdentity;
    private List<UserCommentModal> userCommentModals;
    private String categoryId;

    private List<String> testUsers;
    private List<String> testComments;

    private String desStr;
    private String postOwnerUserId;
    private String shareUsrName;
    private String shareDate;
    private String shareCat;
    private JSONObject shrObj;
    private JSONObject postObj;
    private String longTime;

    public List<String> getTestUsers() {
        return testUsers;
    }

    public void setTestUsers(List<String> testUsers) {
        this.testUsers = testUsers;
    }

    public List<String> getTestComments() {
        return testComments;
    }

    public void setTestComments(List<String> testComments) {
        this.testComments = testComments;
    }

    public List<UserCommentModal> getUserCommentModals() {
        return userCommentModals;
    }

    public List<CommentModal> commentModals;

    public void setUserCommentModals(List<UserCommentModal> userCommentModals) {
        this.userCommentModals = userCommentModals;
    }

    public String getCatNameStr() {
        return catNameStr;
    }

    public void setCatNameStr(String catNameStr) {
        this.catNameStr = catNameStr;
    }

    public String getUserNameStr() {
        return userNameStr;
    }

    public void setUserNameStr(String userNameStr) {
        this.userNameStr = userNameStr;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getPostDataStr() {
        return postDataStr;
    }

    public void setPostDataStr(String postDataStr) {
        this.postDataStr = postDataStr;
    }

    public String getProfilePicStr() {
        return profilePicStr;
    }

    public void setProfilePicStr(String profilePicStr) {
        this.profilePicStr = profilePicStr;
    }

    public String getLikeStr() {
        return likeStr;
    }

    public void setLikeStr(String likeStr) {
        this.likeStr = likeStr;
    }

    public String getVerifyStr() {
        return verifyStr;
    }

    public void setVerifyStr(String verifyStr) {
        this.verifyStr = verifyStr;
    }

    public String getSocioCreStr() {
        return socioCreStr;
    }

    public void setSocioCreStr(String socioCreStr) {
        this.socioCreStr = socioCreStr;
    }

    public String getCommentStr() {
        return commentStr;
    }

    public void setCommentStr(String commentStr) {
        this.commentStr = commentStr;
    }
    public String getPostIdStr() {
        return postIdStr;
    }

    public void setPostIdStr(String postIdStr) {
        this.postIdStr = postIdStr;
    }
    public List<String> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<String> mediaList) {
        this.mediaList = mediaList;
    }

    public String getmVeryfyStr() {
        return mVeryfyStr;
    }

    public void setmVeryfyStr(String mVeryfyStr) {
        this.mVeryfyStr = mVeryfyStr;
    }

    public String getmLikeStr() {
        return mLikeStr;
    }

    public void setmLikeStr(String mLikeStr) {
        this.mLikeStr = mLikeStr;
    }

    public String getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDesStr() {
        return desStr;
    }

    public void setDesStr(String desStr) {
        this.desStr = desStr;
    }

    public String getPostOwnerUserId() {
        return postOwnerUserId;
    }

    public void setPostOwnerUserId(String postOwnerUserId) {
        this.postOwnerUserId = postOwnerUserId;
    }

    public List<CommentModal> getCommentModals() {
        return commentModals;
    }

    public void setCommentModals(List<CommentModal> commentModals) {
        this.commentModals = commentModals;
    }

    public String getShareUsrName() {
        return shareUsrName;
    }

    public void setShareUsrName(String shareUsrName) {
        this.shareUsrName = shareUsrName;
    }

    public String getShareDate() {
        return shareDate;
    }

    public void setShareDate(String shareDate) {
        this.shareDate = shareDate;
    }

    public String getShareCat() {
        return shareCat;
    }

    public void setShareCat(String shareCat) {
        this.shareCat = shareCat;
    }

    public JSONObject getShrObj() {
        return shrObj;
    }

    public void setShrObj(JSONObject shrObj) {
        this.shrObj = shrObj;
    }

    public JSONObject getPostObj() {
        return postObj;
    }

    public void setPostObj(JSONObject postObj) {
        this.postObj = postObj;
    }

    public String getLongTime() {
        return longTime;
    }

    public void setLongTime(String longTime) {
        this.longTime = longTime;
    }
}