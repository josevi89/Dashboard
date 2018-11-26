package com.josevi.gastos.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.josevi.gastos.R;
import com.josevi.gastos.adapters.ShippingListAdapter;
import com.josevi.gastos.adapters.ShippingShortListAdapter;
import com.josevi.gastos.dialogs.TwoButtonsDialog;
import com.josevi.gastos.repositories.ShippingRepository;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

import static com.josevi.gastos.utils.Constantes.monthYearFormatter;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_SINGLE;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SHOW_NONE;

public class ShippingsActivity extends AppCompatActivity {

    private ImageView calendarTabSelector, listTabSelector;

    private LinearLayout calendarTab;
    private MaterialCalendarView calendar;
    private RecyclerView calendarListView;
    private ShippingShortListAdapter shippingShortListAdapter;

    private LinearLayout listTab;
    private ImageView listLeftArrow, listRightArrow;
    private TextView monthLabel;
    private RecyclerView listListView;

    private Button deleteBtn;

    private ShippingRepository shippingRepository;
    private Calendar dateSelected;
    private int tabSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeLayout();
        initializeParameters();
        configureHeader();
        configureCalendarTab();
        configureListTab();
    }

    private void initializeLayout() {
        setContentView(R.layout.activity_shippings);

        calendarTabSelector = findViewById(R.id.shippings_calendar_selector);
        listTabSelector = findViewById(R.id.shippings_list_selector);

        calendarTab = findViewById(R.id.shippings_calendar_tab);
        calendar = findViewById(R.id.shippings_calendar_calendar);
        calendarListView = findViewById(R.id.shippings_calendar_list);

        listTab = findViewById(R.id.shippings_list_tab);
        listLeftArrow = findViewById(R.id.shippings_list_left_arrow);
        listRightArrow = findViewById(R.id.shippings_list_right_arrow);
        monthLabel = findViewById(R.id.shippings_list_month_label);
        listListView = findViewById(R.id.shippings_list_list);
        deleteBtn = findViewById(R.id.shippings_list_delete_btn);
    }

    private void initializeParameters() {
        shippingRepository = new ShippingRepository();
        dateSelected = Calendar.getInstance();
        tabSelected = 0;
    }

    public void configureHeader() {
        calendarTabSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabSelected != 0) {
                    tabSelected = 0;
                    dateSelected = Calendar.getInstance();
                    calendar.setSelectedDate(dateSelected);
                    calendarListView.setAdapter(new ShippingShortListAdapter(shippingRepository.getShippingsInDay(dateSelected), ShippingsActivity.this));
                    calendarTabSelector.setImageDrawable(getResources().getDrawable(R.mipmap.icon_calendar_red));
                    listTabSelector.setImageDrawable(getResources().getDrawable(R.mipmap.icon_list_red_unchecked));
                    calendarTab.setVisibility(View.VISIBLE);
                    listTab.setVisibility(View.GONE);
                }
            }
        });

        listTabSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabSelected != 1) {
                    tabSelected = 1;
                    dateSelected = Calendar.getInstance();
                    calendar.setSelectedDate(dateSelected);
                    calendarListView.setAdapter(new ShippingShortListAdapter(shippingRepository.getShippingsInDay(dateSelected), ShippingsActivity.this));
                    calendarTabSelector.setImageDrawable(getResources().getDrawable(R.mipmap.icon_calendar_red_unchecked));
                    listTabSelector.setImageDrawable(getResources().getDrawable(R.mipmap.icon_list_red));
                    calendarTab.setVisibility(View.GONE);
                    listTab.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void configureCalendarTab() {
        configureCalendar();
        calendarListView.setAdapter(new ShippingShortListAdapter(shippingRepository.getShippingsInDay(dateSelected), ShippingsActivity.this));
        calendarListView.setLayoutManager(new LinearLayoutManager(this));
    }
    
    private void configureCalendar() {
        calendar.setTopbarVisible(true);
        calendar.setSelectionMode(SELECTION_MODE_SINGLE);
        calendar.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .commit();
        calendar.setShowOtherDates(SHOW_NONE);
        calendar.setSelectedDate(dateSelected);
        calendar.setCurrentDate(dateSelected);
        calendar.setAllowClickDaysOutsideCurrentMonth(false);

        calendar.addDecorator(new ShippingsDecorator(shippingRepository.getShippingDatesInMonth(dateSelected)));

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                dateSelected = date.getCalendar();
                calendarListView.setAdapter(new ShippingShortListAdapter(shippingRepository.getShippingsInDay(dateSelected), ShippingsActivity.this));
                calendar.removeDecorators();
                calendar.addDecorator(new ShippingsDecorator(shippingRepository.getShippingDatesInMonth(dateSelected)));
            }
        });
        calendar.setSelectionColor(getResources().getColor(R.color.red_app));
        calendar.setArrowColor(getResources().getColor(R.color.red_app));
        calendar.setLeftArrowMask(getResources().getDrawable(R.mipmap.icon_left_arrow_red));
        calendar.setRightArrowMask(getResources().getDrawable(R.mipmap.icon_right_arrow_red));
        calendar.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                dateSelected = date.getCalendar();
                calendar.removeDecorators();
                calendar.addDecorator(new ShippingsDecorator(shippingRepository.getShippingDatesInMonth(dateSelected)));
            }
        });
    }

    private void configureListTab() {
        monthLabel.setText(monthYearFormatter.format(dateSelected.getTime()));

        listListView.setLayoutManager(new LinearLayoutManager(this));
        listListView.setAdapter(new ShippingListAdapter(shippingRepository.getShippingsInMonth(dateSelected), ShippingsActivity.this));

        deleteBtn.setVisibility(View.GONE);

        listLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateSelected.set(Calendar.DAY_OF_MONTH, 1);
                dateSelected.add(Calendar.MONTH, -1);
                monthLabel.setText(monthYearFormatter.format(dateSelected.getTime()));
                listListView.setAdapter(new ShippingListAdapter(shippingRepository.getShippingsInMonth(dateSelected), ShippingsActivity.this));
                deleteBtn.setVisibility(View.GONE);

            }
        });

        listRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateSelected.set(Calendar.DAY_OF_MONTH, 1);
                dateSelected.add(Calendar.MONTH, 1);
                monthLabel.setText(monthYearFormatter.format(dateSelected.getTime()));
                listListView.setAdapter(new ShippingListAdapter(shippingRepository.getShippingsInMonth(dateSelected), ShippingsActivity.this));
                deleteBtn.setVisibility(View.GONE);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean onlyOne = true;
                final TwoButtonsDialog deleteDialog = new TwoButtonsDialog(ShippingsActivity.this, R.color.red_app);
                deleteDialog.setMessage("Se borrará" +(onlyOne ? "" : "n") +" " +"1" +" compra" +(onlyOne ? "." : "s."));
                deleteDialog.setLeftButtonListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //delete shipping
                    }
                });
                deleteDialog.show();
            }
        });
    }

    public void setDeleteButtonVisibility(boolean visible) {
        deleteBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (deleteBtn.getVisibility() == View.VISIBLE) {
            final TwoButtonsDialog backDialog = new TwoButtonsDialog(this, R.color.red_app);
            backDialog.setMessage("Si sale, no se borrarán las compras marcados.");
            backDialog.setLeftButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShippingsActivity.super.onBackPressed();
                }
            });
            backDialog.show();
        }
        else
            super.onBackPressed();
    }

    public class ShippingsDecorator implements DayViewDecorator {

        private final HashSet<CalendarDay> dates;
        private Context mContext;

        public ShippingsDecorator(Collection<CalendarDay> dates) {
            this.mContext = ShippingsActivity.this;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(12, mContext.getResources().getColor(R.color.red_app)));
        }
    }
}
