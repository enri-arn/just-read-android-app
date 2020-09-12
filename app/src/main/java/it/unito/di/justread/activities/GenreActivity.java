package it.unito.di.justread.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class GenreActivity extends AppCompatActivity implements GenreAdapter.GenreItemAdapterOnClickHandler, TaskCompletionListener<String>, SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<DefaultBookData> books;
    private GenreAdapter adapterGenre;
    private ProgressBar progressBar;

    private String URL = "http://192.168.0.1:8080/JustRead/api/catalog/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String title = null;
        if (bundle != null) {
            title = bundle.getString("title");
        }
        this.setTitle(title);
        URL += title;

        books = new ArrayList<>();
        GetJsonTask task = new GetJsonTask("GET", this);
        task.execute(URL);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView genreRecyclerView = findViewById(R.id.genre_books_recyclerview);
        genreRecyclerView.setHasFixedSize(true);
        progressBar = findViewById(R.id.wait_progressbar_genre);
        SwipeRefreshLayout refreshLayout = findViewById(R.id.genre_refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.blue, R.color.purple, R.color.green, R.color.orange);
        refreshLayout.setOnRefreshListener(this);

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
                Log.i("EXCEPTION", "genre books =  " + books.toString());
                progressBar.setVisibility(View.INVISIBLE);
                adapterGenre.notifyDataSetChanged();
                Log.i("EXCEPTION", "Notify fatta!");
            } catch (Exception e) {
                Log.i("EXCEPTION", "Error parsing json");
            }
        } else {
            Intent goToOption = new Intent(getApplicationContext(), ServerOfflineActivity.class);
            startActivity(goToOption);
            finish();
        }
    }

    @Override
    public void onRefresh() {
        adapterGenre.notifyDataSetChanged();
        Snackbar.make(getCurrentFocus(), "Catalog refresh", Snackbar.LENGTH_SHORT).show();
    }
}
