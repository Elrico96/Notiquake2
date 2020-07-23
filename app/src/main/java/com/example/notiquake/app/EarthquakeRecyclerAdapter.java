package com.example.notiquake.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notiquake.R;
import com.example.notiquake.app.activties.EarthquakeActivity;
import com.example.notiquake.app.model.Earthquake;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EarthquakeRecyclerAdapter extends  RecyclerView.Adapter<EarthquakeRecyclerAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<Earthquake> earthquakes;
    private List<Earthquake> earthquakesAll;

    public EarthquakeRecyclerAdapter(Context context, List<Earthquake> earthquakes) {
        this.context = context;
        this.earthquakes = earthquakes;
        this.earthquakesAll = new ArrayList<>(earthquakes);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item , parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Earthquake curremtEarthquake = earthquakes.get(position);

        String mag = formatMag(curremtEarthquake.getMag());
        String location = curremtEarthquake.getPlace();
        Date dateOject = new Date(curremtEarthquake.getTimeInMiliseconds());
        String date = formatDate(dateOject);
        String time = formatTime(dateOject);

        holder.tvMagnitude.setText(mag);
        GradientDrawable magBackground = (GradientDrawable) holder.tvMagnitude.getBackground();
        int magColor = getMagnitudeColor(curremtEarthquake.getMag());
        magBackground.setColor(magColor);

        holder.tvPlace.setText(location);
        holder.tvDate.setText(date);
        holder.tvTime.setText(time);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EarthquakeActivity.class);
                intent.putExtra("EARTHQUAKE_POSITION",curremtEarthquake);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return earthquakes.size() ;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Earthquake> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(earthquakesAll);
            }else {
                for (Earthquake earthquake: earthquakesAll){
                    if (earthquake.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(earthquake);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            earthquakes.clear();
            earthquakes.addAll((Collection<? extends Earthquake>) results.values);
            notifyDataSetChanged();
        }
    };

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }

        return ContextCompat.getColor(context, magnitudeColorResourceId);
    }

    private String formatMag(double mag){
        DecimalFormat magformat = new DecimalFormat("0.0");
        return  magformat.format(mag);
    }

    private String formatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMMM yyyy");
        return  dateFormat.format(date);
    }

    private String formatTime(Date date){
        SimpleDateFormat timeFormat = new SimpleDateFormat("k:mm:ss a z", Locale.getDefault());
        return  timeFormat.format(date);
    }


    public  class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        private TextView tvMagnitude;
        private TextView tvPlace;
        private TextView tvDate;
        private TextView tvTime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView =  itemView.findViewById(R.id.cardView);
            tvMagnitude = itemView.findViewById(R.id.tv_mag);
            tvPlace = itemView.findViewById(R.id.tv_location);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTime = itemView.findViewById(R.id.tv_time);

        }
    }
}
