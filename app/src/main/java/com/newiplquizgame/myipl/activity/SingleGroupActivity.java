package com.newiplquizgame.myipl.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.extra.Common;

import me.relex.circleindicator.CircleIndicator;

public class SingleGroupActivity extends AppCompatActivity {
    int anInt = 0;
    ViewPager viewpager;
    PagerAdapter adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.particular_group_info);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Common.GROUP_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        findViewBinder();
    }

    private void findViewBinder() {

        viewpager = findViewById(R.id.pager);
        viewpager.setClipToPadding(false);
        viewpager.setPageMargin(24);
        viewpager.setPadding(8, 0, 48, 0);
        viewpager.setOffscreenPageLimit(3);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewpager);
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
        findViewById(R.id.ll_point_table).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog mdialog = new Dialog(SingleGroupActivity.this);
                mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mdialog.setContentView(R.layout.pop_item_match_point);
                mdialog.setCancelable(true);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(mdialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                ((ImageButton) mdialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mdialog.dismiss();
                    }
                });

                mdialog.show();
                mdialog.getWindow().setAttributes(lp);
            }
        });
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

}
