package com.newiplquizgame.myipl.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.GroupDatum;
import com.newiplquizgame.myipl.pkg.GruopMaster;
import com.newiplquizgame.myipl.recyclerview_adapter.RVAllMatchListAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PastMatchesFrg extends Fragment implements APIcall.ApiCallListner {

    static Activity activity;
    private ProgressDialog dialog;
    LinearLayout ll_no_data;
    LinearLayout ll_invite;
    static RecyclerView recycler_view;
    private SharedPreferenceManagerFile sharedPref;
    GruopMaster mgruopMaster;
    List<GroupDatum> mGroupData;
    static RVAllMatchListAdapter allMatchListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pending, container, false);
        findViewHolder(root);
        Log.i("SUMITPATEL", "Call Response");
        return root;
    }

    private void findViewHolder(View root) {
        recycler_view = root.findViewById(R.id.recycler_view);
        ll_no_data = root.findViewById(R.id.ll_no_data);
        ll_invite = root.findViewById(R.id.ll_invite);
        ll_invite.setVisibility(View.GONE);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setLayoutManager(gridLayoutManager);
        recycler_view.setVisibility(View.GONE);
        ll_no_data.setVisibility(View.GONE);

        ApiGettournament();
    }

    private void ApiGettournament() {
        String url = AppConstant.GET_TOURNAMENT;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_ALL_TOURNAMENT, this);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

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

        Log.d("SUMITPATEL", "MAINURL" + response);
        try {
            if (operationCode == APIcall.OPERATION_ALL_TOURNAMENT) {
                Gson gson = new Gson();
                mgruopMaster = gson.fromJson(response, GruopMaster.class);
                if (mgruopMaster.getStatus() == 0) {
                    recycler_view.setVisibility(View.VISIBLE);
                    ll_no_data.setVisibility(View.GONE);
                    mGroupData = new ArrayList<>();
                    mGroupData = mgruopMaster.getData();
                    allMatchListAdapter = new RVAllMatchListAdapter(getActivity(), mGroupData.get(0).getScheduleLst());
                    recycler_view.setAdapter(allMatchListAdapter);
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
                    Toast.makeText(getContext(), "" + mgruopMaster.getMsg(), Toast.LENGTH_SHORT).show();
                    recycler_view.setVisibility(View.GONE);
                    ll_no_data.setVisibility(View.VISIBLE);
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
        hideDialog();
    }

    @Override
    public void onFail(int operationCode, String response) {
        hideDialog();
    }
}
