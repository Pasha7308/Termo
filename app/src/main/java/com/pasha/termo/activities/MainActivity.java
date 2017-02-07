package com.pasha.termo.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.pasha.termo.DownloadWebpageGraph;
import com.pasha.termo.DownloadWebpageText;
import com.pasha.termo.R;

public class MainActivity extends Activity {

    private static final int SETTINGS_RESULT = 1;

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
        ImageView imgGraphCollected = (ImageView) findViewById(R.id.imgGraphCollected);
     	imgGraph.setImageBitmap(null);
        new DownloadWebpageText().execute(lblTextTermo, lblTextTor, imgGraphCollected);
    	new DownloadWebpageGraph().execute(getString(R.string.strUrlGraph), imgGraph);
    }

    private void setTypeFace(TextView textView) {
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivityForResult(i, SETTINGS_RESULT);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
