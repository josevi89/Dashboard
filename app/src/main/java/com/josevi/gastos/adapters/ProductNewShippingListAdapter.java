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

public class ProductNewShippingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Shipping shipping;
    Activity activity;
    ProductRepository productRepository;

    public ProductNewShippingListAdapter(Shipping shipping, Activity activity) {
        this.shipping = shipping;
        this.activity = activity;
        this.productRepository = new ProductRepository();
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_view_product_new_shipping, null);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ProductViewHolder productViewHolder = (ProductViewHolder) holder;
        String productCode = new ArrayList<String>(shipping.keySet()).get(position);
        final Product product = productRepository.findProductByCode(productCode);
        productViewHolder.name.setText(product.getName());

        Double prize = shipping.get(productCode).second != null && shipping.get(productCode).second > 1 ?
                shipping.get(productCode).second : null;
        if (prize != null && prize.doubleValue() != -1) {
            productViewHolder.prize.setText(String.format(String.format("%.2f", prize)) + " €");
            productViewHolder.setNewPrizeContainer.setVisibility(View.GONE);
            productViewHolder.setQtyContainer.setVisibility(View.VISIBLE);
        }
        else {
            productViewHolder.setNewPrizeContainer.setVisibility(View.VISIBLE);
            productViewHolder.setQtyContainer.setVisibility(View.GONE);
        }
        int qty = shipping.get(productCode).first;
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
                    if (product.getPrize() != -1)
                        productViewHolder.prize.setText(String.format("%.2f", product.getPrize()) +" €");
                    else
                        productViewHolder.prize.setText("");
                }
            }
        });

        productViewHolder.setNewPrizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!productViewHolder.newPrize.getText().toString().isEmpty()) {
                    double prize = Double.parseDouble(productViewHolder.newPrize.getText().toString());
                    productViewHolder.prize.setText(String.format("%.2f", prize) +" €");
                    ((NewShippingActivity)activity).setPrizeToProduct(product.getCode(), prize);
                    productViewHolder.setQtyContainer.setVisibility(View.VISIBLE);
                    productViewHolder.setNewPrizeContainer.setVisibility(View.GONE);
                    productViewHolder.minusBtn.setVisibility(View.VISIBLE);
                    productViewHolder.layout.setBackgroundColor(activity.getResources().getColor(R.color.red_app_light));
                    if (Integer.parseInt(productViewHolder.qty.getText().toString().trim()) == 0)
                        productViewHolder.qty.setText(String.valueOf(1));
                    notifyDataSetChanged();
                }
            }
        });

        productViewHolder.prize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!productViewHolder.prize.getText().toString().isEmpty())
                    productViewHolder.newPrize.setText(
                            productViewHolder.prize.getText().toString().replace("€", "").trim());
                productViewHolder.setNewPrizeContainer.setVisibility(View.VISIBLE);
                productViewHolder.setQtyContainer.setVisibility(View.GONE);
            }
        });

        productViewHolder.cancelSetNewPrizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!productViewHolder.newPrize.getText().toString().isEmpty()) {
                    productViewHolder.setNewPrizeContainer.setVisibility(View.GONE);
                    productViewHolder.setQtyContainer.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shipping.size();
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
            name = v.findViewById(R.id.product_new_shipping_name);
            prize = v.findViewById(R.id.product_new_shipping_prize);
            qty = v.findViewById(R.id.product_new_shipping_qty);
            minusBtn = v.findViewById(R.id.minus_button);
            newPrize = v.findViewById(R.id.product_new_shipping_new_prize);
            setNewPrizeBtn = v.findViewById(R.id.product_new_shipping_new_prize_button);
            cancelSetNewPrizeBtn = v.findViewById(R.id.product_new_shipping_cancel_set_new_prize_button);
            setQtyContainer = v.findViewById(R.id.product_new_shipping_set_qty_container);
            setNewPrizeContainer = v.findViewById(R.id.product_new_shipping_edit_prize_container);
            plusBtn = v.findViewById(R.id.plus_button);
        }
    }
}
