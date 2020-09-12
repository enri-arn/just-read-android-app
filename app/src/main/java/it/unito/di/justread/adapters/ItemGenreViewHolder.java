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

public class ItemGenreViewHolder extends RecyclerView.ViewHolder {


    private String BOOKED_URL = "http://192.168.0.1:8080/JustRead/api/borrowing/book/";

    private ImageView bookCover;
    private TextView bookTitle;
    private TextView bookAuthor;
    private RatingBar ratingBar;
    private FloatingActionButton fabBookItem;
    private FloatingActionButton fabSendHomeItem;

    public ItemGenreViewHolder(View itemView, final GenreAdapter.GenreItemAdapterOnClickHandler handler) {
        super(itemView);
        bookCover = itemView.findViewById(R.id.item_book_cover_genre);
        bookTitle = itemView.findViewById(R.id.item_book_title);
        bookAuthor = itemView.findViewById(R.id.item_book_author);
        ratingBar = itemView.findViewById(R.id.ratingBar_item_genre);
        fabBookItem = itemView.findViewById(R.id.fab_book_item);
        fabSendHomeItem = itemView.findViewById(R.id.fab_send_home_item);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.onClick(view, getAdapterPosition());
            }
        });
    }

    void bindToView(final DefaultBookData item, Context context) {
        bookTitle.setText(item.getTitle());
        bookAuthor.setText(item.getAuthor());
        Picasso.with(context).load(item.getCover()).fit().centerCrop().into(bookCover);
        ratingBar.setRating(item.getRate());
        fabBookItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookTheBook(0, view, item.getIsbn());
            }
        });
        fabSendHomeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookTheBook(1, view, item.getIsbn());
            }
        });
    }

    private void bookTheBook(final int sentHome, final View view, String isbn){
        String bookedurl = BOOKED_URL + isbn;
        final GetJsonTask bookedTask = new GetJsonTask( "PUT" , new TaskCompletionListener<String>() {
            @Override
            public void onComplete(String result) {
                if (result.equalsIgnoreCase("true")) {
                    String text = sentHome == 0 ? "Booked book" : "Book will  be sent home";
                    Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
                } else {
                    String text = "Book is not avaiable now";
                    Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
                }
            }
        });
        bookedTask.execute(bookedurl);
    }
}
