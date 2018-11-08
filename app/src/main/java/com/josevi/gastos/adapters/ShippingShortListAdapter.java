package com.josevi.gastos.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.josevi.gastos.R;
import com.josevi.gastos.models.Shipping;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.josevi.gastos.utils.Constantes.timeDateFormat;

public class ShippingShortListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Shipping> shippings;
    Activity activity;
    List<String> shippingsToDelete;

    public ShippingShortListAdapter(List<Shipping> shippings, Activity activity) {
        this.shippings = shippings;
        this.activity = activity;
        shippingsToDelete = new ArrayList<String>();
    }

    public void setShippings(List<Shipping> shippings) {
        this.shippings = shippings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_view_shipping_short, null);
        return new ShippingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ShippingViewHolder shippingViewHolder = (ShippingViewHolder) holder;
        final Shipping shipping = shippings.get(position);

        switch(shipping.getStore()) {
            case MERCADONA:
                shippingViewHolder.store.setImageDrawable(activity.getResources().getDrawable(R.mipmap.logo_mercadona));
                break;
            case ESTANCO:
                shippingViewHolder.store.setImageDrawable(activity.getResources().getDrawable(R.mipmap.logo_estanco));
                break;
            default:
                shippingViewHolder.store.setImageDrawable(null);
                shippingViewHolder.store.setBackgroundColor(activity.getResources().getColor(R.color.red_app));
                break;
        }
        Calendar shippingDate = Calendar.getInstance();
        shippingDate.setTime(shipping.getDate());
        String date = String.valueOf(shippingDate.get(Calendar.DAY_OF_MONTH)) +" "
                +activity.getResources().getStringArray(
                        R.array.months_short_array)[shippingDate.get(Calendar.MONTH)] +" "
                + timeDateFormat.format(shippingDate.getTime());
        shippingViewHolder.date.setText(date);
        shippingViewHolder.total.setText(String.format("%.2f", shipping.getTotalPrize()) +" €");
    }

    @Override
    public int getItemCount() {
        return shippings.size();
    }

    public static class ShippingViewHolder extends RecyclerView.ViewHolder {

        ImageView store;
        TextView date, total;

        public ShippingViewHolder(View v) {
            super(v);
            this.store = v.findViewById(R.id.shipping_short_store);
            this.date = v.findViewById(R.id.shipping_short_date);
            this.total = v.findViewById(R.id.shipping_short_total);
        }
    }

}
