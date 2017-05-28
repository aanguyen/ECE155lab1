package lab1_202_08.uwaterloo.ca.lab1_202_08;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Lab1_202_08 extends AppCompatActivity {

    //I put all the TextView at the top to make them global variables in the bundle
    TextView lightText;
    TextView lightMaxText;
    TextView lightMaxNumber;

    TextView accelerometerText;
    TextView accelerometerMaxText;
    TextView accelerometerMaxNumber;

    TextView magneticFieldText;
    TextView magneticFieldMaxText;
    TextView magneticFieldMaxNumber;

    TextView rotationVectorText;
    TextView rotationVectorMaxText;
    TextView rotationVectorMaxNumber;

    private void writeToFile() {
        File myFile;
        PrintWriter myPrinter = null;
        try {
            //Open/create Output.csv in the folder "Lab1_202_08"
            myFile = new File(getExternalFilesDir("Lab1_202_08"), "Output.csv");
            //Initialize a new printWriter to write to file
            myPrinter = new PrintWriter(myFile);

            myPrinter.println("test string for the file, insert csv here!!!!!!"); //INSERT CSV CODE HERE SOMEHOW...
        } catch (IOException e) {
            Log.d("Lab 1 Error:", e.toString());
        } finally {
            if (myPrinter != null) {
                myPrinter.close();
            }
            Log.d("Lab 1: ", "File writing complete.");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab1_202_08);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //Create all the needed TextViews
        //Do you guys think this qualifies as code duplication? I feel like some code could be deleted
        //By creating a function that sets all the things for us, but to me it feels like that's time
        //That could be better used to do other things
        LinearLayout LL = (LinearLayout) findViewById(R.id.linearLayout);
        lightText = new TextView(getApplicationContext());
        lightText.setTextColor(Color.WHITE);
        LL.addView(lightText);
        lightMaxText = new TextView(getApplicationContext());
        lightMaxText.setText("The Maximum Light Sensor Reading is:");
        lightMaxText.setTextColor(Color.WHITE);
        LL.addView(lightMaxText);
        lightMaxNumber = new TextView(getApplicationContext());
        lightMaxNumber.setText("0");
        lightMaxNumber.setTextColor(Color.WHITE);
        LL.addView(lightMaxNumber);

        accelerometerText = new TextView(getApplicationContext());
        accelerometerText.setTextColor(Color.WHITE);
        LL.addView(accelerometerText);
        accelerometerMaxText  = new TextView(getApplicationContext());
        accelerometerMaxText.setText("The Maximum Accelerometer Sensor Reading is:");
        accelerometerMaxText.setTextColor(Color.WHITE);
        LL.addView(accelerometerMaxText);
        accelerometerMaxNumber = new TextView(getApplicationContext());
        accelerometerMaxNumber.setText("0");
        accelerometerMaxNumber.setTextColor(Color.WHITE);
        LL.addView(accelerometerMaxNumber);

        magneticFieldText = new TextView(getApplicationContext());
        magneticFieldText.setTextColor(Color.WHITE);
        LL.addView(magneticFieldText);
        magneticFieldMaxText  = new TextView(getApplicationContext());
        magneticFieldMaxText.setText("The Maximum Magnetic Sensor Reading is:");
        magneticFieldMaxText.setTextColor(Color.WHITE);
        LL.addView(magneticFieldMaxText);
        magneticFieldMaxNumber = new TextView(getApplicationContext());
        magneticFieldMaxNumber.setText("0");
        magneticFieldMaxNumber.setTextColor(Color.WHITE);
        LL.addView(magneticFieldMaxNumber);

        rotationVectorText = new TextView(getApplicationContext());
        rotationVectorText.setTextColor(Color.WHITE);
        LL.addView(rotationVectorText);
        rotationVectorMaxText  = new TextView(getApplicationContext());
        rotationVectorMaxText.setText("The Maximum Rotation Vector Sensor Reading is:");
        rotationVectorMaxText.setTextColor(Color.WHITE);
        LL.addView(rotationVectorMaxText);
        rotationVectorMaxNumber = new TextView(getApplicationContext());
        rotationVectorMaxNumber.setText("0");
        rotationVectorMaxNumber.setTextColor(Color.WHITE);
        LL.addView(rotationVectorMaxNumber);

        Button fileButton = (Button) findViewById(R.id.fileButton);
        fileButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        writeToFile();
                    }
                }
        );

        if(savedInstanceState != null){
            //Save all the max readings, and stores them with in a key-value pair
            lightMaxNumber.setText(savedInstanceState.getString("maxLight"));
            accelerometerMaxNumber.setText(savedInstanceState.getString("maxAccelerometer"));
            magneticFieldMaxNumber.setText(savedInstanceState.getString("maxMagneticSensor"));
            rotationVectorMaxNumber.setText(savedInstanceState.getString("maxRotationSensor"));
        }

        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        SensorEventListener light = new LightSensorEventListener(lightText, lightMaxNumber);
        sensorManager.registerListener(light, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener accelerometer = new AccelerometerSensorEventListener(accelerometerText, accelerometerMaxNumber);
        sensorManager.registerListener(accelerometer, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        SensorEventListener magneticField = new MagneticSensorEventListener(magneticFieldText, magneticFieldMaxNumber);
        sensorManager.registerListener(magneticField, magneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        SensorEventListener rotationVector = new RotationalVectorSensorEventListener(rotationVectorText, rotationVectorMaxNumber);
        sensorManager.registerListener(rotationVector, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("maxLight", lightMaxNumber.getText().toString());
        savedInstanceState.putString("maxAccelerometer", accelerometerMaxNumber.getText().toString());
        savedInstanceState.putString("maxMagneticSensor", magneticFieldMaxNumber.getText().toString());
        savedInstanceState.putString("maxRotationSensor", rotationVectorMaxNumber.getText().toString());
    }

}

//Note: All se.values are given to us by Android OS as floats
class LightSensorEventListener implements SensorEventListener {
    private TextView output;
    private TextView outMax;
    private float maxLight = 0;
    //do we have to initialize these TextViews to null?

   // public LightSensorEventListener(TextView outputView, TextView outputMax, float maxLightFloat){
    public LightSensorEventListener(TextView outputView, TextView outputMax){
        outMax = outputMax;
        output = outputView;
        if (Float.parseFloat(outMax.getText().toString()) != 0){
            maxLight = Float.parseFloat(outMax.getText().toString());
        }
    }
    public void onAccuracyChanged(Sensor s, int i){

    }

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_LIGHT){
            output.setText("The Light Sensor Reading is:\n" +  String.valueOf(se.values[0]));
            if (se.values[0] > maxLight) {
                maxLight = se.values[0];
                outMax.setText(String.valueOf(maxLight));

            }
        }
    }
}

