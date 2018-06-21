package com.hello.one.x_cut;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.media.MediaCodec;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hello.one.x_cut.Common.Common;
import com.hello.one.x_cut.Model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "error";
    private int sendORresend = 0;
    private TextInputEditText mnumber,mpass1,mName,mAge,mGender,mVerificationCode;
    private Button mlogin1,mregister1;
    private Button mDriver, mCust;
    private LinearLayout registrationDetailes,mVerificationLayout;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private DatabaseReference current_user_dp;
    private SharedPreferences mCurrentUser;
    private RadioGroup mRadioGroup;
    private FirebaseUser user;
    private String signinORsignup,Name,Age,Gender,phoneVerificationId,signUpStatus,signInStatus;
    private ProgressDialog mProgressBar;
   // private FirebaseAuth.AuthStateListener FBAL;
    private TextView mSendCode,mVerify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressBar = new ProgressDialog(this);
        mnumber= (TextInputEditText) findViewById(R.id.number);
        mAge= (TextInputEditText) findViewById(R.id.userAge);
        mRadioGroup = (RadioGroup) findViewById(R.id.genderGroup);
        mpass1= (TextInputEditText) findViewById(R.id.pass1);
        mlogin1= (Button) findViewById(R.id.login1);
        registrationDetailes = (LinearLayout) findViewById(R.id.RegisterDetails);
        mregister1= (Button) findViewById(R.id.register1);
        mVerificationLayout = (LinearLayout) findViewById(R.id.verificationLayout);
        mVerificationCode = (TextInputEditText) findViewById(R.id.verificationCode);
        mVerify = (TextView) findViewById(R.id.verifyCode);
        mSendCode = (TextView) findViewById(R.id.sendCode);
        mName = (TextInputEditText) findViewById(R.id.userName);


        mAuth = FirebaseAuth.getInstance();
