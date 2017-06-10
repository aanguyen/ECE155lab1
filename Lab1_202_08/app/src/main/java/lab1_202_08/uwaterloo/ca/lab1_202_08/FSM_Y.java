package lab1_202_08.uwaterloo.ca.lab1_202_08;

import android.util.Log;
import android.widget.TextView;

/**
 * Created by Janet Liu on 6/9/2017.
 */

public class FSM_Y {

    //FSM parameters
    enum FSMStates{WAIT, RISE, FALL, STABLE, DETERMINED};
    private FSMStates myStates;

    //Signature parameters
    enum Signatures{UP, DOWN, UNDETERMINED};
    private Signatures mySig;

    //These are the characteristic thresholds of my choice.
    //1st threshold: minimum slope of the response onset
    //2nd threshold: the minimum response max amplitude of the first peak
    //3rd threshold: the maximum response amplitude after settling for 30 samples.

    private final float[] THRESHOLD_UP = {-4f, 15f, 4f};
    //Will need to change the values
    private final float[] THRESHOLD_DOWN = {4f, 10f, -10f};

    //This is the sample counter.
    //We expect the reading to settle down to near zero after 30 samples since the
    //occurrence of the minimum of the 1st response peak.
    private int sampleCounter;
    private final int SAMPLE_COUNTER_DEFAULT = 30;

    //Keep the most recent historical reading so we can calculate the most recent slope
    private float previousReading;

    //Keep a reference of the TextView from the layout.
    private TextView DirectionTV;


    //Constructor.  FSM is started into WAIT state.
    public FSM_Y(TextView displayTV){
        myStates = FSMStates.WAIT;
        mySig = Signatures.UNDETERMINED;
        sampleCounter = SAMPLE_COUNTER_DEFAULT;
        previousReading = 0;
        DirectionTV = displayTV;
        //DirectionTV.setText("The FSM changed this");
    }

    //Resetting the FSM back to the initial state
    public void resetFSM(){
        myStates = FSMStates.WAIT;
        mySig = Signatures.UNDETERMINED;
        sampleCounter = SAMPLE_COUNTER_DEFAULT;
        previousReading = 0;
    }

    //This is the main FSM body
    public void activateFSM(float accInput){

        //First, calculate the slope between the most recent input and the
        //most recent historical readings
        float accSlope = accInput - previousReading;

        //Then, implement the state flow.
        switch(myStates){

            case WAIT:
                //To check UP, implement this is the UP transition

                //DirectionTV.setText("The FSM state is at WAIT");

                //DirectionTV.setText("The accSlope is at :" + Float.toString(accSlope));

                //If the slope is negative, then that is the first bump of the rise case.
                if(accSlope < THRESHOLD_UP[0]){
                    myStates = FSMStates.RISE;
                    //DirectionTV.setText("The FSM state is at WAIT but it's rising!!");
                }

                //This is for the first bump of the down
                if(accSlope > THRESHOLD_DOWN[0]){
                    myStates = FSMStates.FALL;
                    //DirectionTV.setText("The FSM state is at WAIT but it's falling!!");
                }

                break;

            case FALL:

                //DirectionTV.setText("FALL");

                //crossing the minima
                if(accSlope <= 0){

                    if(previousReading <= THRESHOLD_DOWN[1]){
                        myStates = FSMStates.STABLE;
                    }
                    else{
                        myStates = FSMStates.DETERMINED;
                        mySig = Signatures.UNDETERMINED;
                    }

                }

                break;

            case RISE:
                //This part is for the UP gesture
                //If the slope is positive, it is rising

                //DirectionTV.setText("The state is RISING");
                if(accSlope >= 0){

                    if(previousReading >= THRESHOLD_UP[1]){
                        myStates = FSMStates.STABLE;
                    }
                    else{
                        myStates = FSMStates.DETERMINED;
                        mySig = Signatures.UNDETERMINED;
                    }

                }
                break;

            case STABLE:

                //This part is to wait for the stabilization.
                //Count down from 30 to 0.
                //DirectionTV.setText("The FSM is at state STABLE");
                sampleCounter--;

                //At zero, check the threshold and determine the gesture.
                if(sampleCounter == 0){

                    myStates = FSMStates.DETERMINED;

                    //You will have to modify this portion to incorporate the UP gesture...
                    //Think about how to do it.  You may have to re-think about the state transition here...
                    if(Math.abs(accInput) < THRESHOLD_UP[2]){
                        mySig = Signatures.UP;
                    }
                    else if(Math.abs(accInput) < THRESHOLD_DOWN[2]){
                        mySig = Signatures.DOWN;
                    }
                    else{
                        mySig = Signatures.UNDETERMINED;
                    }
                }

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
        //I was debating if I should send the array into here,
        //But it shouldn't matter because only the filtered readings are being entered.
        previousReading = accInput;

    }

}
