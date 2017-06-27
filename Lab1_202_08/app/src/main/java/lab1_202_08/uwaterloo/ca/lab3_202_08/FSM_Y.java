package lab1_202_08.uwaterloo.ca.lab3_202_08;

import android.widget.TextView;


public class FSM_Y {

    //FSM parameters
    enum FSMStates{NO_MOVEMENT, UP, DOWN, RIGHT, LEFT};
    private FSMStates myStates;

    private float[] minZ = {0,0};
    private float[] maxZ = {-10,0};
    private float[] minX = {0,0};
    private float[] maxX = {-10,0};

    //These are the characteristic thresholds of my choice.
    //1st threshold: minimum slope of the response onset
    //2nd threshold: the minimum response maxZ amplitude of the first peak
    //3rd threshold: the maximum response amplitude after settling for 30 samples.

    private final float THRESHOLD = 20f;

    //This is the sample counter.
    private int sampleCounter = 0;

    //Keep the most recent historical reading so we can calculate the most recent slope
    private float previousReading;

    //Keep a reference of the TextView from the layout.
    private TextView DirectionTV;


    public String getState(){
        return myStates.toString();
    }

    //Constructor.  FSM is started into NO_MOVEMENT state.
    public FSM_Y(TextView displayTV){
        //The default state is NO_MOVEMENT
        myStates = FSMStates.NO_MOVEMENT;
        //Sets previous reading as 0 for initial value
        previousReading = 0;
        DirectionTV = displayTV;
        //Text views maxText and minText is seen for easy debugging


    }


    //Resetting the FSM back to the initial state
    public void resetFSM(){
        myStates = FSMStates.NO_MOVEMENT;
        previousReading = 0;
    }

    //This is the main FSM body
    public void activateFSM(float accInputY, float accInputX){
        // increment counter
        sampleCounter++;
        //First, calculate the slope between the most recent input and the
        //most recent historical readings
        float accSlope = accInputY - previousReading;

        //This is to make sure that only the most recent 30 readings are used
        if (sampleCounter - maxZ[1] > 30) {
            maxZ[0] = 0;
        }
        if (sampleCounter - minZ[1] > 30){
            minZ[0] = 0;
        }
        if (sampleCounter - maxX[1] > 30) {
            maxX[0] = 0;
        }
        if (sampleCounter - minX[1] > 30){
            minX[0] = 0;
        }

        //Changes it to a new max and min in the x, z plane
        if (accInputY > maxZ[0]){
            maxZ[0] = accInputY;
            maxZ[1] = sampleCounter;
        }
        else if (accInputY < minZ[0]){
            minZ[0] = accInputY;
            minZ[1] = sampleCounter;
        }
        if (accInputX > maxX[0]) {
            maxX[0] = accInputX;
            maxX[1] = sampleCounter;
        }
        else if (accInputX < minX[0]) {
            minX[0] = accInputX;
            minX[1] = sampleCounter;
        }



        //Then, implement the state flow.
        switch(myStates){

            case NO_MOVEMENT:


                DirectionTV.setText("The FSM state is at NO_MOVEMENT");

                if (((maxZ[0] - minZ[0]) > THRESHOLD) || (maxX[0] - minX[0] > THRESHOLD)){
                    //UP/DOWN/LEFT/RIGHT gesture detected.

                    if (accInputY < -2) {
                        myStates = FSMStates.DOWN;
                    }
                    else if (accInputY > 2) {
                        myStates = FSMStates.UP;
                    }
                    else if (accInputX < -2) {
                        myStates = FSMStates.RIGHT;
                    }
                    else if (accInputX > 2) {
                        myStates = FSMStates.LEFT;
                    }
                }

                break;

            case DOWN:

                DirectionTV.setText("DOWN");
                //Reset the max and min
                sampleCounter = 0;
                for (int i = 0; i < 2; i++){
                    minZ[i] = 0;
                    maxZ[i] = 0;
                }
                //when reading is stable again
                if (accInputY < 2 && accInputY > -2) {
                    myStates = FSMStates.NO_MOVEMENT;
                }

                break;

            case UP:

                DirectionTV.setText("UP");
                //Reset the max and min
                sampleCounter = 0;
                for (int i = 0; i < 2; i++) {
                    minZ[i] = 0;
                    maxZ[i] = 0;
                }
                //when reading is stable again
                if (accInputY < 2 && accInputY > -2) {
                    myStates = FSMStates.NO_MOVEMENT;
                }
                break;
            case RIGHT:
                //Reset the max and min
                DirectionTV.setText("RIGHT");
                sampleCounter = 0;
                for (int i = 0; i < 2; i++) {
                    minX[i] = 0;
                    maxX[i] = 0;
                }
                //when reading is stable again
                if (accInputX < 2 && accInputX > -2) {
                    myStates = FSMStates.NO_MOVEMENT;
                }
                break;
            case LEFT:
                DirectionTV.setText("LEFT");
                //Reset the max and min
                sampleCounter = 0;
                for (int i = 0; i < 2; i++) {
                    minX[i] = 0;
                    maxX[i] = 0;
                }
                //when reading is stable again
                if (accInputX < 2 && accInputX > -2) {
                    myStates = FSMStates.NO_MOVEMENT;
                }
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
