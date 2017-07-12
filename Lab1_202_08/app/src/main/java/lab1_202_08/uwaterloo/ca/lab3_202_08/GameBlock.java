package lab1_202_08.uwaterloo.ca.lab3_202_08;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Janet Liu on 6/26/2017.
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class GameBlock extends ImageView {
    private final float IMAGE_SCALE = 0.70f;
    private final int MAX_BOUND = -78;
    private final int MIN_BOUND = 1002;
    private final int coord1Max    = 278;
    private final int coord2Min    = 640;
    private int curCoordX;
    private int curCoordY;
    private int targetX;
    private int targetY;
    private int velocity = 0;
    private int GB_ACC = 10;
    public int value = 2;

    private GameLoopTask.gameDirection myDir;

    private GameLoopTask.gameDirection lastDir;
    private boolean doneMoving = true;

    //Constructor that initializes all the variables
    public GameBlock(Context myContext, int coordX, int coordY, int initValue) {
        super(myContext);
        this.setX(coordX);
        this.setY(coordY);
        this.setImageResource(R.drawable.gameblock);
        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);
        this.value = initValue;
        curCoordX = coordX;
        curCoordY = coordY;
    }

    //Sets the direction of the block
    public void setBlockDirection(GameLoopTask.gameDirection newDir) {
        if (!doneMoving) {
            this.myDir = this.lastDir;
        }
        else {
            this.myDir = newDir;
        }
        Log.d("Setting direction: ", this.myDir.toString());
    }
    //Moves the block in the direction corresponding to the gesture command
    //Makes sure that the block does not go out of bounds, and that the speed increases with time
    public void move() {

        switch (myDir) {
            case UP:
                targetY = MAX_BOUND;
                if (curCoordY - velocity <= targetY) {
                    curCoordY = targetY;
                    velocity = 0;
                    doneMoving = true;
                } else {
                    curCoordY -= velocity;
                    doneMoving = false;
                }
                break;

            case DOWN:
                targetY = MIN_BOUND;
                if (curCoordY + velocity >= targetY) {
                    curCoordY = targetY;
                    velocity = 0;
                    doneMoving = true;

                } else {
                    curCoordY += velocity;
                    doneMoving = false;
                }
                break;

            case LEFT:
                targetX = MAX_BOUND;
                if (curCoordX - velocity <= targetX) {
                    curCoordX = MAX_BOUND;
                    velocity = 0;
                    doneMoving = true;
                } else {
                    curCoordX -= velocity;
                    doneMoving = false;
                }
                break;

            case RIGHT:
                if (curCoordX + velocity >= MIN_BOUND) {
                    curCoordX += velocity;
                    curCoordX = MIN_BOUND;
                    velocity = 0;
                    doneMoving = true;
                } else {
                    curCoordX += velocity;
                    doneMoving = false;
                }
                break;

            default:
                break;
        }
        velocity += GB_ACC;
        this.setY(curCoordY);
        this.setX(curCoordX);
        this.lastDir = myDir;
    }
}
