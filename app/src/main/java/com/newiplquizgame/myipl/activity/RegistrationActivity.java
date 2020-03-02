package com.newiplquizgame.myipl.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.Example;
import com.newiplquizgame.myipl.pkg.Register;
import com.newiplquizgame.myipl.pkg.UserFileUpload;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import RestClient.ApiClient;
import RestClient.ApiInterface;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.newiplquizgame.myipl.extra.AppValidation.validateEmail;
import static com.newiplquizgame.myipl.extra.AppValidation.validateImage;
import static com.newiplquizgame.myipl.extra.AppValidation.validateMatchingPassword;
import static com.newiplquizgame.myipl.extra.AppValidation.validateName;
import static com.newiplquizgame.myipl.extra.AppValidation.validateNickName;
import static com.newiplquizgame.myipl.extra.AppValidation.validatePassword;
import static com.newiplquizgame.myipl.extra.Common.getCompressed;
import static com.newiplquizgame.myipl.extra.Common.isempty;

public class RegistrationActivity extends AppCompatActivity implements APIcall.ApiCallListner {

    Toolbar mToolbar;
    private ProgressDialog dialog;
    Register mregister;
    Example example;
    private SharedPreferenceManagerFile sharedPref;
    TextInputEditText ed_name;
    TextInputEditText ed_email_id;
    TextInputEditText ed_nick_name;
    TextInputEditText ed_password;
    TextInputEditText ed_con_password;

    String u_name;
    String u_email;
    String u_password;
    String u_nickname;
    String u_profile = "";
    CircleImageView iv_add_profile;
    CircleImageView iv_profile;


    TextInputLayout ti_name;
    TextInputLayout ti_nick_name;
    TextInputLayout ti_email_id;
    TextInputLayout ti_password;
    TextInputLayout ti_con_password;

    public static Integer SELECT_PICTURE = 101;
    String imageUploadpath;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.get("image/*");
    UserFileUpload userFileUpload;

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
        ed_nick_name = findViewById(R.id.ed_nick_name);
        ed_password = findViewById(R.id.ed_password);
        iv_add_profile = findViewById(R.id.iv_add_profile);
        iv_profile = findViewById(R.id.iv_profile);
        ed_con_password = findViewById(R.id.ed_con_password);


        ti_name = findViewById(R.id.ti_name);
        ti_nick_name = findViewById(R.id.ti_nick_name);
        ti_email_id = findViewById(R.id.ti_email_id);
        ti_password = findViewById(R.id.ti_password);
        ti_con_password = findViewById(R.id.ti_con_password);

