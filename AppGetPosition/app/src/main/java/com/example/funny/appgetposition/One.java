package com.example.funny.appgetposition;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;


public class One extends Activity implements View.OnClickListener, LocationListener {

    TextView whereIAm;
    CheckBox chGPS;
    Button bAsk;
    EditText eTime;
    LocationManager locationManager;
    Location loc;
    double osH, osW;
    SimpleDateFormat sdf;
    String[] perm = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    String time;
    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one);

        whereIAm = findViewById(R.id.twhereAmI);
        whereIAm.setOnClickListener(this);
        eTime = findViewById(R.id.eTime);
        chGPS = findViewById(R.id.chGPS);
        chGPS.setOnClickListener(this);
        bAsk = findViewById(R.id.bAsk);
        bAsk.setOnClickListener(this);

        sdf = new SimpleDateFormat("dd:HH:mm");
        time = sdf.format(new Date());
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bAsk: {
                whereIAm.setText("Location: Широта: " + osH + ";" + "Долгота:" + osW);
                break;
            }

            case R.id.chGPS: {
                if (chGPS.isChecked()) {
                    requestGPS();
                } else {

                        if (checkSelfPermission(perm[0]) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(perm[1]) == PackageManager.PERMISSION_GRANTED)
                        {
                            try {
                            locationManager.removeUpdates(this);
                            }
                            catch (Exception e) {}
                            Toast.makeText(getBaseContext(), "Данные GPS не получаем", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getBaseContext(), "Permission isn't granted", Toast.LENGTH_SHORT).show();
                        }
                    }
                break;
            }
            case R.id.twhereAmI:
            {
                TabActivity main = (TabActivity) this.getParent();
                TabHost tabHost = main.getTabHost();
                tabHost.setCurrentTabByTag("tag2");
                break;
            }

        }
    }


    public void requestGPS() {
        if (ContextCompat.checkSelfPermission(this, perm[0]) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, perm[1]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getParent(), perm, 1);
        } else {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 6, 1, this);
            if (loc != null) {
                osH = loc.getLatitude();
                osW = loc.getLongitude();
                Log.i("Location:", "Широта: " + osH + ";" + "Долгота:" + osW);

            } else {

                    loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    whereIAm.setText("Ожидание координат");

            }


        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("latitude",osH);
        outState.putDouble("longitude",osW);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        osH = savedInstanceState.getDouble("latitude");
        osW = savedInstanceState.getDouble("longitude");
    }


    @Override
    public void onLocationChanged(final Location loc) {


                    osH = loc.getLatitude();
                    osW = loc.getLongitude();
                    Toast.makeText(getBaseContext(), "Location:" + osH + ";" + osW, Toast.LENGTH_LONG).show();
                    Log.i("Location:", "Широта: " + osH + ";" + "Долгота:" + osW);
                    recInFile();
                     eTime.setText(time);

                }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getBaseContext(), "GPS is Enabled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getBaseContext(), "GPS is Disabled", Toast.LENGTH_LONG).show();
    }

    private void recInFile()
    {
        File dir = new File(getFilesDir().getAbsolutePath().toString() + File.separator + "test");
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        File file = new File(dir,"test.txt");
        try {
            FileWriter fw = new FileWriter(file,true);
            fw.append("Time: " + time + " Location: Широта: " + osH + " " + "Долгота: " + osW + "\n" );
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
