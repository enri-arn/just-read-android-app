package it.unito.di.justread.adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import it.unito.di.justread.R;
import it.unito.di.justread.asynctasks.GetJsonTask;
import it.unito.di.justread.asynctasks.TaskCompletionListener;
import it.unito.di.justread.model.BorrowingBookData;

public class BorrowedViewHolder extends RecyclerView.ViewHolder{


    private String DELIVER_BOOK = "http://192.168.0.1:8080/JustRead/api/borrowing/";
    private String INCREASELOAN = "http://192.168.0.1:8080/JustRead/api/borrowing/increase/";

    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView endBorrowDate;
    private ImageView itemBookCover;
    private ProgressBar endBorrowProgress;
    private FloatingActionButton fabDeliveryItem;
    private FloatingActionButton fabRefreshItem;

    BorrowedViewHolder(View item, final BorrowedAdapter.BookAdapterOnClickHandler handler){
        super(item);
        bookTitle = item.findViewById(R.id.item_book_title_borrowed);
        bookAuthor = item.findViewById(R.id.item_book_author_borrowed);
        endBorrowDate = item.findViewById(R.id.item_book_end_date);
        itemBookCover = item.findViewById(R.id.item_book_cover_borrowed);
        endBorrowProgress = item.findViewById(R.id.progressbar_end_date);
        endBorrowProgress.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        fabDeliveryItem = item.findViewById(R.id.fab_delivery_item);
        fabRefreshItem = item.findViewById(R.id.fab_refresh_item);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.onClick(view, getAdapterPosition());
            }
        });
    }

    void bindToView(final BorrowingBookData item, Context context){
        bookTitle.setText(item.getTitle());
        bookAuthor.setText(item.getAuthor());
        Picasso.with(context).load(item.getCover()).fit().centerCrop().into(itemBookCover);
        endBorrowDate.setText(item.getDates());
        endBorrowProgress.setProgress(item.getProgress());
        fabDeliveryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliverBorrow(view, item.getIsbn());
            }
        });
        fabRefreshItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseLoan(view, item.getIsbn());
            }
        });
    }

    private void deliverBorrow(final View view, String isbn){
        String deliverBook = DELIVER_BOOK + isbn;
        final GetJsonTask bookedTask = new GetJsonTask("DELETE", new TaskCompletionListener<String>() {
            @Override
            public void onComplete(String result) {
                if (result.equalsIgnoreCase("true")) {
                    Snackbar.make(view, "Booked book cancelled", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        bookedTask.execute(deliverBook);
    }

    private void increaseLoan(final View view, String isbn){
        String increaseUrl = INCREASELOAN + isbn;
        GetJsonTask increaseLoan = new GetJsonTask("PUT", new TaskCompletionListener<String>() {
            @Override
            public void onComplete(String result) {
                if (result.equalsIgnoreCase("true")) {
                    Snackbar.make(view, "Book will be increase", Snackbar.LENGTH_LONG).show();
                } else {
                    String text = "Operation wasn't successful, we invite you to try again";
                    Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
                }
            }
        });
        increaseLoan.execute(increaseUrl);
    }
}
