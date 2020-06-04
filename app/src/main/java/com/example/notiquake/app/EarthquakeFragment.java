package com.example.notiquake.app;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.notiquake.R;
import com.example.notiquake.app.model.Earthquake;

import java.util.List;


public class EarthquakeFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private TextView empty_view;
    private RecyclerView recyclerView;
    private EarthquakeRecyclerAdapter earthquakeRecyclerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final String EARTHQUAKE_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private static  final int EARTHQUAKE_LOADER_ID = 1;
    private View loadingIndicator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.earthquakefragment_view , container,false);

        recyclerView = rootView.findViewById(R.id.recyclerviewList);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRereshLayout);

        loadingIndicator = rootView.findViewById(R.id.progress_indicator);
        empty_view = rootView.findViewById(R.id.empty_view);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener((SharedPreferences.OnSharedPreferenceChangeListener) getActivity());

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getActivity().getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }else {
            loadingIndicator.setVisibility(View.GONE);
            empty_view.setText(R.string.no_connection);
        }

    }



    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(EARTHQUAKE_URL);
        Uri.Builder builder = baseUri.buildUpon();

        builder.appendQueryParameter("format","geojson");
        builder.appendQueryParameter("minmagnitude",minMagnitude);
        builder.appendQueryParameter("orderby",orderBy);
        builder.appendQueryParameter("mincdi","1");
        builder.appendQueryParameter("minfelt","1");

        return new EarthquakeLoader(getActivity(), builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        loadingIndicator.setVisibility(View.GONE);

        if(earthquakes != null && !earthquakes.isEmpty()){
            earthquakeRecyclerAdapter = new EarthquakeRecyclerAdapter(getActivity(), earthquakes);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            recyclerView.setAdapter(earthquakeRecyclerAdapter);

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override public void run() {
                            earthquakeRecyclerAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 5000);
                }
            });
            swipeRefreshLayout.setColorSchemeColors(
                    getResources().getColor(android.R.color.holo_blue_bright),
                    getResources().getColor(android.R.color.holo_green_light),
                    getResources().getColor(android.R.color.holo_orange_light),
                    getResources().getColor(android.R.color.holo_red_light)
            );

        }else {
            empty_view.setText(R.string.no_earthquakes_found);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
    }
}
