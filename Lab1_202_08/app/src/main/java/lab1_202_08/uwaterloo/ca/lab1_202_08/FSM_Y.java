package lab1_202_08.uwaterloo.ca.lab1_202_08;

import android.util.Log;
import android.widget.TextView;


public class FSM_Y {

    //FSM parameters
    enum FSMStates{WAIT, RISE, FALL, RIGHT, LEFT, STABLE, DETERMINED};
    private FSMStates myStates;

    //Signature parameters
    enum Signatures{UP, DOWN, UNDETERMINED};
    private Signatures mySig;

    private float[] minY = {0,0};
    private float[] maxY = {-10,0};
    private float[] minX = {0,0};
    private float[] maxX = {-10,0};

    //These are the characteristic thresholds of my choice.
    //1st threshold: minimum slope of the response onset
    //2nd threshold: the minimum response maxY amplitude of the first peak
    //3rd threshold: the maximum response amplitude after settling for 30 samples.

    private final float[] THRESHOLD_UP = {20f, 15f, 4f};
    //Will need to change the values
    private final float[] THRESHOLD_DOWN = {20f, -10f, 3f};

    //This is the sample counter.
    //We expect the reading to settle down to near zero after 30 samples since the
    //occurrence of the minimum of the 1st response peak.
    private int sampleCounter = 0;
    //private final int SAMPLE_COUNTER_DEFAULT = 15;

    //Keep the most recent historical reading so we can calculate the most recent slope
    private float previousReading;

    //Keep a reference of the TextView from the layout.
    private TextView DirectionTV;
    private TextView maxText;
    private TextView minText;




    //Constructor.  FSM is started into WAIT state.
    public FSM_Y(TextView displayTV, TextView maxText, TextView minText){
        myStates = FSMStates.WAIT;
        mySig = Signatures.UNDETERMINED;
        //sampleCounter = SAMPLE_COUNTER_DEFAULT;
        previousReading = 0;
        DirectionTV = displayTV;
        this.maxText = maxText;
        this.maxText.setText(Float.toString(maxY[0]));
        this.minText = minText;
        this.minText.setText(Float.toString(minY[0]));
        //DirectionTV.setText("The FSM changed this");
    }


    //Resetting the FSM back to the initial state
    public void resetFSM(){
        myStates = FSMStates.WAIT;
        mySig = Signatures.UNDETERMINED;
        previousReading = 0;
    }

