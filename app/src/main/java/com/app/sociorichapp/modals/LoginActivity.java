package com.app.sociorichapp.modals;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.app.sociorichapp.R;
import com.app.sociorichapp.activities.AboutUsActivity;
import com.app.sociorichapp.activities.BaseActivity;
import com.app.sociorichapp.activities.CreateAccActivity;
import com.app.sociorichapp.activities.DashboardActivity;
import com.app.sociorichapp.activities.ForgetPassword;
import com.app.sociorichapp.activities.StringRequestCustom;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.app.sociorichapp.app_utils.UserRequestCustom;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.app.sociorichapp.app_utils.AppApis.BASE_URL;


public class LoginActivity extends BaseActivity {
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

    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private TextView createAccTxt,termsTxt;

    private static final int RC_SIGN_IN = 234;

    //Tag for the logs optional
    private static final String TAG = "simplifiedcoding";

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;

    //And also a Firebase Auth object
    FirebaseAuth mAuth;

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            displayMessage(profile);
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
        email = (EditText) findViewById(R.id.email_edt);
        password = (EditText) findViewById(R.id.password_edt);
        createAccTxt = findViewById(R.id.create_acc_txt);
        pDialog = new ProgressDialog(this);
        forgotText = findViewById(R.id.forgot_txt);
        termsTxt = findViewById(R.id.terms_txt);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        //Context.context=this.context;
        Button loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(v -> {
            UserLogin();
        });
        termsTxt.setOnClickListener(t->{
            Intent intent = new Intent(this, AboutUsActivity.class);
            intent.putExtra("url_is","http://dev.sociorich.com/terms");
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
                .requestIdToken(getString(R.string.action_sign_in))
                .requestEmail()
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
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
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
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    public void UserLogin() {
        if (email.getText().toString().equals("") || password.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
        } else {
            verifyCode();
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

    private void verifyCode() {
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
//                    User_Profile.usertoken=accesstoken;
//                    Methods.saveAccessToken(LoginActivity.this,accesstoken);
//                    Methods.saveTokenRefresh(LoginActivity.this,refresh_token);
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
                params.put("username", email.getText().toString());
                params.put("password", password.getText().toString());
                Log.e("params", String.valueOf(params));
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
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

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

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String mailId = account.getEmail();

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
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

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }

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
}
