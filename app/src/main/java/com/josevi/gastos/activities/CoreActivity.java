package com.josevi.gastos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.josevi.gastos.R;
import com.josevi.gastos.adapters.CoreCardsViewAdapter;
import com.josevi.gastos.cards.CoreCard;

import java.util.ArrayList;

import static com.josevi.gastos.utils.Constantes.CORE_CARD_GASTOS_NUMBER;
import static com.josevi.gastos.utils.Constantes.CORE_CARD_NOTIFICATIONS_NUMBER;

public class CoreActivity extends AppCompatActivity {

    private RecyclerView coreCardRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabGastos = (FloatingActionButton) findViewById(R.id.fab_gastos);
        fabGastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CoreActivity.this, NewShippingActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fabNotifications = (FloatingActionButton) findViewById(R.id.fab_notifications);
        fabNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CoreActivity.this, NewNotificationActivity.class);
                startActivity(intent);
            }
        });

        coreCardRecyclerView = findViewById(R.id.core_card_container);

        initializeCardContainers();
    }

    public void initializeCardContainers() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        ArrayList<CoreCard> listOfCards = new ArrayList<>();
        listOfCards.add(new CoreCard(R.layout.core_card_notifications, CORE_CARD_NOTIFICATIONS_NUMBER));
        listOfCards.add(new CoreCard(R.layout.core_card_gastos, CORE_CARD_GASTOS_NUMBER));

        coreCardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        coreCardRecyclerView.setAdapter(new CoreCardsViewAdapter(listOfCards, this));

    }
}
