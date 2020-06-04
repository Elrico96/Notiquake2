package com.example.notiquake.app.activties;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notiquake.R;
import com.example.notiquake.app.model.Earthquake;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EarthquakeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthqauke);


        Intent intent = getIntent();
        final Earthquake earthquake = intent.getParcelableExtra("EARTHQUAKE_POSITION");


        TextView tvtitle = findViewById(R.id.title);
        TextView tvstrength = findViewById(R.id.perceived_strength);
        TextView tvNoPeople = findViewById(R.id.number_of_people);
        TextView tvdate = findViewById(R.id.date);
        TextView tvcoordinates = findViewById(R.id.tv_coordinates);
        TextView tvdepth = findViewById(R.id.tv_depth);
        TextView tvtsunamiAlert = findViewById(R.id.tv_tsunami_alert);
        TextView tvMoreInformation = findViewById(R.id.tv_more_info);
        TextView tvReport = findViewById(R.id.tv_tell_us);


        tvtitle.setText(earthquake.getTitle());
        tvstrength.setText(formatMag(earthquake.getCdi()));

        String no_people = earthquake.getFelt()+" "+getResources().getString(R.string.number_people);
        tvNoPeople.setText(no_people);

        Date dateOject = new Date(earthquake.getTimeInMiliseconds());
        tvdate.setText(formatDate(dateOject));

        String longitude = formatCoordinate(earthquake.getLongitude())+ determineLongitude(earthquake.getLongitude());
        String latitude =  formatCoordinate(earthquake.getLatitude())+ determineLatitude(earthquake.getLatitude());

        String coordinate = longitude+" "+latitude;
        tvcoordinates.setText(coordinate);

        String depth = formatMag(earthquake.getDepth())+ getResources().getString(R.string.text_depth);
        tvdepth.setText(depth);

        String tsunamiAlert = getResources().getString(R.string.text_tsunami)+ determineTsunamiAlert(earthquake.getTsunameAlert());
        tvtsunamiAlert.setText(tsunamiAlert);

        tvMoreInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = earthquake.getUrl();
                Uri earthquakeUri = Uri.parse(url);
                startActivity(new Intent(Intent.ACTION_VIEW , earthquakeUri));
            }
        });

        tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , ReportActivity.class));
            }
        });
    }

    private String formatMag(double mag){
        DecimalFormat magformat = new DecimalFormat("0.0");
        return  magformat.format(mag);
    }

    private String formatCoordinate(double coordinate){
        DecimalFormat coordinateformat = new DecimalFormat("0.000");
        return  coordinateformat.format(coordinate);
    }

    private String formatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd LLL yyyy 'at' hh:mm a z");
        return  dateFormat.format(date);
    }

    private String formatTime(Date date){
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a z");
        return  timeFormat.format(date);
    }

    private String determineTsunamiAlert(String alert){
        if(alert.equals("1")){
            alert = "Yes";
        }else {
            alert = "No";
        }
        return alert;
    }

    private String determineLongitude(Double longitude){
        String direction = "";
        if(longitude >= 0){
            direction = " 'N";
        }else if (longitude < 0){
            direction = " 'S";
        }
        return direction;
    }

    private String determineLatitude(Double latitude){
        String direction = "";
        if(latitude >= 0){
            direction = " 'E";
        }else if (latitude < 0){
            direction = " 'W";
        }
        return direction;
    }
}
