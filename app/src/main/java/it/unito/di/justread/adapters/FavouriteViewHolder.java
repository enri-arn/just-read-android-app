package it.unito.di.justread.adapters;


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import it.unito.di.justread.R;
import it.unito.di.justread.asynctasks.GetJsonTask;
import it.unito.di.justread.asynctasks.TaskCompletionListener;
import it.unito.di.justread.model.DefaultBookData;

public class FavouriteViewHolder extends RecyclerView.ViewHolder{

    private static final String FAVOURITE_URL = "http://192.168.0.1:8080/JustRead/api/favourites/";
    private ImageView bookCover;
    private TextView bookTitle;
    private TextView bookAuthor;
    private RatingBar rating;
    private FloatingActionButton fabFavouriteItem;

    FavouriteViewHolder(View item, final FavouriteAdapter.BookAdapterOnClickHandler handler){
        super(item);
        bookCover = itemView.findViewById(R.id.item_book_cover_favourite);
        bookTitle = itemView.findViewById(R.id.item_book_title_favourite);
        bookAuthor = itemView.findViewById(R.id.item_book_author_favourite);
        rating = itemView.findViewById(R.id.ratingBar_item_favourite);
        fabFavouriteItem = item.findViewById(R.id.fab_favourite_item);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.onClick(view, getAdapterPosition());
            }
        });
    }

    void bindToView(final DefaultBookData item, Context context){
        bookTitle.setText(item.getTitle());
        bookAuthor.setText(item.getAuthor());
        rating.setRating(item.getRate());
        Picasso.with(context).load(item.getCover()).fit().centerCrop().into(bookCover);
        fabFavouriteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToFavourite(item.getIsbn(), view);
            }
        });
    }

    private void addToFavourite(String isbn, final View view){
        String favouriteUrl = FAVOURITE_URL + isbn;
        GetJsonTask addToFavourite = new GetJsonTask("DELETE" ,new TaskCompletionListener<String>() {
            @Override
            public void onComplete(final String result) {
                if (result.equalsIgnoreCase("true")){
                    Snackbar.make(view, "Book deleted to your favourite list", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(view, "Book has not been deleted, we invite you to try again", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        addToFavourite.execute(favouriteUrl);
    }
}