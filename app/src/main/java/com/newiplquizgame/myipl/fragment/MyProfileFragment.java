package com.newiplquizgame.myipl.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.activity.DashboardActivity;
import com.newiplquizgame.myipl.activity.MyGroupInfoActivity;
import com.newiplquizgame.myipl.activity.RegistrationActivity;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.GruopMaster;
import com.newiplquizgame.myipl.pkg.UserFileUpload;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.newiplquizgame.myipl.extra.AppValidation.edvalidateMatchingPassword;
import static com.newiplquizgame.myipl.extra.AppValidation.edvalidateName;
import static com.newiplquizgame.myipl.extra.AppValidation.edvalidatePassword;
import static com.newiplquizgame.myipl.extra.AppValidation.validateEmail;
import static com.newiplquizgame.myipl.extra.AppValidation.validateMatchingPassword;
import static com.newiplquizgame.myipl.extra.AppValidation.validatePassword;
import static com.newiplquizgame.myipl.extra.Common.getCompressed;
import static com.newiplquizgame.myipl.extra.Common.isempty;
import static com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile.EMAIL;
import static com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile.ISLOGINBYINT;
import static com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile.NICKNAME;
import static com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile.USERID;
import static com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile.USER_NAME;
import static com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile.USER_PROFILE_PIC;

public class MyProfileFragment extends Fragment implements APIcall.ApiCallListner {
    CircleImageView iv_profile;
    CircleImageView iv_add_profile;
    TextView txt_nikname;
    TextView txt_emailid;
    TextView txt_status;
    Activity activity;
    String u_profile = "";
    CircleImageView d_iv_profile;
    Dialog mdialog;
    private ProgressDialog dialog;

