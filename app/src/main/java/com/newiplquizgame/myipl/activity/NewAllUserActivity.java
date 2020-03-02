package com.newiplquizgame.myipl.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.GroupDatum;
import com.newiplquizgame.myipl.pkg.GruopMaster;
import com.newiplquizgame.myipl.pkg.UserFileUpload;
import com.newiplquizgame.myipl.recyclerview_adapter.RVGroupUserListAdapter;
import com.newiplquizgame.myipl.recyclerview_adapter.RVUserListAdapter;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.newiplquizgame.myipl.extra.Common.getCompressed;

public class NewAllUserActivity extends AppCompatActivity implements APIcall.ApiCallListner {
    GruopMaster mgruopMaster;
    List<GroupDatum> mGroupData;
    List<GroupDatum> mUserGroup;
    RVGroupUserListAdapter groupUserListAdapter;
    public static Integer SELECT_PICTURE = 2;
    RVUserListAdapter rvUserListAdapter;
    private ProgressDialog dialog;
    LinearLayout ll_no_data;
    static RecyclerView recycler_view;
    RecyclerView recycler_user_view;
    TextView ed_gruop_name;
    Button ll_leave_group;
    Toolbar toolbar;
    CircleImageView iv_ed_profile;
    String imageString;
    CircleImageView iv_profile;
    CircleImageView iv_main_profile;
    GoogleSignInClient mGoogleSignInClient;
    private static SharedPreferenceManagerFile sharedPref;
    String image_path;
    UserFileUpload userFileUpload;
    String file_name;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.get("image/*");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_all_user);
        Bindview();
    }

    private void Bindview() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sharedPref = new SharedPreferenceManagerFile(this);
        recycler_view = findViewById(R.id.recycler_view);
        ll_leave_group = findViewById(R.id.ll_leave_group);
        ll_leave_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPILeaveGroup();
            }
        });
        ll_no_data = findViewById(R.id.ll_no_data);
        ed_gruop_name = findViewById(R.id.ed_gruop_name);
        ed_gruop_name.setText(Common.GROUP_NAME);
        recycler_user_view = findViewById(R.id.recycler_user_view);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_user_view.setLayoutManager(gridLayoutManager);
        recycler_user_view.setItemAnimator(new DefaultItemAnimator());
        recycler_user_view.setVisibility(View.GONE);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setVisibility(View.GONE);

        ll_no_data.setVisibility(View.GONE);

        iv_main_profile = findViewById(R.id.iv_profile);

        SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.onActionViewExpanded(); //new Added line
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search Here");
        Common.hideKeyboard(NewAllUserActivity.this);

        searchView.setOnQueryTextListener((new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recycler_user_view.setVisibility(View.VISIBLE);
                recycler_view.setVisibility(View.GONE);
                rvUserListAdapter.getFilter().filter(newText);
                if (rvUserListAdapter.mFilteredkgroupData.size() == 0 || newText.length() == 0) {
                    recycler_user_view.setVisibility(View.GONE);
                    recycler_view.setVisibility(View.VISIBLE);
                } else {
                    recycler_user_view.setVisibility(View.VISIBLE);
                    recycler_view.setVisibility(View.GONE);
                }
                return true;
            }
        }));
        iv_ed_profile = findViewById(R.id.iv_ed_profile);
        iv_ed_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogCreatGroup();
            }
        });
        try {
            Glide.with(NewAllUserActivity.this).load(Common.GROUP_URL).centerCrop().placeholder(R.drawable.logo).into(iv_main_profile);
        } catch (Exception e) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                iv_main_profile.setImageDrawable(getDrawable(R.drawable.logo));
            }
        }
        if (Common.GROUP_ADMIN == 1) {
            iv_ed_profile.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);
            ll_leave_group.setVisibility(View.GONE);
        } else {
            iv_ed_profile.setVisibility(View.GONE);
            searchView.setVisibility(View.GONE);
            ll_leave_group.setVisibility(View.VISIBLE);
        }
        callAPIAllUserList();
        callAPIUserList();

    }

    private void callAPIAllUserList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Id", Common.GROUP_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_USER_LIST;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_USER_LIST, this);
    }

    private void callAPIUserList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Id", Common.GROUP_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url2 = AppConstant.GET_GROUP_USER_LIST;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url2, APIcall.OPERATION_GROUP_USER_LIST, this);

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

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            if (operationCode == APIcall.OPERATION_GROUP_USER_LIST) {
                Gson gson = new Gson();
                mgruopMaster = gson.fromJson(response, GruopMaster.class);
                if (mgruopMaster.getStatus() != 0) {
                    recycler_view.setVisibility(View.GONE);
                    ll_no_data.setVisibility(View.VISIBLE);
                    Common.displayToastMessageShort(this, "" + Common.isempty(mgruopMaster.getMsg()), true);
                } else {
                    mGroupData = new ArrayList<>();
                    mGroupData = mgruopMaster.getData();
                    if (Common.GROUP_ADMIN == 1) {
                        Comparator c = Collections.reverseOrder(new Comparator<GroupDatum>() {
                            @Override
                            public int compare(GroupDatum lhs, GroupDatum rhs) {
                                return lhs.getIsApproved().compareTo(rhs.getIsApproved());
                            }
                        });
                        Collections.sort(mGroupData, c);
                 //       groupUserListAdapter = new RVGroupUserListAdapter(this, mGroupData);
                    } else {
                        ArrayList<GroupDatum> mfGroupData = new ArrayList<>();
                        for (int i = 0; i < mgruopMaster.getData().size(); i++) {
                            if (mGroupData.get(i).getIsApproved() == 1) {
                                mfGroupData.add(mGroupData.get(i));
                            }
                        }
                       // groupUserListAdapter = new RVGroupUserListAdapter(this, mfGroupData);
                    }
                    recycler_view.setVisibility(View.VISIBLE);
                    recycler_user_view.setVisibility(View.GONE);
                    ll_no_data.setVisibility(View.GONE);
                    recycler_view.setAdapter(groupUserListAdapter);
                    recycler_view.getViewTreeObserver().addOnPreDrawListener(
                            new ViewTreeObserver.OnPreDrawListener() {
                                @Override
                                public boolean onPreDraw() {
                                    recycler_view.getViewTreeObserver().removeOnPreDrawListener(this);
                                    for (int i = 0; i < recycler_view.getChildCount(); i++) {
                                        View v = recycler_view.getChildAt(i);
                                        v.setAlpha(0.0f);
                                        v.animate().alpha(1.0f)
                                                .setDuration(300)
                                                .setStartDelay(i * 50)
                                                .start();
                                    }

                                    return true;
                                }
                            });
                }
                hideDialog();
            }
            if (operationCode == APIcall.OPERATION_USER_LIST) {
                Gson gson = new Gson();
                mgruopMaster = gson.fromJson(response, GruopMaster.class);
                if (mgruopMaster.getStatus() == 4) {
                    Common.displayToastMessageShort(this, "" + Common.isempty(mgruopMaster.getMsg()), true);
                } else if (mgruopMaster.getStatus() == 0) {
                    mUserGroup = mgruopMaster.getData();
                  //  rvUserListAdapter = new RVUserListAdapter(this, mUserGroup);
                    recycler_user_view.setAdapter(rvUserListAdapter);

                } else if (mgruopMaster.getStatus() == 1) {
                    Common.displayToastMessageShort(this, "" + Common.isempty(mgruopMaster.getMsg()), true);
                }
                hideDialog();
            }
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
                callAPIAllUserList();
                callAPIUserList();
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
                    Common.GROUP_ID = mgruopMaster.getResult().getGroupId();
                    ed_gruop_name.setText(Common.GROUP_NAME);
                    Glide.with(NewAllUserActivity.this).load(Common.GROUP_URL).placeholder(R.drawable.logo).into(iv_main_profile);
                    Common.displayToastMessageShort(NewAllUserActivity.this, mgruopMaster.getMsg(), true);
                } else {
                    Common.displayToastMessageShort(NewAllUserActivity.this, mgruopMaster.getMsg(), true);
                }
                hideDialog();
            }
            if (operationCode == APIcall.OPERATION_IMAGE_UPLOAD) {
                Gson gson = new Gson();
                userFileUpload = gson.fromJson(response, UserFileUpload.class);
                if (userFileUpload.getStatus()) {
                    Common.GROUP_URL = userFileUpload.getFilePath();
                    Glide.with(NewAllUserActivity.this).load(Common.GROUP_URL).placeholder(R.drawable.logo).into(iv_profile);
                    Log.d("SUMITPATEL", "UPLOAD_IMAGE_URL" + userFileUpload.getFilePath());

                } else {
                    Log.d("SUMITPATEL", "UPLOAD_IMAGE_URL Error" + userFileUpload.getMsg());
                    Common.displayToastMessageShort(this, "" + Common.isempty(userFileUpload.getMsg()), true);
                }
                hideDialog();
            }
            hideDialog();
        } catch (Exception e) {
            if (Common.isInternetAvailable(NewAllUserActivity.this)) {
                try {
                    mGoogleSignInClient.signOut();
                    mGoogleSignInClient.revokeAccess();
                } catch (Exception e1) {

                }
                sharedPref.clearPreference();
                startActivity(new Intent(getApplicationContext(), StartActivity.class));
                finish();
            } else {
                Common.displayToastMessageShort(NewAllUserActivity.this, "No connection found. Please connect & try again.", true);
            }
        }
    }

    public void showSendInvitation(int USER_ID, String oneSignalToken) {
        try {
            OneSignal.postNotification(new JSONObject("{'contents': {'en':'" + Common.GROUP_DES + "'}, " +
                            "'key1':['" + Common.GROUP_ID + "']" +
                            "'include_player_ids': ['" + oneSignalToken + "'], " +
                            "'headings': {'en': ' " + Common.GROUP_NAME + "joint GROUP" + "'}, " +
                            "'data': {'GROUP_ID': '" + Common.GROUP_ID + "','GROUP_IMAGE_URL': '" + Common.GROUP_URL + "','GROUP_NAME': '" + Common.GROUP_NAME + "','GROUP_DES': '" + Common.GROUP_DES + "'}," +
                            "'buttons':[{'id': 'id1', 'text': ''}, {'id':'id2', 'text': 'Joint Group'}]}"),


                    new OneSignal.PostNotificationResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.e("OneSignalExample", "postNotification Success: " + response);
                        }

                        @Override
                        public void onFailure(JSONObject response) {
                            Log.e("OneSignalExample", "postNotification Failure: " + response);
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("OneSignalExample", "postNotification Error: " + e);
        }


        recycler_user_view.setVisibility(View.GONE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("GroupId", Common.GROUP_ID);
            jsonObject.put("UserId", USER_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url2 = AppConstant.GET_SEND_INVITATION;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url2, APIcall.OPERATION_SEND_INVITATION, this);


    }

    @Override
    public void onFail(int operationCode, String response) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            Glide.with(NewAllUserActivity.this).load(Common.GROUP_URL).placeholder(R.drawable.logo).into(iv_profile);
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
                    Common.displayToastMessageShort(NewAllUserActivity.this, "Please select image.", true);
                    return;
                }
                if (ed_gruop_name.getText().toString().equals("") || ed_gruop_name.getText().toString() == null) {
                    Common.displayToastMessageShort(NewAllUserActivity.this, "Enter your name.", true);
                    return;
                }
                if (ed_gruop_des.getText().toString().equals("") || ed_gruop_des.getText().toString() == null) {
                    Common.displayToastMessageShort(NewAllUserActivity.this, "Enter your description.", true);
                    return;
                }
                try {
                    Glide.with(NewAllUserActivity.this).load(Common.GROUP_URL).placeholder(R.drawable.logo).into(iv_profile);
                } catch (Exception e) {

                }
                Common.GROUP_NAME = ed_gruop_name.getText().toString();
                Common.GROUP_DES = ed_gruop_des.getText().toString();
                callAPItoCreatGroup(ed_gruop_name.getText().toString(), ed_gruop_des.getText().toString(), Common.GROUP_URL);
                mdialog.cancel();

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
        Common.hideKeyboard(NewAllUserActivity.this);
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
                    File N_file = getCompressed(NewAllUserActivity.this, image_path);
                    image_path = N_file.getPath();
                     file_name = new File(image_path).getName();
                    callApiImageUpload(image_path, file_name, "GroupIcon");
                } catch (Exception e) {
                    Log.d("SUMITPATEL", "EROOR" + e.toString());
                }
            }
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


}
