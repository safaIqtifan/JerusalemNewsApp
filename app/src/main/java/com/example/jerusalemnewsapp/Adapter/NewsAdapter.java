package com.example.jerusalemnewsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jerusalemnewsapp.Model.ArticlesModel;
import com.example.jerusalemnewsapp.R;
import com.example.jerusalemnewsapp.RootApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    public Context context;
    public List<ArticlesModel> list;
    private Map<String, ArticlesModel> favMap;
    public boolean isFavorite;

    public NewsAdapter(Context context, List<ArticlesModel> newsList, boolean isFavorite) {
        this.context = context;
        this.list = new ArrayList<>(newsList);
        this.isFavorite = isFavorite;

        favMap = new HashMap<>();
        getLocalFavorite();

    }

    public void getLocalFavorite() {
        favMap.clear();
        RealmResults<ArticlesModel> favModelList = RootApplication.dbRealm.where(ArticlesModel.class).findAll();
        for (ArticlesModel articlesModel : favModelList) {
            favMap.put(articlesModel.publishedAt, articlesModel);
        }
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

        if (favMap.containsKey(articlesModel.publishedAt)) {
            holder.favBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_fill));
        } else {
            holder.favBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_empty));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView title, describction;
        ImageView post_photo;
        ImageView favBtn;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleEd);
            describction = itemView.findViewById(R.id.describctionEd);
            post_photo = itemView.findViewById(R.id.posts_photo);
            favBtn = itemView.findViewById(R.id.favBtn);

            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();
                    ArticlesModel articlesModel = list.get(pos);

                    if (favMap.containsKey(articlesModel.publishedAt)) {
                        // need to delete news from favorite
                        RootApplication.dbRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                ArticlesModel deleteArticle = favMap.get(articlesModel.publishedAt);
                                if (deleteArticle != null) {
                                    deleteArticle.deleteFromRealm();
//                                deleteArtcile.deleteFromRealm();
                                    if (isFavorite) {
                                        list.remove(pos);
                                        notifyItemRemoved(pos);
                                    } else {
                                        favMap.remove(articlesModel.publishedAt);
                                        notifyItemChanged(getAdapterPosition());
                                    }
                                }


                            }
                        });
                    } else {
                        // need to add news to favorite
                        RootApplication.dbRealm.beginTransaction();
                        ArticlesModel addedModel = RootApplication.dbRealm.copyToRealm(articlesModel);
                        RootApplication.dbRealm.commitTransaction();
                        favMap.put(articlesModel.publishedAt, addedModel);
                        notifyItemChanged(getAdapterPosition());
                    }

                }
            });
        }
    }
}