    private SharedPreferenceManagerFile sharedPref;
    public static Integer SELECT_PICTURE = 101;
    String imageUploadpath;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.get("image/*");
    UserFileUpload userFileUpload;
    GruopMaster gruopMaster;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        activity = getActivity();
        sharedPref = new SharedPreferenceManagerFile(activity);
        findviewData(root);
        return root;
    }

    private void findviewData(View root) {
        iv_profile = root.findViewById(R.id.iv_profile);
        iv_add_profile = root.findViewById(R.id.iv_add_profile);
        txt_nikname = root.findViewById(R.id.txt_nikname);
        txt_emailid = root.findViewById(R.id.txt_emailid);
        txt_status = root.findViewById(R.id.txt_status);
        try {
            imageUploadpath = sharedPref.getFromStringSharedPreference(USER_PROFILE_PIC);
            Glide.with(activity).load(imageUploadpath).placeholder(R.drawable.logo).into(iv_profile);
        } catch (Exception e) {
            Toast.makeText(activity, "Profile Image Not Found", Toast.LENGTH_SHORT).show();
        }
        if (sharedPref.getIntSharedPreference(ISLOGINBYINT) != 1)
            txt_emailid.setVisibility(View.VISIBLE);
        else txt_emailid.setVisibility(View.GONE);

        txt_nikname.setText(sharedPref.getFromStringSharedPreference(USER_NAME));
        txt_emailid.setText(sharedPref.getFromStringSharedPreference(EMAIL));
        txt_status.setText(sharedPref.getFromStringSharedPreference(NICKNAME));


        iv_add_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUpdateProfile();
            }
        });
    }

    private void callApiImageUpload(String image_path, String file_name, String groupIcon) {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UploadedFolder", groupIcon)
                .addFormDataPart("UploadedImage", file_name,
                        RequestBody.create(new File(image_path), MEDIA_TYPE_PNG))
                .build();
        String url = AppConstant.GET_USER_IMAGE_UPLOAD;
        APIcall apIcall = new APIcall(activity.getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_IMAGE_UPLOAD, this);

    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_IMAGE_UPLOAD) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_UPDATE_PROFILE) {
            showDialog();
        }
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        if (operationCode == APIcall.OPERATION_IMAGE_UPLOAD) {
            Gson gson = new Gson();
            userFileUpload = gson.fromJson(response, UserFileUpload.class);
            if (userFileUpload.getStatus()) {
                imageUploadpath = userFileUpload.getFilePath();
                sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.USER_PROFILE_PIC, imageUploadpath);
                Glide.with(activity).load(imageUploadpath).placeholder(R.drawable.logo).into(d_iv_profile);
            } else {
                Common.displayToastMessageShort(getContext(), "" + Common.isempty(userFileUpload.getMsg()), true);
            }
            hideDialog();
        }
        if (operationCode == APIcall.OPERATION_UPDATE_PROFILE) {
            Gson gson = new Gson();
            gruopMaster = gson.fromJson(response, GruopMaster.class);
            if (gruopMaster.getStatus() == 0) {
                mdialog.dismiss();
                sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.USER_PROFILE_PIC, gruopMaster.getResult().getPPhoto());
                sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.NICKNAME, gruopMaster.getResult().getNickName());
                sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.EMAIL, isempty(gruopMaster.getResult().getEmail()));
                Glide.with(activity).load(sharedPref.getFromStringSharedPreference(USER_PROFILE_PIC)).placeholder(R.drawable.logo).into(iv_profile);
                txt_nikname.setText(sharedPref.getFromStringSharedPreference(USER_NAME));
                txt_emailid.setText(sharedPref.getFromStringSharedPreference(EMAIL));
                txt_status.setText(sharedPref.getFromStringSharedPreference(NICKNAME));
            } else {
                Common.displayToastMessageShort(getContext(), "" + Common.isempty(gruopMaster.getMsg()), true);
            }
            hideDialog();
        }
        hideDialog();
    }

    @Override
    public void onFail(int operationCode, String response) {

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private void showDialog() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    private void showDialogUpdateProfile() {
        mdialog = new Dialog(getContext());
        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialog.setContentView(R.layout.pop_update_profile);
        mdialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mdialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ImageButton bt_close = mdialog.findViewById(R.id.bt_close);
        bt_close.setOnClickListener(v -> mdialog.dismiss());

        d_iv_profile = mdialog.findViewById(R.id.d_iv_profile);

        CircleImageView d_iv_add_profile = mdialog.findViewById(R.id.d_iv_add_profile);

        d_iv_add_profile.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, SELECT_PICTURE);
        });

        try {
            Glide.with(getActivity()).load(imageUploadpath).placeholder(R.drawable.logo).into(d_iv_profile);
        } catch (Exception e) {

        }
        LinearLayout ll_password = mdialog.findViewById(R.id.ll_password);
        ll_password.setVisibility(View.GONE);
        final EditText ed_user_name = mdialog.findViewById(R.id.ed_user_name);
        final EditText ed_user_email = mdialog.findViewById(R.id.ed_user_email);
        ed_user_email.setVisibility(View.GONE);
        final EditText ed_user_nickname = mdialog.findViewById(R.id.ed_user_nickname);

        ed_user_name.setText(sharedPref.getFromStringSharedPreference(USER_NAME));
        ed_user_email.setText(sharedPref.getFromStringSharedPreference(EMAIL));
        ed_user_nickname.setText(sharedPref.getFromStringSharedPreference(NICKNAME));

        if (sharedPref.getIntSharedPreference(ISLOGINBYINT) != 1)
            ed_user_email.setVisibility(View.VISIBLE);
        else ed_user_email.setVisibility(View.GONE);

        if (sharedPref.getIntSharedPreference(ISLOGINBYINT) == 0)
            ll_password.setVisibility(View.VISIBLE);
        else
            ll_password.setVisibility(View.GONE);

        final EditText ed_user_pwd = mdialog.findViewById(R.id.ed_user_pwd);
        final EditText ed_user_pwd_conf = mdialog.findViewById(R.id.ed_user_pwd_conf);


        ((Button) mdialog.findViewById(R.id.bt_send_invitation)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (sharedPref.getIntSharedPreference(ISLOGINBYINT)) {
                    case 0:
                        if (ed_user_pwd.getText().toString().equals("")) {
                            if (!edvalidateName(ed_user_nickname.getText().toString().trim(), ed_user_nickname)) {
                                return;
                            }
                        } else {
                            if (!edvalidateName(ed_user_nickname.getText().toString().trim(), ed_user_nickname) | !edvalidatePassword(ed_user_pwd.getText().toString().trim(), ed_user_pwd)
                                    | !edvalidateMatchingPassword(ed_user_pwd.getText().toString(), ed_user_pwd_conf, ed_user_pwd_conf.getText().toString())) {
                                return;
                            }
                        }
                        break;
                    default:
                        if (!edvalidateName(ed_user_nickname.getText().toString().trim(), ed_user_nickname)) {
                            return;
                        }
                        break;
                }
                String username = ed_user_name.getText().toString();
                String emailid = ed_user_email.getText().toString();
                String nickname = ed_user_nickname.getText().toString();
                String password = ed_user_pwd_conf.getText().toString();
                callApiUpdateProfile(username, emailid, nickname, password, imageUploadpath);
            }
        });
        ((ImageButton) mdialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialog.dismiss();
            }
        });
        mdialog.show();
        mdialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                try {
                    Uri selectedImageUri = data.getData();
                    u_profile = getPath(selectedImageUri);
                    getCompressed(getContext(), u_profile);
                    File N_file = getCompressed(getContext(), u_profile);
                    u_profile = N_file.getPath();
                    String file_name = new File(u_profile).getName();
                    callApiImageUpload(u_profile, file_name, "GroupIcon");
                } catch (Exception e) {
                    Log.d("SUMITPATEL", "EROOR" + e.toString());
                }

            }
        }
    }

    private void callApiUpdateProfile(String username, String emailid, String nickname, String password, String imageUploadpath) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", sharedPref.getIntSharedPreference(USERID));
            jsonObject.put("Email", emailid);
            jsonObject.put("Password", password);
            jsonObject.put("ProfilePic", imageUploadpath);
            jsonObject.put("NickName", nickname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_UPDATE_PROFILE;
        APIcall apIcall = new APIcall(activity.getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_UPDATE_PROFILE, this);
    }
}