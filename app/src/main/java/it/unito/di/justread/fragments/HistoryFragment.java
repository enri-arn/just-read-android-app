package it.unito.di.justread.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.unito.di.justread.R;
import it.unito.di.justread.activities.BookActivity;
import it.unito.di.justread.activities.ServerOfflineActivity;
import it.unito.di.justread.adapters.GenreAdapter;
import it.unito.di.justread.asynctasks.GetJsonTask;
import it.unito.di.justread.asynctasks.TaskCompletionListener;
import it.unito.di.justread.model.DefaultBookData;

public class HistoryFragment extends Fragment implements GenreAdapter.GenreItemAdapterOnClickHandler, TaskCompletionListener<String>, SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final String URL = "http://192.168.0.1:8080/JustRead/api/history";

    private String mParam1;
    private String mParam2;

    private ArrayList<DefaultBookData> books;
    public RecyclerView bookRecycler;
    private GenreAdapter adapter;
    private ProgressBar progressBar;

    private OnFragmentInteractionListener mListener;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().setTitle(R.string.title_fragment_history);
        books = new ArrayList<>();
        GetJsonTask task = new GetJsonTask("GET", this);
        task.execute(URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        bookRecycler = view.findViewById(R.id.history_recyclerview);
        bookRecycler.setHasFixedSize(true);
        progressBar = view.findViewById(R.id.wait_progressbar_history);
        SwipeRefreshLayout refreshLayout = view.findViewById(R.id.history_refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.blue, R.color.purple, R.color.green, R.color.orange);
        refreshLayout.setOnRefreshListener(this);

        float metrics = getResources().getDisplayMetrics().density;

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            bookRecycler.setLayoutManager(layoutManager);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && metrics >= 1.0f){
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            bookRecycler.setLayoutManager(layoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            bookRecycler.setLayoutManager(layoutManager);
        }

        adapter = new GenreAdapter(books, this, getContext());
        bookRecycler.setAdapter(adapter);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View source, int position) {
        Intent intent = new Intent(getContext(), BookActivity.class);
        intent.putExtra("title", books.get(position).getTitle());
        intent.putExtra("author", books.get(position).getAuthor());
        intent.putExtra("isbn", books.get(position).getIsbn());
        startActivity(intent);
    }

    @Override
    public void onComplete(String result) {
        if (result != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<DefaultBookData> list = mapper.readValue(result, new TypeReference<List<DefaultBookData>>(){});
                books.addAll(list);
                progressBar.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.i("EXCEPTION", "Error parsing jsonArray");
            }
        } else {
            Intent goToOption = new Intent(getContext(), ServerOfflineActivity.class);
            startActivity(goToOption);
            getActivity().finish();
        }
    }

    @Override
    public void onRefresh() {
        adapter.notifyDataSetChanged();
        Snackbar.make(getView(), "History refresh", Snackbar.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
