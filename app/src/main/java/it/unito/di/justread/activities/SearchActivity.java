package it.unito.di.justread.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import it.unito.di.justread.R;
import it.unito.di.justread.adapters.GenreAdapter;
import it.unito.di.justread.asynctasks.GetJsonTask;
import it.unito.di.justread.asynctasks.TaskCompletionListener;
import it.unito.di.justread.model.DefaultBookData;

public class SearchActivity extends AppCompatActivity implements GenreAdapter.GenreItemAdapterOnClickHandler, TaskCompletionListener<String> {

    private String URL = "http://192.168.0.1:8080/JustRead/api/search";

    private ArrayList<DefaultBookData> books;
    private GenreAdapter adapterGenre;
    private ProgressBar progressBar;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Log.i("EXCEPTION", "Url della serach =  " + URL);
        handleIntent(getIntent());
        Log.i("EXCEPTION", "Url della serach =  " + URL);

        books = new ArrayList<>();
        GetJsonTask task = new GetJsonTask("GET", this);
        task.execute(URL);

        RecyclerView genreRecyclerView = findViewById(R.id.search_books_recyclerview);
        genreRecyclerView.setHasFixedSize(true);
        progressBar = findViewById(R.id.wait_progressbar_search);

        float metrics = getResources().getDisplayMetrics().density;

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            genreRecyclerView.setLayoutManager(layoutManager);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && metrics >= 1.0f){
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            genreRecyclerView.setLayoutManager(layoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            genreRecyclerView.setLayoutManager(layoutManager);
        }

        adapterGenre = new GenreAdapter(books, this, getApplicationContext());
        genreRecyclerView.setAdapter(adapterGenre);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            URL += "search?=" + query;
        }
    }

    @Override
    public void onClick(View source, int position) {
        Intent intent = new Intent(source.getContext(), BookActivity.class);
        intent.putExtra("isbn", books.get(position).getIsbn());
        startActivity(intent);
    }

    @Override
    public void onComplete(String result) {
        if (result != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<DefaultBookData> defaultBookDataList = mapper.readValue(result, new TypeReference<List<DefaultBookData>>(){});
                books.addAll(defaultBookDataList);
                Log.i("EXCEPTION", "search books =  " + books.toString());
                progressBar.setVisibility(View.INVISIBLE);
                adapterGenre.notifyDataSetChanged();
            } catch (Exception e) {
                Log.i("EXCEPTION", "Error parsing json");
            }
        }
    }
}
