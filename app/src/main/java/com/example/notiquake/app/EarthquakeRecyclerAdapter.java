package com.example.notiquake.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notiquake.R;
import com.example.notiquake.app.activties.EarthquakeActivity;
import com.example.notiquake.app.model.Earthquake;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EarthquakeRecyclerAdapter extends  RecyclerView.Adapter<EarthquakeRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Earthquake> earthquakes;

    public EarthquakeRecyclerAdapter(Context context, List<Earthquake> earthquakes) {
        this.context = context;
        this.earthquakes = earthquakes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item , parent ,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Earthquake curremtEarthquake = earthquakes.get(position);

        String mag = formatMag(curremtEarthquake.getMag());
        String location = curremtEarthquake.getPlace();
        Date dateOject = new Date(curremtEarthquake.getTimeInMiliseconds());
        String date = formatDate(dateOject);
        String time = formatTime(dateOject);

        holder.tv_magnitude.setText(mag);
        holder.tv_place.setText(location);
        holder.tv_date.setText(date);
        holder.tv_time.setText(time);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EarthquakeActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return earthquakes.size() ;
    }

    private String formatMag(double mag){
        DecimalFormat magformat = new DecimalFormat("0.0");
        return  magformat.format(mag);
    }

    private String formatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return  dateFormat.format(date);
    }

    private String formatTime(Date date){
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a z");
        return  timeFormat.format(date);
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        private TextView tv_magnitude;
        private TextView tv_place;
        private TextView tv_date;
        private TextView tv_time;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView =  itemView.findViewById(R.id.cardView);
            tv_magnitude = itemView.findViewById(R.id.tv_mag);
            tv_place = itemView.findViewById(R.id.tv_location);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_time = itemView.findViewById(R.id.tv_time);

        }
    }
}
