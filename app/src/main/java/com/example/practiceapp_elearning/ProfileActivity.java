package com.example.practiceapp_elearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.practiceapp_elearning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";
    // key for storing email.
    public static final String USER_KEY = "userId_key";
    SharedPreferences sharedReferences;
    String USERID, toPassFirstname, toPassLasttname, toPassEmail, toPassContactNumber, toPassPassword;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    //call the Article Model


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //db
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");

        // initializing our shared preferences.
        sharedReferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // getting data from shared prefs and
        // storing it in our string variable.
        USERID = sharedReferences.getString(USER_KEY, null);
        Log.d("Profile Activity", "onComplete: " + USERID);
        //go to User collection and find the userID na mo match sa USERID and get the all data

        //TextView
        TextView profTxtViewFirstname = findViewById(R.id.prof_txtViewFirstname);
        EditText editTextCompletename = findViewById(R.id.editText_completeName);

        EditText editTextEmail = findViewById(R.id.editText_email);
        EditText editTextContactnumber = findViewById(R.id.editText_contactnumber);

        Button updateprofilebtn = findViewById(R.id.profileUpdateBtn);

        if (USERID != null) {
            databaseReference.orderByChild("id").equalTo(USERID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapShot : snapshot.getChildren()) {
                            UserClass userClass = userSnapShot.getValue(UserClass.class);
                            if (userClass != null) {


                                //assign the fetch data
                                toPassFirstname = userClass.getFirstname();
                                toPassLasttname = userClass.getLastname();
                                toPassEmail = userClass.getEmail();
                                toPassContactNumber = userClass.getContactNumber();
                                toPassPassword = userClass.getPassword();
                               // Log.d("TAG", "User data retrieved: " +toPassFirstname+toPassPassword+toPassLasttname+toPassEmail+toPassContactNumber);

                                // Set the first name in your TextView
                                profTxtViewFirstname.setText("Welcome, " + userClass.getFirstname()+"!");
                                editTextCompletename.setText(userClass.getFirstname() + " " + userClass.getLastname());
                                editTextEmail.setText(userClass.getEmail());
                                editTextContactnumber.setText(userClass.getContactNumber());


                                //set to color black
                                editTextContactnumber.setTextColor(Color.BLACK);
                                editTextCompletename.setTextColor(Color.BLACK);
                                editTextEmail.setTextColor(Color.BLACK);
                            } else {
                                Log.d("TAG", "User data is null");
                            }
                        }
                    } else {
                        // Handle the case where no user with USERID is found
                        Log.d("TAG", "No user found with USERID: " + USERID);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        //click the update button and move to another screen
        updateprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;
                intent = new Intent(ProfileActivity.this, EditProfile.class);
                intent.putExtra("USERID", USERID);
                intent.putExtra("passFirstname", toPassFirstname);
                intent.putExtra("passLasttname", toPassLasttname);
                intent.putExtra("passEmail", toPassEmail);
                intent.putExtra("passContactnumber", toPassContactNumber);
                intent.putExtra("passPassword", toPassPassword);
                startActivity(intent);
            }
        });


    }
}