package com.example.practiceapp_elearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class activity_add_course extends AppCompatActivity {

    //variable declaration
    Button addArticleButton;
    EditText article_name, article_type, article_ImageLink, article_link, article_description;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        // adding click listener for our add course button.


    }

    public void AddArticlebtn(View view) {
        String unique_ArticleId = UUID.randomUUID().toString();
        // initializing all our variables.
        addArticleButton = findViewById(R.id.AddArticleBtn);
        article_name = findViewById(R.id.edittext_articleName);
        article_type = findViewById(R.id.edittext_articleType);
        article_ImageLink = findViewById(R.id.edittext_articleImageLink);
        article_link = findViewById(R.id.edittext_articleLink);
        article_description = findViewById(R.id.edittext_articleDescription);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // on below line creating our database reference.
        databaseReference = firebaseDatabase.getReference("Articles");
        addArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting data from our edit text.
                String ArticleName = article_name.getText().toString();
                String ArticleType = article_type.getText().toString();
                String ArticleImageLink = article_ImageLink.getText().toString();
                String ArticleLink = article_link.getText().toString();
                String ArticleDescription = article_description.getText().toString();
                courseID = unique_ArticleId;
                //  ArticleModal articleModal = new ArticleModal(ArticleName, ArticleType, ArticleImageLink, ArticleLink, ArticleDescription, courseID);
                Article article = new Article();
                try {
                    if (article_name.getText().toString().isEmpty()) {
                        article_name.requestFocus();
                        article_name.setError("Enter article name");
                    } else if (article_type.getText().toString().isEmpty()) {
                        article_type.requestFocus();
                        article_type.setError("Enter article type");
                    } else if (article_link.getText().toString().isEmpty()) {
                        article_link.requestFocus();
                        article_link.setError("Enter article link");
                    } else if (article_description.getText().toString().isEmpty()) {
                        article_description.requestFocus();
                        article_description.setError("Enter article link");
                    } else {
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //insert to database
                                article.setArticleName(ArticleName);
                                article.setArticleType(ArticleType);
                                article.setArticleImageLink(ArticleImageLink);
                                article.setArticleLink(ArticleLink);
                                article.setArticleDescription(ArticleDescription);
                                article.setArticleID(courseID);
                                databaseReference.child(courseID).setValue(article);

                                // displaying a toast message.
                                Toast.makeText(activity_add_course.this, "Article Added", Toast.LENGTH_LONG).show();
                                new android.os.Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        article_name.getText().clear();
                                        article_type.getText().clear();
                                        article_ImageLink.getText().clear();
                                        article_link.getText().clear();
                                        article_description.getText().clear();
                                        Intent intent;
                                        intent=new Intent(activity_add_course.this, HomePage.class);
                                        startActivity(intent);
                                    }
                                }, 1500);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // displaying a failure message on below line.
                                Toast.makeText(activity_add_course.this, "Fail to add Article", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                } catch (Exception ex) {

                }

            }
        });


    }

}