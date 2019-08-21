package com.app.sociorichapp.app_utils;

public interface AppApis {
    String BASE_URL = "http://dev.sociorich.com/";
    String INDIVISUAL_SIGNUP = BASE_URL+"api/v1/user/create";
    String HOMEPAGE_URL_2 = BASE_URL+"api/v1/postcategory/all";
    String HOMEPAGE_URL_LOGOUT = BASE_URL+"api/v1/post/featured";
    String HOMEPAGE_URL_LOGIN = BASE_URL+"api/v1/post/global?pageno=";
    String COUNTRY_URL = BASE_URL+"assets/JSON/countries.json";
    String OTP_URL = BASE_URL+"api/v1/user/verifyph";
    String UPLAOD_SINGLE_IMAGE = BASE_URL+"api/v1/media/upload";
    String MY_INTEREST_URL = BASE_URL+"api/v1/post/userinterest?pageno=";
    String MY_NETWORK_URL = BASE_URL+"api/v1/post/usernetwork?pageno=";
    String MY_POST_URL = BASE_URL+"api/v1/post/byuser/";
    String MY_COMMENT = BASE_URL+"api/v1/post/comment/create";
    String MY_UPDATE_COMMENT = BASE_URL+"v1/post/ffad5032-0144-4d24-a9fc-6529c16adfa3/comments?pageno=4";
    String MY_INSPIRE_VERIFY = BASE_URL+"api/v1/expr/create";
    String UPLOAD_PROFILE_PICK = BASE_URL+"api/v1/user/profilepic";
    String GET_PROFILE_PICK = BASE_URL+"api/v1/user/currentuserprofile";
    String SAVE_PROFILE_INTRO = BASE_URL+"api/v1/user/profile";
    String SAVE_ABOUT_UPDATE = BASE_URL+"api/v1/user/profile";
    String GALLERY_DATA = BASE_URL+"api/v1/media/byuser/";
    String FEEDBACK_DATA = BASE_URL+"api/v1/site/contactus";
    String CHANGE_PASSWORD = BASE_URL+"api/v1/user/changepwd";
    String MY_INTEREST = BASE_URL+"api/v1/user/profile";
    String UPDATE_PHONENO = BASE_URL+"api/v1/user/phoneupdate";
    String SEND_REWARD = BASE_URL+"api/v1/donate";
    String SHARE_POST = BASE_URL+"api/v1/post/create";
    String SEARCH_FRIEND_POST = BASE_URL+"api/v1/search/people?q=";
    String SEARCH_POST = BASE_URL+"api/v1/search/post?q=";
    String UPDATE_POST = BASE_URL+"api/v1/post/update";
    String DELETE_POST = BASE_URL+"api/v1/post/";
    String SEND_REQUEST = BASE_URL+"api/v1/conn/request";
    String CANCEL_REQUEST = BASE_URL+"api/v1/conn/request/cancel";
    String ALL_NOTIFICATION = BASE_URL+"api/v1/user/notifications?pageno=0";
    String GET_BANNER_DATA = BASE_URL+"api/v1/post/sotd";
    String SOCIAL_LOGIN = BASE_URL+"api/v1/user/signup/mobile";
//    api/v1/conn/request/cancel

//    http://dev.sociorich.com/api/v1/post/featured
}
