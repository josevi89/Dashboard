package com.josevi.gastos.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.josevi.gastos.R;
import com.josevi.gastos.adapters.ProductListAdapter;
import com.josevi.gastos.dialogs.TwoButtonsDialog;
import com.josevi.gastos.fragments.ShippingFragment;
import com.josevi.gastos.models.Product;
import com.josevi.gastos.models.Shipping;
import com.josevi.gastos.models.enums.Group;
import com.josevi.gastos.models.enums.Store;
import com.josevi.gastos.repositories.ProductRepository;
import com.josevi.gastos.repositories.ShippingRepository;
import com.josevi.gastos.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.josevi.gastos.utils.Constantes.SHIPPING_FRAGMENT_SHIPPING;
import static com.josevi.gastos.utils.Constantes.SHIPPING_FRAGMENT_TAG;

public class NewShippingActivity extends AppCompatActivity {

    private ImageView selectorMercadona, selectorEstanco;
    private LinearLayout checksGroupsMercadona, checksGroupsEstanco;
    private CheckBox checkMeat, checkFish, checkMilk, checkBreakfast, checkDrinks, checkFrozen,
            checkBread, checkPrepared, checkAnimal, checkVegetables, checkOthers;
    private CheckBox checkTobacco, checkPaper, checkFilters;
    private EditText findProduct, othersText;
    private RecyclerView listProductsFinded;
    private TextView totalText;
    private Button viewShipping, saveShipping;
    private ProductListAdapter productListAdapter;

    private Store storeSelected;
    private List<Group> groupsSelected;
    private List<Product> productsFinded;
    private Shipping shipping;

    private ProductRepository productRepository;
    private ShippingRepository shippingRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cost);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        productRepository = new ProductRepository();
        shippingRepository = new ShippingRepository();
        productListAdapter = new ProductListAdapter(new ArrayList<Product>(), this);

        selectorMercadona = findViewById(R.id.new_shipping_mercadona_selector);
        selectorEstanco = findViewById(R.id.new_shipping_estanco_selector);

        checksGroupsMercadona = findViewById(R.id.new_shipping_mercadona_groups);
        checksGroupsEstanco = findViewById(R.id.new_shipping_estanco_groups);

        checkMeat = findViewById(R.id.new_shipping_meat_checkbox);
        checkFish = findViewById(R.id.new_shipping_fish_checkbox);
        checkMilk = findViewById(R.id.new_shipping_milk_checkbox);
        checkBreakfast = findViewById(R.id.new_shipping_breakfast_checkbox);
        checkDrinks = findViewById(R.id.new_shipping_drinks_checkbox);
        checkFrozen = findViewById(R.id.new_shipping_frozen_checkbox);
        checkBread = findViewById(R.id.new_shipping_bread_checkbox);
        checkPrepared = findViewById(R.id.new_shipping_prepared_checkbox);
        checkAnimal = findViewById(R.id.new_shipping_animal_checkbox);
        checkVegetables = findViewById(R.id.new_shipping_vegetable_checkbox);
        checkOthers = findViewById(R.id.new_shipping_others_checkbox);
        checkTobacco = findViewById(R.id.new_shipping_tobacco_checkbox);
        checkPaper = findViewById(R.id.new_shipping_paper_checkbox);
        checkFilters = findViewById(R.id.new_shipping_filters_checkbox);

        findProduct = findViewById(R.id.new_shipping_find_add_item);
        listProductsFinded = findViewById(R.id.products_finded_list);

        othersText = findViewById(R.id.new_shipping_others_text);
        totalText = findViewById(R.id.new_shipping_total);
        viewShipping = findViewById(R.id.new_shipping_see_shipping);
        viewShipping.setEnabled(false);
        saveShipping = findViewById(R.id.new_shipping_save_shipping);
        saveShipping.setEnabled(false);

        selectStore(Store.MERCADONA);

        selectorMercadona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (storeSelected != Store.MERCADONA) {
                    if (!shipping.getShipping().isEmpty()) {
                        final TwoButtonsDialog dialog = new TwoButtonsDialog(NewShippingActivity.this);
                        dialog.setMessage("Si cambias de establecimiento se reiniciará la compra.");
                        dialog.setLeftButtonListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectStore(Store.MERCADONA);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    else {
                        selectStore(Store.MERCADONA);
                    }
                }
            }
        });

        selectorEstanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (storeSelected != Store.ESTANCO) {
                    if (!shipping.getShipping().isEmpty()) {
                        final TwoButtonsDialog dialog = new TwoButtonsDialog(NewShippingActivity.this);
                        dialog.setMessage("Si cambias de establecimiento se reiniciará la compra.");
                        dialog.setLeftButtonListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectStore(Store.ESTANCO);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    else {
                        selectStore(Store.ESTANCO);
                    }
                }
            }
        });

        checkMeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked)
                    groupsSelected.add(Group.MEAT);
                else
                    groupsSelected.remove(Group.MEAT);
                reloadProductsFinded();
            }
        });

        checkFish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked)
                    groupsSelected.add(Group.FISH);
                else
                    groupsSelected.remove(Group.FISH);
                reloadProductsFinded();
            }
        });

        checkMilk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked)
                    groupsSelected.add(Group.MILK);
                else
                    groupsSelected.remove(Group.MILK);
                reloadProductsFinded();
            }
        });

        checkBreakfast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked)
                    groupsSelected.add(Group.BREAKFAST);
                else
                    groupsSelected.remove(Group.BREAKFAST);
                reloadProductsFinded();
            }
        });

        checkDrinks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked)
                    groupsSelected.add(Group.DRINKS);
                else
                    groupsSelected.remove(Group.DRINKS);
                reloadProductsFinded();
            }
        });

        checkFrozen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked)
                    groupsSelected.add(Group.FROZEN);
                else
                    groupsSelected.remove(Group.FROZEN);
                reloadProductsFinded();
            }
        });

        findProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                reloadProductsFinded();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        othersText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    shipping.setOthers(Double.parseDouble(othersText.getText().toString()));
                }
                catch (Exception pe) {
                    shipping.setOthers(0);
                }
                reloadTotal();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listProductsFinded.setLayoutManager(new LinearLayoutManager(this));
        listProductsFinded.setAdapter(productListAdapter);

