package com.pasha.termo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pasha.termo.R;

import java.util.Random;

public class MainActivity extends Activity {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activityMain);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                requestSite();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        setTypeFace((TextView) findViewById(R.id.lblTextTermo));
        setTypeFace((TextView) findViewById(R.id.lblTextIao));

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        requestSite();
    }

    @Override
    public boolean onCreateOptionsMenu(
        Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public boolean isNetworkEnabled()
    {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
	}

    public void requestSite()
    {
        TextView lblTextTermo = (TextView) findViewById(R.id.lblTextTermo);
        TextView lblTextTor = (TextView) findViewById(R.id.lblTextIao);
     	if (!isNetworkEnabled()) {
            lblTextTermo.setText(getString(R.string.strNoNetwork));
            lblTextTor.setText(getString(R.string.strNoNetwork));
         	return;
     	}
        lblTextTermo.setText(getString(R.string.strDoRequest));
        lblTextTor.setText(getString(R.string.strDoRequest));
     	ImageView imgGraph = (ImageView) findViewById(R.id.imgGraph);
        DrawView drawView = (DrawView) findViewById(R.id.drawView);
     	imgGraph.setImageBitmap(null);
        new DownloadWebpageText().execute(lblTextTermo, lblTextTor, drawView);
    	new DownloadWebpageGraph().execute(getString(R.string.strUrlGraph), imgGraph);
    }

    private void setTypeFace(TextView textView) {
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
    }

}
