package lab1_202_08.uwaterloo.ca.lab1_202_08;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Lab1_202_08 extends AppCompatActivity {

    private void writeToFile() {
        File myFile = null;
        PrintWriter myPrinter = null;
        try {
            //Open/create Output.csv in the folder "Lab1_202_08"
            myFile = new File(getExternalFilesDir("Lab1_202_08"), "Output.csv");

            //Initialize a new printwriter to write to file
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

        Button fileButton = (Button) findViewById(R.id.fileButton);
        fileButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        writeToFile();
                    }
                }
        );

        TextView lightText = (TextView) findViewById(R.id.lightFieldText);
        TextView lightMax = (TextView) findViewById(R.id.lightFieldMaxText);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        SensorEventListener light = new LightSensorEventListener(lightText, lightMax);
        sensorManager.registerListener(light, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        TextView accelerometerText = (TextView) findViewById(R.id.accelerometerText);
        TextView accelerometerMax = (TextView) findViewById(R.id.accelerometerMaxText);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener accelerometer = new AccelerometerSensorEventListener(accelerometerText, accelerometerMax);
        sensorManager.registerListener(accelerometer, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        TextView magneticFieldText = (TextView) findViewById(R.id.magneticFieldText);
        TextView magneticFieldMax = (TextView) findViewById (R.id.magneticFieldMaxText);
        Sensor magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        SensorEventListener magneticField = new MagneticSensorEventListener(magneticFieldText, magneticFieldMax);
        sensorManager.registerListener(magneticField, magneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL);

        TextView rotationVectorText = (TextView) findViewById(R.id.rotationVectorText);
        TextView rotationVectorMax = (TextView) findViewById(R.id.rotationVectorMaxText);
        Sensor rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        SensorEventListener rotationVector = new RotataionVectorSensorEventListener(rotationVectorText, rotationVectorMax);
        sensorManager.registerListener(rotationVector, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void onSaveInstanceState(Bundle savedInstanceState){
        TextView lightMax = (TextView) findViewById(R.id.lightFieldMaxText);
        savedInstanceState.putString("maxLight", lightMax.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

}

//Note: All se.values are given to us by Android OS as floats
class LightSensorEventListener implements SensorEventListener {
    private TextView output;
    private TextView outMax;
    private float maxLight = 0.0f;
    //do we have to initialize these TextViews to null?

    public LightSensorEventListener(TextView outputView, TextView outputMax){
        outMax = outputMax;
        output = outputView;
    }
    public void onAccuracyChanged(Sensor s, int i){

    }

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_LIGHT){
            output.setText("The Light Sensor Reading is:\n" +  String.valueOf(se.values[0]));
            if (se.values[0] > maxLight) {
                maxLight = se.values[0];
                outMax.setText("The Highest Light Sensor Reading is:\n" + String.valueOf(maxLight));

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
    }

    public void onAccuracyChanged(Sensor s, int i) {

    }

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            boolean changed = false;
            output.setText("The Accelerometer Sensor Reading is:\n(" +  (String.format("%.2f", se.values[0])) + ", " + (String.format("%.2f", se.values[1])) + ", " + (String.format("%.2f", se.values[2])) + ")");
            String maxText = "The Maximum Accelerometer Sensor Reading is:\n(" + (String.format("%.2f", maxX)) + ", " + (String.format("%.2f", maxY)) + ", " + (String.format("%.2f", maxZ)) + ")";
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
    }
    public void onAccuracyChanged(Sensor s, int i) {

    }

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            boolean changed = false;
            output.setText("The Magnetic Sensor Reading is: \n(" + String.format("%.2f", se.values[0]) + ", " + String.format("%.2f", se.values[1]) + ", " + String.format("%.2f", se.values[2]) + ")");
            String maxText = "The Maximum Magnetic Sensor Reading is:\n(" + (String.format("%.2f", maxX)) + ", " + (String.format("%.2f", maxY)) + ", " + (String.format("%.2f", maxZ)) + ")";
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

class RotataionVectorSensorEventListener implements SensorEventListener {
    private TextView output;
    private TextView outMax;
    private float maxX = -999.9f;
    private float maxY = -999.9f;
    private float maxZ = -999.9f;

    public RotataionVectorSensorEventListener(TextView outputView, TextView outputMax) {
        output = outputView;
        outMax = outputMax;
    }

    public void onAccuracyChanged(Sensor s, int i) {

    }

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
            boolean changed = false;
            output.setText("The Rotation Vector Sensor Reading is:\n(" +  (String.format("%.2f", se.values[0])) + ", " + (String.format("%.2f", se.values[1])) + ", " + (String.format("%.2f", se.values[2])) + ")");
            String maxText = "The Maximum Rotation Vector Sensor Reading is:\n(" + (String.format("%.2f", maxX)) + ", " + (String.format("%.2f", maxY)) + ", " + (String.format("%.2f", maxZ)) + ")";
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