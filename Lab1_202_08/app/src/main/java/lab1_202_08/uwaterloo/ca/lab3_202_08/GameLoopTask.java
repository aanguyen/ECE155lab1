package lab1_202_08.uwaterloo.ca.lab3_202_08;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.RelativeLayout;

import java.util.TimerTask;

/**
 * Created by Janet Liu on 6/26/2017.
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class GameLoopTask extends TimerTask {
    private Activity myActivity;
    private RelativeLayout MyRL;
    private Context myContext;
    public enum gameDirection{UP, DOWN, LEFT, RIGHT ,NO_MOVEMENT};
    private gameDirection currentGameDirection;
    private GameBlock myBlock;

    GameLoopTask(Activity myActivity, RelativeLayout MyRL, Context myContext){
        this.myActivity = myActivity;
        this.MyRL = MyRL;
        this.myContext = myContext;
        createBlock();
    }

    private void createBlock(){
        GameBlock newBlock = new GameBlock(myContext, 1002, 1002);
        this.myBlock = newBlock;
        MyRL.addView(newBlock);
    }
    public void run(){
        this.myActivity.runOnUiThread(
          new Runnable(){
              public void run(){
                  myBlock.move();
              }
          }
        );
    }
    public void setStringDir(String newDirection) {
        switch (newDirection) {
            case "RIGHT":
                this.currentGameDirection = gameDirection.RIGHT;
                break;
            case "LEFT":
                this.currentGameDirection = gameDirection.LEFT;
                break;
            case "UP":
                this.currentGameDirection = gameDirection.UP;
                break;
            case "DOWN":
                this.currentGameDirection = gameDirection.DOWN;
                break;
            default:
                this.currentGameDirection = gameDirection.NO_MOVEMENT;
        }
        myBlock.setBlockDirection(this.currentGameDirection);
    }
    public void setDirection(gameDirection newDirection){
        this.currentGameDirection = newDirection;
        Log.d("Setting direction:", currentGameDirection.toString());
    }

}
