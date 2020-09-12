package it.unito.di.justread.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.unito.di.justread.R;
import it.unito.di.justread.model.BorrowingBookData;

public class BorrowedAdapter extends RecyclerView.Adapter<BorrowedViewHolder> {

    private ArrayList<BorrowingBookData> items;
    private final BorrowedAdapter.BookAdapterOnClickHandler clickHandler;
    private Context context;

    public interface BookAdapterOnClickHandler {
        void onClick(View source, int position);
    }

    public BorrowedAdapter(ArrayList<BorrowingBookData> items, BorrowedAdapter.BookAdapterOnClickHandler clickHandler, Context context) {
        this.items = items;
        this.clickHandler = clickHandler;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public BorrowedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_borrowed, parent, false);
        return new BorrowedViewHolder(v, clickHandler);
    }

    @Override
    public void onBindViewHolder(BorrowedViewHolder holder, int position) {
        holder.bindToView(items.get(position), context);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}