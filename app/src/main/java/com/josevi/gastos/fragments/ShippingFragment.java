package com.josevi.gastos.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.josevi.gastos.R;
import com.josevi.gastos.adapters.ProductListAdapter;
import com.josevi.gastos.models.Shipping;

import static com.josevi.gastos.utils.Constantes.SHIPPING_FRAGMENT_SHIPPING;

public class ShippingFragment extends DialogFragment {

    RecyclerView productList;
    ProductListAdapter productListAdapter;

    Shipping shipping;
    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shipping, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        this.activity = getActivity();

        shipping = getArguments().getParcelable(SHIPPING_FRAGMENT_SHIPPING);

        productList = rootView.findViewById(R.id.products_shipping_list);
        productListAdapter = new ProductListAdapter(shipping.getProducts(), activity);
        productList.setLayoutManager(new LinearLayoutManager(activity));
        productList.setAdapter(productListAdapter);

        return rootView;
    }



}
