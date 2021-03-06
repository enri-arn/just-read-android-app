package it.unito.di.justread.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.unito.di.justread.R;
import it.unito.di.justread.model.DefaultBookData;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteViewHolder> {

    private ArrayList<DefaultBookData> items;
    private Context context;
    private final FavouriteAdapter.BookAdapterOnClickHandler clickHandler;

    public interface BookAdapterOnClickHandler {
        void onClick(View source, int position);
    }

    public FavouriteAdapter(ArrayList<DefaultBookData> items, FavouriteAdapter.BookAdapterOnClickHandler clickHandler, Context context) {
        this.items = items;
        this.clickHandler = clickHandler;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favourite, parent, false);
        return new FavouriteViewHolder(v, clickHandler);
    }

    @Override
    public void onBindViewHolder(FavouriteViewHolder holder, int position) {
        holder.bindToView(items.get(position), context);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}