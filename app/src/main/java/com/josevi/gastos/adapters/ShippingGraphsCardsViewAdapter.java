package com.josevi.gastos.adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.josevi.gastos.R;
import com.josevi.gastos.cards.Card;
import com.josevi.gastos.models.Shipping;
import com.josevi.gastos.models.enums.Store;
import com.josevi.gastos.repositories.ShippingRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.josevi.gastos.utils.Constantes.SHIPPINGS_GRAPHS_CARD_PIECHART_NUMBER;

public class ShippingGraphsCardsViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Card> cards;
    ShippingRepository shippingRepository;
    Activity activity;

    Calendar start, end;

    public ShippingGraphsCardsViewAdapter(List<Card> cards, Calendar start, Calendar end, Activity activity) {
        this.cards = cards;
        this.shippingRepository = new ShippingRepository();
        this.activity = activity;
        this.start = start;
        this.end = end;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        switch(viewType) {
            case SHIPPINGS_GRAPHS_CARD_PIECHART_NUMBER:
                view = View.inflate(parent.getContext(), R.layout.card_shipping_graphs_piechart, null);
                viewHolder = new ViewHolderPieChart(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch(getItemViewType(position)) {
            case SHIPPINGS_GRAPHS_CARD_PIECHART_NUMBER:
                ViewHolderPieChart viewHolderPieChart = (ViewHolderPieChart) holder;
                configureHolderPieChart(viewHolderPieChart);
                break;
        }
    }

    private void configureHolderPieChart(ViewHolderPieChart viewHolder) {

    }

    private void configurePieChart(PieChart chart) {

    }

    private void configureHolderAvgDailyValues(ViewHolderPieChart viewHolder) {
        viewHolder.chart.setUsePercentValues(true);
        viewHolder.chart.getDescription().setEnabled(false);
        viewHolder.chart.setExtraOffsets(5, 10, 5, 5);

        viewHolder.chart.setDragDecelerationFrictionCoef(1f);

        viewHolder.chart.setDrawHoleEnabled(false);
//        viewHolder.chart.setHoleColor(Color.WHITE);

//        viewHolder.chart.setTransparentCircleColor(Color.WHITE);
//        viewHolder.chart.setTransparentCircleAlpha(150);

//        viewHolder.chart.setHoleRadius(70f);
//        viewHolder.chart.setTransparentCircleRadius(75f);

//        viewHolder.chart.setDrawCenterText(true);

//        viewHolder.chart.setRotationAngle(270);
        // enable rotation of the chart by touch
        viewHolder.chart.setRotationEnabled(false);
        viewHolder.chart.setHighlightPerTapEnabled(false);
        viewHolder.chart.setDrawEntryLabels(false);

        Legend l = viewHolder.chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);
        l.setEnabled(true);

        setPieChartData(viewHolder.chart);

        viewHolder.chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private void setPieChartData(PieChart chart) {
        List<PieEntry> entries = new ArrayList<PieEntry>();
        List<Integer> colors = new ArrayList<Integer>();

        Map<Store, List<Shipping>> shippingsMappedByStore =
                shippingRepository.getShippingsInMonthMappedByStore(start);
        Map<Store, Double> totalShippingsMappedByStore = new HashMap<Store, Double>();
        double total = 0;
        for (Store store: Store.values()) {
            double totalStore = 0;
            for (Shipping shipping: shippingsMappedByStore.get(store))
                totalStore += shipping.getTotalPrize();
            totalShippingsMappedByStore.put(store, totalStore);
            total += totalStore;
        }

        if (total != 0) {
            int idx = 0;
            for (Store store : Store.values()) {
                entries.add(new PieEntry((float) (100 * totalShippingsMappedByStore.get(store) / total), store));
                colors.add(activity.getResources().getColor(R.color.red_app) - 10 * idx++);
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);
        dataSet.setDrawValues(true);
        dataSet.setValueTextSize(10);

        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(1f);
        
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.format("%.1f", value) +" %";
            }
        });
        chart.setData(data);
        // undo all highlights
        chart.highlightValues(null);
        chart.setExtraBottomOffset(-5); //elimina espacio entre el grafico y la leyenda

        chart.invalidate();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolderPieChart extends RecyclerView.ViewHolder {

        private CardView cardContainer;
        private PieChart chart;

        public ViewHolderPieChart(View v) {
            super(v);
            cardContainer = v.findViewById(R.id.shipping_graphs_piechart_card_card_view);
            chart = v.findViewById(R.id.shipping_graphs_piechart_chart);
        }
    }
}
