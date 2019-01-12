package ro.sapientia.ms.sapientiaadvertiser;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    //Layout elements
    public EditText phoneField;
    public static String phonePrefix = "+4";

    String codeSent;

    //Database reference
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        Button sendCodeButton, verifySignInButton;
        phoneField = findViewById(R.id.login_phone_number);
        sendCodeButton = findViewById(R.id.send_code_button);
        final EditText editTextCode = findViewById(R.id.editTextEnterCode);
        verifySignInButton = findViewById(R.id.button_verify);
        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = phoneField.getText().toString();

                if (phone.isEmpty()) {
                    phoneField.setError("Please enter your phone number!");
                    phoneField.requestFocus();
                    return;
                }
                if (phone.length() < 10) {
                    phoneField.setError("Please enter a valid phone number!");
                    phoneField.requestFocus();
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), "Code sent!", Toast.LENGTH_SHORT).show();
                }
                sendVerificationCode();
            }
        });

        verifySignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = editTextCode.getText().toString();
                if (code.isEmpty()) {
                    editTextCode.setError("Please enter the code received!");
                    editTextCode.requestFocus();
                    return;
                }
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
                signInWithPhoneAuthCredential(credential);
            }
        });
    }

    private void sendVerificationCode() {
        String phone = phonePrefix + phoneField.getText().toString();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.w(TAG, "onVerificationFailed", e);
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            Log.d(TAG, "onCodeSent:" + s);
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;
        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Verification Successfull", Toast.LENGTH_LONG).show();
                            Intent nextIntent = new Intent(LoginActivity.this, BottomNavigationActivity.class);
                            LoginActivity.this.startActivity(nextIntent);
                            LoginActivity.this.finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),
                                        "Incorrect Verification Code ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {

    }
}
