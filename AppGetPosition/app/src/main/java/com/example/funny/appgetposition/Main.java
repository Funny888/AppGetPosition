package com.example.funny.appgetposition;

import android.Manifest;
import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PatternMatcher;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.internal.SnackbarContentLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.zip.Inflater;


public class Main extends TabActivity implements TabHost.OnTabChangeListener {

    TabHost tabHost;
    WebView web;
    ConstraintLayout constraintLayout;
    String whereIMm = "Где я";
    Double latitude,longitude;
    String _lat,_long;
    public static final int BEGIN_LATITUDE=33,BEGIN_LONGTITUDE=54,FINAL_LATITUDE=44,FINAL_LONGTITUDE=65;
    static {
        System.loadLibrary("native-lib");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tabHost = getTabHost();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(getResources().getString(R.string.tab1));
        tabSpec.setContent(new Intent(this,One.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(whereIMm);
        tabSpec.setContent(tabFactory);
        tabHost.setOnTabChangedListener(this);
        tabHost.addTab(tabSpec);

        constraintLayout = findViewById(R.id.Constlay);




        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.twhereAmI);
//        tv.setText(stringFromJNI());
    }
    TabHost.TabContentFactory tabFactory = new TabHost.TabContentFactory() {
        @Override
        public View createTabContent(String tag) {

            if (tag == "tag2") {

                web = new WebView(Main.this);
                web.setWebViewClient(new WebViewClient());//search?q=56.30286133+44.02253449
                WebSettings webSettings = web.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setSupportZoom(true);
                webSettings.setGeolocationEnabled(true);
                webSettings.getLoadWithOverviewMode();

           return web;

            }
            return null;
        }

    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,R.integer.item0,0,"Настройки");
        menu.add(0,R.integer.item1,0,"Выйти");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.integer.item0:
            {
                Intent intent =  new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,Uri.parse("package:"+ getPackageName()));
                startActivity(intent);
                break;
            }
            case R.integer.item1:

            {
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1)
        {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    Toast.makeText(this,"Пожалуйста подтвердите разрешение",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Snackbar.make(constraintLayout,"Проверьте настройки",Snackbar.LENGTH_LONG)
                            .setAction("Настройки", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent =  new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,Uri.parse("package:" + getPackageName()));
                                    startActivity(intent);

                                }
                            })
                            .show();
                }
            }

        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onTabChanged(String tabId) {
        switch (tabId)
        {
            case "tag2":
            {
                File file = new File(getFilesDir().getAbsolutePath().toString() + File.separator + "test" + File.separator + "test.txt");
                try {
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    String d = "";
                        while (br.ready() == true) {
                        d = br.readLine();
                         }
                    Log.d("LOG_TEST: ", d);
                    _lat = d.substring(BEGIN_LATITUDE,FINAL_LATITUDE);
                    _long = d.substring(BEGIN_LONGTITUDE,FINAL_LONGTITUDE);
                    br.close();
                    fr.close();
                    Log.d("LOG_TEST: ", _lat + " " + _long);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getBaseContext(),"Последних данных не было",Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                latitude = Double.valueOf(_lat);
                longitude = Double.valueOf(_long);
                web.loadUrl("https://www.google.ru/maps/search/"+ latitude + " " + longitude);
                break;
            }
        }
    }

}