class AccelerometerSensorEventListener implements SensorEventListener {
    private TextView output;
    private TextView outMax;
    private float maxX = -999.999f;
    private float maxY = -999.999f;
    private float maxZ = -999.999f;

    public AccelerometerSensorEventListener(TextView outputView, TextView outputMax) {
        output = outputView;
        outMax = outputMax;
        //If the maxValue for the sensor exists, then use regex to extract the relevant information
        if (!outMax.getText().toString().equals("0")){
            String maxData = outMax.getText().toString();
            Pattern patternX = Pattern.compile("\\(([0-9.]*?)\\,");
            Matcher matcherX = patternX.matcher(maxData);
            Pattern patternY = Pattern.compile("\\,([0-9.]*?)\\,");
            Matcher matcherY = patternY.matcher(maxData);
            Pattern patternZ = Pattern.compile("\\,([0-9.]*?)\\)");
            Matcher matcherZ = patternZ.matcher(maxData);
            //matcherX finds the number between ( and ,
            if (matcherX.find()) {
                maxX = Float.parseFloat(matcherX.group(1));
            }
            //matcherY finds the number between , and ,
            if (matcherY.find()) {
                maxY = Float.parseFloat(matcherY.group(1));
            }
            //matcherZ finds the number between , and )
            if (matcherZ.find()) {
                maxZ = Float.parseFloat(matcherZ.group(1));
            }
        }
    }

    public void onAccuracyChanged(Sensor s, int i) {

    }

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            boolean changed = false;
            output.setText("The Accelerometer Sensor Reading is:\n(" + (String.format("%.2f, %.2f, %.2f", se.values[0], se.values[1], se.values[2])) + ")" );
            String maxText = "(" + (String.format("%.2f, %.2f, %.2f", maxX, maxY, maxZ)) + ")";
            if (se.values[0] > maxX) {
                maxX = se.values[0];
                changed = true;
            }
            if (se.values[1] > maxY) {
                maxY = se.values[1];
                changed = true;
            }
            if (se.values[2] > maxZ) {
                maxZ = se.values[2];
                changed = true;
            }
            if (changed)
                outMax.setText(maxText);
        }
    }
}

