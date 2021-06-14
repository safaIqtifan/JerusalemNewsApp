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
import com.example.jerusalemnewsapp.Model.PostModel;
import com.example.jerusalemnewsapp.R;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ItemViewHolder> {

    public Context context;
    public List<PostModel> list;

    public PostsAdapter(Context context, List<PostModel> newsList) {
        this.context = context;
        this.list = newsList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemViewHolder viewHolder = new ItemViewHolder(inflater.inflate(R.layout.item_news_post, parent, false));

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        PostModel postModel = list.get(position);

        holder.title.setText(postModel.title);
        holder.describction.setText(postModel.description);

        Glide.with(context).asBitmap().load(postModel.photo).placeholder(R.drawable.camera).into(holder.post_photo);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView title, describction;
        ImageView post_photo;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleEd);
            describction = itemView.findViewById(R.id.describctionEd);
            post_photo = itemView.findViewById(R.id.posts_photo);
        }
    }
}
