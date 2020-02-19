package com.newiplquizgame.myipl.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile.EMAIL;
import static com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile.USER_NAME;
import static com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile.USER_PROFILE_PIC;


public class MyProfileFragment extends Fragment {
    CircleImageView iv_profile;
    TextView txt_nikname;
    TextView txt_emailid;
    Activity activity;
    private SharedPreferenceManagerFile sharedPref;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        activity = getActivity();
        sharedPref = new SharedPreferenceManagerFile(activity);
        findviewData(root);
        return root;
    }

    private void findviewData(View root) {
        iv_profile = root.findViewById(R.id.iv_profile);
        txt_nikname = root.findViewById(R.id.txt_nikname);
        txt_emailid = root.findViewById(R.id.txt_emailid);
        try {
            Picasso.get().load(sharedPref.getFromSharedPreference(USER_PROFILE_PIC)).into(iv_profile);
        } catch (Exception e) {
            Toast.makeText(activity, "Profile Image Not Found", Toast.LENGTH_SHORT).show();
        }
        txt_nikname.setText(sharedPref.getFromSharedPreference(USER_NAME));
        txt_emailid.setText(sharedPref.getFromSharedPreference(EMAIL));
    }
}