package com.hello.one.x_cut;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends Activity implements Animation.AnimationListener {
    Animation animFadeIn;
    RelativeLayout relativeLayout;
    private FirebaseAuth.AuthStateListener FBAL;
    private FirebaseAuth mAuth;
    private FirebaseUser user1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        FBAL = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user1=FirebaseAuth.getInstance().getCurrentUser();

            }
        };

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
        }
        // load the animation
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim);
        // set animation listener
        animFadeIn.setAnimationListener(this);
        // animation for image
        relativeLayout = (RelativeLayout) findViewById(R.id.splashLayout);
        // start the animation
        relativeLayout.setVisibility(View.VISIBLE);
        relativeLayout.startAnimation(animFadeIn);

    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void onAnimationStart(Animation animation) {
        //under Implementation
    }

    public void onAnimationEnd(Animation animation) {
        // Start Main Screen
        if(user1!=null)
        {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
            return;

        }
        else{
            Intent profile = new Intent(SplashActivity.this, SignInActivity.class);
            startActivity(profile);
            finish();
            return;
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        //under Implementation
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(FBAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(FBAL);
    }

}
