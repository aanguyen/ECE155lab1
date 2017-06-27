package lab1_202_08.uwaterloo.ca.lab3_202_08;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

public class Lab1_202_08 extends AppCompatActivity {
    //I put all the TextView and Graph at the top to make them global variables in the bundle
    public TextView gesture;
    private FSM_Y FSM_MY_FSM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab1_202_08);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        RelativeLayout RL = (RelativeLayout) findViewById(R.id.relativeLayout);

        //RL.setLayoutParams(new RelativeLayout.LayoutParams(1440, 120));
        //RL.LayoutParams(1440,120);
        RL.getLayoutParams().height = 1440;
        RL.getLayoutParams().width = 1440;
        RL.setBackgroundResource(R.drawable.gameboard);
        //Create all the needed TextView

        gesture = new TextView(getApplicationContext());
        gesture.setTextColor(Color.BLACK);
        gesture.setTextSize(35);
        RL.addView(gesture);

        GameLoopTask myGameLoopTask = new GameLoopTask(this,RL,getApplicationContext());
        Timer myGameLoop = new Timer();
        myGameLoop.schedule(myGameLoopTask,50,50);

        //Save all the max readings, and stores them with in a key-value pair
        if (savedInstanceState != null) {

        }

        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener accelerometer = new AccelerometerSensorEventListener(gesture, myGameLoopTask);
        sensorManager.registerListener(accelerometer, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);

        FSM_MY_FSM = new FSM_Y(gesture);
        //FSM_UP.activateFSM(coordinates[2]);
        //gesture.setText(Float.toString(coordinates[2]));

    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }


    //Note: All se.values are given to us by Android OS as floats

    private class AccelerometerSensorEventListener implements SensorEventListener {

        private TextView gesture;
        private final float constant = 25;
        private float[] previousNum;
        private float[] tempElem = new float[3];
        private GameLoopTask GLT;

        private AccelerometerSensorEventListener(TextView gestureTV, GameLoopTask myGameLoopTask) {
            gesture = gestureTV;
            GLT = myGameLoopTask;
            //If the maxValue for the sensor exists, then use regex to extract the relevant information from the TextView
        }

        public void onAccuracyChanged(Sensor s, int i) {

        }

        public void onSensorChanged(SensorEvent se) {
            if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                //If this is the first number, set that as the previous number
                if (previousNum == null) {
                    previousNum = se.values;
                } else {
                    //This goes through the X,Y,and Z values
                    for (int i = 0; i < 3; i++) {
                        //This just finds the difference between the two and divides it by a constant number
                        if (se.values[i] > previousNum[i]) {
                            tempElem[i] = (se.values[i] - previousNum[i]) / constant;
                        } else if (se.values[i] < previousNum[i]) {
                            tempElem[i] = (se.values[i] + previousNum[i]) / constant;
                        } else {
                            tempElem[i] = se.values[i];
                        }
                    }
                }
                FSM_MY_FSM.activateFSM(tempElem[2], tempElem[0]);
                String currentState = FSM_MY_FSM.getState();
                GLT.setStringDir(currentState);

            }
        }

    }
}
