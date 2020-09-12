package it.unito.di.justread.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import it.unito.di.justread.R;
import it.unito.di.justread.model.ReviewsData;

class ReviewViewHolder extends RecyclerView.ViewHolder{

    private ImageView userProfileImage;
    private TextView userName;
    private RatingBar userRate;
    private TextView userReview;

    public ReviewViewHolder(View itemView) {
        super(itemView);
        userProfileImage = (ImageView)itemView.findViewById(R.id.profile_image_user_review);
        userName = (TextView)itemView.findViewById(R.id.user_name_review);
        userRate = (RatingBar)itemView.findViewById(R.id.ratingBar_item_review);
        userReview = (TextView)itemView.findViewById(R.id.user_review);
    }

    void bindToView(ReviewsData item, Context context){
        //Picasso.with(context).load(item.getProfileImage()).fit().transform((new CircleTransform())).centerCrop().into(userProfileImage);
        userName.setText(item.getName());
        userReview.setText(item.getComment());
    }
}
