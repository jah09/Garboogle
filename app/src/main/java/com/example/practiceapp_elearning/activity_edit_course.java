package com.example.practiceapp_elearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_edit_course extends AppCompatActivity {
    private Article articles;
    EditText eTArticlename;
    EditText eTArticletype;
    EditText eTArticleImageLink;
    EditText eTArticleArticlelink;
    EditText eTArticleDescription;

    String articleName, articleType, articleDescription, articleLink, articleImageLink, articleID;
    DatabaseReference databaseReference;

    DatabaseReference articleRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
//        firebaseDatabase = FirebaseDatabase.getInstance();
        // databaseReference =  FirebaseDatabase.getInstance().getReference("Articles");
//        articleRef=databaseReference.child(articleID);firebaseDatabase.getReference("Articles");
        //Declare the variable Edit_UpdateArticleBtn
        eTArticlename = findViewById(R.id.edittext_Edit_articleName);
        eTArticletype = findViewById(R.id.edittext_Edit_articleType);
        eTArticleImageLink = findViewById(R.id.edittext_Edit_articleImageLink);
        eTArticleArticlelink = findViewById(R.id.edittext_Edit_articleLink);
        eTArticleDescription = findViewById(R.id.edittext_Edit_articleDescription);
        Button btnUpdate = findViewById(R.id.Edit_UpdateArticleBtn);
        Button btnDeleteArticle=findViewById(R.id.DeleteArticleBtn);
        // Retrieve the article data from the Intent extras
        Intent intent = getIntent();
        articleName = intent.getStringExtra("articleName");
        articleType = intent.getStringExtra("articleType");
        articleDescription = intent.getStringExtra("articleDescription");
        articleLink = intent.getStringExtra("articleLink");
        articleImageLink = intent.getStringExtra("articleImageLink");
        articleID = intent.getStringExtra("articleID");

        eTArticlename.setText(articleName);
        eTArticletype.setText(articleType);
        eTArticleImageLink.setText(articleImageLink);
        eTArticleArticlelink.setText(articleLink);
        eTArticleDescription.setText(articleDescription);

        //listener for updating the article
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String article_name = eTArticlename.getText().toString().trim();
                String article_type = eTArticletype.getText().toString().trim();
                String article_link = eTArticleArticlelink.getText().toString().trim();
                String article_imagelink = eTArticleImageLink.getText().toString().trim();
                String article_description = eTArticleDescription.getText().toString().trim();
//             if(!TextUtils.isEmpty(article_name)&&!TextUtils.isEmpty(article_type)&&!TextUtils.isEmpty(article_link)&&!TextUtils.isEmpty(article_imagelink)&&!TextUtils.isEmpty(article_description)){
//
//             }
                boolean isUpdated = false;
                if (!articleName.equals(article_name)) {
                    isUpdated = true;
                    articleName = article_name;
                } else if (!articleType.equals(article_type)) {
                    isUpdated = true;
                    articleType = article_type;

                } else if (!articleLink.equals(article_link)) {
                    isUpdated = true;
                    articleLink = article_link;

                } else if (!articleImageLink.equals(article_imagelink)) {
                    isUpdated = true;
                    articleLink = article_imagelink;

                } else if (!articleDescription.equals(article_description)) {
                    isUpdated = true;
                    articleDescription = article_description;

                }
                if (isUpdated) {
                    updateArtist(articleID, article_name, article_type, article_link, article_imagelink, article_description);
                } else {
                    Toast.makeText(activity_edit_course.this, "Data is same and cannot be updated", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //listener for deleting the article
        btnDeleteArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteArticle(articleID);
            }
        });
    }

    private boolean updateArtist(String id, String Articlename, String Articletype, String Articlelink, String ArticleImagelink, String Articledescription) {
        //getting the specified artist reference
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Articles").child(id);


        // Creating a map to update specific fields
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("articleName", Articlename);
        updateData.put("articleType", Articletype);
        updateData.put("articleLink", Articlelink);
        updateData.put("articleImageLink", ArticleImagelink);
        updateData.put("articleDescription", Articledescription);
        databaseRef.updateChildren(updateData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Article Updated", Toast.LENGTH_LONG).show();
                new android.os.Handler().postDelayed(new Runnable() {
                    public void run() {

                        Intent intent;
                        intent = new Intent(activity_edit_course.this, HomePage.class);
                        startActivity(intent);
                    }
                }, 1500);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to update
                Toast.makeText(getApplicationContext(), "Failed to update article", Toast.LENGTH_LONG).show();
            }
        });
        return true;
    }

    //deleting a article
    private  boolean deleteArticle(String articleId){
        //getting the specified artist reference
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Articles").child(articleId);

        //removing artist
        databaseRef.removeValue();


        Toast.makeText(getApplicationContext(), "Artist Deleted", Toast.LENGTH_LONG).show();
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {

                Intent intent;
                intent = new Intent(activity_edit_course.this, HomePage.class);
                startActivity(intent);
            }
        }, 1500);
        return true;
    }


}