    //This is the main FSM body
    public void activateFSM(float accInputY, float accInputX){
        // increment counter
        sampleCounter++;
        //First, calculate the slope between the most recent input and the
        //most recent historical readings
        float accSlope = accInputY - previousReading;

        if (sampleCounter - maxY[1] > 30) {
            maxY[0] = 0;
        }
        if (sampleCounter - minY[1] > 30){
            minY[0] = 0;
        }
        if (sampleCounter - maxX[1] > 30) {
            maxX[0] = 0;
        }
        if (sampleCounter - minX[1] > 30){
            minX[0] = 0;
        }

        if (accInputY > maxY[0]){
            maxY[0] = accInputY;
            maxY[1] = sampleCounter;
        }
        else if (accInputY < minY[0]){
            minY[0] = accInputY;
            minY[1] = sampleCounter;
        }
        if (accInputX > maxX[0]) {
            maxX[0] = accInputX;
            maxX[1] = sampleCounter;
        }
        else if (accInputX < minX[0]) {
            minX[0] = accInputX;
            minX[1] = sampleCounter;
        }

        maxText.setText(Float.toString(maxY[0]));
        minText.setText(Float.toString(minY[0]));

        //Then, implement the state flow.
        switch(myStates){

            case WAIT:
                //To check UP, implement this is the UP transition


                DirectionTV.setText("The FSM state is at WAIT");

                //DirectionTV.setText("The accSlope is at :" + Float.toString(accSlope));

                //If the slope is negative, then that is the first bump of the rise case.


                if (((maxY[0] - minY[0]) > THRESHOLD_UP[0]) || (maxX[0] - minX[0] > THRESHOLD_UP[0])){
                    //UP/DOWN gesture detected.
                    //DirectionTV.setText(Float.toString(accInputY));
                    if (accInputY < -2) {
                        myStates = FSMStates.FALL;
                    }
                    else if (accInputY > 2) {
                        myStates = FSMStates.RISE;
                    }
                    else if (accInputX < -2) {
                        myStates = FSMStates.RIGHT;
                    }
                    else if (accInputX > 2) {
                        myStates = FSMStates.LEFT;
                    }
                }

                /*if(accSlope >= 0){

                    if(previousReading >= THRESHOLD_UP[1]){
                        myStates = FSMStates.STABLE;
                    }
                    else{
                        myStates = FSMStates.DETERMINED;
                        mySig = Signatures.UNDETERMINED;
                    }

                }*/

                break;

            case FALL:

                DirectionTV.setText("DOWN");

                sampleCounter = 0;
                for (int i = 0; i < 2; i++){
                    minY[i] = 0;
                    maxY[i] = 0;
                }
                if (accInputY < 2 && accInputY > -2) {
                    myStates = FSMStates.WAIT;
                }
                //crossing the minima
//                if(accSlope <= 0){
//
//                    if(previousReading <= THRESHOLD_DOWN[1]){
//                        myStates = FSMStates.STABLE;
//                    }
//                    else{
//                        myStates = FSMStates.DETERMINED;
//                        mySig = Signatures.UNDETERMINED;
//                    }
//
//                }

                break;

            case RISE:

                DirectionTV.setText("UP");
                sampleCounter = 0;
                for (int i = 0; i < 2; i++) {
                    minY[i] = 0;
                    maxY[i] = 0;
                }
                if (accInputY < 2 && accInputY > -2) {
                    myStates = FSMStates.WAIT;
                }
                break;
            case RIGHT:
                DirectionTV.setText("RIGHT");
                sampleCounter = 0;
                for (int i = 0; i < 2; i++) {
                    minX[i] = 0;
                    maxX[i] = 0;
                }
                if (accInputX < 2 && accInputX > -2) {
                    myStates = FSMStates.WAIT;
                }
                break;
            case LEFT:
                DirectionTV.setText("LEFT");
                sampleCounter = 0;
                for (int i = 0; i < 2; i++) {
                    minX[i] = 0;
                    maxX[i] = 0;
                }
                if (accInputX < 2 && accInputX > -2) {
                    myStates = FSMStates.WAIT;
                }
                break;
            case STABLE:

                //This part is to wait for the stabilization.
                //Count down from 30 to 0.
                //DirectionTV.setText("The FSM is at state STABLE");


                //At zero, check the threshold and determine the gesture.
//                if(sampleCounter == 0){
//
//                    myStates = FSMStates.DETERMINED;
//
//                    //You will have to modify this portion to incorporate the UP gesture...
//                    //Think about how to do it.  You may have to re-think about the state transition here...
//                    if(Math.abs(accInputY) < THRESHOLD_UP[2]){
//                        mySig = Signatures.UP;
//                    }
//                    else if(Math.abs(accInputY) < THRESHOLD_DOWN[2]){
//                        mySig = Signatures.DOWN;
//                    }
//                    else{
//                        mySig = Signatures.UNDETERMINED;
//                    }
//                }

                break;

            case DETERMINED:

                DirectionTV.setText(mySig.toString());

                //Once determined, report the gesture and reset the FSM.
                Log.d("My FSM Says:", String.format("I've got signature %s", mySig.toString()));

                //Show the signature on the textview
                //DirectionTV.setText("The FSM is at state DETERMINED");
                DirectionTV.setText(mySig.toString());

                resetFSM();

                break;

            default:
                resetFSM();
                break;

        }

        //After every FSM iteration, make sure to record the input as the most recent
        //history reading.
        previousReading = accInputY;


    }

}
