package com.newiplquizgame.myipl.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.Example;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import okhttp3.RequestBody;

import static com.newiplquizgame.myipl.extra.Common.isempty;
import static com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile.ISLOGINBYINT;

public class StartActivity extends AppCompatActivity implements APIcall.ApiCallListner {

    // Google log in
    GoogleSignInClient mGoogleSignInClient;
    public static String photo_Image = "";
    private static final int RC_SIGN_IN = 101;
    private ProgressDialog dialog;
    Example example;
    private SharedPreferenceManagerFile sharedPref;
    String onesignaltoken;
    // facebook Log in
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();

        sharedPref = new SharedPreferenceManagerFile(this);
        onesignaltoken = sharedPref.getFromStringSharedPreference(SharedPreferenceManagerFile.ONESIGNAL_TOKeN);

        Common.checkAndRequestPermissions(StartActivity.this);
        printHashKey(StartActivity.this);

        findViewBind();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.btn_Google_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isInternetAvailable(StartActivity.this)) {
                    signIn();
                } else {
                    Common.displayToastMessageShort(StartActivity.this, "No connection found. Please connect & try again.", true);
                }
            }
        });


        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        findViewById(R.id.btn_Facebook_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isInternetAvailable(StartActivity.this)) {
                    LoginManager.getInstance().logInWithReadPermissions(StartActivity.this, Arrays.asList("public_profile", "user_friends"));
                } else {
                    Common.displayToastMessageShort(StartActivity.this, "No connection found. Please connect & try again.", true);
                }
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if (AccessToken.getCurrentAccessToken() != null) { //error code  #155
                            Profile profile = Profile.getCurrentProfile();
                            if (profile != null) {
                                String User_id = loginResult.getAccessToken().getUserId();
                                String usertoken = loginResult.getAccessToken().getToken();
                                String User_Fname = Profile.getCurrentProfile().getFirstName() + " " + Profile.getCurrentProfile().getMiddleName() + " " + Profile.getCurrentProfile().getLastName();
                                String User_Profile = Profile.getCurrentProfile().getProfilePictureUri(500, 500).toString();
                                callAPIFacebooktoLogin(User_id, User_Fname, User_Profile);
                            }
                        }
                    }

                    @Override
                    public void onCancel() {
                        LoginManager.getInstance().logOut();
                        LoginManager.getInstance().logInWithReadPermissions(StartActivity.this, Arrays.asList("public_profile", "user_friends"));

                    }

                    @Override
                    public void onError(FacebookException exception) {

                    }
                });


    }

    private void findViewBind() {
        findViewById(R.id.bt_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isInternetAvailable(StartActivity.this)) {

                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                } else {
                    Common.displayToastMessageShort(StartActivity.this, "No connection found. Please connect & try again.", true);
                }
            }
        });

        findViewById(R.id.ll_Reg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isInternetAvailable(StartActivity.this)) {
                    startActivity(new Intent(StartActivity.this, RegistrationActivity.class));
                } else {
                    Common.displayToastMessageShort(StartActivity.this, "No connection found. Please connect & try again.", true);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.w("signin", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            try {
                photo_Image = isempty(account.getPhotoUrl().toString());
            } catch (Exception e) {
                photo_Image = "";
            }
            callAPIGoogletoLogin(account);


        }
    }

    private void callAPIGoogletoLogin(GoogleSignInAccount account) {
        Common.hideKeyboard(StartActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProfilePic", isempty(photo_Image));
            jsonObject.put("Email", isempty(account.getEmail().toString()));
            jsonObject.put("DisplayName", isempty(account.getDisplayName().toString()));
            jsonObject.put("TokenId", isempty(account.getId().toString()));
            jsonObject.put("LoginType", "2");
            jsonObject.put("OneSignalToken", onesignaltoken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url2 = AppConstant.GET_LOGIN;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url2, APIcall.OPERATION_LOGIN, (APIcall.ApiCallListner) StartActivity.this);
    }

    private void callAPIFacebooktoLogin(String user_id, String user_Fname, String user_Profile) {
        Common.hideKeyboard(StartActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProfilePic", isempty(user_Profile));
            jsonObject.put("DisplayName", isempty(user_Fname));
            jsonObject.put("TokenId", isempty(user_id));
            jsonObject.put("LoginType", "1");
            jsonObject.put("OneSignalToken", onesignaltoken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url2 = AppConstant.GET_LOGIN;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url2, APIcall.OPERATION_LOGIN, (APIcall.ApiCallListner) StartActivity.this);
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_LOGIN) {
            showDialog();
        }
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            if (operationCode == APIcall.OPERATION_LOGIN) {
                Log.i("onSuccess", "" + response);
                Gson gson = new Gson();
                example = gson.fromJson(response, Example.class);
                if (example.getAccessToken() != null) {
                    sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.SESSION_GUID, isempty(example.getTokenType()) + " " + isempty(example.getAccessToken()));
                    sharedPref.setBooleanSharedPreference(SharedPreferenceManagerFile.ISLOGIN, true);
                    sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.NICKNAME, "Your Nick Name");
                    sharedPref.setIntSharedPreference(SharedPreferenceManagerFile.ISLOGINBYINT, example.getUser().getLoginType());
                    sharedPref.setIntSharedPreference(SharedPreferenceManagerFile.USERID, example.getUser().getUserId());
                    sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.EMAIL, isempty(example.getUser().getEmail()));
                    sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.USER_NAME, isempty(example.getUser().getDisplayName()));
                    sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.USER_PROFILE_PIC, isempty(example.getUser().getPhoto()));
                    Intent intent = new Intent(StartActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    Common.displayToastMessageShort(StartActivity.this, "Successful login", true);
                } else {
                    try {
                        JSONObject json = new JSONObject(response);
                        String json_LL = json.getString("invalid_grant");
                        Log.i("SUMIT_PATEL", "GET_LOGIN" + json_LL);
                        Toast.makeText(this, "" + json_LL, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {

        }
        hideDialog();
    }

    @Override
    public void onFail(int operationCode, String response) {
        hideDialog();
    }

    private void showDialog() {
        dialog = new ProgressDialog(StartActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("SUMITPATEL", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            //   Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            //     Log.e(TAG, "printHashKey()", e);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(StartActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

}