//        FBAL = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user1=FirebaseAuth.getInstance().getCurrentUser();
//                if(user1!=null)
//                {
//
//                    Intent profile = new Intent(LoginActivity.this, MapsActivity.class);
//                    startActivity(profile);
//                    finish();
//                    return;
//
//                }
//            }
//        };

        signinORsignup = null;
        Intent intentFromsignInUp = getIntent();
        Bundle b = intentFromsignInUp.getExtras();
        if(b!=null)
        {
            signinORsignup = b.getString("checkLoginStatus");
            if(signinORsignup.equals("signIn"))
            {
                registrationDetailes.setVisibility(View.GONE);
                mregister1.setVisibility(View.GONE);

            }
            else if(signinORsignup.equals("signUp"))
            {
                mlogin1.setVisibility(View.GONE);
            }
        }

        mregister1.setEnabled(false);

        mlogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        mregister1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerLogin();
            }
        });

        mSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendORresend==0 && numberValidator(mnumber.getText().toString())) {
                    sendCode("signup");
                    mSendCode.setText("Resend Code");
                }
                else if(!numberValidator(mnumber.getText().toString())){
                    mnumber.setError("Please Enter Valid Number");
                }
                else if(sendORresend==1)
                    resendCode();
            }
        });

        mVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode("signup");
            }
        });

    }
    private void signIn(){
        mProgressBar.setMessage("Signing in");
        mProgressBar.setCanceledOnTouchOutside(false);
        mProgressBar.show();

        sendCode("signin");

            }


    private void registerLogin(){
        if(TextUtils.isEmpty(mnumber.getText().toString().trim())||TextUtils.isEmpty(mpass1.getText().toString().trim())){
            mnumber.setError("Fields can't be Empty");
            mpass1.setError("Fields can't be Empty");
        }
        else if(!numberValidator(mnumber.getText().toString())){
            mnumber.setError("Please Enter Valid Number");
        }
        else if(!passwordValidator(mpass1.getText().toString())){
            mpass1.setError("Password - Minimum One digit(0-9),No white space,Minimum 8-char length ");
        }
        else{
            Register();
        }
    }
    private void Register(){
//        final String number = mnumber.getText().toString();
//        final String pass = mpass1.getText().toString();
        mProgressBar.setMessage("Registering User...");
        mProgressBar.setCanceledOnTouchOutside(false);
        mProgressBar.show();



                    String user_id = mAuth.getCurrentUser().getUid();

        Name = mName.getText().toString();
        Age = mAge.getText().toString();

        int selectId = mRadioGroup.getCheckedRadioButtonId();
        final RadioButton radioButton = (RadioButton) findViewById(selectId);
        if(radioButton.getText()== null){
            return;
        }

        Gender  = radioButton.getText().toString();

        current_user_dp = FirebaseDatabase.getInstance().getReference("users").child("Customers").child(user_id);
        //current_user_dp.push().setValue(true);
                      //saveUserInformation();
        User Currentuser = new User(Name,mpass1.getText().toString(),mnumber.getText().toString(),Gender,Age);
        mCurrentUser = getSharedPreferences("CurrentUser",MODE_PRIVATE);
        SharedPreferences.Editor editor = mCurrentUser.edit();
        editor.putString("CurrenUserName",Currentuser.getName());
        editor.putString("CurrenUserNumber",Currentuser.getNumber());
        editor.putString("CurrenUserAge",Currentuser.getAge());
        editor.putString("CurrenUserGender",Currentuser.getGender());
        editor.apply();
        Common.currentUser = Currentuser;

        //User Currentuser = new User("Rishabh","7838257301","password","Gender","Age");

        current_user_dp.setValue(Currentuser);
        Intent Go = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(Go);
        finish();

            }



    private void saveUserInformation() {
            Name = mName.getText().toString();
            Age = mAge.getText().toString();

            int selectId = mRadioGroup.getCheckedRadioButtonId();
            final RadioButton radioButton = (RadioButton) findViewById(selectId);
            if(radioButton.getText()== null){
                return;
            }

            Gender  = radioButton.getText().toString();
            Map userInfo = new HashMap();
            userInfo.put("name",Name);
            userInfo.put("age",Age);
            userInfo.put("gender",Gender);
            userInfo.put("password",mpass1.getText().toString());
            current_user_dp.updateChildren(userInfo);
    }

    private void Login(){
        if(TextUtils.isEmpty(mnumber.getText().toString().trim())||TextUtils.isEmpty(mpass1.getText().toString().trim())){
            mnumber.setError("Fields can't be Empty");
            mpass1.setError("Fields can't be Empty");
        }
        else if(!numberValidator(mnumber.getText().toString())){
            mnumber.setError("Please Enter Valid number");
        }
        else{
            signIn();
        }
    }
    private Boolean numberValidator(String email){
        Pattern pattern;
        Matcher matcher;
        final String Number = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$";//"^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(Number);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private Boolean passwordValidator(String email){
        Pattern pattern;
        Matcher matcher;
        final String Password_Pattern = "^(?=.*[0-9])(?=\\S+$).{8,}$";
        pattern = Pattern.compile(Password_Pattern);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void sendCode(String s)
    {
        Toast.makeText(this, "sendcode()", Toast.LENGTH_SHORT).show();
        sendORresend=1;
        String phoneNumber = mnumber.getText().toString();

        setUpverificationCallbacks(s);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks);        // OnVerificationStateChangedCallbacks
    }
    private void resendCode()
    {
        Toast.makeText(this, "Resend()", Toast.LENGTH_SHORT).show();
        String phoneNumber = mnumber.getText().toString();

        setUpverificationCallbacks("resend code");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks,
                resendingToken);        // OnVerificationStateChangedCallbacks
    }

    private void setUpverificationCallbacks(final String s) {
        Toast.makeText(this, "setupverificationncallback", Toast.LENGTH_SHORT).show();
        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(LoginActivity.this, "onverificationcompleted", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential,s);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
// This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(LoginActivity.this, "onverificationfailed", Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                Toast.makeText(LoginActivity.this, "codesent", Toast.LENGTH_SHORT).show();
                phoneVerificationId = s;
                //mVerificationCode.setText(s);
                resendingToken = forceResendingToken;

                mSendCode.setEnabled(false);
                mVerify.setEnabled(true);

            }
        };
    }

    private void verifyCode(String s)
    {
        mProgressBar.setMessage("Verifying User...");
        mProgressBar.setCanceledOnTouchOutside(false);
        mProgressBar.show();
        String code = mVerificationCode.getText().toString();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId,code);
        signInWithPhoneAuthCredential(credential,s);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential , final String s) {

        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mVerificationLayout.setVisibility(View.GONE);
                            mregister1.setEnabled(true);
                             user = task.getResult().getUser();
                            if(mProgressBar!=null)
                            mProgressBar.dismiss();

                            if(s.equals("signin"))
                            {
                                signInStatus = "success";
                                verifyPassword();
                            }
                            else if(s.equals("signup"))
                            signUpStatus = "success";

                            //Toast.makeText(LoginActivity.this, "user signIn", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(LoginActivity.this, "SignIn failed", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                signUpStatus = "failed";
                                Toast.makeText(LoginActivity.this, "Verification code was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void verifyPassword() {
        String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference current_user_dp = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(mnumber.getText().toString()).child(user_id);

        current_user_dp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("password").getValue().toString().equals(mpass1.getText().toString()))
                    {
                        Intent Go = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(Go);
                        finish();
                    }
                    else
                    {
                        mpass1.setError("Invalid Password");
                        mpass1.setText("");
                    }

                }
                else{
                    Toast.makeText(LoginActivity.this, "Please signUp first", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
       // mAuth.addAuthStateListener(FBAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
     //   mAuth.removeAuthStateListener(FBAL);
    }
}
