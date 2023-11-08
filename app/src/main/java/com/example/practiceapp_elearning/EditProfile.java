package com.example.practiceapp_elearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    public static final String SHARED_PREFS = "shared_prefs";
    // key for storing email.
    public static final String USER_KEY = "userId_key";
    SharedPreferences sharedReferences;
    String USERID;
    DatabaseReference databaseReference;
    String passedFirstName, passedLastName, passedContactnumber, passedEmail, passedPAssword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        sharedReferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // getting data from shared prefs and
        // storing it in our string variable.
        USERID = sharedReferences.getString(USER_KEY, null);
        Log.d("Edit profile Activity", "onComplete: " + USERID);

        //receive by the intent
        Intent intent = getIntent();
        passedFirstName = intent.getStringExtra("passFirstname");
        passedLastName = intent.getStringExtra("passLasttname");
        passedContactnumber = intent.getStringExtra("passContactnumber");
        passedEmail = intent.getStringExtra("passEmail");
        passedPAssword = intent.getStringExtra("passPassword");


        //get the ID from XML
        EditText editTextFn = findViewById(R.id.editProf_edittext_firstname);
        EditText editTextLn = findViewById(R.id.editProf_edittext_lastname);
        EditText editTextEmail = findViewById(R.id.editProf_edittext_email);
        EditText editTextContactNumber = findViewById(R.id.editProf_edittext_contactnumber);
        EditText editTextPassword = findViewById(R.id.editProf_edittext_password);
        Button buttonSaveUpdate = findViewById(R.id.editProfileSavetBtn);

        //assign to edittext, whose value is from the intent
        editTextFn.setText(passedFirstName);
        editTextLn.setText(passedLastName);
        editTextEmail.setText(passedEmail);
        editTextContactNumber.setText(passedContactnumber);
        editTextPassword.setText(passedPAssword);

        //get the text input of the
        buttonSaveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //assign EditText ID to a string
                String newFirstname = editTextFn.getText().toString().trim();
                String newLastname = editTextLn.getText().toString().trim();
                String newEmail = editTextEmail.getText().toString().trim();
                String newContactnumber = editTextContactNumber.getText().toString().trim();
                String newPassword = editTextPassword.getText().toString().trim();
                boolean isUpdated = false;
                if (!passedFirstName.equals(newFirstname)) {
                    isUpdated = true;
                    passedFirstName = newFirstname;
                } else if (!passedLastName.equals(newLastname)) {
                    isUpdated = true;
                    passedLastName = newLastname;
                } else if (!passedEmail.equals(newEmail)) {
                    isUpdated = true;
                   // passedEmail = newEmail;
                } else if (!passedContactnumber.equals(newContactnumber)) {
                    isUpdated = true;
                    passedContactnumber = newContactnumber;
                } else if (!passedPAssword.equals(newPassword)) {
                    isUpdated = true;
                    passedPAssword = newPassword;
                }


                if (isUpdated) {
                    UpdateUser(USERID, newFirstname, newLastname, newEmail, newContactnumber, newPassword);
                } else {
                    Toast.makeText(EditProfile.this, "Data is same and cannot be updated", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private boolean UpdateUser(String userID, String newFirstname, String newLastname, String newEmail, String newContactnumber, String newPassword) {
        //reference to the USER collection
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userID);

        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(passedEmail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if(task.isSuccessful()){
                    SignInMethodQueryResult result = task.getResult();
                    if (result != null && result.getSignInMethods() != null && !result.getSignInMethods().isEmpty()) {
                        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

                        AuthCredential credential= EmailAuthProvider.getCredential(passedEmail,passedPAssword);
                        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                                    firebaseUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //Log.d("onComplete update email", "User email address updated.");
                                           passedEmail=newEmail;
                                           // Toast.makeText(EditProfile.this, "", Toast.LENGTH_SHORT).show();
                                            Map<String, Object> updateData = new HashMap<>();
                                            updateData.put("contactNumber", newContactnumber);
                                            updateData.put("email", newEmail);
                                            updateData.put("firstname", newFirstname);
                                            updateData.put("lastname", newLastname);
                                            updateData.put("password", newPassword);
                                            databaseReference.updateChildren(updateData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(getApplicationContext(), "User profile updated", Toast.LENGTH_LONG).show();
                                                    new android.os.Handler().postDelayed(new Runnable() {
                                                        public void run() {

                                                            Intent intent;
                                                            intent = new Intent(EditProfile.this, ProfileActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    }, 1500);
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
                    }
                    else {
                        // User does not exist, handle the case accordingly (e.g., show an error message).
                        Toast.makeText(getApplicationContext(), "User does not exist.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    // Handle the case where checking user existence fails.
                    Log.e("User Existence Check", "onComplete: ", task.getException());
                }


            }
        });


        return true;
    }

    //function in updating user information in DB

}