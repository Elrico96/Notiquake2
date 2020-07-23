package com.example.notiquake.app;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notiquake.R;
import com.example.notiquake.app.model.News;

import java.util.ArrayList;
import java.util.List;

public class EarhquakeNewsFragment extends Fragment implements LoaderCallbacks<List<News>> {
    private static final String EARTHQUAKE_URL =
            "https://newsapi.org/v2/everything";
    private static  final int EARTHQUAKE_LOADER_ID = 2;
    private TextView news_empty_view;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private View loadingIndicator;
    private LoaderManager loaderManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final  View rootView = inflater.inflate(R.layout.news_frag_view, container, false);


        recyclerView = rootView.findViewById(R.id.news_recyclerviewList);
        loadingIndicator = rootView.findViewById(R.id.news_progress_indicator);
        news_empty_view = rootView.findViewById(R.id.news_empty_view);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            loaderManager = getActivity().getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }else {
            loadingIndicator.setVisibility(View.GONE);
            news_empty_view.setText(R.string.no_connection);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(EARTHQUAKE_URL);
        Uri.Builder builder = baseUri.buildUpon();

        builder.appendQueryParameter("q","earthquake");
        builder.appendQueryParameter("apiKey","0ffccf2492d448d9a776df90863ed9b3");


        return new NewsLoader(getActivity(), builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> articles) {
        loadingIndicator.setVisibility(View.GONE);

        if( articles!= null && !articles.isEmpty()){
            articles = new ArrayList<>(articles);
            newsAdapter = new NewsAdapter(getActivity(), articles);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(newsAdapter);

        }else {
            news_empty_view.setText(R.string.no_earthquakes_found);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        loader.reset();
    }
}
