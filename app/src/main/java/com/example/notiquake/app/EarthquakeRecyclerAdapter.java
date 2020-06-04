package com.example.notiquake.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
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
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Earthquake curremtEarthquake = earthquakes.get(position);

        String mag = formatMag(curremtEarthquake.getMag());
        String location = curremtEarthquake.getPlace();
        Date dateOject = new Date(curremtEarthquake.getTimeInMiliseconds());
        String date = formatDate(dateOject);
        String time = formatTime(dateOject);

        holder.tvMagnitude.setText(mag);
        holder.tvPlace.setText(location);
        holder.tvDate.setText(date);
        holder.tvTime.setText(time);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               navigatetoMoreInfo(position);
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

    private void navigatetoMoreInfo(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.more_dialog_msg);
        builder.setPositiveButton(R.string.more_info_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, EarthquakeActivity.class);
                intent.putExtra("EARTHQUAKE_POSITION",earthquakes.get(position));
                context.startActivity(intent);
            }
        }).setNegativeButton(R.string.more_info_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.setTitle(R.string.more_information);
        dialog.show();
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
