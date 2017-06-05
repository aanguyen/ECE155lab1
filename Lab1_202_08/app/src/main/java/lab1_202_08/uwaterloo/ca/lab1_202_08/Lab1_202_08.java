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
    TextView lightText, lightMaxText, lightMaxNumber;
    TextView accelerometerText, accelerometerMaxText, accelerometerMaxNumber;
    TextView magneticFieldText, magneticFieldMaxText, magneticFieldMaxNumber;
    TextView rotationVectorText, rotationVectorMaxText, rotationVectorMaxNumber;

    LineGraphView graph;




    private void writeToFile(){
        File myFile;
        PrintWriter myPrinter = null;
        try {
            //Open/create Output.csv in the folder "Lab1_202_08"
            myFile = new File(getExternalFilesDir(FOLDER_NAME), FILE_NAME);
            //Initialize a new printWriter to write to file
            myPrinter = new PrintWriter(myFile);

            //Initialize a new arraylist for the acclerometer readings
            ArrayList listValues = new ArrayList(accelReadings);
/*
            for (int i = 1; i < listValues.size(); i++){

                //This is a constant for which to do the low pass filter.
                //This constant will need to be adjusted based on the phone
                float consNum = (float) 0.5;
                float[] tempElem = {0,0,0};
                //Now, this is adding the low pass filter
                //This starts at 1 because it needs the element at index 0

                float[] previousNum = (float[]) listValues.get(i-1);
                float[] currentNum = (float[]) listValues.get(i);
                //This divides the difference between the current number and the previous number by the constant
                for (int x = 0; x < 3; x ++){
                    if (currentNum[x] > previousNum[x]){
                        tempElem[x] = (currentNum[x] - previousNum[x])/consNum;
                    }
                    else if (currentNum[x] < previousNum[x]){
                        tempElem[x] = (currentNum[x] + previousNum[x])/consNum;
                    }
                    else{
                        tempElem[x] = currentNum[x];
                    }
                }
                listValues.set(i,tempElem);
            }
            */
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

        //Save all the max readings, and stores them with in a key-value pair
        if(savedInstanceState != null){
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
        sensorManager.registerListener(accelerometer, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);

        Sensor magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        SensorEventListener magneticField = new MagneticSensorEventListener(magneticFieldText, magneticFieldMaxNumber);
        sensorManager.registerListener(magneticField, magneticFieldSensor, SensorManager.SENSOR_DELAY_GAME);

        Sensor rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        SensorEventListener rotationVector = new RotationalVectorSensorEventListener(rotationVectorText, rotationVectorMaxNumber);
        sensorManager.registerListener(rotationVector, rotationVectorSensor, SensorManager.SENSOR_DELAY_GAME);
/*
        //set highest thresholds to 5 and -5

        class myFSM {
            //enum FSMstates(WAIT, RISE, FALL, STABLE, DETERMINED);
            // private FSMstates myStates;

            //Signature parameters,
            // enum Signatures(LEFT, RIGHT, UNDETERMINED);
            //   private Signatures mySig;

            private final float[] THRESHOLD_RIGHT = (0.5f,0.8f,0.2f);

            private int sampleCounter;
            private final int SAMPLE_COUNTER_DEFAULT = 40;
            private TextView myDisplay;


            private float prev_reading = 0;

            //constructor and set parameters
            public myFSM(TextView tv) {

                //      myStates = FSMStates.WAIT;
                //    mySig = Signatures.UNDETERMINED;
                sampleCounter = SAMPLE_COUNTER_DEFAULT;
                myDisplay = tv;
            }

            public void activateFSM(float accInput) {

                float accSlope = accInput - prev_reading;
                //    switch(myStates){
                //      case WAIT:
                //        Log.d("FSM Says:", String.format("Waiting, Slope: %f", accSlope);
                //      if(accSlope >= THRESHOLD_RIGHT[0]){
                //        myStates = FSMStates.RISE;
            }
        }
        */
    }
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("maxLight", lightMaxNumber.getText().toString());
        savedInstanceState.putString("maxAccelerometer", accelerometerMaxNumber.getText().toString());
        savedInstanceState.putString("maxMagneticSensor", magneticFieldMaxNumber.getText().toString());
        savedInstanceState.putString("maxRotationSensor", rotationVectorMaxNumber.getText().toString());
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
        private float[] consNum = {0.5f, 0.5f, 0.5f};
        private float[] previousNum;
        private float[] tempElem = new float[3];

        public AccelerometerSensorEventListener(TextView outputView, TextView outputMax) {
            output = outputView;
            outMax = outputMax;
            //If the maxValue for the sensor exists, then use regex to extract the relevant information from the TextView
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
                String maxText = "(" + (String.format("%.2f, %.2f, %.2f", maxX, maxY, maxZ)) + ")";;
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
                if (previousNum == null ){
                    previousNum = se.values;
                }
                else{
                    //This goes through the X,Y,and Z values
                    for (int i = 0; i < 3; i ++){
                        //This just finds the difference between the two and divides it by a constant number
                        //The constant number is a decimal so it's easier to specify, so I multiplied it
                        if (se.values[i] > previousNum[i]){
                            tempElem[i] = (se.values[i] - previousNum[i])*consNum[i];
                        }
                        else if (se.values[i] < previousNum[i]){
                            tempElem[i] = (se.values[i] + previousNum[i])*consNum[i];
                        }
                        else{
                            tempElem[i] = se.values[i];
                        }
                    }
                }

                //Regardless whether changed or not, pass the point to the graph
                //Why doesn't android recognize "graph" when below is uncommented?!
                graph.addPoint(tempElem);
                //Putting the data here to array to be passed to the CSV
                float[] readings = new float[3];
                for (int i = 0; i < 3; i++) {
                    readings[i] = tempElem[i];
                }
                if (accelReadings.size() < 100 ) {
                    accelReadings.add(readings);
                } else {        //now we need to remove the 100th element (first LL element as it's a queue)to add the new reading
                    accelReadings.remove();
                    accelReadings.add(readings);
                }
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

}