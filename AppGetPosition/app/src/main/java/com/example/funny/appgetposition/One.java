package com.example.funny.appgetposition;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class One extends Activity implements View.OnClickListener, LocationListener {

    TextView whereIAm;
    CheckBox chGPS, chInternet;
    Button bAsk;
    LocationManager locationManager;
    Location loc ;
    double osH, osW;

    String[] perm = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one);

        whereIAm = findViewById(R.id.twhereAmI);
        chGPS = findViewById(R.id.chGPS);
        chGPS.setOnClickListener(this);
        chInternet = findViewById(R.id.chInet);
        bAsk = findViewById(R.id.bAsk);
        bAsk.setOnClickListener(this);





        if (chInternet.isChecked()) {

        }






        }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bAsk:
            {
                whereIAm.setText("Location: Широта: " + osH + ";" + "Долгота:" + osW);

                break;
            }

            case R.id.chGPS:
            {
                if(!chGPS.isChecked()) {
                    if (ContextCompat.checkSelfPermission(this, perm[0]) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, perm[1]) == PackageManager.PERMISSION_GRANTED) {
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if (loc != null) {
                            double ds = loc.getLatitude();
                            Log.i("latitude:", String.valueOf(ds));
                        } else {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, this);
                        }
                        List<String> f = locationManager.getProviders(true);
                        Log.i("providers", String.valueOf(f));


                    }
                }
                else
                {
                    ActivityCompat.requestPermissions(this.getParent(),perm,1);
                }

                }
                break;
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1)
        {
            if (shouldShowRequestPermissionRationale(perm[0]))
            {
                Toast.makeText(getBaseContext(),"Пожалуйста подтвердите разрешение",Toast.LENGTH_SHORT);
            }
            else
            {
                Snackbar.make(new View(this),"Проверьте настройки",Snackbar.LENGTH_LONG)
                        .setAction("Настройки", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri.parse("package:"+ getPackageName());

                            }
                        });
            }
        }
    }

    @Override
    public void onLocationChanged(final Location loc) {
       osH = loc.getLatitude();
       osW = loc.getLongitude();
        Toast.makeText(getBaseContext(),"Location:" + osH+ ";" + osW,Toast.LENGTH_LONG).show();
        Log.i("Location:", "Широта: " + osH + ";" + "Долгота:" + osW);
        File file = new File(File.pathSeparator + "Coordinate's");
        try {
            FileOutputStream fileStream = openFileOutput(file.getPath(),Context.MODE_PRIVATE);
            fileStream.write(("Location: Широта: " + osH + ";" + "Долгота:" + osW).getBytes());
            fileStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getBaseContext(),"GPS is Enabled",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getBaseContext(),"GPS is Disabled",Toast.LENGTH_LONG).show();
    }
}
