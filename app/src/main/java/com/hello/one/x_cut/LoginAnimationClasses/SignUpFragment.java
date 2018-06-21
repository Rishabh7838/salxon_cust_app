package com.hello.one.x_cut.LoginAnimationClasses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hello.one.x_cut.Common.Common;
import com.hello.one.x_cut.LoginActivity;
import com.hello.one.x_cut.MainActivity;
import com.hello.one.x_cut.Model.User;
import com.hello.one.x_cut.R;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.support.annotation.Nullable;
import android.annotation.TargetApi;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindViews;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


public class SignUpFragment extends AuthFragment{

    @BindViews(value = {R.id.email_input_edit,
            R.id.password_input_edit,
            R.id.confirm_password_edit})
    protected List<TextInputEditText> views;
    private TextInputLayout inputLayout, numberLayout;
    private int sendORresend = 0;
    private TextInputEditText mnumber, OTPnumber,nameBox,ageBox;
    private TextView  ResendOTP;
    private Button SendOTP;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private DatabaseReference current_user_dp;
    private SharedPreferences mCurrentUser;
    private RadioGroup mRadioGroup;
    private FirebaseUser user;
    private View copyView;
    private String signinORsignup, Name, Age, Gender, phoneVerificationId, signUpStatus;
    private ProgressDialog mProgressBar;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view!=null){
            ResendOTP = ButterKnife.findById(view, R.id.Resend_OTP_Signup);
            SendOTP = ButterKnife.findById(view, R.id.Send_OTP_Signup);
            mnumber = ButterKnife.findById(view, R.id.email_input_edit);
            OTPnumber = ButterKnife.findById(view, R.id.password_input_edit);
            nameBox = ButterKnife.findById(view, R.id.confirm_password_edit);
            ageBox = ButterKnife.findById(view, R.id.age_box_edit);
            mRadioGroup = ButterKnife.findById(view, R.id.genderBox);
            mAuth = FirebaseAuth.getInstance();
            view.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.color_sign_up));
            caption.setText(getString(R.string.sign_up_label));
            caption.setEnabled(false);
            for(final TextInputEditText editText:views){
                if(editText.getId()==R.id.password_input_edit){
                    final TextInputLayout inputLayout= ButterKnife.findById(view,R.id.passwprd_input_signup);
                    final TextInputLayout confirmLayout=ButterKnife.findById(view,R.id.confirm_password_signup);
                    Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                    inputLayout.setTypeface(boldTypeface);
                    confirmLayout.setTypeface(boldTypeface);
                    editText.addTextChangedListener(new TextWatcherAdapter(){
                        @Override
                        public void afterTextChanged(Editable editable) {
                            inputLayout.setPasswordVisibilityToggleEnabled(editable.length()>0);
                        }
                    });
                }
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus) {
                            boolean isEnabled = editText.getText().length() > 0;
                            editText.setSelected(isEnabled);
                        }
                    }
                });


                                                  }
            caption.setVerticalText(true);
            foldStuff();
            caption.setTranslationX(getTextPadding());
        }


        //OTP and number only
        OTPnumber.setVisibility(View.GONE);
        ResendOTP.setVisibility(View.GONE);
        ageBox.setVisibility(View.GONE);
        nameBox.setVisibility(View.GONE);
        mRadioGroup.setVisibility(View.GONE);
        SendOTP.setEnabled(false);

        mnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(numberValidator(mnumber.getText().toString())){
                    SendOTP.setEnabled(true);
                    SendOTP.setTextColor(getResources().getColor(R.color.buttonSignUp));
                }
                else{
                    SendOTP.setEnabled(false);
                    SendOTP.setTextColor(getResources().getColor(R.color.overlayActionbar));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        SendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Sending OTP", Toast.LENGTH_SHORT).show();
                if (sendORresend == 0 && numberValidator(mnumber.getText().toString())) {
                    sendCode("signin");
                    SendOTP.animate().alpha(1.0f)
                            .setDuration(350);
                    ResendOTP.setEnabled(true);
                    OTPnumber.setVisibility(View.VISIBLE);
                    ResendOTP.setVisibility(View.VISIBLE);
                    SendOTP.setText("Verify");

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
                int selectId = mRadioGroup.getCheckedRadioButtonId();
                if (TextUtils.isEmpty(mnumber.getText().toString().trim())) {
                    mnumber.setError("Number can't be Empty");

                } else if (TextUtils.isEmpty(nameBox.getText().toString().trim()))
                    nameBox.setError("Name Can't be empty");

                else if (TextUtils.isEmpty(ageBox.getText().toString().trim()))
                    ageBox.setError("Age Can't be empty");

                else if (!numberValidator(mnumber.getText().toString())) {
                    mnumber.setError("Please Enter Valid Number");
                }

                else{

                mProgressBar = new ProgressDialog(getActivity());
                mProgressBar.setMessage("Registering User...");
                mProgressBar.setCanceledOnTouchOutside(false);
                mProgressBar.show();


                String user_id = mAuth.getCurrentUser().getUid();

                Name = nameBox.getText().toString();
                Age = ageBox.getText().toString();


                final RadioButton radioButton = ButterKnife.findById(view, selectId);
                if (radioButton.getText() == null) {
                    return;
                }

                Gender = radioButton.getText().toString();

                current_user_dp = FirebaseDatabase.getInstance().getReference("users").child("Customers").child(user_id);
                //current_user_dp.push().setValue(true);
                //saveUserInformation();
                User Currentuser = new User(Name, "", mnumber.getText().toString(), Gender, Age);
                mCurrentUser = getActivity().getSharedPreferences("CurrentUser", MODE_PRIVATE);
                SharedPreferences.Editor editor = mCurrentUser.edit();
                editor.putString("CurrenUserName", Currentuser.getName());
                editor.putString("CurrenUserNumber", Currentuser.getNumber());
                editor.putString("CurrenUserAge", Currentuser.getAge());
                editor.putString("CurrenUserGender", Currentuser.getGender());
                editor.apply();
                Common.currentUser = Currentuser;

                //User Currentuser = new User("Rishabh","7838257301","password","Gender","Age");

                current_user_dp.setValue(Currentuser);
                Intent Go = new Intent(getActivity(), MainActivity.class);
                startActivity(Go);
                getActivity().finish();
            }
            }
        });

    }


    private Boolean numberValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String Number = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$";//"^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(Number);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void sendCode(String s) {
        //Toast.makeText(this, "sendcode()", Toast.LENGTH_SHORT).show();
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

                            OTPnumber.animate()
                                    .alpha(0.0f).setDuration(400)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            OTPnumber.setVisibility(View.GONE);
                                            SendOTP.setVisibility(View.GONE);
                                            ResendOTP.setVisibility(View.GONE);

                                            nameBox.setVisibility(View.VISIBLE);
                                            ageBox.setVisibility(View.VISIBLE);
                                            mRadioGroup.setVisibility(View.VISIBLE);
                                            OTPnumber.setSelected(false);
                                        }
                                    });



                      //      ResendOTP.setTextColor(getResources().getColor(R.color.no_color));

                            //signInStatus = "success";
                            caption.setEnabled(true);
                            caption.setTextColor(getResources().getColor(R.color.dot_dark_screen1));

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
        return R.layout.sign_up_fragment;
    }

    @Override
    public void clearFocus() {
        for(View view:views) view.clearFocus();
    }

    @Override
    public void fold() {
        lock=false;
        com.hello.one.x_cut.LoginAnimationClasses.Rotate transition = new com.hello.one.x_cut.LoginAnimationClasses.Rotate();
        transition.setEndAngle(-90f);
        transition.addTarget(caption);
        TransitionSet set=new TransitionSet();
        set.setDuration(getResources().getInteger(R.integer.duration));
        ChangeBounds changeBounds=new ChangeBounds();
        set.addTransition(changeBounds);
        set.addTransition(transition);
        TextSizeTransition sizeTransition=new TextSizeTransition();
        sizeTransition.addTarget(caption);
        set.addTransition(sizeTransition);
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);
        set.addListener(new Transition.TransitionListenerAdapter(){
            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);
                caption.setTranslationX(getTextPadding());
                caption.setRotation(0);
                caption.setVerticalText(true);
                caption.requestLayout();

            }
        });
        TransitionManager.beginDelayedTransition(parent,set);
        foldStuff();
        caption.setTranslationX(-caption.getWidth()/8+getTextPadding());
    }

    private void foldStuff(){
        caption.setTextSize(TypedValue.COMPLEX_UNIT_PX,caption.getTextSize()/2f);
        caption.setTextColor(Color.WHITE);
        ConstraintLayout.LayoutParams params=getParams();
        params.rightToRight=ConstraintLayout.LayoutParams.UNSET;
        params.verticalBias=0.5f;
        caption.setLayoutParams(params);
    }

    private float getTextPadding(){
        return getResources().getDimension(R.dimen.folded_label_padding)/2.1f;
    }
}
