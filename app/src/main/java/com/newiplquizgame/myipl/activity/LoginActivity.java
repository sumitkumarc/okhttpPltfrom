package com.newiplquizgame.myipl.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.Example;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

import static com.newiplquizgame.myipl.extra.Common.EMAIL_PATTEN;
import static com.newiplquizgame.myipl.extra.Common.isempty;

public class LoginActivity extends AppCompatActivity implements APIcall.ApiCallListner {

    private ProgressDialog dialog;
    Example example;
    private SharedPreferenceManagerFile sharedPref;
    TextInputEditText ed_email_id;
    TextInputEditText ed_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPref = new SharedPreferenceManagerFile(this);
        findViewBind();
    }

    private void findViewBind() {
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
                if (ed_email_id.getText().toString().equals("") || ed_email_id.getText().toString() == null) {
                    Common.displayToastMessageShort(LoginActivity.this, "Enter your email-id.", true);
                    return;
                }
                if (ed_password.getText().toString().equals("") || ed_password.getText().toString() == null) {
                    Common.displayToastMessageShort(LoginActivity.this, "Enter your password.", true);
                    return;
                }

                if (ed_email_id.getText().toString().matches(EMAIL_PATTEN)) {
                    if (Common.isInternetAvailable(LoginActivity.this)) {
                        callAPItoLogin(ed_email_id.getText().toString(), ed_password.getText().toString(), 0);
                    } else {
                        Common.displayToastMessageShort(LoginActivity.this, "No connection found. Please connect & try again.", true);
                    }
                    return;
                } else {
                    Common.displayToastMessageShort(LoginActivity.this, "Enter Your Email Valid Address.", true);
                    return;
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
                sharedPref.setInSharedPreference(SharedPreferenceManagerFile.SESSION_GUID, isempty(example.getTokenType()) + " " + isempty(example.getAccessToken()));
                sharedPref.setBooleanSharedPreference(SharedPreferenceManagerFile.ISLOGIN, true);
                sharedPref.setInSharedPreference(SharedPreferenceManagerFile.EMAIL, isempty(example.getUser().getEmail()));
                sharedPref.setInSharedPreference(SharedPreferenceManagerFile.USER_NAME, isempty(example.getUser().getDisplayName()));
                sharedPref.setInSharedPreference(SharedPreferenceManagerFile.USER_PROFILE_PIC, isempty(example.getUser().getPhoto()));
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
                Common.displayToastMessageShort(LoginActivity.this, "Successful login", true);
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
}
