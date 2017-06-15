package lab1_202_08.uwaterloo.ca.lab1_202_08;

import android.util.Log;
import android.widget.TextView;


public class FSM_X {

    //FSM parameters
    enum FSMStates{WAIT, RIGHT, LEFT, STABLE, DETERMINED};
    private FSMStates myStates;

    //Signature parameters
    enum Signatures{UP, DOWN, UNDETERMINED};
    private Signatures mySig;

    private float[] min = {0,0};
    private float[] max = {-10,0};

    //These are the characteristic thresholds of my choice.
    //1st threshold: minimum slope of the response onset
    //2nd threshold: the minimum response max amplitude of the first peak
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
    public FSM_X(TextView displayTV, TextView maxText, TextView minText){
        myStates = FSMStates.WAIT;
        mySig = Signatures.UNDETERMINED;
        //sampleCounter = SAMPLE_COUNTER_DEFAULT;
        previousReading = 0;
        DirectionTV = displayTV;
        this.maxText = maxText;
        this.maxText.setText(Float.toString(max[0]));
        this.minText = minText;
        this.minText.setText(Float.toString(min[0]));
        //DirectionTV.setText("The FSM changed this");
    }


    //Resetting the FSM back to the initial state
    public void resetFSM(){
        myStates = FSMStates.WAIT;
        mySig = Signatures.UNDETERMINED;
        previousReading = 0;
    }

    //This is the main FSM body
    public void activateFSM(float accInput){
        // increment counter
        sampleCounter++;
        //First, calculate the slope between the most recent input and the
        //most recent historical readings
        float accSlope = accInput - previousReading;

        if (sampleCounter - max[1] > 30) {
            max[0] = 0;
        }
        if (sampleCounter - min[1] > 30){
            min[0] = 0;
        }

        if (accInput > max[0]){
            max[0] = accInput;
            max[1] = sampleCounter;
        }
        else if (accInput < min[0]){
            min[0] = accInput;
            min[1] = sampleCounter;
        }

        maxText.setText(Float.toString(max[0]));
        minText.setText(Float.toString(min[0]));

        //Then, implement the state flow.
        switch(myStates){

            case WAIT:
                //To check UP, implement this is the UP transition


                DirectionTV.setText("The FSM state is at WAIT");

                //DirectionTV.setText("The accSlope is at :" + Float.toString(accSlope));

                //If the slope is negative, then that is the first bump of the RIGHT case.


                if ((max[0] - min[0]) > THRESHOLD_UP[0]){
                    //Gesture detected.
                    //DirectionTV.setText(Float.toString(accInput));
                    if (accInput > 2) {
                        myStates = FSMStates.LEFT;
                    }
                    else if (accInput  < -2) {
                        myStates = FSMStates.RIGHT;
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

            case LEFT:

                DirectionTV.setText("LEFT");

                sampleCounter = 0;
                for (int i = 0; i < 2; i++){
                    min[i] = 0;
                    max[i] = 0;
                }
                if (accInput < 2 && accInput > -2) {
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

            case RIGHT:

                DirectionTV.setText("RIGHT");
                sampleCounter = 0;
                for (int i = 0; i < 2; i++) {
                    min[i] = 0;
                    max[i] = 0;
                }
                if (accInput < 2 && accInput > -2) {
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
//                    if(Math.abs(accInput) < THRESHOLD_UP[2]){
//                        mySig = Signatures.UP;
//                    }
//                    else if(Math.abs(accInput) < THRESHOLD_DOWN[2]){
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
        previousReading = accInput;


    }

}
