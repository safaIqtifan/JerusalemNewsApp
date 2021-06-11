package com.example.jerusalemnewsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jerusalemnewsapp.Model.ArticlesModel;
import com.example.jerusalemnewsapp.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    public Context context;
    public List<ArticlesModel> list;

    public NewsAdapter(Context context, List<ArticlesModel> newsList) {
        this.context = context;
        this.list = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        NewsViewHolder viewHolder = new NewsViewHolder(inflater.inflate(R.layout.item_news_post, parent, false));

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

        ArticlesModel articlesModel = list.get(position);

        holder.title.setText(articlesModel.title);
        holder.describction.setText(articlesModel.description);

        Glide.with(context).asBitmap().load(articlesModel.urlToImage).placeholder(R.drawable.camera).into(holder.post_photo);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView title, describction;
        ImageView post_photo;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleEd);
            describction = itemView.findViewById(R.id.describctionEd);
            post_photo = itemView.findViewById(R.id.posts_photo);
        }
    }
}
