package com.example.practiceapp_elearning;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleBottomSheetFragment extends BottomSheetDialogFragment {
    private Article selectedArticle;

    public ArticleBottomSheetFragment(Article article) {

        this.selectedArticle = article;
    }
    public ArticleBottomSheetFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        // Populate views with data from the selected article
        TextView textviewArticleName = view.findViewById(R.id.textView_bottom_ArticleName);
        TextView textviewArticleType = view.findViewById(R.id.textView_bottom_ArticleType);
        TextView textviewArticleDescription = view.findViewById(R.id.textView_bottom_ArticleDescription);
        ImageView imageViewArticleImage=view.findViewById(R.id.bottom_ArticleImage);
        Button buttonClick_viewArticleLink = view.findViewById(R.id.idBtnViewDetails); //btn for view link
        Button buttonClick_editArticleDetails = view.findViewById(R.id.idBtnUpdateArticle);
        textviewArticleName.setText(selectedArticle.getArticleName());
        textviewArticleType.setText(selectedArticle.getArticleType());
        textviewArticleDescription.setText(selectedArticle.getArticleDescription());
        Picasso.get().load(selectedArticle.getArticleImageLink()).into(imageViewArticleImage);
        //listener for the VIEW DETAILS BUTTON OR VIEW LINK and if click, it will navigate to external app
        buttonClick_viewArticleLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the article link from the selected article
                String articleLink = selectedArticle.getArticleLink();
                // String articleLink = "https://www.google.com/";
                // Check if the article link is not empty or null
                if (articleLink != null && !articleLink.isEmpty()) {
                    // Create an intent to open the link in a web browser
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(articleLink));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    // Handle the case where the article link is empty or null (e.g., show a message)
                    Toast.makeText(getActivity(), "No article link available.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //listener for the EDIT DETAILS BUTTOn
        buttonClick_editArticleDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(view.getContext(), activity_edit_course.class);
                // Add the selected article's data as extras
                intent.putExtra("articleName", selectedArticle.getArticleName());
                intent.putExtra("articleType", selectedArticle.getArticleType());
                intent.putExtra("articleDescription", selectedArticle.getArticleDescription());
                intent.putExtra("articleLink", selectedArticle.getArticleLink());
                intent.putExtra("articleImageLink", selectedArticle.getArticleImageLink());
                intent.putExtra("articleID", selectedArticle.getArticleID());
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }
}
