package com.example.notiquake.app;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.AsyncTaskLoader;

import com.example.notiquake.app.model.Earthquake;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private String url;

    public EarthquakeLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Earthquake> loadInBackground() {
        if(url == null){
            return null;
        }

        List<Earthquake> earthquakes = FetchDataUtils.fetchEarthquakeData(url);
        return earthquakes;
    }
}
