package com.josevi.gastos.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.josevi.gastos.R;
import com.josevi.gastos.cards.Legend;
import com.josevi.gastos.models.Shipping;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.josevi.gastos.utils.Constantes.timeDateFormat;

public class LegendListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Legend> legendItems;
    Activity activity;

    public LegendListAdapter(List<Legend> legendItems, Activity activity) {
        this.legendItems = legendItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_view_legend, null);
        return new LegendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final LegendViewHolder legendViewHolder = (LegendViewHolder) holder;
        final Legend legend = legendItems.get(position);
        legendViewHolder.color.setBackgroundColor(legend.getColor());
        legendViewHolder.label.setText(legend.getLabel());
    }

    @Override
    public int getItemCount() {
        return legendItems.size();
    }

    public static class LegendViewHolder extends RecyclerView.ViewHolder {

        TextView color, label;

        public LegendViewHolder(View v) {
            super(v);
            this.color = v.findViewById(R.id.legend_color);
            this.label = v.findViewById(R.id.legend_label);
        }
    }

}