class MagneticSensorEventListener implements SensorEventListener {
    private TextView output;
    private TextView outMax;
    private float maxX = -999.9f;
    private float maxY = -999.9f;
    private float maxZ = -999.9f;

    public MagneticSensorEventListener(TextView outputView, TextView outputMax){
        output = outputView;
        outMax = outputMax;
        if (!outMax.getText().toString().equals("0")){
            String maxData = outMax.getText().toString();
            Pattern patternX = Pattern.compile("\\(([0-9.]*?)\\,");
            Matcher matcherX = patternX.matcher(maxData);
            Pattern patternY = Pattern.compile("\\,([0-9.]*?)\\,");
            Matcher matcherY = patternY.matcher(maxData);
            Pattern patternZ = Pattern.compile("\\,([0-9.]*?)\\)");
            Matcher matcherZ = patternZ.matcher(maxData);
            if (matcherX.find()) {
                maxX = Float.parseFloat(matcherX.group(1));
            }
            if (matcherY.find()) {
                maxY = Float.parseFloat(matcherY.group(1));
            }
            if (matcherZ.find()) {
                maxZ = Float.parseFloat(matcherZ.group(1));
            }
        }
    }
    public void onAccuracyChanged(Sensor s, int i) {

    }

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            boolean changed = false;
            output.setText("The Magnetic Sensor Reading is: \n(" + (String.format("%.2f, %.2f, %.2f", se.values[0], se.values[1], se.values[2])) + ")" );
            String maxText = "(" + (String.format("%.2f, %.2f, %.2f", maxX, maxY, maxZ)) + ")";
            if (se.values[0] > maxX) {
                maxX = se.values[0];
                changed = true;
            }
            if (se.values[1] > maxY) {
                maxY = se.values[1];
                changed = true;
            }
            if (se.values[2] > maxZ) {
                maxZ = se.values[2];
                changed = true;
            }
            if (changed)
                outMax.setText(maxText);

        }
    }
}

class RotationalVectorSensorEventListener implements SensorEventListener {
    private TextView output;
    private TextView outMax;
    private float maxX = -999.9f;
    private float maxY = -999.9f;
    private float maxZ = -999.9f;

    public RotationalVectorSensorEventListener(TextView outputView, TextView outputMax) {
        output = outputView;
        outMax = outputMax;
        if (!outMax.getText().toString().equals("0")){
            String maxData = outMax.getText().toString();
            Pattern patternX = Pattern.compile("\\(([0-9.]*?)\\,");
            Matcher matcherX = patternX.matcher(maxData);
            Pattern patternY = Pattern.compile("\\,([0-9.]*?)\\,");
            Matcher matcherY = patternY.matcher(maxData);
            Pattern patternZ = Pattern.compile("\\,([0-9.]*?)\\)");
            Matcher matcherZ = patternZ.matcher(maxData);
            if (matcherX.find()) {
                maxX = Float.parseFloat(matcherX.group(1));
            }
            if (matcherY.find()) {
                maxY = Float.parseFloat(matcherY.group(1));
            }
            if (matcherZ.find()) {
                maxZ = Float.parseFloat(matcherZ.group(1));
            }
        }
    }

    public void onAccuracyChanged(Sensor s, int i) {

    }

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
            boolean changed = false;
            output.setText("The Rotation Vector Sensor Reading is:\n(" + (String.format("%.2f, %.2f, %.2f", se.values[0], se.values[1], se.values[2])) + ")" );
            String maxText = "(" + (String.format("%.2f, %.2f, %.2f", maxX, maxY, maxZ)) + ")";
            if (se.values[0] > maxX) {
                maxX = se.values[0];
                changed = true;
            }
            if (se.values[1] > maxY) {
                maxY = se.values[1];
                changed = true;
            }
            if (se.values[2] > maxZ) {
                maxZ = se.values[2];
                changed = true;
            }
            if (changed)
                outMax.setText(maxText);
        }

    }
}