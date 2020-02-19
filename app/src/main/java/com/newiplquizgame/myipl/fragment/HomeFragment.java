package com.newiplquizgame.myipl.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.activity.StartActivity;
import com.newiplquizgame.myipl.extra.ZoomOutTransformation;
import com.newiplquizgame.myipl.view_pager_Adapter.VPVideoAdapter;
import com.newiplquizgame.myipl.view_pager_Adapter.VPTournamentAdapter;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.GroupDatum;
import com.newiplquizgame.myipl.pkg.GruopMaster;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

import static com.facebook.FacebookSdk.getApplicationContext;


public class HomeFragment extends Fragment implements APIcall.ApiCallListner {
    ViewPager viewpager;
    PagerAdapter adapter;
    GruopMaster mgruopMaster;
    List<GroupDatum> mGroupData;
    CircleIndicator indicator;
    CircleIndicator indicator_1;
    private SharedPreferenceManagerFile sharedPref;
    GoogleSignInClient mGoogleSignInClient;
    ViewPager pager_video;
    VPVideoAdapter mVideoAdapter;
    LinearLayout ll_main;
    private ProgressDialog dialog;
    ZoomOutTransformation  zoom = new ZoomOutTransformation();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPref = new SharedPreferenceManagerFile(getActivity());
        findView(root);
        ApiGettournament();
        ApiGetVideos();
        return root;
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

    private void findView(View root) {
        indicator_1 = root.findViewById(R.id.indicator_1);
        ll_main = root.findViewById(R.id.ll_main);
        ll_main.setVisibility(View.GONE);
        viewpager = root.findViewById(R.id.pager);
        viewpager.setClipToPadding(false);
        viewpager.setPadding(100, 0, 100, 0);
        indicator = root.findViewById(R.id.indicator);

        viewpager.setPageTransformer(false,zoom);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pager_video = root.findViewById(R.id.pager_video);
        pager_video.setClipToPadding(false);
        pager_video.setPadding(100, 0, 100, 0);
        pager_video.setPageTransformer(false,zoom);
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_ALL_TOURNAMENT) {
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
                    Toast.makeText(getContext(), "" + mgruopMaster.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    ll_main.setVisibility(View.VISIBLE);
                    mGroupData = new ArrayList<>();
                    mGroupData = mgruopMaster.getData();
                    adapter = new VPTournamentAdapter(getContext(), mGroupData.get(0).getScheduleLst());
                    viewpager.setAdapter(adapter);
                    indicator.setViewPager(viewpager);
                }
                hideDialog();
            }
            if (operationCode == APIcall.OPERATION_ALL_VIDEO) {

                Gson gson = new Gson();
                mgruopMaster = gson.fromJson(response, GruopMaster.class);
                if (mgruopMaster.getStatus() == 1) {
                    Toast.makeText(getContext(), "" + mgruopMaster.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    ll_main.setVisibility(View.VISIBLE);
                    mGroupData = new ArrayList<>();
                    mGroupData = mgruopMaster.getData();
                    mVideoAdapter = new VPVideoAdapter(getContext(), mGroupData);
                    pager_video.setAdapter(mVideoAdapter);
                    indicator_1.setViewPager(pager_video);
                }
                hideDialog();
            }
        } catch (Exception e) {
            showDialog();
            if (Common.isInternetAvailable(getActivity())) {
                try {
                    mGoogleSignInClient.signOut();
                } catch (Exception e1) {

                }
                sharedPref.clearPreference();
                startActivity(new Intent(getApplicationContext(), StartActivity.class));
                getActivity().finish();
            } else {
                Common.displayToastMessageShort(getActivity(), "No connection found. Please connect & try again.", true);
            }

        }
        hideDialog();
    }

    @Override
    public void onFail(int operationCode, String response) {

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
}