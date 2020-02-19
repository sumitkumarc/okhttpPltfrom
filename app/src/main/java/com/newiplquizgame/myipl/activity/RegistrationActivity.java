package com.newiplquizgame.myipl.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.Example;
import com.newiplquizgame.myipl.pkg.Register;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;

import static com.newiplquizgame.myipl.extra.Common.EMAIL_PATTEN;
import static com.newiplquizgame.myipl.extra.Common.isempty;

public class RegistrationActivity extends AppCompatActivity implements APIcall.ApiCallListner {

    Toolbar mToolbar;
    private ProgressDialog dialog;
    Register mregister;
    Example example;
    private SharedPreferenceManagerFile sharedPref;
    TextInputEditText ed_name;
    TextInputEditText ed_email_id;
    TextInputEditText ed_date_birth;
    TextInputEditText ed_password;
    String u_name;
    String u_email;
    String u_password;
    String u_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        sharedPref = new SharedPreferenceManagerFile(this);
        findViewBind();


    }


    private void findViewBind() {
        ed_name = findViewById(R.id.ed_name);
        ed_email_id = findViewById(R.id.ed_email_id);
        ed_date_birth = findViewById(R.id.ed_date_birth);
        ed_password = findViewById(R.id.ed_password);

        findViewById(R.id.ll_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });
        ((TextInputEditText) findViewById(R.id.ed_password)).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        findViewById(R.id.bt_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_name.getText().toString().equals("") || ed_name.getText().toString() == null) {
                    Common.displayToastMessageShort(RegistrationActivity.this, "Enter your name.", true);
                    return;
                }
                if (ed_email_id.getText().toString().equals("") || ed_email_id.getText().toString() == null) {
                    Common.displayToastMessageShort(RegistrationActivity.this, "Enter your emailid.", true);
                    return;
                }

//                if (ed_date_birth.getText().toString().equals("") || ed_date_birth.getText().toString() == null) {
//                    Toast.makeText(RegistrationActivity.this, "Enter your password.", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if (ed_password.getText().toString().equals("") || ed_password.getText().toString() == null) {
                    Common.displayToastMessageShort(RegistrationActivity.this, "Enter your password.", true);
                    return;
                }
                if (ed_email_id.getText().toString().matches(EMAIL_PATTEN)) {
                    if (Common.isInternetAvailable(RegistrationActivity.this)) {
                        u_name = ed_name.getText().toString();
                        u_email = ed_email_id.getText().toString();
                        u_password = ed_password.getText().toString();
                        callAPItoRegistration(u_name, u_email, u_password, u_profile);
                    } else {
                        Common.displayToastMessageShort(RegistrationActivity.this, "No connection found. Please connect & try again.", true);
                    }
                    return;
                } else {
                    Common.displayToastMessageShort(RegistrationActivity.this, "Enter Your Email Valid Address.", true);
                    return;
                }
            }
        });

    }

    private void callAPItoRegistration(String m_name, String m_email, String m_password, String m_profile) {
        Common.hideKeyboard(RegistrationActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProfilePic", m_profile);
            jsonObject.put("Email", m_email);
            jsonObject.put("Password", m_password);
            jsonObject.put("DisplayName", m_name);
            jsonObject.put("LoginType", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url2 = AppConstant.GET_SING_UP;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url2, APIcall.OPERATION_REGEGISTRATION, (APIcall.ApiCallListner) RegistrationActivity.this);
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_REGEGISTRATION) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_LOGIN) {
            showDialog();
        }
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        if (operationCode == APIcall.OPERATION_REGEGISTRATION) {
            Log.i("onSuccess", "" + response);
            Gson gson = new Gson();
            mregister = gson.fromJson(response, Register.class);
            if (mregister.getStatus() == 1) {
                Common.displayToastMessageShort(RegistrationActivity.this, isempty(mregister.getMsg()), true);
            } else {
                Common.displayToastMessageShort(RegistrationActivity.this, isempty(mregister.getMsg()), true);
                callAPItoLogin();
            }
        }
        if (operationCode == APIcall.OPERATION_LOGIN) {
            Gson gson = new Gson();
            example = gson.fromJson(response, Example.class);
            if (example.getAccessToken() != null) {
                sharedPref.setInSharedPreference(SharedPreferenceManagerFile.SESSION_GUID, isempty(example.getTokenType()) + " " + isempty(example.getAccessToken()));
                sharedPref.setBooleanSharedPreference(SharedPreferenceManagerFile.ISLOGIN, true);
                sharedPref.setInSharedPreference(SharedPreferenceManagerFile.EMAIL, isempty(example.getUser().getEmail()));
                sharedPref.setInSharedPreference(SharedPreferenceManagerFile.USER_NAME, isempty(example.getUser().getDisplayName()));
                sharedPref.setInSharedPreference(SharedPreferenceManagerFile.USER_PROFILE_PIC, isempty(example.getUser().getPhoto()));
                Intent intent = new Intent(RegistrationActivity.this, DashboardActivity.class);
                startActivity(intent);
                Common.displayToastMessageShort(RegistrationActivity.this, "Successful login", true);
            } else {
                try {
                    JSONObject json = new JSONObject(response);
                    String json_LL = json.getString("invalid_grant");
                    Common.displayToastMessageShort(RegistrationActivity.this, json_LL, true);
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
        dialog = new ProgressDialog(RegistrationActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    private void callAPItoLogin() {
        Common.hideKeyboard(RegistrationActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Email", this.u_email);
            jsonObject.put("Password", this.u_password);
            jsonObject.put("DisplayName", this.u_name);
            jsonObject.put("LoginType", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url2 = AppConstant.GET_LOGIN;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url2, APIcall.OPERATION_LOGIN, (APIcall.ApiCallListner) RegistrationActivity.this);
    }

}
