import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTime;
    private Button buttonStart, buttonPause, buttonReset;

    private Handler handler = new Handler();
    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private boolean isRunning = false;
    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeInMilliseconds + timeSwapBuff;
            int seconds = (int) (updatedTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (updatedTime % 1000);
            milliseconds = milliseconds / 10;
            textViewTime.setText(String.format("%02d:%02d:%02d", minutes, seconds, milliseconds));
            handler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTime = findViewById(R.id.textViewTime);
        buttonStart = findViewById(R.id.buttonStart);
        buttonPause = findViewById(R.id.buttonPause);
        buttonReset = findViewById(R.id.buttonReset);

        buttonStart.setOnClickListener(v -> startStopwatch());
        buttonPause.setOnClickListener(v -> pauseStopwatch());
        buttonReset.setOnClickListener(v -> resetStopwatch());
    }

    private void startStopwatch() {
        if (!isRunning) {
            startTime = SystemClock.uptimeMillis();
            handler.postDelayed(updateTimerThread, 0);
            isRunning = true;
        }
    }

    private void pauseStopwatch() {
        if (isRunning) {
            timeSwapBuff += timeInMilliseconds;
            handler.removeCallbacks(updateTimerThread);
            isRunning = false;
        }
    }

    private void resetStopwatch() {
        timeSwapBuff = 0L;
        timeInMilliseconds = 0L;
        updatedTime = 0L;
        textViewTime.setText("00:00:00");
        handler.removeCallbacks(updateTimerThread);
        isRunning = false;
    }
}
