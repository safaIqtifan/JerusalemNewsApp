package com.example.jerusalemnewsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    Context context;
    List<NewsModel> list;

    public NewsAdapter(Context context, List<NewsModel> newsList) {
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

        NewsModel farmModel = list.get(position);

        holder.title.setText(NewsModel.title);
        holder.describction.setText(NewsModel.describction);

        Glide.with(context).asBitmap().load(farmModel.photo).placeholder(R.drawable.camera).into(holder.post_photo);


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
            post_photo = itemView.findViewById(R.id.post_photo);
        }
    }
}