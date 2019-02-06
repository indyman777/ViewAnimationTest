package com.flavorburst.myapplication;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    AnimatedView animatedView;
    float progress = 0f;
    Handler simulateHandler;
    Runnable simulateRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animatedView = findViewById(R.id.customView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = 0f;
                animatedView.reset();
                simulateHandler.post(simulateRunnable);
            }
        });

        simulateHandler = new Handler();
        simulateRunnable = new Runnable() {
            @Override
            public void run() {

                Random rand = new Random();
//                float newFloat = rand.nextFloat();

                progress = progress + rand.nextInt(8) - 1;
                Log.i("test", "progress: " + String.valueOf(progress));
                if(progress > 100f) progress = 100f;

                animatedView.startAnimation(progress, 100f);

                if(progress < 100f) simulateHandler.postDelayed(this, 100);
            }
        };
    }
}
