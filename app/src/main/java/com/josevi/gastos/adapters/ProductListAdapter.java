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
import com.josevi.gastos.activities.NewNotificationActivity;
import com.josevi.gastos.activities.NewShippingActivity;
import com.josevi.gastos.models.Product;
import com.josevi.gastos.utils.Utils;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Product> products;
    Activity activity;

    public ProductListAdapter(List<Product> products, Activity activity) {
        this.products = products;
        this.activity = activity;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
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
        final Product product = products.get(position);

        productViewHolder.name.setText(product.getName());
        if (product.getPrize() != -1) {
            productViewHolder.prize.setText(String.format(String.format("%.2f", product.getPrize())) + " €");
            productViewHolder.setNewPrizeContainer.setVisibility(View.VISIBLE);
            productViewHolder.setQtyContainer.setVisibility(View.GONE);
        }
        else {
            productViewHolder.setNewPrizeContainer.setVisibility(View.VISIBLE);
            productViewHolder.setQtyContainer.setVisibility(View.GONE);
        }
        int qty = ((NewShippingActivity)activity).getQtyFromProductCode(product.getCode());
        productViewHolder.qty.setText(String.valueOf(qty));
        if (qty > 0) {
            productViewHolder.minusBtn.setVisibility(View.VISIBLE);
            productViewHolder.layout.setBackgroundColor(activity.getResources().getColor(R.color.red_app_light));
        }
        else {
            productViewHolder.minusBtn.setVisibility(View.INVISIBLE);
            productViewHolder.layout.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
        }

        productViewHolder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewShippingActivity)activity).addProductToShipping(product.getCode());
                productViewHolder.qty.setText(String.valueOf(Integer.parseInt(productViewHolder.qty.getText().toString()) + 1));
                productViewHolder.minusBtn.setVisibility(View.VISIBLE);
                productViewHolder.layout.setBackgroundColor(activity.getResources().getColor(R.color.red_app_light));
            }
        });

        productViewHolder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewShippingActivity)activity).deleteProductFromShipping(product.getCode());
                productViewHolder.qty.setText(String.valueOf(Integer.parseInt(productViewHolder.qty.getText().toString()) - 1));
                if (productViewHolder.qty.getText().toString().equals("0")) {
                    productViewHolder.minusBtn.setVisibility(View.GONE);
                    productViewHolder.layout.setBackgroundColor(activity.getResources().getColor(R.color.white));
                }
            }
        });

        productViewHolder.setNewPrizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!productViewHolder.newPrize.getText().toString().isEmpty()) {
                    double prize = Double.parseDouble(productViewHolder.newPrize.getText().toString());
                    productViewHolder.prize.setText(String.format("%.2f", prize));
                    ((NewShippingActivity)activity).setPrizeToProduct(product.getCode(), prize);
                    productViewHolder.setQtyContainer.setVisibility(View.VISIBLE);
                    productViewHolder.setNewPrizeContainer.setVisibility(View.GONE);
                    productViewHolder.minusBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        productViewHolder.prize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productViewHolder.setNewPrizeContainer.setVisibility(View.VISIBLE);
                productViewHolder.setQtyContainer.setVisibility(View.GONE);
            }
        });

        productViewHolder.cancelSetNewPrizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productViewHolder.newPrize.getText().toString().isEmpty()) {
                    productViewHolder.setNewPrizeContainer.setVisibility(View.GONE);
                    productViewHolder.setQtyContainer.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        public TextView name, prize, qty;
        public ImageView minusBtn, plusBtn, setNewPrizeBtn, cancelSetNewPrizeBtn;
        public EditText newPrize;
        public LinearLayout setQtyContainer, setNewPrizeContainer;
        public RelativeLayout layout;

        public ProductViewHolder(View v) {
            super(v);
            layout = v.findViewById(R.id.item_view_product);
            name = v.findViewById(R.id.product_name);
            prize = v.findViewById(R.id.product_prize);
            qty = v.findViewById(R.id.product_qty);
            minusBtn = v.findViewById(R.id.minus_button);
            newPrize = v.findViewById(R.id.product_new_prize);
            setNewPrizeBtn = v.findViewById(R.id.product_new_prize_button);
            setNewPrizeBtn = v.findViewById(R.id.product_cancel_set_new_prize_button);
            setQtyContainer = v.findViewById(R.id.product_set_qty_container);
            setNewPrizeContainer = v.findViewById(R.id.product_edit_prize_container);
            plusBtn = v.findViewById(R.id.plus_button);
        }
    }

}
