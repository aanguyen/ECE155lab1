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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uwaterloo.sensortoy.LineGraphView;

import static lab1_202_08.uwaterloo.ca.lab1_202_08.Lab1_202_08.accelReadings;


public class Lab1_202_08 extends AppCompatActivity {
    String FOLDER_NAME = "Output_Files";
    String FILE_NAME = "Output.csv";
    public static Queue accelReadings = new LinkedList();
    //I put all the TextView and Graph at the top to make them global variables in the bundle
    public TextView gesture;
    public TextView accelerometerText, accelerometerMaxText, accelerometerMaxNumber;
    private myFSM FSM_UP;
    public static float [] coordinates = new float[3];
    public TextView coordinView;

    LineGraphView graph;

    private void writeToFile() {
        File myFile;
        PrintWriter myPrinter = null;
        try {
            //Open/create Output.csv in the folder "Lab1_202_08"
            myFile = new File(getExternalFilesDir(FOLDER_NAME), FILE_NAME);
            //Initialize a new printWriter to write to file
            myPrinter = new PrintWriter(myFile);

            //Initialize a new arraylist for the acclerometer readings
            ArrayList listValues = new ArrayList(accelReadings);

            for (int i = 0; i < listValues.size(); i++) {   //Size theoretically can be hardcoded to 100 but we'll just be safe here
                float[] toPrint = (float[]) listValues.get(i);
                myPrinter.println(String.format("%f,%f,%f", toPrint[0], toPrint[1], toPrint[2]));
            }


        } catch (IOException e) {
            Log.d("Lab 1 Error:", e.toString());
        } finally {
            if (myPrinter != null) {
                myPrinter.flush();
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

        LinearLayout LL = (LinearLayout) findViewById(R.id.linearLayout);

        graph = new LineGraphView(getApplicationContext(), 100, Arrays.asList("x", "y", "z"));
        LL.addView(graph); //addView adds the graph to the linear layout
        graph.setVisibility(View.VISIBLE);

        Button fileButton;
        fileButton = new Button(getApplicationContext());
        fileButton.setText("GENERATE CSV RECORD FOR ACC.SEN");
        fileButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        writeToFile();
                    }
                }
        );
        LL.addView(fileButton);

        //Create all the needed TextViews

        accelerometerText = new TextView(getApplicationContext());
        accelerometerText.setTextColor(Color.WHITE);
        LL.addView(accelerometerText);
        accelerometerMaxText = new TextView(getApplicationContext());
        accelerometerMaxText.setText("The Maximum Accelerometer Sensor Reading is:");
        accelerometerMaxText.setTextColor(Color.WHITE);
        LL.addView(accelerometerMaxText);
        accelerometerMaxNumber = new TextView(getApplicationContext());
        accelerometerMaxNumber.setText("0");
        accelerometerMaxNumber.setTextColor(Color.WHITE);
        LL.addView(accelerometerMaxNumber);

        coordinView = new TextView(getApplicationContext());
        coordinView.setTextColor(Color.WHITE);
        coordinView.setText(Float.toString(coordinates[0]));
        LL.addView(coordinView);


        gesture = new TextView(getApplicationContext());
        gesture.setTextColor(Color.WHITE);
        LL.addView(gesture);

        //Save all the max readings, and stores them with in a key-value pair
        if (savedInstanceState != null) {
            accelerometerMaxNumber.setText(savedInstanceState.getString("maxAccelerometer"));
        }

        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener accelerometer = new AccelerometerSensorEventListener(accelerometerText, accelerometerMaxNumber, gesture);
        sensorManager.registerListener(accelerometer, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);

        FSM_UP = new myFSM(gesture);
        //FSM_UP.activateFSM(coordinates[2]);
        //gesture.setText(Float.toString(coordinates[2]));

    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("maxAccelerometer", accelerometerMaxNumber.getText().toString());
    }


    //Note: All se.values are given to us by Android OS as floats

    public class AccelerometerSensorEventListener implements SensorEventListener {

        private TextView output;
        private TextView outMax;
        private TextView gesture;
        private float maxX = -999.999f;
        private float maxY = -999.999f;
        private float maxZ = -999.999f;
        private final float constant = 25;
        private float[] previousNum;
        private float[] tempElem = new float[3];

        public AccelerometerSensorEventListener(TextView outputView, TextView outputMax, TextView gestureTV) {
            output = outputView;
            outMax = outputMax;
            gesture = gestureTV;
            //If the maxValue for the sensor exists, then use regex to extract the relevant information from the TextView
            if (!outMax.getText().toString().equals("0")) {
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
            if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                boolean changed = false;

                for (int i = 0; i < 3; i++) {
                    coordinates[i] = se.values[i];
                }

                output.setText("The Accelerometer Sensor Reading is:\n(" + (String.format("%.2f, %.2f, %.2f", coordinates[0], coordinates[1], coordinates[2])) + ")");
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
                //gesture.setText("This needs to change");

                //Regardless whether changed or not, pass the point to the graph
                //Why doesn't android recognize "graph" when below is uncommented?!
                graph.addPoint(tempElem);
                //Putting the data here to array to be passed to the CSV


                float[] readings = new float[3];
                for (int i = 0; i < 3; i++) {
                    readings[i] = tempElem[i];
                }
                if (accelReadings.size() < 100) {
                    accelReadings.add(readings);
                } else {        //now we need to remove the 100th element (first LL element as it's a queue)to add the new reading
                    accelReadings.remove();
                    accelReadings.add(readings);
                }
                FSM_UP.activateFSM(tempElem[2]);

            }
        }

    }
}
