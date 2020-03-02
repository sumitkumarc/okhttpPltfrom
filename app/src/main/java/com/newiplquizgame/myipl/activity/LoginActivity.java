package com.newiplquizgame.myipl.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

import okhttp3.RequestBody;

import static com.newiplquizgame.myipl.extra.AppValidation.validateEmail;
import static com.newiplquizgame.myipl.extra.AppValidation.validatePassword;
import static com.newiplquizgame.myipl.extra.Common.isempty;

public class LoginActivity extends AppCompatActivity implements APIcall.ApiCallListner {

    private ProgressDialog dialog;
    Example example;
    private SharedPreferenceManagerFile sharedPref;
    TextInputEditText ed_email_id;
    TextInputEditText ed_password;
    String onesignaltoken;
    TextInputLayout ti_password;
    TextInputLayout ti_email_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPref = new SharedPreferenceManagerFile(this);
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        onesignaltoken = status.getSubscriptionStatus().getUserId();
        findViewBind();
    }

    private void findViewBind() {
        ti_email_id = findViewById(R.id.ti_email_id);
        ti_password = findViewById(R.id.ti_password);

        ed_email_id = findViewById(R.id.ed_email_id);
        ed_password = findViewById(R.id.ed_password);

        findViewById(R.id.ll_Reg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                finish();
            }
        });
        ((TextInputEditText) findViewById(R.id.ed_password)).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        findViewById(R.id.bt_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail(ed_email_id.getText().toString().trim(), ti_email_id) | !validatePassword(ed_password.getText().toString().trim(), ti_password)) {
                    return;
                }
                if (Common.isInternetAvailable(LoginActivity.this)) {
                    callAPItoLogin(ed_email_id.getText().toString(), ed_password.getText().toString(), 0);
                } else {
                    Common.displayToastMessageShort(LoginActivity.this, "No connection found. Please connect & try again.", true);
                }
            }
        });
    }

    private void callAPItoLogin(String emailid, String password, int loginType) {
        Common.hideKeyboard(LoginActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Email", emailid);
            jsonObject.put("Password", password);
            jsonObject.put("LoginType", loginType);
            jsonObject.put("OneSignalToken", onesignaltoken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url2 = AppConstant.GET_LOGIN;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url2, APIcall.OPERATION_LOGIN, this);
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
            Gson gson = new Gson();
            example = gson.fromJson(response, Example.class);

            if (example.getAccessToken() != null) {
                sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.SESSION_GUID, isempty(example.getTokenType()) + " " + isempty(example.getAccessToken()));
                sharedPref.setBooleanSharedPreference(SharedPreferenceManagerFile.ISLOGIN, true);
                sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.NICKNAME, example.getUser().getNickName());
                sharedPref.setIntSharedPreference(SharedPreferenceManagerFile.ISLOGINBYINT, example.getUser().getLoginType());
                sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.EMAIL, isempty(example.getUser().getEmail()));
                sharedPref.setIntSharedPreference(SharedPreferenceManagerFile.USERID, example.getUser().getUserId());
                sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.USER_NAME, isempty(example.getUser().getDisplayName()));
                sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.USER_PROFILE_PIC, isempty(example.getUser().getPhoto()));
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
                Common.displayToastMessageShort(LoginActivity.this, "Successful login", true);
                cleared();
            } else {
                try {
                    JSONObject json = new JSONObject(response);
                    String json_LL = json.getString("invalid_grant");
                    Common.displayToastMessageShort(LoginActivity.this, json_LL, true);
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
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    private void cleared() {
        ed_email_id.setText("");
        ed_password.setText("");
    }
}