//        listProductsFinded.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                Log.d("OnLoadMor", "LoadMore");
//                view.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        onClickLoadMore();
//                    }
//                });
//
//            }
//        });

        productListAdapter.notifyDataSetChanged();

        viewShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShippingFragment shippingFragment = new ShippingFragment();
                shippingFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                Bundle shippingBundle = new Bundle();
                shippingBundle.putParcelable(SHIPPING_FRAGMENT_SHIPPING, shipping);
                shippingFragment.setArguments(shippingBundle);
                shippingFragment.show(getFragmentManager(), SHIPPING_FRAGMENT_TAG);
            }
        });

        saveShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!shipping.getShipping().isEmpty()) {
                    shippingRepository.addShippingToDb(shipping);
                    NewShippingActivity.super.onBackPressed();
                }
            }
        });
    }

    public void selectStore(Store store) {
        storeSelected = store;
        uncheckAllGroups();
        findProduct.setText("");
        switch (store) {
            case MERCADONA:
                selectorMercadona.setImageDrawable(getResources().getDrawable(R.mipmap.logo_mercadona));
                selectorEstanco.setImageDrawable(getResources().getDrawable(R.mipmap.logo_estanco_bn));
                checksGroupsMercadona.setVisibility(View.VISIBLE);
                checksGroupsEstanco.setVisibility(View.GONE);
                break;
            case ESTANCO:
                selectorMercadona.setImageDrawable(getResources().getDrawable(R.mipmap.logo_mercadona_bn));
                selectorEstanco.setImageDrawable(getResources().getDrawable(R.mipmap.logo_estanco));
                checksGroupsMercadona.setVisibility(View.GONE);
                checksGroupsEstanco.setVisibility(View.VISIBLE);
                break;
        }
        productsFinded = new ArrayList<Product>();
        shipping = new Shipping(store);
        saveShipping.setEnabled(false);
        viewShipping.setEnabled(false);
        reloadTotal();
    }

    public int getQtyFromProductCode(String code) {
        if (shipping.getShipping().containsKey(code))
            return shipping.getShipping().get(code).first;
        else
            return 0;
    }

    public void setPrizeToProduct(String code, double prize) {
        if (shipping.getShipping().containsKey(code))
            shipping.getShipping().put(code, new Pair(shipping.getShipping().get(code).first, prize));
        else
            shipping.getShipping().put(code, new Pair(1, prize));
    }

    public void addProductToShipping(String code) {
        shipping.addProduct(code);
        viewShipping.setEnabled(true);
        saveShipping.setEnabled(true);
        reloadTotal();
    }

    public void addProductToShipping(String code, double prize) {
        shipping.addProduct(code);
        viewShipping.setEnabled(true);
        saveShipping.setEnabled(true);
        reloadTotal();
    }

    public void deleteProductFromShipping(String code) {
        shipping.deleteProduct(code);
        if (shipping.getShipping().isEmpty()) {
            saveShipping.setEnabled(false);
            viewShipping.setEnabled(false);
        }
        reloadTotal();
    }

    public void reloadTotal() {
        totalText.setText(String.format("%.2f", shipping.getTotalPrize()));
    }

    public void reloadProductsFinded() {
        if (findProduct.getText() != null && !findProduct.getText().toString().isEmpty())
            productsFinded = productRepository.findProductsFromStoreAndGroups(storeSelected, groupsSelected, findProduct.getText().toString());
        else
            productsFinded = new ArrayList<Product>();
        productListAdapter.setProducts(productsFinded);
    }

    public void uncheckAllGroups() {
        checkMeat.setChecked(false);
        checkFish.setChecked(false);
        checkMilk.setChecked(false);
        checkBreakfast.setChecked(false);
        checkDrinks.setChecked(false);
        checkFrozen.setChecked(false);
        checkBread.setChecked(false);
        checkPrepared.setChecked(false);
        checkAnimal.setChecked(false);
        checkVegetables.setChecked(false);
        checkOthers.setChecked(false);
        checkTobacco.setChecked(false);
        checkPaper.setChecked(false);
        checkFilters.setChecked(false);
        this.groupsSelected = new ArrayList<Group>();
    }
}
