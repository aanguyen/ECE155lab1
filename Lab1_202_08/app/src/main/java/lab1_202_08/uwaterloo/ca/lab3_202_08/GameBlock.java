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
    private int curCoordX;
    private int curCoordY;
    private int targetX;
    private int targetY;
    private int velocity = 0;
    private int GB_ACC = 10;
    private GameLoopTask.gameDirection myDir;



    public GameBlock(Context myContext, int coordX, int coordY) {
        super(myContext);
        this.setX(coordX);
        this.setY(coordY);
        this.setImageResource(R.drawable.gameblock);
        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);
        curCoordX = coordX;
        curCoordY = coordY;
    }
    public void setBlockDirection(GameLoopTask.gameDirection newDir) {
        this.myDir = newDir;
        Log.d("Setting direction: ", this.myDir.toString());
    }
    public void move() {
        switch (myDir) {
            case UP:
                targetY = MAX_BOUND;
                if (curCoordY - velocity <= targetY) {
                    curCoordY = targetY;
                    velocity = 0;
                }
                else {
                    curCoordY -= velocity;
                }
                break;
            case DOWN:
                targetY = MIN_BOUND;
                if (curCoordY + velocity >= targetY) {
                    curCoordY = targetY;
                    velocity = 0;
                }
                else {
                    curCoordY += velocity;
                }
                break;
            case LEFT:
                targetX = MAX_BOUND;
                if (curCoordX - velocity <= targetX) {
                    curCoordX = targetX;
                    velocity = 0;
                }
                else {
                    curCoordX -= velocity;
                }
                break;
            case RIGHT:
                targetX = MIN_BOUND;
                if (curCoordX + velocity >= targetX) {
                    curCoordX = targetX;
                    velocity = 0;
                }
                else {
                    curCoordX += velocity;
                }
                break;
            default:
                break;
        }
        velocity += GB_ACC;
        this.setX(curCoordX);
        this.setY(curCoordY);
    }
}
