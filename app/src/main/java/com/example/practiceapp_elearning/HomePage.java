package com.example.practiceapp_elearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //a list to store all the artist from firebase database
    List<Article> articles = new ArrayList<>();
    ListView listViewArticle;
    private FirebaseAuth mAuth;
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
        setContentView(R.layout.activity_home_page);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ImageView imgvLogouBtn = findViewById(R.id.imgLogout);
        sharedReferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        USERID = sharedReferences.getString(USER_KEY, null);
        USEREMAIL = sharedReferences.getString(USER_EMAIL, null);
        USERPASSWORD = sharedReferences.getString(USER_PASSWORD, null);
        listViewArticle = (ListView) findViewById(R.id.HomepageListviewId);
        // on below line creating our database reference.
        databaseReference = firebaseDatabase.getReference("Articles");
        Log.d("TAG", "onCreate: " + databaseReference);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clearing the previous artist list
                articles.clear();
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //getting articleModal
                    Article article = postSnapshot.getValue(Article.class);

                    articles.add(article);


                }
                //creating adapter
                ArticleList artistAdapter = new ArticleList(HomePage.this, articles);
                //attaching adapter to the listview
                listViewArticle.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //if  click a article, a bottom fragment will appear
        listViewArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get the selected article
                Article selectedArticle = articles.get(i);
                // Create and show the bottom sheet
                ArticleBottomSheetFragment bottomSheetFragment = new ArticleBottomSheetFragment(selectedArticle);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });

        //click image then logout
        imgvLogouBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);


            }
        });
    }

    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menuProfile) {
                    Intent i = new Intent(HomePage.this, ProfileActivity.class);
                    startActivity(i);
                    return true;
                } else if (item.getItemId() == R.id.menuLogout) {

                    Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_LONG).show();

                    // on below line we are opening our login activity.

                    mAuth.signOut();
                    Intent i = new Intent(HomePage.this, LoginActivity.class);
                    SharedPreferences.Editor editor = sharedReferences.edit();
                    editor.remove(USER_EMAIL);
                    editor.remove(USER_PASSWORD);
                    editor.apply();
                    i.putExtra("isLogout", true);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }


    public void AddCourse(View v) {
        Intent i;
        i = new Intent(this, activity_add_course.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);

        startActivity(i);
    }



}