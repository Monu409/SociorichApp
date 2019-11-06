package com.sociorich.app.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sociorich.app.R;
import com.sociorich.app.app_utils.ConstantMethods;
import com.sociorich.app.app_utils.UserRequestCustom;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.sociorich.app.app_utils.AppApis.BASE_URL;
import static com.sociorich.app.app_utils.AppApis.SOCIAL_LOGIN;


public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener{
    EditText email, password;
    String url = BASE_URL + "oauth/token";
    String purl = BASE_URL + "api/v1/user/currentuserprofile";
    static public String username, coverpic, profilepic, socialmoneybalance, equamoneybalance, identity;
    ProgressDialog dialog;
    private TextView forgotText;
    //  public MyDialog dialog;
    Context context;
    StringRequestCustom stringRequest;
    StringRequestCustom stringRequest1;
    private RequestQueue requestQueue;
    //private String TAG = JsonRequestActivity.class.getSimpleName();
    UserRequestCustom stringRequest12;
    private ProgressDialog pDialog;
    private String emailIdFacebook;

    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private TextView createAccTxt,termsTxt;

    private static final int RC_SIGN_IN = 234;

    //Tag for the logs optional
    private static final String TAG = "simplifiedcoding";

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;

    //And also a Firebase Auth object
    FirebaseAuth mAuth;

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private TwitterLoginButton twitterLoginButton;
    String displayName = "";
    private RelativeLayout facebookRel,googleRel,twitterRel;
    LoginButton loginButton;


    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
//            displayMessage(profile);

            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("LoginActivity", response.toString());
                            try {
                                displayName = object.getString("name");
                                String email = response.getJSONObject().getString("email");
                                socialLogin(email,displayName);
                            }catch(Exception e){
                                e.printStackTrace();
                                noEmailPopup("Facebook");
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Login");
        ConstantMethods.printHashKey(this);
        init();
        email = (EditText) findViewById(R.id.email_edt);
        password = (EditText) findViewById(R.id.password_edt);
        createAccTxt = findViewById(R.id.create_acc_txt);
        pDialog = new ProgressDialog(this);
        forgotText = findViewById(R.id.forgot_txt);
        termsTxt = findViewById(R.id.terms_txt);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        facebookRel = findViewById(R.id.facebook_rel);
        googleRel = findViewById(R.id.google_rel);
        twitterRel = findViewById(R.id.twitter_rel);
        facebookRel.setOnClickListener(v->{
            loginButton.performClick();
            ConstantMethods.setStringPreference("login_type","social",this);
        });
        googleRel.setOnClickListener(v->{
            ConstantMethods.setStringPreference("login_type","social",this);
            signIn();
        });
        twitterRel.setOnClickListener(v-> {
            twitterLoginButton.performClick();
            ConstantMethods.setStringPreference("login_type","social",this);
        });
        //Context.context=this.context;
        Button loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(v -> {
            UserLogin();
            ConstantMethods.setStringPreference("login_type","normal",this);
        });
        termsTxt.setOnClickListener(t->{
            Intent intent = new Intent(this, AboutUsActivity.class);
            intent.putExtra("url_is",BASE_URL+"terms");
            intent.putExtra("title_is","Terms & Conditions");
            startActivity(intent);
        });
        forgotText.setOnClickListener(v -> startActivity(new Intent(this, ForgetPassword.class)));
        createAccTxt.setOnClickListener(v->startActivity(new Intent(this, CreateAccActivity.class)));
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Now we will attach a click listener to the sign_in_button
        //and inside onClick() method we are calling the signIn() method that will open
        //google sign in intent

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("user_birthday", "email"));
        //loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, callback);
        FacebookSdk.sdkInitialize(getApplicationContext());



        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        twitterLoginButton = findViewById(R.id.default_twitter_login_button);
//        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
//            @Override
//            public void success(Result<TwitterSession> result) {
//                Log.e("res",result.toString());
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                Log.e("res",exception.toString());
//            }
//        });

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
                AccountService accountService = twitterApiClient.getAccountService();
                Call<com.twitter.sdk.android.core.models.User> call = accountService.verifyCredentials(true, true, true);
                call.enqueue(new Callback<com.twitter.sdk.android.core.models.User>() {
                    @Override
                    public void success(Result<com.twitter.sdk.android.core.models.User> result) {
                        User user = result.data;
                        String fullname = user.name;
//                        long twitterID = user.getId();
//                        String userSocialProfile = user.profileImageUrl;
                        String userEmail = user.email;
//                        String userFirstNmae = fullname.substring(0, fullname.lastIndexOf(" "));
//                        String userLastNmae = fullname.substring(fullname.lastIndexOf(" "));
//                        String userScreenName = user.screenName;
                        socialLogin(userEmail,fullname);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        noEmailPopup("Twitter");
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });

        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
//        TwitterAuthClient authClient = new TwitterAuthClient();
//        authClient.requestEmail(session, new Callback<String>() {
//            @Override
//            public void success(Result<String> result) {
//                Log.e("res",result.toString());
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                Log.e("res",exception.toString());
//            }
//        });

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText("Log in with Google");
                tv.setTextSize(16);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(30, 0, 0, 0);
                return;
            }
        }

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    public void UserLogin() {
        String normEmail = email.getText().toString();
        String normPass = password.getText().toString();
        if (normEmail.equals("") || normPass.equals("")) {
            Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
        } else {
            verifyCode(normEmail,normPass);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    private void verifyCode(String normEmail, String normPass) {
        //   dialog.ShowProgressDialog();
        if (requestQueue == null) {
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("please wait");
            dialog.show();
            dialog.setCancelable(false);
            requestQueue = Volley.newRequestQueue(LoginActivity.this);
        }
        stringRequest = new StringRequestCustom(LoginActivity.this, Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    //  dialog.CancelProgressDialog();
                    JSONObject obj = new JSONObject(response);
                    String accesstoken = obj.getString("access_token");
                    String token_type = obj.getString("access_token");
                    String refresh_token = obj.getString("refresh_token");
                    ConstantMethods.setStringPreference("login_status", "login", LoginActivity.this);
                    ConstantMethods.setStringPreference("user_token", accesstoken, LoginActivity.this);

                    ConstantMethods.setStringPreference("norm_email", normEmail, LoginActivity.this);
                    ConstantMethods.setStringPreference("norm_pass", normPass, LoginActivity.this);
//                    User_Profile.usertoken=accesstoken;
//                    Methods.saveAccessToken(LoginActivity.this,accesstoken);
                    ConstantMethods.saveTokenRefresh(LoginActivity.this,refresh_token);
//                    Methods.saveSession(LoginActivity.this,"true");
                    verifyCode_user();
                    /*if (error.equals("true")) {*/


                    //  Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();


                   /* } else {
                       // TastyToast.makeText(getApplicationContext(), obj.getString("msg"), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_LONG).show();
                    }*/
                } catch (JSONException e) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                // dialog.CancelProgressDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("grant_type", "password");
                params.put("username", normEmail);
                params.put("password", normPass);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void verifyCode_user() {
        //   dialog.ShowProgressDialog();
        if (requestQueue == null) {
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("please wait");
            dialog.show();
            dialog.setCancelable(false);
            requestQueue = Volley.newRequestQueue(LoginActivity.this);
        }
        stringRequest12 = new UserRequestCustom(LoginActivity.this, Request.Method.GET, purl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    //  dialog.CancelProgressDialog();
                    JSONObject ob1j = new JSONObject(response);
                    username = ob1j.getString("displayName");
                    identity = ob1j.getString("identity");
                    ConstantMethods.saveUserID(LoginActivity.this, identity);
                    String profileType = ob1j.getString("profileType");
                    String displayName = ob1j.getString("displayName");
                    String phoneNo = ob1j.getString("phoneNo");
                    String email = ob1j.getString("email");
                    if(profileType.equals("INDV")) {
                        String firstName = ob1j.getString("firstName");
                        ConstantMethods.setStringPreference("first_name", firstName, LoginActivity.this);
                    }
                    else if(profileType.equals("ORG")){
                        JSONObject adminObj = ob1j.getJSONObject("admin");
                        String firstName = adminObj.getString("name");
                        ConstantMethods.setStringPreference("first_name", firstName, LoginActivity.this);
                    }
                    ConstantMethods.setStringPreference("display_name_prif", displayName, LoginActivity.this);
                    ConstantMethods.setStringPreference("phone_no_prif", phoneNo, LoginActivity.this);
                    ConstantMethods.setStringPreference("email_prif", email, LoginActivity.this);

                    //   Toast.makeText(getApplicationContext(),"Invalid username or password-2289",Toast.LENGTH_SHORT).show();
                    Intent intent_call = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent_call.addFlags(intent_call.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent_call);

                } catch (JSONException e) {
                    Intent intent_call = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent_call.addFlags(intent_call.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent_call);
                    // Toast.makeText(getApplicationContext(),"Invalid username or password---111",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent_call = new Intent(LoginActivity.this, DashboardActivity.class);
                intent_call.addFlags(intent_call.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_call);
                //  Toast.makeText(getApplicationContext(),"Invalid username or password-222",Toast.LENGTH_SHORT).show();
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                // dialog.CancelProgressDialog();
            }
        }) {
        };

        stringRequest12.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest12);
    }

    private void signIn() {
        //getting the google signin intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mGoogleApiClient.clearDefaultAccountAndReconnect();
        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        if (mAuth.getCurrentUser() != null) {
            finish();
//            startActivity(new Intent(this, ProfileActivity.class));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    private void displayMessage(Profile profile){
        if(profile != null){
            Log.e("name is",profile.getName());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        Profile profile = Profile.getCurrentProfile();
//        displayMessage(profile);
//    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(LoginActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
//            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();
            socialLogin(email,personName);


        } else {
            // Signed out, show unauthenticated UI.

        }
    }

    private void socialLogin(String email, String displayName){
        ConstantMethods.showProgressbar(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email",email);
            jsonObject.put("displayName",displayName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking
                .post(SOCIAL_LOGIN)
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ConstantMethods.dismissProgressBar();
                        try {
                            ConstantMethods.setStringPreference("email_prif",email,LoginActivity.this);
                            ConstantMethods.setStringPreference("first_name",displayName,LoginActivity.this);
                            String identity = response.getString("identity");
                            String accesstoken = response.getString("accessToken");
                            ConstantMethods.saveUserID(LoginActivity.this, identity);
                            ConstantMethods.setStringPreference("login_status", "login", LoginActivity.this);
                            ConstantMethods.setStringPreference("user_token", accesstoken, LoginActivity.this);
                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            Intent intent_call = new Intent(LoginActivity.this, DashboardActivity.class);
                            intent_call.addFlags(intent_call.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent_call);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ConstantMethods.dismissProgressBar();
                        Toast.makeText(LoginActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void init(){

        TwitterAuthConfig authConfig = new TwitterAuthConfig(getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY), getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET));
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(authConfig)
                .debug(true)
                .build();
        Twitter.initialize(config);

    }

    private String noEmailPopup(String socialType){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        final EditText emailEdt = new EditText(this);
        alert.setMessage("You don't have email id on "+socialType);
        alert.setTitle("Enter Your Email");

        alert.setView(emailEdt);

        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                emailIdFacebook = emailEdt.getText().toString();
                if(ConstantMethods.isValidMail(emailIdFacebook)){
                    socialLogin(emailIdFacebook,displayName);
                }
                else {
                    Toast.makeText(LoginActivity.this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
                    LoginManager.getInstance().logOut();
                }
            }
        });
        alert.show();
        return emailIdFacebook;
    }

}
