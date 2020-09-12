package it.unito.di.justread.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import it.unito.di.justread.R;
import it.unito.di.justread.model.Category;

class TagViewHolder extends RecyclerView.ViewHolder {

    private TextView genreTag;

    TagViewHolder(View item, final TagAdapter.TagAdapterOnClickHandler handler) {
        super(item);
        genreTag = (TextView) item.findViewById(R.id.tag_genre);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.onClick(view, getAdapterPosition());
            }
        });
    }

    void bindToView(Category item) {
        genreTag.setText(item.getName());
    }
}
