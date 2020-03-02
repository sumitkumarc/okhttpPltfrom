package com.newiplquizgame.myipl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.view_pager_Adapter.VPFragmentAdapter;

public class PridicastionFrg extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pridicastion, container, false);
        findViewHolder(root);
        return root;
    }

    private void findViewHolder(View root) {

        viewPager = (ViewPager) root.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = root.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        VPFragmentAdapter adapter = new VPFragmentAdapter(getChildFragmentManager());
        adapter.addFragment(new UpcomingMatchesFrg(), "Upcoming Matches");
        adapter.addFragment(new PastMatchesFrg(), "Past Matches");
        viewPager.setAdapter(adapter);
    }


}
