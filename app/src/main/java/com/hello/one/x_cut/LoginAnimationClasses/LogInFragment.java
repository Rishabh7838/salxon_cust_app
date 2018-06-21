package com.hello.one.x_cut.LoginAnimationClasses;

/**
 * Created by one on 6/7/2018.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;

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
import com.hello.one.x_cut.LoginActivity;
import com.hello.one.x_cut.MainActivity;
import com.hello.one.x_cut.R;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Rotate;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionInflater;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.annotation.TargetApi;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInFragment extends AuthFragment {


    private static final String TAG = "";
    @BindViews(value = {R.id.email_input_edit, R.id.password_input_edit})
    //@BindViews(R.id.Send_OTP) TextView OTP;
    protected List<TextInputEditText> views;
    private TextInputLayout inputLayout, numberLayout;
    private int sendORresend = 0;
    private TextInputEditText mnumber, OTPnumber;
    private TextView SendOTP, VerifyOTP, ResendOTP,changeNumber;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private DatabaseReference current_user_dp;
    private SharedPreferences mCurrentUser;
    private RadioGroup mRadioGroup;
    private FirebaseUser user;
    private View copyView;
    private String signinORsignup, Name, Age, Gender, phoneVerificationId, signUpStatus, signInStatus;
    private ProgressDialog mProgressBar;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view != null) {
            copyView = view;
            caption.setText(getString(R.string.log_in_label));
            caption.setEnabled(false);
            //ButterKnife.bind(getActivity());
            SendOTP = ButterKnife.findById(view, R.id.Send_OTP);
            //VerifyOTP = ButterKnife.findById(view,R.id.verify_OTP);
            mnumber = ButterKnife.findById(view, R.id.email_input_edit);
            OTPnumber = ButterKnife.findById(view, R.id.password_input_edit);
            ResendOTP = ButterKnife.findById(view, R.id.Resend_OTP);
            OTPnumber.setVisibility(View.GONE);
            mAuth = FirebaseAuth.getInstance();
            changeNumber = ButterKnife.findById(view, R.id.changeNumberTV);
            ResendOTP.setVisibility(View.GONE);
//            mnumber.setTextColor(getResources().getColor(R.color.overlayActionbar));
//            OTPnumber.setTextColor(getResources().getColor(R.color.overlayActionbar));
//            mnumber.setHintTextColor(getResources().getColor(R.color.overlayActionbar));
//            OTPnumber.setHintTextColor(getResources().getColor(R.color.overlayActionbar));
//            caption.setTextColor(getResources().getColor(R.color.overlayActionbar));
//            caption.setVisibility(View.GONE);
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_log_in));
            for (final TextInputEditText editText : views) {
                if (editText.getId() == R.id.password_input_edit) {
                    inputLayout = ButterKnife.findById(view, R.id.password_input);
                    Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                    inputLayout.setTypeface(boldTypeface);
                    editText.addTextChangedListener(new TextWatcherAdapter() {
                        @Override
                        public void afterTextChanged(Editable editable) {
                            inputLayout.setPasswordVisibilityToggleEnabled(editable.length() > 0);
                        }
                    });
                }
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            boolean isEnabled = editText.getText().length() > 0;
                            editText.setSelected(isEnabled);
                        }
                    }
                });

            }
            final TextView sendOTP = ButterKnife.findById(view, R.id.Send_OTP);
        }

//        caption.setVerticalText(true);
//        foldStuff();
//        caption.setTranslationX(getTextPadding());

        mnumber.addTextChangedListener(new TextWatcherAdapter(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(numberValidator(mnumber.getText().toString())){
                    SendOTP.setEnabled(true);
                    SendOTP.setTextColor(getResources().getColor(R.color.buttonSignUp));
                }
                else{
                    SendOTP.setEnabled(false);
                    SendOTP.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        changeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mnumber.setText("");
                OTPnumber.setText("");
                mnumber.setFocusableInTouchMode(true);
                if(OTPnumber.getVisibility()==View.VISIBLE)
                    OTPnumber.setVisibility(View.GONE);
                if(SendOTP.getVisibility()==View.VISIBLE)
                    mnumber.setVisibility(View.GONE);
                SendOTP.setText("Send OTP");
                caption.setTextColor(getResources().getColor(R.color.white));
                caption.setEnabled(false);
                sendORresend = 0;
            }
        });

        SendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Sending OTP", Toast.LENGTH_SHORT).show();
                if (sendORresend == 0 && numberValidator(mnumber.getText().toString())) {
                    verifyExistenceOfNumber();

                    SendOTP.animate().alpha(1.0f)
                            .setDuration(350);
                    caption.setVisibility(View.VISIBLE);
                    ResendOTP.setEnabled(true);
                    ResendOTP.setTextColor(getResources().getColor(R.color.bottomGreyColor));


                } else if (!numberValidator(mnumber.getText().toString())) {
                    mnumber.setError("Please Enter Valid Number");
                } else if (SendOTP.getText().toString().equals("Verify")) {
                    if (OTPnumber.length() <= 3)
                        OTPnumber.setError("Enter a valid OTP");
                    else {
                        verifyCode("signin");
                    }
                }

            }
        });

        caption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar = new ProgressDialog(getActivity());
                mProgressBar.setMessage("Signing In...");
                mProgressBar.setCanceledOnTouchOutside(false);
                mProgressBar.show();
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getActivity().finish();

            }
        });

        ResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sendORresend == 1) {
                    resendCode();
                    Toast.makeText(getContext(), "Resending OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


//    @OnClick(R.id.caption)
//    public void onLoginClick(){
//
//
//    }

//    @OnClick(R.id.Send_OTP)
//    public void onClick(View view){
//
//    }
//
//    @OnClick(R.id.Resend_OTP)
//    public void onClick(){
//
//    }


    private Boolean numberValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String Number = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$";//"^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(Number);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void verifyExistenceOfNumber()
    {
        mProgressBar = new ProgressDialog(getActivity());
        mProgressBar.setMessage("Verifying Number...");
        mProgressBar.setCanceledOnTouchOutside(false);
        mProgressBar.show();
        DatabaseReference checkNumber = FirebaseDatabase.getInstance().getReference("users").child("Customers");
        checkNumber.orderByChild("number").equalTo(mnumber.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                            mProgressBar.dismiss();
                            sendCode("signin");
                        }
                        else {
                            mProgressBar.dismiss();
                            mnumber.setError("Number is not registered");
                        }
                    }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void sendCode(String s) {
        Toast.makeText(getContext(), "OTP Sent", Toast.LENGTH_SHORT).show();
        OTPnumber.setVisibility(View.VISIBLE);
        ResendOTP.setVisibility(View.VISIBLE);
        SendOTP.setText("Verify");
        sendORresend = 1;
        String phoneNumber = mnumber.getText().toString();

        setUpverificationCallbacks(s);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                verificationCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void resendCode() {
        Toast.makeText(getContext(), "Resend()", Toast.LENGTH_SHORT).show();
        String phoneNumber = mnumber.getText().toString();

        setUpverificationCallbacks("resend code");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                verificationCallbacks,
                resendingToken);
    }

    private void setUpverificationCallbacks(final String s) {
        //Toast.makeText(this, "setupverificationncallback", Toast.LENGTH_SHORT).show();
        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //      Toast.makeText(LoginActivity.this, "onverificationcompleted", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential, s);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
// This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                //  Toast.makeText(LoginActivity.this, "onverificationfailed", Toast.LENGTH_SHORT).show();
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
                //    Toast.makeText(LoginActivity.this, "codesent", Toast.LENGTH_SHORT).show();
                phoneVerificationId = s;
                //mVerificationCode.setText(s);
                resendingToken = forceResendingToken;

//                mSendCode.setEnabled(false);
//                mVerify.setEnabled(true);

            }
        };
    }

    private void verifyCode(String s) {
        mProgressBar = new ProgressDialog(getActivity());
        mProgressBar.setMessage("Verifying User...");
        mProgressBar.setCanceledOnTouchOutside(false);
        mProgressBar.show();
        String code = OTPnumber.getText().toString();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, code);
        signInWithPhoneAuthCredential(credential, s);
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential phoneAuthCredential, final String s) {

        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //     mVerificationLayout.setVisibility(View.GONE);
                            //   mregister1.setEnabled(true);
                            user = task.getResult().getUser();
                            if (mProgressBar != null)
                                mProgressBar.dismiss();
                            if (!phoneAuthCredential.getSmsCode().isEmpty())
                                OTPnumber.setText(phoneAuthCredential.getSmsCode());
                            OTPnumber.setSelected(true);
                            SendOTP.setText("Verified");
                            SendOTP.setTextColor(getResources().getColor(R.color.buttonSignIn));
                            SendOTP.setVisibility(View.GONE);
                            ResendOTP.setEnabled(false);
                            ResendOTP.setTextColor(getResources().getColor(R.color.no_color));
                            signInStatus = "success";
                            caption.setEnabled(true);
                            caption.setTextColor(getResources().getColor(R.color.dot_dark_screen1));

                            mnumber.setFocusable(false);
                            OTPnumber.setFocusable(false);

                            //Toast.makeText(LoginActivity.this, "user signIn", Toast.LENGTH_SHORT).show();

                        } else {

                            //Toast.makeText(LoginActivity.this, "SignIn failed", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                if (mProgressBar != null)
                                    mProgressBar.dismiss();
                                signUpStatus = "failed";
                                Toast.makeText(getContext(), "Verification code was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    public int authLayout() {
        return R.layout.login_fragment;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void fold() {
        lock = false;
        com.hello.one.x_cut.LoginAnimationClasses.Rotate transition = new com.hello.one.x_cut.LoginAnimationClasses.Rotate();
        transition.setEndAngle(-90f);
        transition.addTarget(caption);
        TransitionSet set = new TransitionSet();
        set.setDuration(getResources().getInteger(R.integer.duration));
        ChangeBounds changeBounds = new ChangeBounds();
        set.addTransition(changeBounds);
        set.addTransition(transition);
        TextSizeTransition sizeTransition = new TextSizeTransition();
        sizeTransition.addTarget(caption);
        set.addTransition(sizeTransition);
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);
        final float padding = getResources().getDimension(R.dimen.folded_label_padding) / 2;
        set.addListener(new Transition.TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);
                caption.setTranslationX(-padding);
                caption.setRotation(0);
                caption.setVerticalText(true);
                caption.requestLayout();

            }
        });
        TransitionManager.beginDelayedTransition(parent, set);
        foldStuff();
        caption.setTranslationX(caption.getWidth() / 8 - padding);
    }
    private void foldStuff(){
        caption.setTextSize(TypedValue.COMPLEX_UNIT_PX,caption.getTextSize()/2f);
        caption.setTextColor(Color.WHITE);
        ConstraintLayout.LayoutParams params=getParams();
        params.leftToLeft=ConstraintLayout.LayoutParams.UNSET;
        params.verticalBias=0.5f;
        caption.setLayoutParams(params);
    }
    private float getTextPadding(){
        return getResources().getDimension(R.dimen.folded_label_padding)/2.1f;
    }
    @Override
    public void clearFocus() {
        for (View view : views) view.clearFocus();
    }

}
