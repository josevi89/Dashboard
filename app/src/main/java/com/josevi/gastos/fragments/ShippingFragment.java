package com.josevi.gastos.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.josevi.gastos.R;
import com.josevi.gastos.adapters.ProductNewShippingListAdapter;
import com.josevi.gastos.models.Shipping;

import static com.josevi.gastos.utils.Constantes.SHIPPING_FRAGMENT_SHIPPING;

public class ShippingFragment extends DialogFragment {

    RecyclerView productList;
    ProductNewShippingListAdapter productNewShippingListAdapter;

    Shipping shipping;
    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shipping, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        this.activity = getActivity();

        shipping = getArguments().getParcelable(SHIPPING_FRAGMENT_SHIPPING);

        productList = rootView.findViewById(R.id.products_shipping_list);
        productNewShippingListAdapter = new ProductNewShippingListAdapter(shipping, null, activity);
        productList.setLayoutManager(new LinearLayoutManager(activity));
        productList.setAdapter(productNewShippingListAdapter);

        return rootView;
    }
}
