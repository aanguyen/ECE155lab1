package lab1_202_08.uwaterloo.ca.lecure12;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout myRL = (RelativeLayout) findViewById(R.id.activity_main);

        ImageView myImage = new ImageView(getApplicationContext());
        myImage.setX(100f);
        myImage.setY(100f);
        myImage.setScaleX(0.3f);
        myImage.setScaleY(0.3f);
        myImage.setImageResource(R.drawable.smileyface);

        myRL.addView(myImage);

        Timer myTimer = new Timer();
        AnimationHandler myAnimHandler;
        myTimer.schedule(handler, 16, 16);

        Button myButton1 = new Button(getApplication());
        myButton1.setX(400f);
        myButton1.setY(700f);
        myButton1.setText("Toggle!");

        myRL.addView(myButton1);
    }
}


class ButtonHandler implements View.OnClickListener{
    public AnimationHandler myAnimationHandler;

    public ButtonHandler(AnimationHandler myHand){
        myAnimHandler = myHand;
    }
    public void onClick(View myView){

    }

}
class AnimationHandler extends TimerTask {
    private Activity myActivity;
    private ImageView myImage;

    private float positionX;
    private float velocityX;

    private final float LEFT_BOUND = 30f;
    private final float RIGHT_BOUND = 700f;

    public AnimationHandler(Activity myAct){
        myActivity = myAct;
        myImage = myImg;
        positionX = LEFT_BOUND;
        velocityX = 20;
    }

    public void run(){
        myActivity.runOnUiThread{
            new Runnable(){
                public void run(){

                    positionX += velocityX;

                    myImage.setX(positionX);
                }
            }
        };
    }
}
