package com.newiplquizgame.myipl.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.recyclerview_adapter.RVAllMatchListAdapter;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.GroupDatum;
import com.newiplquizgame.myipl.pkg.GruopMaster;

import java.util.ArrayList;
import java.util.List;


public class AllMatchFragment extends Fragment implements APIcall.ApiCallListner, SwipeRefreshLayout.OnRefreshListener {
    static RecyclerView recycler_view;
    SwipeRefreshLayout swipeToRefresh;
    Handler handler = new Handler();
    static Activity activity;
    private ProgressDialog dialog;
    LinearLayout ll_no_data;
    static RVAllMatchListAdapter allMatchListAdapter;
    GruopMaster mgruopMaster;
    List<GroupDatum> mGroupData;
    private SharedPreferenceManagerFile sharedPref;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gruops, container, false);
        sharedPref = new SharedPreferenceManagerFile(getActivity());
        findViewHolder(root);
        return root;
    }

    private void findViewHolder(View root) {
        activity = getActivity();
        recycler_view = root.findViewById(R.id.recycler_view);
        ll_no_data = root.findViewById(R.id.ll_no_data);

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(gridLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        this.swipeToRefresh = root.findViewById(R.id.swipeToRefresh);
        this.swipeToRefresh.setOnRefreshListener(this);
        recycler_view.setVisibility(View.GONE);

        ll_no_data.setVisibility(View.GONE);
        ApiGettournament();
    }

    private void ApiGettournament() {
        String url = AppConstant.GET_TOURNAMENT;
        APIcall apIcall = new APIcall(activity.getApplicationContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_ALL_TOURNAMENT, this);
    }


    @Override
    public void onRefresh() {
        swipeToRefresh.setRefreshing(true);
        recycler_view.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ApiGettournament();
            }
        }, 500);
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
                if (mgruopMaster.getStatus() == 0) {
                    mGroupData = new ArrayList<>();
                    mGroupData = mgruopMaster.getData();
                    recycler_view.setVisibility(View.VISIBLE);
                    swipeToRefresh.setRefreshing(false);
                    ll_no_data.setVisibility(View.GONE);
                    allMatchListAdapter = new RVAllMatchListAdapter(activity, mGroupData.get(0).getScheduleLst());
                    recycler_view.setAdapter(allMatchListAdapter);
                    allMatchListAdapter.notifyDataSetChanged();
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

                } else {
                    recycler_view.setVisibility(View.GONE);
                    swipeToRefresh.setVisibility(View.GONE);
                    ll_no_data.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "" + mgruopMaster.getMsg(), Toast.LENGTH_SHORT).show();
                }
                hideDialog();
            }
        } catch (Exception e) {
            showDialog();
            if (Common.isInternetAvailable(getActivity())) {
                try {
                    // mGoogleSignInClient.signOut();
                } catch (Exception e1) {

                }
                sharedPref.clearPreference();
            } else {
                Common.displayToastMessageShort(getActivity(), "No connection found. Please connect & try again.", true);
            }
            getActivity().finish();
        }
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
