package com.josevi.gastos.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.josevi.gastos.R;
import com.josevi.gastos.activities.NewShippingActivity;
import com.josevi.gastos.models.Product;
import com.josevi.gastos.models.Shipping;
import com.josevi.gastos.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Shipping shipping;
    Activity activity;
    ProductRepository productRepository;

    public ProductListAdapter(Shipping shipping, Activity activity) {
        this.shipping = shipping;
        this.activity = activity;
        this.productRepository = new ProductRepository();
    }

    public void setProducts(Shipping shipping) {
        this.shipping = shipping;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_view_product, null);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ProductViewHolder productViewHolder = (ProductViewHolder) holder;
        String productCode = new ArrayList<String>(shipping.keySet()).get(position);
        final Product product = productRepository.findProductByCode(productCode);
        productViewHolder.name.setText(product.getName());

        int qty = shipping.get(productCode).qty;
        double prize = shipping.get(productCode).prize != -1 ?
                shipping.get(productCode).prize : product.getPrize() > 0 ? product.getPrize() : 0;

        productViewHolder.prize.setText(String.format(String.format("%.2f", prize)) + " €");
        productViewHolder.qty.setText(String.valueOf(qty));
    }

    @Override
    public int getItemCount() {
        return shipping.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        public TextView name, prize, qty;

        public ProductViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.product_name);
            prize = v.findViewById(R.id.product_prize);
            qty = v.findViewById(R.id.product_qty);
        }
    }

}
