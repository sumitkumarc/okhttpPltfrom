package com.newiplquizgame.myipl.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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

import java.util.Arrays;

import okhttp3.RequestBody;

import static com.newiplquizgame.myipl.extra.Common.isempty;

public class StartActivity extends AppCompatActivity implements APIcall.ApiCallListner {

    // Google log in
    GoogleSignInClient mGoogleSignInClient;
    public static String photo_Image = "";
    private static final int RC_SIGN_IN = 101;
    private ProgressDialog dialog;
    Example example;
    private SharedPreferenceManagerFile sharedPref;

    // facebook Log in
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = new SharedPreferenceManagerFile(this);
        Common.checkAndRequestPermissions(StartActivity.this);
       // ((ImageView) findViewById(R.id.iv_image)).setImageBitmap(Common.decodeBase64(s));

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
                OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
                String userID = status.getSubscriptionStatus().getUserId();
//                String userID = "2b9c5829-8099-497f-9546-2ca9f89c90e6";
                boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();

             //   textView.setText("Subscription Status, is subscribed:" + isSubscribed);

                if (!isSubscribed)
                    return;

                try {
                    OneSignal.postNotification(new JSONObject("{'contents': {'en':'Tag substitution value for key1 = {{key1}}'}, " +
                                    "'include_player_ids': ['" + userID + "'], " +
                                    "'headings': {'en': 'Tag sub Title HI {{user_name}}'}, " +
                                    "'data': {'openURL': 'https://imgur.com'}," +
                                    "'buttons':[{'id': 'id1', 'text': ''}, {'id':'id2', 'text': 'Joint Group'}]}"),

                            new OneSignal.PostNotificationResponseHandler() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    Log.i("OneSignalExample", "postNotification Success: " + response);
                                }

                                @Override
                                public void onFailure(JSONObject response) {
                                    Log.e("OneSignalExample", "postNotification Failure: " + response);
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }



//                if (Common.isInternetAvailable(StartActivity.this)) {
//                    LoginManager.getInstance().logInWithReadPermissions(StartActivity.this, Arrays.asList("public_profile", "user_friends"));
//                } else {
//                    Common.displayToastMessageShort(StartActivity.this, "No connection found. Please connect & try again.", true);
//                }

            }
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(StartActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(StartActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(StartActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
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
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
            callAPItoLogin(account, 2);
            Log.d("SUMITPATEL", "ID" + account.getId());


        }
    }

    private void callAPItoLogin(GoogleSignInAccount account, int loginType) {
        Common.hideKeyboard(StartActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProfilePic", isempty(photo_Image));
            jsonObject.put("Email", isempty(account.getEmail().toString()));
            jsonObject.put("DisplayName", isempty(account.getDisplayName().toString()));
            jsonObject.put("TokenId", isempty(account.getId().toString()));
            jsonObject.put("LoginType", "2");
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
        if (operationCode == APIcall.OPERATION_LOGIN) {
            Log.i("onSuccess", "" + response);
            Gson gson = new Gson();
            example = gson.fromJson(response, Example.class);
            if (example.getAccessToken() != null) {
                sharedPref.setInSharedPreference(SharedPreferenceManagerFile.SESSION_GUID, isempty(example.getTokenType()) + " " + isempty(example.getAccessToken()));
                sharedPref.setBooleanSharedPreference(SharedPreferenceManagerFile.ISLOGIN, true);
                sharedPref.setInSharedPreference(SharedPreferenceManagerFile.EMAIL, isempty(example.getUser().getEmail()));
                sharedPref.setInSharedPreference(SharedPreferenceManagerFile.USER_NAME, isempty(example.getUser().getDisplayName()));
                sharedPref.setInSharedPreference(SharedPreferenceManagerFile.USER_PROFILE_PIC, isempty(example.getUser().getPhoto()));
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

}
