package com.newiplquizgame.myipl.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.recyclerview_adapter.RVAllMatchListAdapter;
import com.newiplquizgame.myipl.recyclerview_adapter.RVAllVideoListAdapter;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.GroupDatum;
import com.newiplquizgame.myipl.pkg.GruopMaster;

import java.util.ArrayList;
import java.util.List;

public class AllVideoActivity extends AppCompatActivity implements APIcall.ApiCallListner {

    RecyclerView recycler_view;
    LinearLayout ll_no_data;
    GruopMaster mgruopMaster;
    List<GroupDatum> mGroupData;
    private ProgressDialog dialog;
    private SharedPreferenceManagerFile sharedPref;
    GoogleSignInClient mGoogleSignInClient;
    int POS = 0;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_video);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        POS = getIntent().getIntExtra("POSTION",0);
        if (POS == 1){
            getSupportActionBar().setTitle("All Videos");
            ApiGetVideos();

        }else {
            getSupportActionBar().setTitle("All Match's");
            ApiGettournament();

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        bindViewData();

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

    private void bindViewData() {
        recycler_view = findViewById(R.id.recycler_view);
        ll_no_data = findViewById(R.id.ll_no_data);
        sharedPref = new SharedPreferenceManagerFile(this);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(gridLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setVisibility(View.GONE);
        ll_no_data.setVisibility(View.GONE);


    }

    private void ApiGetVideos() {
        String url = AppConstant.GET_VIDEO;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_ALL_VIDEO, this);
    }

    private void ApiGettournament() {
        String url = AppConstant.GET_TOURNAMENT;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_ALL_TOURNAMENT, this);
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_ALL_TOURNAMENT) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_ALL_VIDEO) {
            showDialog();
        }
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            if (operationCode == APIcall.OPERATION_ALL_TOURNAMENT) {
                Gson gson = new Gson();
                mgruopMaster = gson.fromJson(response, GruopMaster.class);
                if (mgruopMaster.getStatus() == 1) {
                    Toast.makeText(this, "" + mgruopMaster.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    ll_no_data.setVisibility(View.GONE);
                    recycler_view.setVisibility(View.VISIBLE);
                    mGroupData = new ArrayList<>();
                    mGroupData = mgruopMaster.getData();
                    RVAllMatchListAdapter mVideoAdapter = new RVAllMatchListAdapter(this, mGroupData.get(0).getScheduleLst());
                    recycler_view.setAdapter(mVideoAdapter);
                }
                hideDialog();
            }
            if (operationCode == APIcall.OPERATION_ALL_VIDEO) {

                Gson gson = new Gson();
                mgruopMaster = gson.fromJson(response, GruopMaster.class);
                if (mgruopMaster.getStatus() == 1) {
                    Toast.makeText(this, "" + mgruopMaster.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    ll_no_data.setVisibility(View.GONE);
                    recycler_view.setVisibility(View.VISIBLE);
                    mGroupData = new ArrayList<>();
                    mGroupData = mgruopMaster.getData();
                    RVAllVideoListAdapter mVideoAdapter = new RVAllVideoListAdapter(this, mGroupData);
                    recycler_view.setAdapter(mVideoAdapter);
                }
                hideDialog();
            }
        } catch (Exception e) {
            showDialog();
            if (Common.isInternetAvailable(this)) {
                try {
                    mGoogleSignInClient.signOut();
                } catch (Exception e1) {

                }
                sharedPref.clearPreference();
                startActivity(new Intent(getApplicationContext(), StartActivity.class));
                finish();
            } else {
                Common.displayToastMessageShort(this, "No connection found. Please connect & try again.", true);
            }

        }
        hideDialog();
    }

    @Override
    public void onFail(int operationCode, String response) {

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
}
