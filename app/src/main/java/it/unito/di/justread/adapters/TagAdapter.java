package it.unito.di.justread.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.unito.di.justread.R;
import it.unito.di.justread.model.Category;

public class TagAdapter extends RecyclerView.Adapter<TagViewHolder>{

    private ArrayList<Category> items;
    private final TagAdapter.TagAdapterOnClickHandler clickHandler;

    public interface TagAdapterOnClickHandler {
        void onClick(View source, int position);
    }

    public TagAdapter(ArrayList<Category> items, TagAdapter.TagAdapterOnClickHandler clickHandler) {
        this.items = items;
        this.clickHandler = clickHandler;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_genre, parent, false);
        return new TagViewHolder(v, clickHandler);
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        holder.bindToView(items.get(position));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
