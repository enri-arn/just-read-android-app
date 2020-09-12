package it.unito.di.justread.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.unito.di.justread.R;
import it.unito.di.justread.activities.GenreActivity;
import it.unito.di.justread.activities.ServerOfflineActivity;
import it.unito.di.justread.adapters.CatalogAdapter;
import it.unito.di.justread.asynctasks.GetJsonTask;
import it.unito.di.justread.asynctasks.TaskCompletionListener;
import it.unito.di.justread.model.Category;


public class CatalogFragment extends Fragment implements CatalogAdapter.BookAdapterOnClickHandler, TaskCompletionListener<String>, SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public RecyclerView bookRecycler;
    private CatalogAdapter adapter;
    private ProgressBar progressBar;

    private final String URL = "http://192.168.0.1:8080/JustRead/api/catalog";

    ArrayList<Category> categories;

    private OnFragmentInteractionListener mListener;
    private ImageView profileImage;
    private TextView nameSurname;

    public CatalogFragment() {
        // Required empty public constructor
    }

    public static CatalogFragment newInstance(String param1, String param2) {
        CatalogFragment fragment = new CatalogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categories = new ArrayList<>();
        getActivity().setTitle(R.string.title_fragment_catalog);
        GetJsonTask task = new GetJsonTask("GET", this);
        task.execute(URL);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        profileImage = view.findViewById(R.id.profile_image_drawer);
        bookRecycler = (RecyclerView) view.findViewById(R.id.catalog_recyclerview);
        progressBar = (ProgressBar) view.findViewById(R.id.wait_progressbar_catalog);
        bookRecycler.setHasFixedSize(true);
        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.catalog_refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.blue, R.color.purple, R.color.green, R.color.orange);
        refreshLayout.setOnRefreshListener(this);

        GridLayoutManager layoutManager = null;
        float metrics = getResources().getDisplayMetrics().density;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(getActivity(), 2);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && metrics >= 1.0f) {
            layoutManager = new GridLayoutManager(getActivity(), 3);
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 2);
        }
        bookRecycler.setLayoutManager(layoutManager);

        adapter = new CatalogAdapter(categories, this, getContext());
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
    public void onComplete(String result) {
        if (result != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<Category> categoryList = mapper.readValue(result, new TypeReference<List<Category>>() {});
                categories.addAll( categoryList );
                Log.i("EXCEPTION", categories.toString());
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
        Snackbar.make(getView(), "Catalog refresh", Snackbar.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onClick(View source, int position) {
        String title = categories.get(position).getName();
        Intent intent = new Intent(getContext(), GenreActivity.class);
        intent.putExtra("title", title);
        startActivity(intent);
    }
}
