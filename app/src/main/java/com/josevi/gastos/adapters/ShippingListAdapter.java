package com.josevi.gastos.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.josevi.gastos.R;
import com.josevi.gastos.activities.NewShippingActivity;
import com.josevi.gastos.dialogs.TwoButtonsDialog;
import com.josevi.gastos.models.Shipping;
import com.josevi.gastos.models.enums.Store;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.josevi.gastos.utils.Constantes.NOTIFICATION_EDIT;
import static com.josevi.gastos.utils.Constantes.SHIPPING_EDIT;
import static com.josevi.gastos.utils.Constantes.timeDateFormat;

public class ShippingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Shipping> shippings;
    Activity activity;
    List<String> shippingsToDelete;

    public ShippingListAdapter(List<Shipping> shippings, Activity activity) {
        this.shippings = shippings;
        this.activity = activity;
        shippingsToDelete = new ArrayList<String>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_view_shipping, null);
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

        shippingViewHolder.shippingView.setLayoutManager(new LinearLayoutManager(activity));
        shippingViewHolder.shippingView.setAdapter(new ProductListAdapter(shipping, activity));
        shippingViewHolder.shippingView.setVisibility(View.GONE);

        shippingViewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, NewShippingActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(NOTIFICATION_EDIT, shipping);
                intent.putExtra(SHIPPING_EDIT, shipping);
                activity.startActivity(intent);
            }
        });

        shippingViewHolder.itemContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (shippingsToDelete.contains(shipping.getCode()))
                    unmarkEventToDelete(shippingViewHolder, shipping.getCode(), shipping.getStore());
                else
                    markEventToDelete(shippingViewHolder, shipping.getCode(), shipping.getStore());
                return false;
            }
        });

        shippingViewHolder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!shippingsToDelete.isEmpty()) {
                    if (shippingsToDelete.contains(shipping.getCode()))
                        unmarkEventToDelete(shippingViewHolder, shipping.getCode(), shipping.getStore());
                    else
                        markEventToDelete(shippingViewHolder, shipping.getCode(), shipping.getStore());
                }
                else {
                    if (shippingViewHolder.shippingView.getVisibility() == View.VISIBLE) {
                        shippingViewHolder.shippingView.setVisibility(View.GONE);
                        shippingViewHolder.total.setText(String.format("%.2f", shipping.getTotalPrize()) + " €");
                    }
                    else {
                        shippingViewHolder.shippingView.setVisibility(View.VISIBLE);
                        shippingViewHolder.total.setText("Total: " +String.format("%.2f", shipping.getTotalPrize()) + " €");
                    }
                }
            }
        });
    }

    public void markEventToDelete(ShippingViewHolder holder, String code, Store store) {
        holder.itemContainer.setBackgroundColor(activity.getResources().getColor(R.color.black));
        holder.shippingView.setVisibility(View.GONE);
        shippingsToDelete.add(code);
        holder.total.setTextColor(activity.getResources().getColor(R.color.white));
        holder.date.setTextColor(activity.getResources().getColor(R.color.white));
        switch(store) {
            case MERCADONA:
                holder.store.setImageDrawable(activity.getResources().getDrawable(R.mipmap.logo_mercadona_bn));
                break;
            case ESTANCO:
                holder.store.setImageDrawable(activity.getResources().getDrawable(R.mipmap.logo_estanco_bn));
                break;
            default:
                holder.store.setImageDrawable(null);
                holder.store.setBackgroundColor(activity.getResources().getColor(R.color.white));
                break;
        }
    }

    public void unmarkEventToDelete(ShippingViewHolder holder, String code, Store store) {
        holder.itemContainer.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
        holder.shippingView.setVisibility(View.GONE);
        shippingsToDelete.remove(code);
        holder.total.setTextColor(activity.getResources().getColor(R.color.black));
        holder.date.setTextColor(activity.getResources().getColor(R.color.red_app));
        switch(store) {
            case MERCADONA:
                holder.store.setImageDrawable(activity.getResources().getDrawable(R.mipmap.logo_mercadona));
                break;
            case ESTANCO:
                holder.store.setImageDrawable(activity.getResources().getDrawable(R.mipmap.logo_estanco));
                break;
            default:
                holder.store.setImageDrawable(null);
                holder.store.setBackgroundColor(activity.getResources().getColor(R.color.red_app));
                break;
        }
//        if (shippingsToDelete.isEmpty())
//            ((ShippingsActivity)activity).setDeleteButtonVisibility(false);
    }

    @Override
    public int getItemCount() {
        return shippings.size();
    }

    public static class ShippingViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemContainer;
        TextView total, date;
        ImageView store, editBtn;
        RecyclerView shippingView;

        public ShippingViewHolder(View v) {
            super(v);
            this.itemContainer = v.findViewById(R.id.item_view_shipping_info);
            this.total = v.findViewById(R.id.shipping_total);
            this.date = v.findViewById(R.id.shipping_date);
            this.store = v.findViewById(R.id.shipping_store);
            this.editBtn = v.findViewById(R.id.shipping_edit_btn);
            this.shippingView = v.findViewById(R.id.shipping_shipping_view);
        }
    }

}
