package com.newiplquizgame.myipl.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
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
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, APIcall.ApiCallListner {

    Toolbar mToolbar;
    BottomNavigationView navView;
    DrawerLayout drawer_layout;
    NavigationView nav_drawer;
    private SharedPreferenceManagerFile sharedPref;
    GoogleSignInClient mGoogleSignInClient;
    GruopMaster gruopMaster;
    private ProgressDialog dialog;
    Dialog mdialog;
    boolean doubleBackToExitPressedOnce = false;
    String image_path;
    public static Integer SELECT_PICTURE = 2;
    CircleImageView iv_profile;
    String imageString;
    int OPEN_FRAGMENT_POS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sharedPref = new SharedPreferenceManagerFile(this);
        Common.checkAndRequestPermissions(DashboardActivity.this);
        findView();
        bindViewData();

    }

    private void bindViewData() {
        OPEN_FRAGMENT_POS = getIntent().getIntExtra("OPEN_FRAGMENT", 0);
        if (OPEN_FRAGMENT_POS == 0) {
            redirectToHomeScreen();
        } else {
            redirectToMyGropsScreen();
            showInvitationDialog();
        }

        navView.getMenu().findItem(R.id.navigation_home).setChecked(true);
        mToolbar.setTitle(getResources().getString(R.string.title_home));

        findViewById(R.id.txt_Log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isInternetAvailable(DashboardActivity.this)) {
                    try {
                        mGoogleSignInClient.signOut();
                    } catch (Exception e) {

                    }
                    sharedPref.clearPreference();
                    drawer_layout.closeDrawers();
                    startActivity(new Intent(DashboardActivity.this, StartActivity.class));
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
        Dialog mdialog;
        mdialog = new Dialog(this);
        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialog.setContentView(R.layout.invitation_to_join);
        mdialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mdialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        CircleImageView iv_profile = mdialog.findViewById(R.id.iv_profile);
        TextView ed_gruop_name = mdialog.findViewById(R.id.ed_gruop_name);
        Button bt_accept = mdialog.findViewById(R.id.bt_accept);
        Button bt_reject = mdialog.findViewById(R.id.bt_reject);
        ed_gruop_name.setText(Common.GROUP_NAME);
        TextView txt_des = mdialog.findViewById(R.id.txt_des);
        txt_des.setText(Common.GROUP_DES);
        bt_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPIInvitationGroup(Common.USER_ID, 1);
            }
        });
        bt_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPIInvitationGroup(Common.USER_ID, 2);
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
        navView.getMenu().findItem(R.id.navigation_home).setChecked(true);
        drawer_layout = findViewById(R.id.drawer_layout);

        nav_drawer = findViewById(R.id.nav_drawer);
        nav_drawer.setNavigationItemSelectedListener(this);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.dr_menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.navigation_log_out:
//                if (Common.isInternetAvailable(DashboardActivity.this)) {
//                    try {
//                        mGoogleSignInClient.signOut();
//                    } catch (Exception e) {
//
//                    }
//                    sharedPref.clearPreference();
//                    drawer_layout.closeDrawers();
//                    startActivity(new Intent(DashboardActivity.this, StartActivity.class));
//                } else {
//                    Common.displayToastMessageShort(DashboardActivity.this, "No connection found. Please connect & try again.", true);
//                }
//                finish();
            //break;

        }
        drawer_layout.closeDrawers();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_group_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out: {
                showDialogCreatGroup(image_path);
                break;
            }
        }
        return true;
    }

    private void showDialogCreatGroup(final String image_path) {
        mdialog = new Dialog(this);
        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialog.setContentView(R.layout.activity_group);
        mdialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mdialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        iv_profile = mdialog.findViewById(R.id.iv_profile);

        if (image_path != null) {
            Glide.with(this).load(image_path).placeholder(R.drawable.login_back).into(iv_profile);
        }
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

                StringBuilder sb3 = new StringBuilder();
                sb3.append(getPackageName());
                sb3.append(".provider");
                Uri str_uri = FileProvider.getUriForFile(DashboardActivity.this, sb3.toString(), new File(image_path));
                try {
                    InputStream imageStream = getContentResolver().openInputStream(str_uri);
                    Bitmap bitmaps = BitmapFactory.decodeStream(imageStream);
                    Common.encodeToBase64(bitmaps);
                    imageString = Common.encodeToBase64(bitmaps);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                callAPItoCreatGroup(ed_gruop_name.getText().toString(), ed_gruop_des.getText().toString(), imageString);
                mdialog.dismiss();
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
                    this.mdialog.dismiss();
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
                if (gruopMaster.getStatus().equals("0")) {
                    mdialog.dismiss();
                    redirectToMyGropsScreen();
                    Common.displayToastMessageShort(this, "" + Common.isempty(gruopMaster.getMsg()), true);
                } else {
                    Common.displayToastMessageShort(this, "" + Common.isempty(gruopMaster.getMsg()), true);
                }
                hideDialog();
            }
        } catch (Exception e) {
            if (Common.isInternetAvailable(DashboardActivity.this)) {
                try {
                    mGoogleSignInClient.signOut();
                } catch (Exception e1) {

                }
                sharedPref.clearPreference();
                drawer_layout.closeDrawers();
                startActivity(new Intent(getApplicationContext(), StartActivity.class));
                finish();
            } else {
                Common.displayToastMessageShort(DashboardActivity.this, "No connection found. Please connect & try again.", true);
            }
        }
    }

    @Override
    public void onFail(int operationCode, String response) {

        Log.d("SUMITPATEL", "MAINURL" + response);

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
        mToolbar.inflateMenu(R.menu.toolbar_group_menu);
        Fragment newFragment = new HomeFragment();
        Bundle b = new Bundle();
        newFragment.setArguments(b);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.nav_host_fragment, newFragment);
        transaction.commit();


    }

    public void redirectToMyGropsScreen() {
        navView.getMenu().findItem(R.id.navigation_group).setChecked(true);
        mToolbar.getMenu().clear();
        mToolbar.setTitle(getResources().getString(R.string.title_activity_dashboard));
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                image_path = getPath(selectedImageUri);
                // Glide.with(this).load(image_path).placeholder(R.drawable.login_back).into(iv_profile);
                //  iv_profile.setImageURI(Uri.parse(getPath(selectedImageUri)));
                showDialogCreatGroup(image_path);
                Log.d("SUMITPATEl", "MAINURL" + image_path);

//                tv.setText(selectedImagePath);
//                img.setImageURI(selectedImageUri);
            }
        }
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
