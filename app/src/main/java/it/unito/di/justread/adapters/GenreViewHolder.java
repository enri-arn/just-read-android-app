package it.unito.di.justread.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import it.unito.di.justread.R;
import it.unito.di.justread.model.Category;

class GenreViewHolder extends RecyclerView.ViewHolder {

    private TextView genreTitle;
    private ImageView genreMainIcon;

    GenreViewHolder(View item, final CatalogAdapter.BookAdapterOnClickHandler handler) {
        super(item);
        genreTitle = item.findViewById(R.id.genre_title);
        genreMainIcon = item.findViewById(R.id.item_catalog_icon);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.onClick(view, getAdapterPosition());
            }
        });
    }

    void bindToView(Category item, Context context) {
        genreTitle.setText(item.getName());
        byte[] decodedString = Base64.decode(item.getIcon(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        genreMainIcon.setImageBitmap(decodedByte);
    }
}
