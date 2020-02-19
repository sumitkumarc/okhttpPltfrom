package com.newiplquizgame.myipl.view_pager_Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.activity.AllVideoActivity;
import com.newiplquizgame.myipl.extra.YouTubeVideos;
import com.newiplquizgame.myipl.pkg.GroupDatum;

import java.util.List;
import java.util.Vector;

public class VPVideoAdapter extends PagerAdapter {
    LayoutInflater inflater;
    Context context;
    List<GroupDatum> myouTubeVideos;

    public VPVideoAdapter(Context context, List<GroupDatum> youtubeVideos) {
        this.context = context;
        this.myouTubeVideos = youtubeVideos;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemview;
        if (position == 5) {
            itemview = inflater.inflate(R.layout.item_row_view_more, container, false);
            Button materialButton = itemview.findViewById(R.id.MB_more);
            materialButton.setText("More Video's");
            materialButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AllVideoActivity.class);
                    intent.putExtra("POSTION",1);
                    context.startActivity(intent);
                }
            });
        } else {
            itemview = inflater.inflate(R.layout.item_video_view, container, false);

            WebView videoWeb = (WebView) itemview.findViewById(R.id.videoWebView);
            videoWeb.getSettings().setJavaScriptEnabled(true);
            videoWeb.getSettings().setLoadWithOverviewMode(true);
            videoWeb.getSettings().setUseWideViewPort(true);
            videoWeb.setWebChromeClient(new WebChromeClient() {
            });

            String html = "<iframe class=\"youtube-player\" style=\"border: 0; width: 100%; height: 100%; padding:0px; margin:0px\" id=\"ytplayer\" type=\"text/html\" " +
                    "src=" + myouTubeVideos.get(position).getVidLink()
                    + "?fs=0\" frameborder=\"0\">\n"
                    + "</iframe>";

            videoWeb.loadData(html, "text/html", "utf-8");
        }
        ((ViewPager) container).addView(itemview);

        return itemview;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);

    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }


}
