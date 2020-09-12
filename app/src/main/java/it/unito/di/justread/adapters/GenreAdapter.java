package it.unito.di.justread.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.unito.di.justread.R;
import it.unito.di.justread.model.DefaultBookData;

public class GenreAdapter extends RecyclerView.Adapter<ItemGenreViewHolder>{

    private ArrayList<DefaultBookData> items;
    private final GenreAdapter.GenreItemAdapterOnClickHandler clickHandler;
    private Context context;

    public interface GenreItemAdapterOnClickHandler {
        void onClick(View source, int position);
    }

    public GenreAdapter(ArrayList<DefaultBookData> items, GenreAdapter.GenreItemAdapterOnClickHandler clickHandler, Context context) {
        this.items = items;
        this.clickHandler = clickHandler;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ItemGenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new ItemGenreViewHolder(v, clickHandler);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(ItemGenreViewHolder holder, int position) {
        holder.bindToView(items.get(position), context);
    }
}
