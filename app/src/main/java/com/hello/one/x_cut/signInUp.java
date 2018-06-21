package com.hello.one.x_cut;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signInUp extends AppCompatActivity {
    private Button signIn, signUp;

    private FirebaseAuth.AuthStateListener FBAL;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_up);
        signIn = (Button) findViewById(R.id.buttonSignIn);
        signUp = (Button) findViewById(R.id.buttonSignUp);

        mAuth = FirebaseAuth.getInstance();
        FBAL = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user1=FirebaseAuth.getInstance().getCurrentUser();
                if(user1!=null)
                {

                    Intent profile = new Intent(signInUp.this, MainActivity.class);
                    startActivity(profile);
                    finish();
                    return;
                }
            }
        };
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(signInUp.this,LoginActivity.class);
                i.putExtra("checkLoginStatus" , "signIn");
                startActivity(i);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(signInUp.this,LoginActivity.class);
                i.putExtra("checkLoginStatus" , "signUp");
                startActivity(i);
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
         mAuth.addAuthStateListener(FBAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
           mAuth.removeAuthStateListener(FBAL);
    }
}
