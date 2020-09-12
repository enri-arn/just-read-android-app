package it.unito.di.justread.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.unito.di.justread.R;
import it.unito.di.justread.model.ReviewsData;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder>{

    private ArrayList<ReviewsData> items;
    private Context context;

    public ReviewAdapter(ArrayList<ReviewsData> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bindToView(items.get(position), context);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
