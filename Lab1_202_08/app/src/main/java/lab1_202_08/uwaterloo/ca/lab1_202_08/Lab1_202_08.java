package lab1_202_08.uwaterloo.ca.lab1_202_08;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Lab1_202_08 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lab1_202_08);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        TextView lightText = (TextView) findViewById(R.id.lightFieldText);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        SensorEventListener light = new LightSensorEventListener(lightText);
        sensorManager.registerListener(light, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        TextView magneticFieldText = (TextView) findViewById(R.id.magneticFieldText);
        Sensor magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        SensorEventListener magneticField = new MagneticSensorEventListener(magneticFieldText);
        sensorManager.registerListener(magneticField, magneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL);

        TextView accelerometerText = (TextView) findViewById(R.id.accelerometerLabel);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener accelerometer = new AccelerometerSensorEventListener(accelerometerText);
        sensorManager.registerListener(accelerometer, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        TextView rotationVectorText = (TextView) findViewById(R.id.rotationVectorLabel1);
        Sensor rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        SensorEventListener rotationVector = new RotataionVectorSensorEventListener(rotationVectorText);
        sensorManager.registerListener(rotationVector, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

}

class MagneticSensorEventListener implements SensorEventListener {
    private TextView output;

    public MagneticSensorEventListener(TextView outputView){
        output = outputView;
    }
    public void onAccuracyChanged(Sensor s, int i){}

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            float magneticFieldValue1 = se.values[0];
            float magneticFieldValue2 = se.values[1];
            float magneticFieldValue3 = se.values[2];
            output.setText("The Magnetic Sensor Reading is: \n(" + String.format("%.2f", se.values[0]) + ", " + String.format("%.2f", se.values[1]) + ", " + String.format("%.2f", se.values[2]) + ")");
        }
    }
}

class LightSensorEventListener implements SensorEventListener {
    private TextView output;

    public LightSensorEventListener(TextView outputView){

        output = outputView;
    }
    public void onAccuracyChanged(Sensor s, int i){}

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_LIGHT){
            float lightValue = se.values[0];
            String lightMessage = "The Light Sensor Reading is:\n" +  String.valueOf(lightValue);
            output.setText(lightMessage);
        }
    }
}

class AccelerometerSensorEventListener implements SensorEventListener {
    private TextView output;

    public AccelerometerSensorEventListener(TextView outputView) {

        output = outputView;

    }

    public void onAccuracyChanged(Sensor s, int i){}

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            //float xValue = se.values[0];
            //float yValue = se.values[1];
            //float zValue = se.values[2];
            String accelerometerMessage = "The Accelerometer Sensor Reading is:\n(" +  (String.format("%.2f", se.values[0])) + ", " + (String.format("%.2f", se.values[1])) + ", " + (String.format("%.2f", se.values[1])) + ")";
            output.setText(accelerometerMessage);
        }
    }
}

class RotataionVectorSensorEventListener implements SensorEventListener {
    private TextView output;

    public RotataionVectorSensorEventListener(TextView outputView) {

        output = outputView;

    }

    public void onAccuracyChanged(Sensor s, int i){}

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
            //float xValue = se.values[0];
            //float yValue = se.values[1];
            //float zValue = se.values[2];
            String rotationVectorMessage = "The Rotation Vector Sensor Reading is:\n(" +  (String.format("%.2f", se.values[0])) + ", " + (String.format("%.2f", se.values[1])) + ", " + (String.format("%.2f", se.values[1])) + ")";
            output.setText(rotationVectorMessage);
        }
    }
}