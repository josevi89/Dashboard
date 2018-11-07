package com.josevi.gastos.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.josevi.gastos.R;
import com.josevi.gastos.activities.NewNotificationActivity;
import com.josevi.gastos.activities.NotificationsActivity;
import com.josevi.gastos.cards.CoreCard;
import com.josevi.gastos.models.Notification;
import com.josevi.gastos.models.Shipping;
import com.josevi.gastos.repositories.NotificationRepository;
import com.josevi.gastos.repositories.ShippingRepository;
import com.josevi.gastos.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.josevi.gastos.utils.Constantes.CORE_CARD_GASTOS_NUMBER;
import static com.josevi.gastos.utils.Constantes.CORE_CARD_NOTIFICATIONS_NUMBER;

public class CoreCardsViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<CoreCard> cards;

    ShippingRepository shippingRepository;
    NotificationRepository notificationRepository;

    Activity activity;

    public CoreCardsViewAdapter(List<CoreCard> cards, Activity activity) {
        this.cards = cards;
        this.shippingRepository = new ShippingRepository();
        this.notificationRepository = new NotificationRepository();
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        if(viewType == CORE_CARD_NOTIFICATIONS_NUMBER) {
            view = View.inflate(parent.getContext(), R.layout.core_card_notifications, null);
            viewHolder = new ViewHolderNotifications(view);
        }
        else if(viewType == CORE_CARD_GASTOS_NUMBER) {
            view = View.inflate(parent.getContext(), R.layout.core_card_gastos, null);
            viewHolder = new ViewHolderGastos(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if(viewType == CORE_CARD_NOTIFICATIONS_NUMBER) {
            ViewHolderNotifications viewHolderNotifications = (ViewHolderNotifications) holder;
            configureHolderNotifications(viewHolderNotifications);
        }
        else if(viewType == CORE_CARD_GASTOS_NUMBER) {
            ViewHolderGastos viewHolderGastos = (ViewHolderGastos) holder;
            configureHolderGastos(viewHolderGastos);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void configureHolderGastos(ViewHolderGastos viewHolderGastos) {
        List<List<List<Shipping>>> monthShippings = shippingRepository.getShippingsInThreeMonths(Calendar.getInstance());
        double total = 0;
        for (List<Shipping> dayShippings: monthShippings.get(1))
            for (Shipping shipping: dayShippings)
                total += shipping.getTotalPrize();
        viewHolderGastos.totalText.setText(Utils.getDecimalFormat(2, total) +" €");
        configureGastosCoreChart(viewHolderGastos.gastosChart);
        setGastosCoreChartData(viewHolderGastos.gastosChart, monthShippings);
    }
    
    public void configureGastosCoreChart(BarChart chart) {
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);

        chart.getDescription().setEnabled(false);

        chart.setNoDataTextColor(activity.getResources().getColor(R.color.red_app));
        chart.setNoDataText(activity.getResources().getString(R.string.no_data_text));

        // enable scaling and dragging
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setHighlightFullBarEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return Utils.getDecimalFormat(2, value);
            }
        });
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(1f);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
//        rightAxis.setValueFormatter(createKcalValueFormatter());
//        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        rightAxis.setAxisMinimum(0f);
//        rightAxis.setGranularity(30f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.getLegend().setEnabled(true);

        chart.invalidate();
    }

    public void setGastosCoreChartData(BarChart chart, List<List<List<Shipping>>> monthShippings) {
        BarDataSet[] dataSets = new BarDataSet[3];
        for (int m = 0; m < 3; m++) {
            if (monthShippings.get(m) != null) {
                List<BarEntry> entries = new ArrayList<BarEntry>();
                float monthTotal = 0;
                for (int d = 0; d < monthShippings.get(m).size(); d++) {
                    float dayTotal = 0;
                    for (Shipping shipping : monthShippings.get(m).get(new Integer(d)))
                        dayTotal += shipping.getTotalPrize();
                    entries.add(new BarEntry((float) d, dayTotal));
                    monthTotal += dayTotal;
                }

                String label = "";
                switch (m) {
                    case 0:
                        label = "Octubre";
                        LimitLine avg = new LimitLine(monthTotal / monthShippings.size(), "media");
                        avg.setLineWidth(0.5f);
                        avg.setLineColor(activity.getResources().getColor(R.color.blue));
                        avg.enableDashedLine(10f, 10f, 0f);
                        avg.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                        avg.setTextSize(10f);
                        chart.getAxisLeft().addLimitLine(avg);

                        break;
                    case 1:
                        label = "Septiembre";
                        break;
                    case 2:
                        label = "Agosto";
                        break;
                }
                BarDataSet dataSet = new BarDataSet(entries, label);
                dataSet.setDrawIcons(false);
                dataSet.setDrawValues(false);
                dataSet.setHighlightEnabled(false);
                int alpha = 100 - 15 * m;
                dataSet.setColor(activity.getResources().getColor(R.color.red), alpha);
                dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                dataSets[m] = dataSet;
            }
            else {
                String label = "";
                switch (m) {
                    case 0:
                        label = "Octubre";
                        break;
                    case 1:
                        label = "Septiembre";
                        break;
                    case 2:
                        label = "Agosto";
                        break;
                }
                dataSets[m] = new BarDataSet(new ArrayList<BarEntry>(), label);
            }
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(31);

        BarData data = new BarData(dataSets);
        chart.setData(data);
    }

    public void configureHolderNotifications(ViewHolderNotifications viewHolderNotifications) {
        Calendar now = Calendar.getInstance();
        viewHolderNotifications.todayText.setText(new SimpleDateFormat("dd MMM yyyy").format(now.getTime()));
        List<Notification> notificationsNextWeek = notificationRepository.getNotificationsNextWeek();
        NotificationListAdapter notificationListAdapter = new NotificationListAdapter(notificationsNextWeek, activity);
        viewHolderNotifications.notificationsView.setAdapter(notificationListAdapter);
        viewHolderNotifications.notificationsView.setLayoutManager(new LinearLayoutManager(activity));
        viewHolderNotifications.notificationsView.setVisibility(notificationsNextWeek.isEmpty() ? View.GONE : View.VISIBLE);

        viewHolderNotifications.cardContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, NotificationsActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    public class ViewHolderNotifications extends RecyclerView.ViewHolder {

        private CardView cardContainer;
        private TextView todayText;
        private RecyclerView notificationsView;

        public ViewHolderNotifications(View v) {
            super(v);
            cardContainer = v.findViewById(R.id.core_notifications_card_card_view);
            todayText = v.findViewById(R.id.core_notifications_today_text);
            notificationsView = v.findViewById(R.id.core_notifications_notifications_container);
        }
    }

    public class ViewHolderGastos extends RecyclerView.ViewHolder {

        private BarChart gastosChart;
        private TextView totalText;

        public ViewHolderGastos(View v) {
            super(v);
            gastosChart = v.findViewById(R.id.core_gastos_chart);
            totalText = v.findViewById(R.id.core_gastos_card_total_text);
        }
    }
}
