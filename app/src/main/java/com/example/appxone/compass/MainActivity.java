package com.example.appxone.compass;

import java.text.DecimalFormat;
import java.util.List;

import com.example.appxone.compass.R;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
//import com.google.ads.AdView;

import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    float microtesls;
    private static SensorManager mSensorManager;

    TextView textviewAzimuth, textviewPitch, textviewRoll;
    public Sensor mSensor;
    TextView angleTextview, diemension;
    ImageView imageView, nidleImageView;
    RelativeLayout relative;
    Typeface tf;
    //public 	AdRequest request;
    Context context;
    LinearLayout adlayout;
    double double_degree;
    double degree_convert;
    double x, y;
    double azim, pit, tot;
    double multiply, check = -1.0;
    double minus, add;
int gg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textviewAzimuth = (TextView) findViewById(R.id.azimuth);

        textviewPitch = (TextView) findViewById(R.id.pitch);
        textviewRoll = (TextView) findViewById(R.id.roll);
        context = this;
        //AdView adView = null;


        //String publisherId = "a1526296aef0e80";
        //	String testingDeviceId = "359918043312594";

        adlayout = (LinearLayout) findViewById(R.id.adLayout);
        // Get Screen
        Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int screenWidth = display.getWidth();
        Log.e("Screen Width", "" + screenWidth);


        //adView = new AdView(this, AdSize.SMART_BANNER, publisherId);

        //adlayout.addView(adView);

        // AdMob Request
        //request = new AdRequest();

        // only for testing Devices
        //request.addTestDevice(AdRequest.TEST_EMULATOR);
        //request.addTestDevice(testingDeviceId);

        // load Ad
        //	adView.loadAd(request);
        imageView = (ImageView) findViewById(R.id.rotation_Image);
        nidleImageView = (ImageView) findViewById(R.id.niddle_Image);
        angleTextview = (TextView) findViewById(R.id.degreeText);
        diemension = (TextView) findViewById(R.id.DiemensionText);
        // relative.setBackgroundResource(R.drawable.clock);
        imageView.setImageResource(R.drawable.clock);
        nidleImageView.setImageResource(R.drawable.niddle);

        tf = Typeface.createFromAsset(getAssets(), "Font/MyriadPro_Regular.otf");

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        //List<Sensor> mySensors = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);


        if (mSensor != null) {
            mSensorManager.registerListener(mySensorEventListener, mSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
            Log.i("Compass MainActivity", "Registerered for ORIENTATION Sensor");

        } else {
            Log.e("Compass MainActivity", "Registerered for ORIENTATION Sensor");
            Toast.makeText(this, "ORIENTATION Sensor not found",
                    Toast.LENGTH_LONG).show();

        }
    }

    SensorEventListener mySensorEventListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // angle between the magnetic north directio
            // 0=North, 90=East, 180=South, 270=West
            // setContentView(linLayout);

            angleTextview.setTypeface(tf);
            microtesls = event.values[0];
            azim = event.values[0];
            pit = event.values[1];
            tot = Math.atan2(azim, pit);
            degree_convert = Math.toDegrees(tot);

            imageView.setRotation((float) degree_convert);
            // Log.e("value", "" + degree);
            // compassView.updateData(degree);

            //degree_convert	=microtesls;

            //Math.toDegrees(degree_convert);
            if (degree_convert < 0) {
              //  Log.i("minus", "minus");

                multiply = check * degree_convert;
                //imageView.setRotation((float)multiply);

//                else
//{
//    gg=0;
//}

                String value = getDirectionFromDegrees((float) multiply);
                angleTextview.setText(String.format("%.0f", multiply) + " " + value);


                textviewAzimuth.setText("Azimuth: " + String.format("%.0f", multiply) + "");
                textviewPitch.setText(" Pitch: " + String.format("%.0f", event.values[1]) + "");
                textviewRoll.setText(" Roll: " + String.format("%.0f", event.values[2]) + "");
                //Log.e("Diemension", value);
            } else if (degree_convert >= 0) {
              //  Log.i("plus", "plus");

                if (multiply <= 1) {
                    add = 360 - degree_convert;
                    //add = minus + multiply;

                }
                else if (multiply <= 180) {
                    minus = multiply - degree_convert;
                    add = minus + multiply;
                }            //	imageView.setRotation((float)add);


                String value = getDirectionFromDegrees((float) add);
                angleTextview.setText(String.format("%.0f", add) + " " + value);


                textviewAzimuth.setText("Azimuth: " + String.format("%.0f", add) + "");
                textviewPitch.setText(" Pitch: " + String.format("%.0f", event.values[1]) + "");
                textviewRoll.setText(" Roll: " + String.format("%.0f", event.values[2]) + "");

            }
//

//			imageView.setRotation((float)degree_convert);

//			String value=getDirectionFromDegrees((float)degree_convert);
//			//Log.e("Diemension", value);

//			angleTextview.setText(String.format("%.0f", degree_convert) + " " + value);
//
//
//			textviewAzimuth.setText("Azimuth: " +String.format("%.0f", degree_convert)+"");
//			textviewPitch.setText(" Pitch: " + String.format("%.0f",event.values[1])+"");
//			textviewRoll.setText(" Roll: " + String.format("%.0f",event.values[2])+"");

        }
    };

    private String getDirectionFromDegrees(float degrees) {
        if (degrees > 354.38 || degrees < 16.87) {
            return "N";
        }
        if (degrees > 16.88 && degrees < 39.37) {
            return "NNE";
        }
        if (degrees > 39.38 && degrees < 61.87) {
            return "NE";
        }
        if (degrees > 61.88 && degrees < 84.37) {
            return "ENE";
        }
        if (degrees > 84.38 && degrees < 106.87) {
            return "E";
        }
        if (degrees > 106.88 && degrees < 129.37) {
            return "ESE";
        }
        if (degrees > 129.38 && degrees < 151.87) {
            return "SE";
        }
        if (degrees > 151.88 && degrees < 174.37) {
            return "SSE";
        }
        if (degrees > 174.38 && degrees < 196.87) {
            return "S";
        }
        if (degrees > 196.88 && degrees < 219.37) {
            return "SSW";
        }
        if (degrees > 219.38 && degrees < 241.87) {
            return "SW";
        }
        if (degrees > 241.88 && degrees < 264.37) {
            return "WSW";
        }
        if (degrees > 264.38 && degrees < 286.87) {
            return "W";
        }
        if (degrees > 286.88 && degrees < 309.37) {
            return "NWN";
        }
        if (degrees > 309.38 && degrees < 331.87) {
            return "NW";
        }
        if (degrees > 331.88 && degrees < 354.37) {
            return "NNW";
        }


        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
