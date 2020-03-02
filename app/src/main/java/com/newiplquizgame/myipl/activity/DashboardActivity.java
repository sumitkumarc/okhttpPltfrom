package com.newiplquizgame.myipl.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.fragment.AllMatchFragment;
import com.newiplquizgame.myipl.fragment.HomeFragment;
import com.newiplquizgame.myipl.fragment.MyGropsFragment;
import com.newiplquizgame.myipl.fragment.MyProfileFragment;
import com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.GruopMaster;
import com.newiplquizgame.myipl.pkg.UserFileUpload;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.newiplquizgame.myipl.extra.Common.getCompressed;
import static com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile.ISLOGINBYINT;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, APIcall.ApiCallListner {

    public  static  Toolbar mToolbar;
  public  static BottomNavigationView navView;
    DrawerLayout drawer_layout;
    NavigationView nav_drawer;
    private SharedPreferenceManagerFile sharedPref;

    private GoogleApiClient mGoogleApiClient;
    GruopMaster gruopMaster;
    private ProgressDialog dialog;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.get("image/*");
    boolean doubleBackToExitPressedOnce = false;
    String image_path;
    public static Integer SELECT_PICTURE = 101;
    CircleImageView iv_profile;
    String imageString;
    int OPEN_FRAGMENT_POS = 0;
    UserFileUpload userFileUpload;
    Dialog mdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sharedPref = new SharedPreferenceManagerFile(this);
        Log.d("SUMITPATEL", "MAINURL" + sharedPref.getIntSharedPreference(ISLOGINBYINT));
        Common.checkAndRequestPermissions(DashboardActivity.this);
        findView();
        bindViewData();

    }

    private void bindViewData() {


        findViewById(R.id.txt_Log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isInternetAvailable(DashboardActivity.this)) {

                    switch(sharedPref.getIntSharedPreference(SharedPreferenceManagerFile.ISLOGINBYINT)){
                        case 0:
                            sharedPref.clearPreference();
                            drawer_layout.closeDrawers();
                            startActivity(new Intent(DashboardActivity.this, StartActivity.class));
                            finish();
                            break;
                        case 1:
                            sharedPref.clearPreference();
                            LoginManager.getInstance().logOut();
                            startActivity(new Intent(DashboardActivity.this, StartActivity.class));
                            finish();
                            break;
                        case 2:
                            sharedPref.clearPreference();
                            try {
                                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                                mGoogleApiClient.disconnect();
                                mGoogleApiClient.connect();

                            } catch (Exception e) {

                            }

                            startActivity(new Intent(DashboardActivity.this, StartActivity.class));
                            finish();
                            break;
                    }





                } else {
                    Common.displayToastMessageShort(DashboardActivity.this, "No connection found. Please connect & try again.", true);
                }
                finish();
            }
        });

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        redirectToHomeScreen();
                        break;
                    case R.id.navigation_group:
                        redirectToMyGropsScreen();
                        break;
                    case R.id.navigation_Prediction:
                        redirectToAllMatchScreen();
                        break;
                    case R.id.navigation_Profile:
                        redirectToMyProfileScreen();
                        break;
                }
                return false;
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void showInvitationDialog() {

        mdialog = new Dialog(this);
        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialog.setContentView(R.layout.invitation_to_join);
        mdialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mdialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        CircleImageView iv_profile = mdialog.findViewById(R.id.iv_profile);
        try {
            Glide.with(this).load(Common.GROUP_URL).error(R.drawable.logo).into(iv_profile);
        } catch (Exception e) {

        }
        ImageButton bt_close = mdialog.findViewById(R.id.bt_close);
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialog.dismiss();
            }
        });
        TextView ed_gruop_name = mdialog.findViewById(R.id.ed_gruop_name);
        Button bt_accept = mdialog.findViewById(R.id.bt_accept);
        Button bt_reject = mdialog.findViewById(R.id.bt_reject);
        ed_gruop_name.setText(Common.GROUP_NAME);
        TextView txt_des = mdialog.findViewById(R.id.txt_des);
        txt_des.setText(Common.GROUP_DES);
        bt_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPIInvitationGroup(Common.GROUP_ID, 1);
            }
        });
        bt_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPIInvitationGroup(Common.GROUP_ID, 2);
            }
        });
        mdialog.show();
        mdialog.getWindow().setAttributes(lp);

    }

    private void callAPIInvitationGroup(Integer USER_ID, int IsAppr) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("GroupUserId", 20);
            jsonObject.put("IsApproved", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url2 = AppConstant.GET_ACCEPT_REJECT;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url2, APIcall.OPERATION_GROUP_ACCEPT_REJECT, this);

    }

    private void findView() {
        navView = findViewById(R.id.nav_view);
        drawer_layout = findViewById(R.id.drawer_layout);

        nav_drawer = findViewById(R.id.nav_drawer);
        nav_drawer.setNavigationItemSelectedListener(this);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.dr_menu);

        OPEN_FRAGMENT_POS = getIntent().getIntExtra("OPEN_FRAGMENT", 0);
        if (OPEN_FRAGMENT_POS == 0) {
            redirectToHomeScreen();
        } else {
            redirectToMyGropsScreen();
            showInvitationDialog();
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
        }
        drawer_layout.closeDrawers();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out: {
                showDialogCreatGroup();
                break;
            }
        }
        return true;
    }

    private void showDialogCreatGroup() {
        final Dialog mdialog = new Dialog(this);
        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialog.setContentView(R.layout.pop_create_group);
        mdialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mdialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        iv_profile = mdialog.findViewById(R.id.iv_profile);

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
        final EditText ed_gruop_name = mdialog.findViewById(R.id.ed_gruop_name);
        final EditText ed_gruop_des = mdialog.findViewById(R.id.ed_gruop_des);

        ((Button) mdialog.findViewById(R.id.bt_send_invitation)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_path == null) {
                    Common.displayToastMessageShort(DashboardActivity.this, "Please select image.", true);
                    return;
                }
                if (ed_gruop_name.getText().toString().equals("") || ed_gruop_name.getText().toString() == null) {
                    Common.displayToastMessageShort(DashboardActivity.this, "Enter your name.", true);
                    return;
                }
                if (ed_gruop_des.getText().toString().equals("") || ed_gruop_des.getText().toString() == null) {
                    Common.displayToastMessageShort(DashboardActivity.this, "Enter your description.", true);
                    return;
                }
                try {
                    if (image_path != null) {
                        Glide.with(DashboardActivity.this).load(imageString).placeholder(R.drawable.logo).into(iv_profile);
                    }
                } catch (Exception e) {

                }
                callAPItoCreatGroup(ed_gruop_name.getText().toString(), ed_gruop_des.getText().toString(), imageString);

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
        Common.hideKeyboard(DashboardActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
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

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_GROUP_CREATE) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_GROUP_ACCEPT_REJECT) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_IMAGE_UPLOAD) {
            showDialog();
        }

    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            if (operationCode == APIcall.OPERATION_GROUP_CREATE) {
                Gson gson = new Gson();
                gruopMaster = gson.fromJson(response, GruopMaster.class);
                if (gruopMaster.getStatus() == 0) {
                    Common.displayToastMessageShort(DashboardActivity.this, "" + Common.isempty(gruopMaster.getMsg()), true);
                    redirectToMyGropsScreen();
                    navView.getMenu().findItem(R.id.navigation_group).setChecked(true);

                } else {
                    Common.displayToastMessageShort(DashboardActivity.this, gruopMaster.getMsg(), true);
                }
                hideDialog();
            }
            if (operationCode == APIcall.OPERATION_GROUP_ACCEPT_REJECT) {
                Gson gson = new Gson();
                gruopMaster = gson.fromJson(response, GruopMaster.class);
                if (gruopMaster.getStatus() == 0) {
                    mdialog.dismiss();
                    redirectToMyGropsScreen();
                    Common.displayToastMessageShort(this, "" + Common.isempty(gruopMaster.getMsg()), true);
                } else {
                    Common.displayToastMessageShort(this, "" + Common.isempty(gruopMaster.getMsg()), true);
                }
                hideDialog();
            }
            if (operationCode == APIcall.OPERATION_IMAGE_UPLOAD) {
                Gson gson = new Gson();
                userFileUpload = gson.fromJson(response, UserFileUpload.class);
                if (userFileUpload.getStatus()) {
                    imageString = userFileUpload.getFilePath();
                    Glide.with(DashboardActivity.this).load(imageString).placeholder(R.drawable.logo).into(iv_profile);
                    Log.d("SUMITPATEL", "UPLOAD_IMAGE_URL" + userFileUpload.getFilePath());
                } else {
                    Log.d("SUMITPATEL", "UPLOAD_IMAGE_URL Error" + userFileUpload.getMsg());
                    Common.displayToastMessageShort(this, "" + Common.isempty(userFileUpload.getMsg()), true);
                }
                hideDialog();
            }
        } catch (Exception e) {
            if (Common.isInternetAvailable(DashboardActivity.this)) {
                try {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                } catch (Exception e1) {

                }
                sharedPref.clearPreference();
                drawer_layout.closeDrawers();
                startActivity(new Intent(getApplicationContext(), StartActivity.class));
                finish();
            } else {
                Common.displayToastMessageShort(DashboardActivity.this, "No connection found. Please connect & try again.", true);
            }
            hideDialog();
        }
    }

    @Override
    public void onFail(int operationCode, String response) {


    }

    private void showDialog() {
        dialog = new ProgressDialog(DashboardActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void redirectToHomeScreen() {
        navView.getMenu().findItem(R.id.navigation_home).setChecked(true);
        mToolbar.setTitle(getResources().getString(R.string.title_home));
        mToolbar.getMenu().clear();
        Fragment newFragment = new HomeFragment();
        Bundle b = new Bundle();
        newFragment.setArguments(b);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, newFragment);
        transaction.commit();


    }

    public void redirectToMyGropsScreen() {
        navView.getMenu().findItem(R.id.navigation_group).setChecked(true);
        mToolbar.getMenu().clear();
        mToolbar.setTitle(getResources().getString(R.string.title_group));
        mToolbar.inflateMenu(R.menu.toolbar_group_menu);
        Fragment newFragment = new MyGropsFragment();
        Bundle b = new Bundle();
        newFragment.setArguments(b);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.nav_host_fragment, newFragment);
        transaction.commit();

    }

    public void redirectToAllMatchScreen() {
        navView.getMenu().findItem(R.id.navigation_Prediction).setChecked(true);
        mToolbar.getMenu().clear();
        mToolbar.setTitle(getResources().getString(R.string.title_prediction));
        Fragment newFragment = new AllMatchFragment();
        Bundle b = new Bundle();
        newFragment.setArguments(b);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.nav_host_fragment, newFragment);
        transaction.commit();
    }

    public void redirectToMyProfileScreen() {
        navView.getMenu().findItem(R.id.navigation_Profile).setChecked(true);
        mToolbar.getMenu().clear();
        mToolbar.setTitle(getResources().getString(R.string.title_profile));
        Fragment newFragment = new MyProfileFragment();
        Bundle b = new Bundle();
        newFragment.setArguments(b);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.nav_host_fragment, newFragment);
        transaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                try {
                    Uri selectedImageUri = data.getData();
                    image_path = "";
                    image_path = getPath(selectedImageUri);
                    getCompressed(DashboardActivity.this, image_path);
                    File N_file = getCompressed(DashboardActivity.this, image_path);
                    image_path = N_file.getPath();
                    String file_name = new File(image_path).getName();
                    callApiImageUpload(image_path, file_name, "GroupIcon");
                } catch (Exception e) {
                    Log.d("SUMITPATEL", "EROOR" + e.toString());
                }

            }
        }
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


}
