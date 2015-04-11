package com.pasha.termo;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pasha.termo.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void onRequestButtonPress(
        View view)
    {
    	requestSite();
    }
    
    public boolean isNetworkEnabled()
    {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean bRet;
        if (networkInfo != null && networkInfo.isConnected()) {
        	bRet = true; 
        } else {
        	bRet = false;
        }
        return bRet;
	}

    public void requestSite()
    {
     	TextView lblText = (TextView) findViewById(R.id.lblText);
     	if (!isNetworkEnabled()) {
         	lblText.setText(getString(R.string.strNoNetwork));
         	return;
     	}
     	lblText.setText(getString(R.string.strDoRequest));
     	ImageView imgGraph = (ImageView) findViewById(R.id.imgGraph);
     	imgGraph.setImageBitmap(null);
    	new DownloadWebpageText().execute(lblText);
    	new DownloadWebpageGraph().execute(getString(R.string.strUrlGraph), imgGraph);
    }
    
}
