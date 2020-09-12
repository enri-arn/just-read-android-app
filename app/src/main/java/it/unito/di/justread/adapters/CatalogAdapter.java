package it.unito.di.justread.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.unito.di.justread.R;
import it.unito.di.justread.model.Category;

public class CatalogAdapter extends RecyclerView.Adapter<GenreViewHolder> {

    private ArrayList<Category> items;
    private final BookAdapterOnClickHandler clickHandler;
    private Context context;

    public interface BookAdapterOnClickHandler {
        void onClick(View source, int position);
    }

    public CatalogAdapter(ArrayList<Category> items, BookAdapterOnClickHandler clickHandler, Context context) {
        this.items = items;
        this.clickHandler = clickHandler;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public GenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catalog, parent, false);
        return new GenreViewHolder(v, clickHandler);
    }

    @Override
    public void onBindViewHolder(GenreViewHolder holder, int position) {
        holder.bindToView(items.get(position), context);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