        findViewById(R.id.ll_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });
        ((TextInputEditText) findViewById(R.id.ed_password)).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        iv_add_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECT_PICTURE);
            }
        });

        findViewById(R.id.bt_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateName(ed_name.getText().toString(), ti_name) | !validateImage(u_profile, getApplicationContext()) |
                        !validateNickName(ed_nick_name.getText().toString(), ti_nick_name) |
                        !validateEmail(ed_email_id.getText().toString(), ti_email_id) |
                        !validatePassword(ed_password.getText().toString(), ti_password) |
                        !validateMatchingPassword(ed_password.getText().toString(), ti_con_password, ed_con_password.getText().toString())) {
                    return;
                }
                if (Common.isInternetAvailable(RegistrationActivity.this)) {
                    u_name = ed_name.getText().toString();
                    u_email = ed_email_id.getText().toString();
                    u_password = ed_password.getText().toString();
                    u_nickname = ed_nick_name.getText().toString();
                    callAPItoRegistration(u_name, u_email, u_password, u_nickname);

                } else {
                    Common.displayToastMessageShort(RegistrationActivity.this, "No connection found. Please connect & try again.", true);
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                try {
                    Uri selectedImageUri = data.getData();
                    u_profile = getPath(selectedImageUri);
                    getCompressed(RegistrationActivity.this, u_profile);
                    File N_file = getCompressed(RegistrationActivity.this, u_profile);
                    u_profile = N_file.getPath();
                    String file_name = new File(u_profile).getName();
                    callApiImageUpload(u_profile, file_name, "GroupIcon");
                } catch (Exception e) {
                    Log.d("SUMITPATEL", "EROOR" + e.toString());
                }

            }
        }
    }

    private void callAPItoRegistration(String m_name, String m_email, String m_password, String u_nickname) {
        Common.hideKeyboard(RegistrationActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProfilePic", imageUploadpath);
            jsonObject.put("Email", m_email);
            jsonObject.put("NickName", u_nickname);
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
        if (operationCode == APIcall.OPERATION_IMAGE_UPLOAD) {
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
            if (mregister.getStatus() == 0) {
                Common.displayToastMessageShort(RegistrationActivity.this, isempty(mregister.getMsg()), true);
                callAPItoLogin();

            } else {
                Common.displayToastMessageShort(RegistrationActivity.this, isempty(mregister.getMsg()), true);
            }
        }
        if (operationCode == APIcall.OPERATION_LOGIN) {
            Gson gson = new Gson();
            example = gson.fromJson(response, Example.class);
            if (example.getAccessToken() != null) {
                sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.SESSION_GUID, isempty(example.getTokenType()) + " " + isempty(example.getAccessToken()));
                sharedPref.setBooleanSharedPreference(SharedPreferenceManagerFile.ISLOGIN, true);
                sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.NICKNAME, isempty(example.getUser().getNickName()));
                sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.EMAIL, isempty(example.getUser().getEmail()));
                sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.USER_NAME, isempty(example.getUser().getDisplayName()));
                sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.USER_PROFILE_PIC, isempty(example.getUser().getPhoto()));
                Intent intent = new Intent(RegistrationActivity.this, DashboardActivity.class);
                startActivity(intent);
                cleared();
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
        if (operationCode == APIcall.OPERATION_IMAGE_UPLOAD) {
            Gson gson = new Gson();
            userFileUpload = gson.fromJson(response, UserFileUpload.class);
            if (userFileUpload.getStatus()) {
                imageUploadpath = userFileUpload.getFilePath();
                Glide.with(RegistrationActivity.this).load(imageUploadpath).placeholder(R.drawable.logo).into(iv_profile);
                Log.d("SUMITPATEL", "UPLOAD_IMAGE_URL" + userFileUpload.getFilePath());
            } else {
                Log.d("SUMITPATEL", "UPLOAD_IMAGE_URL Error" + userFileUpload.getMsg());
                Common.displayToastMessageShort(this, "" + Common.isempty(userFileUpload.getMsg()), true);
            }
            hideDialog();
        }
        hideDialog();
    }


//    private void uploadImage() {
//        StringBuilder sb3 = new StringBuilder();
//        sb3.append(getPackageName());
//        sb3.append(".provider");
//        image_path = String.valueOf(FileProvider.getUriForFile(this, sb3.toString(), new File(image_path)));
//
//        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), image_path);
//        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("UploadedImage", new File(image_path).getName(), requestBody);
//        MultipartBody.Part UploadedFolder = MultipartBody.Part.createFormData("UploadedFolder", "UserIcon");
//        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//        Call<UserFileUpload> memberCall = apiService.uploadImage(fileToUpload, UploadedFolder);
//        memberCall.enqueue(new Callback<UserFileUpload>() {
//
//            @Override
//            public void onResponse(Call<UserFileUpload> call, Response<UserFileUpload> response) {
//                Log.d("SUMITPATEL", "onResponse: " + response.body().getStatus());
//                Log.d("SUMITPATEL", "onResponse: " + response.body().getFilePath());
////                imageUploadpath = userFileUpload.getFilePath();
////                Glide.with(RegistrationActivity.this).load(imageUploadpath).placeholder(R.drawable.logo).into(iv_profile);
//            }
//
//            @Override
//            public void onFailure(Call<UserFileUpload> call, Throwable t) {
//            }
//        });
//
//    }

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
            jsonObject.put("NickName", this.u_nickname);
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

    private void cleared() {
        ed_email_id.setText("");
        ed_password.setText("");
        ed_nick_name.setText("");
        ed_password.setText("");
        ed_con_password.setText("");
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private void callApiImageUpload(String image_path, String file_name, String groupIcon) {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UploadedFolder", groupIcon)
                .addFormDataPart("UploadedImage", file_name,
                        RequestBody.create(new File(image_path), MEDIA_TYPE_PNG))
                .build();
        String url = AppConstant.GET_USER_IMAGE_UPLOAD;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_IMAGE_UPLOAD, this);

    }
}
