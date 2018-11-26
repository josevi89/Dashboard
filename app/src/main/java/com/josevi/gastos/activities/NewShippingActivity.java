package com.josevi.gastos.activities;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.josevi.gastos.R;
import com.josevi.gastos.adapters.ProductNewShippingListAdapter;
import com.josevi.gastos.dialogs.TwoButtonsDialog;
import com.josevi.gastos.fragments.ShippingFragment;
import com.josevi.gastos.models.Product;
import com.josevi.gastos.models.Shipping;
import com.josevi.gastos.models.ShippingQty;
import com.josevi.gastos.models.enums.Group;
import com.josevi.gastos.models.enums.Store;
import com.josevi.gastos.repositories.ProductRepository;
import com.josevi.gastos.repositories.ShippingRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.josevi.gastos.utils.Constantes.SHIPPING_EDIT;
import static com.josevi.gastos.utils.Constantes.SHIPPING_FRAGMENT_SHIPPING;
import static com.josevi.gastos.utils.Constantes.SHIPPING_FRAGMENT_TAG;
import static com.josevi.gastos.utils.Constantes.prettyDayDateFormat;
import static com.josevi.gastos.utils.Constantes.timeDateFormat;

public class NewShippingActivity extends AppCompatActivity {

    private ImageView selectorMercadona, selectorEstanco;
    private ImageView daySelector, timeSelector;
    private TextView dayLabel, timeLabel;
    private LinearLayout checksGroupsMercadona, checksGroupsEstanco;
    private CheckBox checkMeat, checkFish, checkMilk, checkBreakfast, checkDrinks, checkFrozen,
            checkBread, checkPrepared, checkAnimal, checkVegetables, checkOthers;
    private CheckBox checkTobacco, checkPaper, checkFilters;
    private EditText findProduct, othersText;
    private RecyclerView listProductsFinded;
    private TextView totalText;
    private Button viewShipping, saveShipping;
    private ProductNewShippingListAdapter productNewShippingListAdapter;

    private Store storeSelected;
    private List<Group> groupsSelected;
    private List<Product> productsFinded;
    private Shipping shipping;
    private Calendar dateSelected;

