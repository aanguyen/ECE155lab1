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
        TextView tv = (TextView) findViewById(R.id.label1);
        tv.setText("I've replaced the text!");
        LinearLayout rl = (LinearLayout) findViewById(R.id.linearLayout);
        TextView tv1 = new TextView(getApplicationContext());
        tv1.setTextColor(Color.MAGENTA);
        tv1.setText("This is another line of text!");
        rl.addView(tv1);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        SensorEventListener l = new LightSensorEventListener(tv);
        sensorManager.registerListener(l, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
            output.setText(String.valueOf(lightValue));
        }
    }
}