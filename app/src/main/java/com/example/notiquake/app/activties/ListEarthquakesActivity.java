package com.example.notiquake.app.activties;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.example.notiquake.R;
import com.example.notiquake.app.EarthqaukeFragment;
import com.example.notiquake.app.EarthquakeRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class ListEarthquakesActivity extends AppCompatActivity  {

    private EarthquakeRecyclerAdapter earthquakeRecyclerAdapter;
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_earthquakes);


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.mainContainer, new EarthqaukeFragment())
                .commit();

    }


}



