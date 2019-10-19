package com.sociorich.app.app_utils;

public interface AppApis {
//    String BASE_URL = "http://dev.sociorich.com/";
    String BASE_URL = "https://www.sociorich.com/";
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
    String BANNER_EDIT = BASE_URL+"api/v1/user/coverpic";
    String BANNER_DELETE = BASE_URL+"api/v1/user//coverpic";
    String CHANGE_NUMBER = BASE_URL+"api/v1/user/verifyphoneupdate";
    String SHOW_PROFILE = BASE_URL+"api/v1/user/profile/";
    String MY_NETWORK = BASE_URL+"api/v1/connections/my?pageno=0";
    String POST_CATEGORY = BASE_URL+"api/v1/postcategory/all";
    String MY_LOAD_COMMENT = BASE_URL+"api/v1/post/";
    String USER_CONNECTIONS = BASE_URL+"api/v1/connections/user/";
    String DELETE_COMMENT = BASE_URL+"api/v1/post/comment/";
    String UPDATE_COMMENT = BASE_URL+"api/v1/post/comment/update";

//    String OTHER_POST = BASE_URL+"api/v1/post/byuser/"
//    api/v1/conn/request/cancel  api/v1/post/byuser/3d6659a7-1791-4180-a0c6-f0d1d127dba0?pageno=0

//    http://dev.sociorich.com/api/v1/post/global?pageno=http://dev.sociorich.com/api/v1/user/profile/5f73505f-4b5a-4beb-bad6-5060b549bd42/
//    http://dev.sociorich.com/api/v1/user//coverpic
}
