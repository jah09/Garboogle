package com.example.practiceapp_elearning;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;


public class ArticleList extends ArrayAdapter<Article> {
    private Activity context;
    List<Article> articles;
    public ArticleList(Activity context, List<Article> articles) {
        super(context, R.layout.course_rv_item, articles);
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem=inflater.inflate(R.layout.course_rv_item, null, true);
        TextView textViewArticleName = (TextView) listViewItem.findViewById(R.id.idTVArticleName);
        TextView textViewArticleType = (TextView) listViewItem.findViewById(R.id.idTVArticleType);
        ImageView imageViewArticleImage=(ImageView) listViewItem.findViewById(R.id.idIVArticleImage);

        Article articleModal=articles.get(position); //instiate the articleModal or Model
        textViewArticleName.setText(articleModal.getArticleName());
        textViewArticleType.setText(articleModal.getArticleType());
        // Use Picasso to load and display the image from the URL
        Picasso.get().load(articleModal.getArticleImageLink()).into(imageViewArticleImage);
      //  imageViewArticleImage.setImageResource(articleModal.getArticleImageLink());
      //  return super.getView(position, convertView, parent);
        return listViewItem;
    }
}
