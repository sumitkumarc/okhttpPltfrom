package com.newiplquizgame.myipl.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.fragment.LeaderbordFrg;
import com.newiplquizgame.myipl.fragment.PridicastionFrg;
import com.newiplquizgame.myipl.fragment.AllUserfragment;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.GruopMaster;
import com.newiplquizgame.myipl.pkg.UserFileUpload;
import com.newiplquizgame.myipl.view_pager_Adapter.VPFragmentAdapter;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.newiplquizgame.myipl.extra.Common.getCompressed;

public class MyGroupInfoActivity extends AppCompatActivity implements APIcall.ApiCallListner {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String image_path;
    UserFileUpload userFileUpload;
    CircleImageView iv_profile;
    String file_name;
    public static Integer SELECT_PICTURE = 101;
    private ProgressDialog dialog;
    GruopMaster mgruopMaster;
    Toolbar toolbar;
    CircleImageView profileImage;
    public Dialog mdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group_info);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Common.GROUP_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setupViewPager(ViewPager viewPager) {
        VPFragmentAdapter adapter = new VPFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new LeaderbordFrg(), "Point Table");
        adapter.addFragment(new PridicastionFrg(), "Prediction");
        adapter.addFragment(new AllUserfragment(), "All user");
        viewPager.setAdapter(adapter);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_group_info, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_two);
        MenuItem pop_edit_group = menu.findItem(R.id.pop_edit_group);
        MenuItem pop_invite_group = menu.findItem(R.id.pop_invite_group);
        MenuItem pop_leave_group = menu.findItem(R.id.pop_leave_group);
        MenuItem pop_admin_panel = menu.findItem(R.id.pop_admin_panel);

        if (Common.GROUP_ADMIN == 1) {
            pop_edit_group.setVisible(true);
            pop_invite_group.setVisible(true);
            pop_admin_panel.setVisible(true);
            pop_leave_group.setVisible(false);
        } else {
            pop_leave_group.setVisible(true);
            pop_edit_group.setVisible(false);
            pop_admin_panel.setVisible(false);
            pop_invite_group.setVisible(false);
        }
        if (Common.GROUP_ID == 0 ) {
            pop_leave_group.setVisible(false);
            pop_edit_group.setVisible(false);
            pop_admin_panel.setVisible(false);
            pop_invite_group.setVisible(false);
        }
        View view = MenuItemCompat.getActionView(menuItem);
        profileImage = view.findViewById(R.id.toolbar_profile_image);
        Glide.with(this).load(Common.GROUP_URL).into(profileImage);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isInternetAvailable(MyGroupInfoActivity.this)){
                    showDialogCreatGroup();
                }else {
                    Common.displayToastMessageShort(MyGroupInfoActivity.this, "No connection found. Please connect & try again.", true);
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.pop_edit_group:
                showDialogCreatGroup();
                return true;
            case R.id.pop_invite_group:
                startActivity(new Intent(this, InviteuserlistActivity.class));
                return true;
            case R.id.pop_leave_group:
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Leave Group");
                alert.setMessage("Are you sure live this Group?");
                alert.setPositiveButton("OK", null);
                alert.setIcon(R.drawable.logo);
                AlertDialog dialog = alert.create();
                alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        callAPILeaveGroup();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                alert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_GROUP_USER_LIST) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_SEND_INVITATION) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_IMAGE_UPLOAD) {
            showDialog();
        }

    }

    private void showDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }


    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onFail(int operationCode, String response) {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE && data != null) {
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
                try {
                    Uri selectedImageUri = data.getData();
                    image_path = getPath(selectedImageUri);
                    File N_file = getCompressed(MyGroupInfoActivity.this, image_path);
                    image_path = N_file.getPath();
                    file_name = new File(image_path).getName();
                    callApiImageUpload(image_path, file_name, "GroupIcon");
                } catch (Exception e) {
                    Log.d("SUMITPATEL", "EROOR" + e.toString());
                }
            }
        }
    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            if (operationCode == APIcall.OPERATION_SEND_INVITATION) {
                Gson gson = new Gson();
                mgruopMaster = gson.fromJson(response, GruopMaster.class);
                if (mgruopMaster.getStatus().equals("0")) {
                    OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
                    String userID = status.getSubscriptionStatus().getUserId();
                    boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();
                    if (!isSubscribed)
                        return;
                    Common.displayToastMessageShort(this, "" + Common.isempty(mgruopMaster.getMsg()), true);
                } else {
                    Common.displayToastMessageShort(this, "" + Common.isempty(mgruopMaster.getMsg()), true);
                }
                hideDialog();
            }
            if (operationCode == APIcall.OPERATION_LEAVEGROUP) {
                Gson gson = new Gson();
                mgruopMaster = gson.fromJson(response, GruopMaster.class);
                if (mgruopMaster.getStatus() == 0) {
                    onBackPressed();
                    finish();
                    Common.displayToastMessageShort(this, "" + Common.isempty(mgruopMaster.getMsg()), true);
                } else {
                    Common.displayToastMessageShort(this, "" + Common.isempty(mgruopMaster.getMsg()), true);
                }
                hideDialog();
            }
            if (operationCode == APIcall.OPERATION_GROUP_CREATE) {
                Gson gson = new Gson();
                mgruopMaster = gson.fromJson(response, GruopMaster.class);
                if (mgruopMaster.getStatus() == 0) {
                    mdialog.cancel();
                    Common.GROUP_ID = mgruopMaster.getResult().getGroupId();
                    getSupportActionBar().setTitle(Common.GROUP_NAME);
                    Glide.with(MyGroupInfoActivity.this).load(Common.GROUP_URL).placeholder(R.drawable.logo).into(profileImage);
                    Common.displayToastMessageShort(MyGroupInfoActivity.this, mgruopMaster.getMsg(), true);

                } else {
                    Common.displayToastMessageShort(MyGroupInfoActivity.this, mgruopMaster.getMsg(), true);
                }
                hideDialog();
            }
            if (operationCode == APIcall.OPERATION_IMAGE_UPLOAD) {
                Gson gson = new Gson();
                userFileUpload = gson.fromJson(response, UserFileUpload.class);
                if (userFileUpload.getStatus()) {
                    Common.GROUP_URL = userFileUpload.getFilePath();
                    Glide.with(MyGroupInfoActivity.this).load(Common.GROUP_URL).placeholder(R.drawable.logo).into(iv_profile);
                    Log.d("SUMITPATEL", "UPLOAD_IMAGE_URL" + userFileUpload.getFilePath());

                } else {
                    Log.d("SUMITPATEL", "UPLOAD_IMAGE_URL Error" + userFileUpload.getMsg());
                    Common.displayToastMessageShort(this, "" + Common.isempty(userFileUpload.getMsg()), true);
                }
                hideDialog();
            }
            hideDialog();
        } catch (Exception e) {
//            if (Common.isInternetAvailable(NewAllUserActivity.this)) {
//                try {
//                    mGoogleSignInClient.signOut();
//                    mGoogleSignInClient.revokeAccess();
//                } catch (Exception e1) {
//
//                }
//                sharedPref.clearPreference();
//                startActivity(new Intent(getApplicationContext(), StartActivity.class));
//                finish();
//            } else {
//                Common.displayToastMessageShort(NewAllUserActivity.this, "No connection found. Please connect & try again.", true);
//            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void callApiImageUpload(String image_path, String file_name, String groupIcon) {
        RequestBody body = new MultipartBody.Builder()

                .setType(MultipartBody.FORM)
                .addFormDataPart("UploadedImage", file_name, RequestBody.create(MediaType.parse("multipart/form-data"), new File(image_path)))
                .addFormDataPart("UploadedFolder", groupIcon)
                .build();
        String url = AppConstant.GET_USER_IMAGE_UPLOAD;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_IMAGE_UPLOAD, this);


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

    private void showDialogCreatGroup() {
         mdialog = new Dialog(this);
        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialog.setContentView(R.layout.pop_create_group);
        mdialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mdialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        iv_profile = mdialog.findViewById(R.id.iv_profile);
        TextView txt_tital = mdialog.findViewById(R.id.txt_tital);
        txt_tital.setText("Update Group");
        ImageButton bt_close = mdialog.findViewById(R.id.bt_close);
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialog.dismiss();
            }
        });
        CircleImageView iv_add_profile = mdialog.findViewById(R.id.iv_add_profile);
        iv_add_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECT_PICTURE);
            }
        });
        try {
            Glide.with(MyGroupInfoActivity.this).load(Common.GROUP_URL).placeholder(R.drawable.logo).into(iv_profile);
        } catch (Exception e) {

        }
        final EditText ed_gruop_name = mdialog.findViewById(R.id.ed_gruop_name);
        ed_gruop_name.setText(Common.GROUP_NAME);
        ed_gruop_name.setEnabled(false);
        final EditText ed_gruop_des = mdialog.findViewById(R.id.ed_gruop_des);
        ed_gruop_des.setText(Common.GROUP_DES);
        ((Button) mdialog.findViewById(R.id.bt_send_invitation)).setText("Update Group");
        ((Button) mdialog.findViewById(R.id.bt_send_invitation)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.GROUP_URL.length() == 0 || Common.GROUP_URL.equals("")) {
                    Common.displayToastMessageShort(MyGroupInfoActivity.this, "Please select image.", true);
                    return;
                }
                if (ed_gruop_name.getText().toString().equals("") || ed_gruop_name.getText().toString() == null) {
                    Common.displayToastMessageShort(MyGroupInfoActivity.this, "Enter your name.", true);
                    return;
                }
                if (ed_gruop_des.getText().toString().equals("") || ed_gruop_des.getText().toString() == null) {
                    Common.displayToastMessageShort(MyGroupInfoActivity.this, "Enter your description.", true);
                    return;
                }
                try {
                    Glide.with(MyGroupInfoActivity.this).load(Common.GROUP_URL).placeholder(R.drawable.logo).into(iv_profile);
                } catch (Exception e) {

                }
                Common.GROUP_NAME = ed_gruop_name.getText().toString();
                Common.GROUP_DES = ed_gruop_des.getText().toString();
                callAPItoCreatGroup(ed_gruop_name.getText().toString(), ed_gruop_des.getText().toString(), Common.GROUP_URL);


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

    private void callAPItoCreatGroup(String g_name, String g_des, String imageString) {
        Common.hideKeyboard(MyGroupInfoActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("GroupId", Common.GROUP_ID);
            jsonObject.put("GroupName", g_name);
            jsonObject.put("Icon", imageString);
            jsonObject.put("Description", g_des);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url2 = AppConstant.GET_GROUP_CREATE;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url2, APIcall.OPERATION_GROUP_CREATE, this);
    }

    private void callAPILeaveGroup() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Id", Common.GROUP_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url2 = AppConstant.GET_LEAVEGROUP;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url2, APIcall.OPERATION_LEAVEGROUP, this);

    }
}
