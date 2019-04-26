package pro.kulebyakin.multiplicationmobile;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;

public class Game extends Activity {
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        TextView timerTextView = findViewById(R.id.timer_text_view);

        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick (long l) {
                int time = (int) l/1000;
                int minutes = (time/60);
                String strMin = "0";
                if (minutes < 10) {
                    strMin += minutes;
                } else {
                    strMin = String.valueOf(minutes);
                }

                int seconds = (time)%60;
                String strSec = "0";
                if (seconds < 10) {
                    strSec += seconds;
                } else {
                    strSec = String.valueOf(seconds);
                }
                timerTextView.setText(strMin + ":" + strSec);
            }
            @Override
            public void onFinish () {
                return;
            }
        }.start();
    }

    public void finishGame(View view) {
        finish();
    }

}
