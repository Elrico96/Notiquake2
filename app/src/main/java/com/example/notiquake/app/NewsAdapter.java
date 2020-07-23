package com.example.notiquake.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notiquake.R;
import com.example.notiquake.app.model.News;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter  extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{

    private Context context;
    private List<News> articles;

    public NewsAdapter(Context context, List<News> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_view, parent ,false);
        return new NewsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News currentArticle = articles.get(position);

        String artTitle = currentArticle.getTitle();
        String artAuthor = currentArticle.getAuthor();
        String artDate  = currentArticle.getPublishedDate().substring(0,10);


        final String url = currentArticle.getUrl();
        String imageUrl = currentArticle.getThumbnail();

        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_read)
                .into(holder.imageView);
        holder.title.setText(artTitle);
        holder.author.setText(artAuthor);
        holder.publisedDate.setText(artDate);


        holder.newsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri articleUri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, articleUri);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return articles == null ? 0 : articles.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout newsCardView;
        private ImageView imageView;
        private TextView title;
        private TextView author;
        private TextView publisedDate;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsCardView = itemView.findViewById(R.id.news_cardview);
            imageView = itemView.findViewById(R.id.imgView);
            title = itemView.findViewById(R.id.articleTitle);
            author = itemView.findViewById(R.id.author);
            publisedDate = itemView.findViewById(R.id.publishedDate);
        }
    }
}
