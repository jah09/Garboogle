package com.example.practiceapp_elearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.SharedPreferences;

public class LoginActivity extends AppCompatActivity {
    DatabaseReference dbRef;
    FirebaseAuth firebaseAuth;
    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";

    // key for storing email.
    public static final String USER_KEY = "userId_key";
    public static final String USER_EMAIL = "useremail_key";
    public static final String USER_PASSWORD = "userpassword_key";


    // key for storing password
    SharedPreferences sharedReferences;
    String USERID, USEREMAIL,USERPASSWORD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // getting the data which is stored in shared preferences.
        sharedReferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        // in shared prefs inside get string method
        // we are passing key value as EMAIL_KEY and
        // default value is
        // set to null if not present.
        USERID = sharedReferences.getString("USER_KEY", null);
        USEREMAIL = sharedReferences.getString("USER_EMAIL", null);
        USERPASSWORD = sharedReferences.getString("USER_PASSWORD", null);

    }

    public void Loginbtn(View v) {

        // Button btnlogin = findViewById(R.id.loginBtn);
        boolean clearFields = getIntent().getBooleanExtra("isLogout", false);


        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth
        EditText editTextLoginEmail = findViewById(R.id.edittext_logInEmail);
        EditText editTextPassword = findViewById(R.id.edittext_Password);
        String email = editTextLoginEmail.getText().toString().trim();
        String passWord = editTextPassword.getText().toString().trim();


        if (clearFields) {

            editTextLoginEmail.setText("");
            editTextPassword.setText("");
        }

        if (editTextLoginEmail.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Warning");
            builder.setMessage("Please enter username or password");
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            // on below line we are calling a sign in method and passing email and password to it.
            mAuth.signInWithEmailAndPassword(email, passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userAuthID = user.getUid();
                        // Log.d("onComplete", "onComplete: "+user+fbEmail);

                        Toast.makeText(getApplicationContext(), "Successfully Login", Toast.LENGTH_LONG).show();
                        //showBtnSuccesLogin();
                        SharedPreferences.Editor editor = sharedReferences.edit();
                        // below two lines will put values for
//                        // email and password in shared preferences.
                        editor.putString(USER_KEY, userAuthID);
                        editor.putString(USER_EMAIL, user.getEmail());
                        editor.putString(USER_PASSWORD, passWord);
                        Log.d("Login Activity", "onComplete: " + USER_KEY + userAuthID);
//
//                        // to save our data with key and value.
                        editor.apply();
                        Intent intent;
                        intent = new Intent(getApplicationContext(), HomePage.class);
                        startActivity(intent);


                    } else {
                        // An error occurred during authentication
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthInvalidUserException) {
                            editTextLoginEmail.requestFocus();
                            editTextLoginEmail.setError("Email not found");
                        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            editTextPassword.requestFocus();
                            editTextPassword.setError("Incorrect password");
                        } else {
                            Log.e("RegistrationError", "Error: " + task.getException()); // Log the error message
                            Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                        }

                    }
                }
            });
        }
    }
    public void showBtnSuccesLogin(){
        ConstraintLayout constraintLayoutSucessLayout=findViewById(R.id.sucessConstraintLayout);
        View view= LayoutInflater.from(LoginActivity.this).inflate(R.layout.success_dialog,constraintLayoutSucessLayout);
        Button sucesDone=view.findViewById(R.id.sucessDone);
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog=builder.create();
        sucesDone.findViewById(R.id.sucessDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   alertDialog.dismiss();
                Intent intent;
                intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
            }
        });
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

    }

    public void togglePasswordVisibility(View v) {
        EditText editTextPassword = findViewById(R.id.edittext_Password);
        ImageView imageViewToogleShowPass = findViewById(R.id.imageViewPasswordToggle);
        if (editTextPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageViewToogleShowPass.setImageResource(R.drawable.baseline_remove_red_eye_24);
        } else {
            editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageViewToogleShowPass.setImageResource(R.drawable.outline_remove_red_eye_24);
        }

        // Move the cursor to the end of the text
        editTextPassword.setSelection(editTextPassword.getText().length());
    }

    public void onClickSignUpNow(View v) {
        TextView txtViewSignUpNowText = findViewById(R.id.signupText);
        Intent intent;
        intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}