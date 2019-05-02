package com.udemyandroid.minitwitter.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.udemyandroid.minitwitter.R;
import com.udemyandroid.minitwitter.common.Constantes;
import com.udemyandroid.minitwitter.common.SharedPreferencesManager;
import com.udemyandroid.minitwitter.retrofit.response.Like;
import com.udemyandroid.minitwitter.retrofit.response.Tweet;

import java.util.List;

public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {

    private List<Tweet> mValues;
    private Context ctx;
    String username;


    public MyTweetRecyclerViewAdapter(Context context, List<Tweet> items) {
        ctx = context;
        mValues = items;
        username = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USERNAME);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mValues != null) {
            holder.mItem = mValues.get(position);

            holder.tvUsername.setText(holder.mItem.getUser().getUsername());
            holder.tvMessage.setText(holder.mItem.getMensaje());
            holder.tvLikeCount.setText(String.valueOf(holder.mItem.getLikes().size()));

            String photo = holder.mItem.getUser().getPhotoUrl();
            if (!photo.equals("")) {
                Glide.with(ctx)
                        .load("https://www.minitwitter.com/apiv1/uploads/photos/" + photo)
                        .error(R.drawable.ic_like)
                        .into(holder.ivAvatar);
            }

            for (Like like : holder.mItem.getLikes()) {
                if (like.getUsername().equals(username)) {
                    Glide.with(ctx)
                            .load(R.drawable.ic_like_pink)
                            .error(R.drawable.ic_like)
                            .into(holder.ivLike);
                    holder.tvLikeCount.setTextColor(ctx.getResources().getColor(R.color.pink));
                    holder.tvLikeCount.setTypeface(null, Typeface.BOLD);
                    break;
                }
            }
        }
    }

    public void setData(List<Tweet> tweetList){
        this.mValues = tweetList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mValues != null){
            return mValues.size();
        }else{
            return 0;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView ivAvatar;
        public final ImageView ivLike;
        public final TextView tvUsername;
        public final TextView tvMessage;
        public final TextView tvLikeCount;
        public Tweet mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivAvatar = view.findViewById(R.id.imageViewAvatar);
            ivLike = view.findViewById(R.id.imageViewLike);
            tvUsername = view.findViewById(R.id.textViewUsername);
            tvMessage = view.findViewById(R.id.textViewMessage);
            tvLikeCount = view.findViewById(R.id.textViewLikeCounts);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvUsername.getText() + "'";
        }
    }
}
