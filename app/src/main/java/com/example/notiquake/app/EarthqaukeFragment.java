package com.example.notiquake.app;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notiquake.R;
import com.example.notiquake.app.activties.LoginActivity;
import com.example.notiquake.app.activties.NewsActivity;
import com.example.notiquake.app.activties.SettingsActivity;
import com.example.notiquake.app.model.Earthquake;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class EarthqaukeFragment extends Fragment implements LoaderCallbacks<List<Earthquake>>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String EARTHQUAKE_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private static  final int EARTHQUAKE_LOADER_ID = 1;
    private TextView empty_view;
    private RecyclerView recyclerView;
    private EarthquakeRecyclerAdapter earthquakeRecyclerAdapter;
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private View loadingIndicator;
    private LoaderManager loaderManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final  View rootView = inflater.inflate(R.layout.earthquakefragment_view, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerviewList);
        loadingIndicator = rootView.findViewById(R.id.progress_indicator);
        empty_view = rootView.findViewById(R.id.empty_view);


        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        firebaseAuth = FirebaseAuth.getInstance();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(this);

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            loaderManager = getActivity().getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }else {
            loadingIndicator.setVisibility(View.GONE);
            empty_view.setText(R.string.no_connection);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.settings_min_magnitude_key)) || key.equals(getString(R.string.settings_order_by_key))){
            empty_view.setVisibility(View.GONE);
            loaderManager = getActivity().getLoaderManager();
            loaderManager.restartLoader(EARTHQUAKE_LOADER_ID,null,this);
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
            earthquakes = new ArrayList<>(earthquakes);
            earthquakeRecyclerAdapter = new EarthquakeRecyclerAdapter(getActivity(), earthquakes);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(earthquakeRecyclerAdapter);

        }else {
            empty_view.setText(R.string.no_earthquakes_found);
        }
    }
    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.signOut:
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.exit:
                System.exit(0);
                getActivity().finish();
                break;
            case R.id.app_bar_search:
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        earthquakeRecyclerAdapter.getFilter().filter(newText);
                        return false;
                    }
                });
                break;
            case R.id.sort:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.news:
                startActivity(new Intent(getActivity(), NewsActivity.class));
                break;

            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void search(MenuItem item){
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                earthquakeRecyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }


}
