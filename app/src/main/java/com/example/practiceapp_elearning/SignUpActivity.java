package com.example.practiceapp_elearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {
    UserClass userclass;
    Button Signup;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void onClickSigninTextView(View v) {
        Intent intent;
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void SignUpBtnFunction(View view) {

        //declaration of the Edit Text
        EditText editText_Firstname = findViewById(R.id.edittext_firstname);
        EditText editText_Lastname = findViewById(R.id.edittext_lastname);
        EditText editText_Email = findViewById(R.id.edittext_email);
        EditText editText_ContactNumber = findViewById(R.id.edittext_contactnumber);

        EditText editText_Password = findViewById(R.id.edittext_password);

        //assign
        String email = editText_Email.getText().toString().trim();
        String password = editText_Password.getText().toString().trim();
        //conditional statement if one of the EditText is empty
        if (editText_Firstname.getText().toString().isEmpty()) {
            editText_Firstname.requestFocus(); //cursor will focus on the empty field
            editText_Firstname.setError("Enter your first name");
        } else if (!editText_Firstname.getText().toString().matches("[a-zA-Z]+")) {
            editText_Firstname.requestFocus();
            editText_Firstname.setError("Enter valid name");
        } else if (editText_Lastname.getText().toString().isEmpty()) {

            editText_Lastname.requestFocus(); //cursor will focus on the empty field
            editText_Lastname.setError("Enter your last name");

        } else if (!editText_Lastname.getText().toString().matches("[a-zA-Z]+")) {
            editText_Lastname.requestFocus();
            editText_Lastname.setError("Enter valid last name");
        } else if (editText_Email.getText().toString().isEmpty()) {

            editText_Email.requestFocus(); //cursor will focus on the empty field
            editText_Email.setError("Enter your email");
        } else if (!editText_Email.getText().toString().matches("[a-zA-Z0-9]+@[a-z]+\\.+[a-z]+")) {
            editText_Email.requestFocus(); //cursor will focus on the empty field
            editText_Email.setError("Enter valid email");
        } else if (editText_ContactNumber.getText().toString().isEmpty()) {
            editText_Email.requestFocus(); //cursor will focus on the empty field
            editText_Email.setError("Enter your contact number");

        }
//        else if (!editText_ContactNumber.getText().toString().matches("^[+][0-9]")) {
//            editText_ContactNumber.requestFocus(); //cursor will focus on the empty field
//            editText_ContactNumber.setError("Enter valid contact number");
//        }

        else if (editText_Password.getText().toString().isEmpty()) {
            editText_Password.requestFocus(); //cursor will focus on the empty field
            editText_Password.setError("Enter your password");
        } else if (editText_Password.getText().toString().length() <= 7) {
            editText_Password.requestFocus(); //cursor will focus on the empty field
            editText_Password.setError("Password must be 8 characters");
        }
        else {

            //if all fields is not null then ELSE will execute and save to database
            userclass = new UserClass(); //initiate
            String unique_UserId = UUID.randomUUID().toString();
            dbRef = FirebaseDatabase.getInstance().getReference().child("User"); // database reference
            FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth
            // Create user with email and password
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // User registration successful
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if(firebaseUser!=null){

                                String userId = firebaseUser.getUid();
                                userclass = new UserClass();
                                userclass.setID(userId);
                                userclass.setFirstname(editText_Firstname.getText().toString().trim());
                                userclass.setLastname(editText_Lastname.getText().toString().trim());
                                userclass.setContactNumber(editText_ContactNumber.getText().toString().trim());
                                userclass.setEmail(email); // Use the same email used for registration
                                userclass.setPassword(editText_Password.getText().toString().trim());
                                // Save user data to Realtime Database
                                dbRef.child(userId).setValue(userclass);
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setTitle("Confirmation");
                                builder.setMessage("Sign up successful");
                                builder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int which) {
                                                editText_Firstname.getText().clear();
                                                editText_Lastname.getText().clear();
                                                editText_ContactNumber.getText().clear();
                                                editText_Email.getText().clear();
                                                editText_Password.getText().clear();
                                            }
                                        });
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }

                        }
                        else{
                            // User registration failed

                            try{
                                throw task.getException();
                            }
                            catch (FirebaseAuthUserCollisionException e){
                                    editText_Email.requestFocus();
                                    editText_Email.setError("Email already exist");
                            }
                            catch (Exception ex){
                                Log.e("RegistrationError", "Error: " + task.getException()); // Log the error message
                                Toast.makeText(getApplicationContext(), "Sign up failed", Toast.LENGTH_LONG).show();
                            }
                        }



                }
            });



        }


    }


    //function to show the type password
    public void signUptogglePasswordVisibility(View v) {
        EditText editTextPassword = findViewById(R.id.edittext_password);
        ImageView imageViewToogleShowPass = findViewById(R.id.SignupImageViewPasswordToggle);
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
}