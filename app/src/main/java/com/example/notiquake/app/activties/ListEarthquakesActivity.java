package com.example.notiquake.app.activties;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notiquake.R;
import com.example.notiquake.app.EarthquakeLoader;
import com.example.notiquake.app.EarthquakeRecyclerAdapter;
import com.example.notiquake.app.model.Earthquake;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ListEarthquakesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {


    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;

    private TextView empty_view;
    private RecyclerView recyclerView;
    private EarthquakeRecyclerAdapter earthquakeRecyclerAdapter;

    private static final String EARTHQUAKE_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private static  final int EARTHQUAKE_LOADER_ID = 1;
    private View loadingIndicator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_earthquakes);

        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);

        recyclerView = findViewById(R.id.recyclerviewList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadingIndicator = findViewById(R.id.progress_indicator);
        empty_view = findViewById(R.id.empty_view);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case  R.id.profile:
                        startActivity(new Intent(ListEarthquakesActivity.this, ProfileActivity.class));
                        break;
                    case R.id.signOut:
                            firebaseAuth.signOut();
                            startActivity(new Intent(ListEarthquakesActivity.this, LoginActivity.class));
                        break;
                    case R.id.about:
                        Toast.makeText(ListEarthquakesActivity.this,"About clicked",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.exit:
                        finish();
                        System.exit(0);
                        break;
                    case R.id.app_bar_search:
                        Toast.makeText(ListEarthquakesActivity.this,"Search clicked",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.sort:
                        Toast.makeText(ListEarthquakesActivity.this,"Sort clicked",Toast.LENGTH_LONG).show();
                        break;
                }
                return false;
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }else {
            loadingIndicator.setVisibility(View.GONE);
            empty_view.setText(R.string.no_connection);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.main_menu, menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        /*switch (item.getItemId()){
            case  R.id.profile:
                startActivity(new Intent(ListEarthquakesActivity.this, ProfileActivity.class));
                break;
            case R.id.signOut:
                firebaseAuth.signOut();
                startActivity(new Intent(ListEarthquakesActivity.this, LoginActivity.class));
                break;
            case R.id.about:
                Toast.makeText(ListEarthquakesActivity.this,"About clicked",Toast.LENGTH_LONG).show();
                break;
            case R.id.exit:
                finish();
                System.exit(0);
                break;
            case R.id.app_bar_search:
                Toast.makeText(ListEarthquakesActivity.this,"Search clicked",Toast.LENGTH_LONG).show();
                break;
            case R.id.sort:
                Toast.makeText(ListEarthquakesActivity.this,"Sort clicked",Toast.LENGTH_LONG).show();
                break;
        }
        return false;*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(EARTHQUAKE_URL);
        Uri.Builder builder = baseUri.buildUpon();

        builder.appendQueryParameter("format","geojson");
        builder.appendQueryParameter("limit","150");

        return new EarthquakeLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        loadingIndicator.setVisibility(View.GONE);

        if(earthquakes != null && !earthquakes.isEmpty()){
            earthquakeRecyclerAdapter = new EarthquakeRecyclerAdapter(this, earthquakes);
            recyclerView.setAdapter(earthquakeRecyclerAdapter);
        }else {
            empty_view.setText(R.string.no_earthquakes_found);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        earthquakeRecyclerAdapter.notifyDataSetChanged();
    }
}
