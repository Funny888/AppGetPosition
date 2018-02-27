package com.example.funny.appgetposition;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class Main extends Activity implements View.OnClickListener{

TextView whereIAm;
CheckBox chGPRS,chInternet;
Button bAsk;

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        whereIAm = findViewById(R.id.twhereAmI);
        chGPRS = findViewById(R.id.chGPRS);
        chInternet = findViewById(R.id.chInet);
        bAsk = findViewById(R.id.bAsk);
        bAsk.setOnClickListener(this);

        if(chGPRS.isChecked())
        {

        }
        if (chInternet.isChecked())
        {

        }


        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.twhereAmI);
//        tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onClick(View v) {

    }
}
