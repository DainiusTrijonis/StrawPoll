package com.example.strawpoll;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        int SPLASH_TIME_OUT = 5000;
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run()
            {
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}