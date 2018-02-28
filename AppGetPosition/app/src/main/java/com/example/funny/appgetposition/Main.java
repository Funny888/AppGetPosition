package com.example.funny.appgetposition;

import android.app.TabActivity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TabHost;
import android.widget.TextView;


public class Main extends TabActivity{


    String whereIMm = "Где я";

    static {
        System.loadLibrary("native-lib");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(getResources().getString(R.string.tab1));
        tabSpec.setContent(new Intent(this,One.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(whereIMm);
        tabSpec.setContent(tabFactory);
        tabHost.addTab(tabSpec);




        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.twhereAmI);
//        tv.setText(stringFromJNI());
    }
    TabHost.TabContentFactory tabFactory = new TabHost.TabContentFactory() {
        @Override
        public View createTabContent(String tag) {

            if (tag == "tag2") {
           WebView web = new WebView(Main.this);
           return web;

            }
            return null;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0,0,0,"Выйти");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


}