    private ProductRepository productRepository;
    private ShippingRepository shippingRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        initializeLayout();
        initializeParameters();
        configureHeader();
        configureDateSelector();
        configureGroupCheckBox();
        configureFindAndAddProducts();
        configureOthers();
        configureButtons();
    }

    public void initializeLayout() {
        setContentView(R.layout.activity_new_cost);

        selectorMercadona = findViewById(R.id.new_shipping_mercadona_selector);
        selectorEstanco = findViewById(R.id.new_shipping_estanco_selector);

        daySelector = findViewById(R.id.new_shipping_day_btn);
        timeSelector = findViewById(R.id.new_shipping_time_btn);
        dayLabel = findViewById(R.id.new_shipping_day_text);
        timeLabel = findViewById(R.id.new_shipping_time_text);

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
    }

    public void initializeParameters() {
        productRepository = new ProductRepository();
        shippingRepository = new ShippingRepository();
        
        dateSelected = Calendar.getInstance();

        shipping = null;
        try {
            shipping = getIntent().getParcelableExtra(SHIPPING_EDIT);
            productNewShippingListAdapter = new ProductNewShippingListAdapter(shipping, new ArrayList<Product>(), this);
            dateSelected.setTime(shipping.getDate());
        }
        catch (Exception e) {}
        if (shipping == null)
            productNewShippingListAdapter = new ProductNewShippingListAdapter(new Shipping(Store.MERCADONA), new ArrayList<Product>(), this);
    }

    public void configureHeader() {
        selectorMercadona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (storeSelected != Store.MERCADONA) {
                    if (!shipping.isEmpty()) {
                        final TwoButtonsDialog dialog = new TwoButtonsDialog(NewShippingActivity.this, R.color.red_app);
                        dialog.setMessage("Si cambias de establecimiento, se reiniciará la compra.");
//                        dialog.setLeftButtonColor(getResources().getColor(R.color.red_app));
                        dialog.setLeftButtonListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectStore(Store.MERCADONA, true);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    else {
                        selectStore(Store.MERCADONA, true);
                    }
                }
            }
        });

        selectorEstanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (storeSelected != Store.ESTANCO) {
                    if (!shipping.isEmpty()) {
                        final TwoButtonsDialog dialog = new TwoButtonsDialog(NewShippingActivity.this, R.color.red_app);
                        dialog.setMessage("Si cambias de establecimiento, se reiniciará la compra.");
//                        dialog.setLeftButtonColor(getResources().getColor(R.color.red_app));
                        dialog.setLeftButtonListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectStore(Store.ESTANCO, true);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    else {
                        selectStore(Store.ESTANCO, true);
                    }
                }
            }
        });

        if (shipping != null) {
            selectStore(shipping.getStore(), false);
            totalText.setText(String.format("%.2f", shipping.getTotalPrize()) +" €");
        }
        else
            selectStore(Store.MERCADONA, true);
    }

    public void configureDateSelector() {
        dayLabel.setText(prettyDayDateFormat.format(dateSelected.getTime()));
        timeLabel.setText(timeDateFormat.format(dateSelected.getTime()));

        daySelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mYear, mMonth, mDay;
                mYear = dateSelected.get(Calendar.YEAR);
                mMonth = dateSelected.get(Calendar.MONTH);
                mDay = dateSelected.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker = new DatePickerDialog(NewShippingActivity.this, R.style.AppTheme , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateSelected.set(year,monthOfYear,dayOfMonth);
                        dayLabel.setText(prettyDayDateFormat.format(dateSelected.getTime()));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();
            }
        });

        timeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mHour, mMinute;
                mHour = dateSelected.get(Calendar.HOUR_OF_DAY);
                mMinute = dateSelected.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(NewShippingActivity.this, R.style.AppTheme , new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        dateSelected.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        dateSelected.set(Calendar.MINUTE,minute);
                        timeLabel.setText(timeDateFormat.format(dateSelected.getTime()));
                    }
                }, mHour, mMinute, true);
                mTimePicker.show();
            }
        });
    }

    public void selectStore(Store store, boolean resetShipping) {
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
        if (resetShipping) {
            shipping = new Shipping(store);
            saveShipping.setEnabled(false);
            viewShipping.setEnabled(false);
        }
        else {
            switch (shipping.getStore()) {
                case MERCADONA:
                    selectorMercadona.setEnabled(false);
                    selectorEstanco.setEnabled(false);
                    selectorMercadona.setVisibility(View.VISIBLE);
                    selectorEstanco.setVisibility(View.GONE);
                    break;
                case ESTANCO:
                    selectorMercadona.setEnabled(false);
                    selectorEstanco.setEnabled(false);
                    selectorMercadona.setVisibility(View.GONE);
                    selectorEstanco.setVisibility(View.VISIBLE);
                    break;
            }
            if (!shipping.isEmpty()) {
                saveShipping.setEnabled(true);
                viewShipping.setEnabled(true);
            }
            else {
                saveShipping.setEnabled(false);
                viewShipping.setEnabled(false);
            }
        }
        reloadTotal();
    }

    public void configureGroupCheckBox() {
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

        checkPrepared.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked)
                    groupsSelected.add(Group.PREPARED);
                else
                    groupsSelected.remove(Group.PREPARED);
                reloadProductsFinded();
            }
        });

        checkBread.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked)
                    groupsSelected.add(Group.BREAD);
                else
                    groupsSelected.remove(Group.BREAD);
                reloadProductsFinded();
            }
        });

        checkAnimal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked)
                    groupsSelected.add(Group.ANIMALS);
                else
                    groupsSelected.remove(Group.ANIMALS);
                reloadProductsFinded();
            }
        });

        checkVegetables.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked)
                    groupsSelected.add(Group.VEGETABLES);
                else
                    groupsSelected.remove(Group.VEGETABLES);
                reloadProductsFinded();
            }
        });

        checkOthers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked)
                    groupsSelected.add(Group.OTHER);
                else
                    groupsSelected.remove(Group.OTHER);
                reloadProductsFinded();
            }
        });

        checkTobacco.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked)
                    groupsSelected.add(Group.TOBACCO);
                else
                    groupsSelected.remove(Group.TOBACCO);
                reloadProductsFinded();
            }
        });

        checkPaper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked)
                    groupsSelected.add(Group.PAPER);
                else
                    groupsSelected.remove(Group.PAPER);
                reloadProductsFinded();
            }
        });

        checkFilters.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked)
                    groupsSelected.add(Group.FILTERS);
                else
                    groupsSelected.remove(Group.FILTERS);
                reloadProductsFinded();
            }
        });
    }

    public void configureFindAndAddProducts() {
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

        listProductsFinded.setLayoutManager(new LinearLayoutManager(this));
        listProductsFinded.setAdapter(productNewShippingListAdapter);
        productNewShippingListAdapter.notifyDataSetChanged();
    }

    public void configureOthers() {
        if (shipping != null)
            othersText.setText(String.format("%.2f", shipping.getOthers()) +" €");
        else
            shipping = new Shipping(Store.MERCADONA);

        othersText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    shipping.setOthers(Double.parseDouble(othersText.getText().toString().replace(" €", "")));
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
    }

    public void configureButtons() {
        viewShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShippingFragment shippingFragment = new ShippingFragment();
                shippingFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                Bundle shippingBundle = new Bundle();
                shippingBundle.putParcelable(SHIPPING_FRAGMENT_SHIPPING, shipping);
                shippingFragment.setArguments(shippingBundle);
                shippingFragment.show(getFragmentManager(), SHIPPING_FRAGMENT_TAG);
                findProduct.setText("");
            }
        });

        saveShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!shipping.isEmpty()) {
                    shippingRepository.addShippingToDb(shipping);
                    NewShippingActivity.super.onBackPressed();
                }
            }
        });
    }

    public void addProductToShipping(Product product) {
        shipping.addProduct(product);
        viewShipping.setEnabled(true);
        saveShipping.setEnabled(true);
        reloadTotal();
    }

    public void setPrizeToProduct(String code, double prize) {
        if (shipping.containsKey(code))
            shipping.put(code, new ShippingQty(shipping.get(code).qty, prize));
        else
            shipping.put(code, new ShippingQty(1, prize));
        viewShipping.setEnabled(true);
        saveShipping.setEnabled(true);
        reloadTotal();
    }

    public void deleteProductFromShipping(String code) {
        shipping.deleteProduct(code);
        if (shipping.isEmpty()) {
            saveShipping.setEnabled(false);
            viewShipping.setEnabled(false);
        }
        reloadTotal();
    }

    public void reloadTotal() {
        totalText.setText(String.format("%.2f", shipping.getTotalPrize()));
    }

    public void reloadProductsFinded() {
        if (findProduct.getText() != null && !findProduct.getText().toString().isEmpty()) {
            if (storeSelected == Store.ESTANCO && findProduct.getText().toString().equals("Roswelll")) {
                productsFinded = productRepository.getRollProducts();
            }
            else
                productsFinded = productRepository.findProductsFromStoreAndGroups(storeSelected, groupsSelected, findProduct.getText().toString());
        }
        else
            productsFinded = new ArrayList<Product>();
        productNewShippingListAdapter.setProducts(shipping, productsFinded);
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

    @Override
    public void onBackPressed() {
        if (!shipping.isEmpty()) {
            final TwoButtonsDialog exitDialog = new TwoButtonsDialog(this, R.color.red_app);
            exitDialog.setMessage("Si sales, se perderá la compra.");
            exitDialog.setLeftButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exitDialog.dismiss();
                    NewShippingActivity.super.onBackPressed();
                }
            });
            exitDialog.show();
        }
        else
            NewShippingActivity.super.onBackPressed();
    }
}
