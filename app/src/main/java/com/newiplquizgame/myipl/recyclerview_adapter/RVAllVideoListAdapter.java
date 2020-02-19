package com.newiplquizgame.myipl.recyclerview_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.activity.AllVideoActivity;
import com.newiplquizgame.myipl.pkg.GroupDatum;

import java.util.List;

public class RVAllVideoListAdapter extends RecyclerView.Adapter {
    LayoutInflater inflater;
    Context context;
    List<GroupDatum> myouTubeVideos;


    public RVAllVideoListAdapter(AllVideoActivity allVideoActivity, List<GroupDatum> mGroupData) {
        this.context = allVideoActivity;
        this.myouTubeVideos = mGroupData;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        WebView videoWeb;

        public MyViewHolder(View view) {
            super(view);
            this.videoWeb = view.findViewById(R.id.videoWebView);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_view, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder myViewHolder = ((MyViewHolder) holder);
        myViewHolder.videoWeb.getSettings().setJavaScriptEnabled(true);
        myViewHolder.videoWeb.getSettings().setLoadWithOverviewMode(true);
        myViewHolder.videoWeb.getSettings().setUseWideViewPort(true);
        myViewHolder.videoWeb.setWebChromeClient(new WebChromeClient() {
        });

        String html = "<iframe class=\"youtube-player\" style=\"border: 0; width: 100%; height: 100%; padding:0px; margin:0px\" id=\"ytplayer\" type=\"text/html\" " +
                "src=" + myouTubeVideos.get(position).getVidLink()
                + "?fs=0\" frameborder=\"0\">\n"
                + "</iframe>";

        myViewHolder.videoWeb.loadData(html, "text/html", "utf-8");
    }

    @Override
    public int getItemCount() {
        return myouTubeVideos.size();
    }
}
