package com.example.notiquake.app.activties;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.notiquake.R;
import com.example.notiquake.app.EarthquakeLoader;
import com.example.notiquake.app.EarthquakeRecyclerAdapter;
import com.example.notiquake.app.model.Earthquake;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ListEarthquakesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>>,SharedPreferences.OnSharedPreferenceChangeListener{

    private FirebaseAuth firebaseAuth;
    private TextView empty_view;
    private RecyclerView recyclerView;
    private EarthquakeRecyclerAdapter earthquakeRecyclerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final String EARTHQUAKE_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private static  final int EARTHQUAKE_LOADER_ID = 1;
    private View loadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_earthquakes);

        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        recyclerView = findViewById(R.id.recyclerviewList);
        swipeRefreshLayout = findViewById(R.id.swipeRereshLayout);
        loadingIndicator = findViewById(R.id.progress_indicator);
        empty_view = findViewById(R.id.empty_view);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }else {
            loadingIndicator.setVisibility(View.GONE);
            empty_view.setText(R.string.no_connection);
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.signOut:
                        firebaseAuth.signOut();
                        startActivity(new Intent(ListEarthquakesActivity.this, LoginActivity.class));
                        finish();
                        break;
                    case R.id.about:
                        startActivity(new Intent(ListEarthquakesActivity.this, AboutActivity.class));
                        break;
                    case R.id.exit:
                        System.exit(0);
                        finish();
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
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        break;

                    default:
                        return true;
                }
                return false;
            }

        });

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.settings_min_magnitude_key)) || key.equals(getString(R.string.settings_order_by_key))){
                empty_view.setVisibility(View.GONE);
            getLoaderManager().restartLoader(EARTHQUAKE_LOADER_ID,null,this);
        }
    }


    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

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

        return new EarthquakeLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        loadingIndicator = findViewById(R.id.progress_indicator);
        loadingIndicator.setVisibility(View.GONE);

        if(earthquakes != null && !earthquakes.isEmpty()){
            earthquakeRecyclerAdapter = new EarthquakeRecyclerAdapter(this, earthquakes);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(earthquakeRecyclerAdapter);

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override public void run() {
                            getLoaderManager().restartLoader(EARTHQUAKE_LOADER_ID,null,ListEarthquakesActivity.this);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 3000);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

}



