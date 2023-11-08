package com.example.practiceapp_elearning;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        Button btnlogin=findViewById(R.id.loginButton);
        String defaultUsername="admin";
        String defaultPassword="admin";
        EditText editTextUsername=findViewById(R.id.username);
        EditText editTextPassword=findViewById(R.id.password);
        TextView txtviewShowpassword=findViewById(R.id.textViewShowpassword);
        txtviewShowpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextUsername.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewMainActivity.this);
                    builder.setTitle("Warning");
                    builder.setMessage("Please enter username or password");
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if(!editTextUsername.getText().toString().equals(defaultUsername) || !editTextPassword.getText().toString().equals(defaultPassword)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewMainActivity.this);
                    builder.setTitle("Warning");
                    builder.setMessage("Invalid username or password");
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewMainActivity.this);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Login Successfully");
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}