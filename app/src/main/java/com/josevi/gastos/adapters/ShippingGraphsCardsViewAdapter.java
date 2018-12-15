package com.josevi.gastos.adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.josevi.gastos.R;
import com.josevi.gastos.cards.Card;
import com.josevi.gastos.cards.Legend;
import com.josevi.gastos.models.Product;
import com.josevi.gastos.models.Shipping;
import com.josevi.gastos.models.enums.Group;
import com.josevi.gastos.models.enums.Store;
import com.josevi.gastos.repositories.ShippingRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.josevi.gastos.utils.Constantes.SHIPPINGS_GRAPHS_CARD_PERCENT_CHARTS_NUMBER;
import static com.josevi.gastos.utils.Constantes.SHIPPINGS_GRAPHS_CARD_TOTAL_CHARTS_NUMBER;
import static com.josevi.gastos.utils.Constantes.SHIPPINGS_GRAPHS_CARD_WEEKLY_TOTAL_CHARTS_NUMBER;

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

    public ShippingGraphsCardsViewAdapter(List<Card> cards, Activity activity) {
        this.cards = cards;
        this.shippingRepository = new ShippingRepository();
        this.activity = activity;
        this.start = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        this.end = Calendar.getInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        switch(viewType) {
            case SHIPPINGS_GRAPHS_CARD_PERCENT_CHARTS_NUMBER:
                view = View.inflate(parent.getContext(), R.layout.card_shipping_graphs_percent_charts, null);
                viewHolder = new ViewHolderPercentCharts(view);
                break;
            case SHIPPINGS_GRAPHS_CARD_TOTAL_CHARTS_NUMBER:
                view = View.inflate(parent.getContext(), R.layout.card_shipping_graphs_total_charts, null);
                viewHolder = new ViewHolderTotalCharts(view);
                break;
            case SHIPPINGS_GRAPHS_CARD_WEEKLY_TOTAL_CHARTS_NUMBER:
                view = View.inflate(parent.getContext(), R.layout.card_shipping_graphs_weekly_total_charts, null);
                viewHolder = new ViewHolderWeeklyTotalCharts(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch(getItemViewType(position)) {
            case SHIPPINGS_GRAPHS_CARD_PERCENT_CHARTS_NUMBER:
                ViewHolderPercentCharts viewHolderPercentCharts = (ViewHolderPercentCharts) holder;
                configureHolderPercentCharts(viewHolderPercentCharts);
                break;
            case SHIPPINGS_GRAPHS_CARD_TOTAL_CHARTS_NUMBER:
                ViewHolderTotalCharts viewHolderTotalCharts = (ViewHolderTotalCharts) holder;
                configureHolderTotalCharts(viewHolderTotalCharts);
                break;
            case SHIPPINGS_GRAPHS_CARD_WEEKLY_TOTAL_CHARTS_NUMBER:
                ViewHolderWeeklyTotalCharts viewHolderWeeklyTotalCharts = (ViewHolderWeeklyTotalCharts) holder;
                configureHolderWeeklyTotalCharts(viewHolderWeeklyTotalCharts);
                break;
        }
    }

    Store percentSecondaryGraphStoreSelected = null;

    private void configureHolderPercentCharts(final ViewHolderPercentCharts holder) {
        configurePieChart(holder.principalChart);
        configurePieChart(holder.secondaryChart);
        setPercentPrincipalChartData(holder);
        holder.secondaryChartContainer.setVisibility(View.GONE);

        holder.mercadonaPercentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configurePieChart(holder.secondaryChart);
                setPercentSecondaryChartData(holder, Store.MERCADONA, holder.secondaryChartHighContrastCheckBox.isChecked());
                holder.secondaryChartContainer.setVisibility(View.VISIBLE);
                holder.secondaryChart.notifyDataSetChanged();
                holder.mercadonaPercentContainer.setAlpha(1f);
                holder.estancoPercentContainer.setAlpha(0.4f);
            }
        });

        holder.estancoPercentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configurePieChart(holder.secondaryChart);
                setPercentSecondaryChartData(holder, Store.ESTANCO, holder.secondaryChartHighContrastCheckBox.isChecked());
                holder.secondaryChartContainer.setVisibility(View.VISIBLE);
                holder.secondaryChart.notifyDataSetChanged();
                holder.mercadonaPercentContainer.setAlpha(0.4f);
                holder.estancoPercentContainer.setAlpha(1f);
            }
        });

        holder.secondaryChartHighContrastCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (percentSecondaryGraphStoreSelected != null)
                    setPercentSecondaryChartData(holder, percentSecondaryGraphStoreSelected, checked);
            }
        });

        holder.unexpandSecondaryChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.secondaryChartContainer.setVisibility(View.GONE);
                holder.mercadonaPercentContainer.setAlpha(1f);
                holder.estancoPercentContainer.setAlpha(1f);
            }
        });
    }

    private void configurePieChart(PieChart chart) {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(1f);

        chart.setDrawHoleEnabled(false);
//        chart.setHoleColor(Color.WHITE);

//        chart.setTransparentCircleColor(Color.WHITE);
//        chart.setTransparentCircleAlpha(150);

//        chart.setHoleRadius(70f);
//        chart.setTransparentCircleRadius(75f);

//        chart.setDrawCenterText(true);

//        chart.setRotationAngle(270);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(false);
        chart.setDrawEntryLabels(false);

        chart.getLegend().setEnabled(false);

        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private void setPercentPrincipalChartData(ViewHolderPercentCharts holder) {
        PieChart chart = holder.principalChart;
        List<PieEntry> entries = new ArrayList<PieEntry>();
        List<Integer> colors = new ArrayList<Integer>();
        List<Legend> legend = new ArrayList<Legend>();

        Map<Store, List<Shipping>> shippingsMappedByStore =
                shippingRepository.getShippingsInMonthMappedByStore(end);
        Map<Group, Double> totalShippingsMappedByGroup = new HashMap<Group, Double>();
        for (Group group: Group.vals()) {
            totalShippingsMappedByGroup.put(group, 0d);
//            colors.add(group.color(activity, false));
        }
        double total = 0, mercadonaTotal = 0, estancoTotal = 0;
        for (Store store: Store.vals()) {
            double storeTotal = 0;
            for (Shipping shipping: shippingsMappedByStore.get(store)) {
                for (Product product: shipping.getProducts()) {
                    totalShippingsMappedByGroup.put(product.getGroup(),
                            totalShippingsMappedByGroup.get(product.getGroup()) +
                                    shipping.get(product.getCode()).prize * shipping.get(product.getCode()).qty);
                }
                storeTotal += shipping.getTotalPrize();
            }
            total += storeTotal;
            switch (store) {
                case MERCADONA:
                    mercadonaTotal = storeTotal;
                    holder.mercadonaPercent.setText(String.format("%.1f", storeTotal) +" %");
                    break;
                case ESTANCO:
                    estancoTotal = storeTotal;
                    break;
            }
        }
        if (total > 0) {
            holder.mercadonaPercent.setText(String.format("%.1f", 100 * mercadonaTotal / total) +" %");
            holder.estancoPercent.setText(String.format("%.1f", 100 * estancoTotal / total) +" %");
            for (Group group: Group.vals()) {
                float groupPercent = (float)(100 * totalShippingsMappedByGroup.get(group) / total);
                if (groupPercent > 0) {
                    entries.add(new PieEntry(groupPercent, group.title() + " [" + String.format("%.1f", groupPercent) + " %]"));
                    colors.add(group.color(activity, false));
                    legend.add(new Legend(group.color(activity, false), group.title() + " [" + String.format("%.1f", groupPercent) + " %]"));
                }
            }
            holder.principalChartLegend.setLayoutManager(new LinearLayoutManager(activity));
            holder.principalChartLegend.setAdapter(new LegendListAdapter(legend, activity));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);
        dataSet.setDrawValues(false);
//        dataSet.setValueTextSize(10);

        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(1f);

        if (!colors.isEmpty())
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
        chart.notifyDataSetChanged();
    }

    private void setPercentSecondaryChartData(ViewHolderPercentCharts holder, Store store, boolean highContrastOn) {
        PieChart chart = holder.secondaryChart;
        percentSecondaryGraphStoreSelected = store;
        List<PieEntry> entries = new ArrayList<PieEntry>();
        List<Integer> colors = new ArrayList<Integer>();
        List<Legend> legend = new ArrayList<Legend>();

        if (store != null) {
            List<Shipping> shippingsInStore =
                    shippingRepository.getShippingsInMonthInStore(store, end);

            Map<Group, Double> totalShippingsMappedByGroup = new HashMap<Group, Double>();
            for (Group group : Group.getGroupsFromStore(store)) {
                totalShippingsMappedByGroup.put(group, 0d);
//                colors.add(group.color(activity, highContrastOn));
            }
            double total = 0;
            for (Shipping shipping : shippingsInStore) {
                for (Product product : shipping.getProducts()) {
                    totalShippingsMappedByGroup.put(product.getGroup(),
                            totalShippingsMappedByGroup.get(product.getGroup()) +
                                    shipping.get(product.getCode()).prize * shipping.get(product.getCode()).qty);
                }
                total += shipping.getTotalPrize();
            }

            if (total > 0) {
                for (Group group : Group.getGroupsFromStore(store)) {
                    float groupPercent = (float) (100 * totalShippingsMappedByGroup.get(group) / total);
                    if (groupPercent > 0) {
                        entries.add(new PieEntry(groupPercent, group.title() + " [" + String.format("%.1f", groupPercent) + " %]"));
                        colors.add(group.color(activity, highContrastOn));
                        legend.add(new Legend(group.color(activity, highContrastOn), group.title() + " [" + String.format("%.1f", groupPercent) + " %]"));
                    }
                }
                holder.secondaryChartLegend.setLayoutManager(new LinearLayoutManager(activity));
                holder.secondaryChartLegend.setAdapter(new LegendListAdapter(legend, activity));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);
        dataSet.setDrawValues(false);
        dataSet.setValueTextSize(10);

        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(1f);

        if (!colors.isEmpty())
            dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
//        data.setValueFormatter(new IValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                return String.format("%.1f", value) +" %";
//            }
//        });
        chart.setData(data);
        // undo all highlights
        chart.highlightValues(null);
        chart.setExtraBottomOffset(-5); //elimina espacio entre el grafico y la leyenda

        chart.invalidate();
        chart.notifyDataSetChanged();
    }

    Store totalSecondaryGraphStoreSelected = null;

    private void configureHolderTotalCharts(final ViewHolderTotalCharts holder) {
        configureBarChart(holder.principalChart);
        configureBarChart(holder.secondaryChart);
        setTotalPrincipalChartData(holder);
        holder.secondaryChartContainer.setVisibility(View.GONE);

        holder.mercadonaTotalContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureBarChart(holder.secondaryChart);
                setTotalSecondaryChartData(holder, Store.MERCADONA, holder.secondaryChartHighContrastCheckBox.isChecked());
                holder.secondaryChartContainer.setVisibility(View.VISIBLE);
                holder.mercadonaTotalContainer.setAlpha(1f);
                holder.estancoTotalContainer.setAlpha(0.4f);
            }
        });

        holder.estancoTotalContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureBarChart(holder.secondaryChart);
                setTotalSecondaryChartData(holder, Store.ESTANCO, holder.secondaryChartHighContrastCheckBox.isChecked());
                holder.secondaryChartContainer.setVisibility(View.VISIBLE);
                holder.mercadonaTotalContainer.setAlpha(0.4f);
                holder.estancoTotalContainer.setAlpha(1f);
            }
        });

        holder.secondaryChartHighContrastCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (totalSecondaryGraphStoreSelected != null)
                    setTotalSecondaryChartData(holder, totalSecondaryGraphStoreSelected, checked);
            }
        });

        holder.unexpandSecondaryChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.secondaryChartContainer.setVisibility(View.GONE);
                holder.mercadonaTotalContainer.setAlpha(1f);
                holder.estancoTotalContainer.setAlpha(1f);
            }
        });
    }

    private void configureBarChart(BarChart chart) {
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(false);

        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setHighlightFullBarEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);

        chart.getLegend().setEnabled(false);

        chart.setFitBars(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineWidth(2f);
        xAxis.setDrawGridLines(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisLineWidth(2f);
        chart.getAxisRight().setEnabled(false);

        chart.invalidate();
        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private void setTotalPrincipalChartData(ViewHolderTotalCharts holder) {
        BarChart chart = holder.principalChart;
        List<BarEntry> entries = new ArrayList<BarEntry>();
        List<Integer> colors = new ArrayList<Integer>();
        List<Legend> legend = new ArrayList<Legend>();
        List<Store> storesInChart = new ArrayList<Store>();

        Map<Store, List<Shipping>> shippingsMappedByStore =
                shippingRepository.getShippingsInMonthMappedByStore(end);
        Map<Group, Double> totalShippingsMappedByGroup = new HashMap<Group, Double>();
        for (Group group: Group.vals()) {
            totalShippingsMappedByGroup.put(group, 0d);
        }
        double total = 0;
        for (Store store: Store.vals()) {
            for (Shipping shipping: shippingsMappedByStore.get(store)) {
                if (!storesInChart.contains(store))
                    storesInChart.add(store);
                for (Product product: shipping.getProducts()) {
                    totalShippingsMappedByGroup.put(product.getGroup(),
                            totalShippingsMappedByGroup.get(product.getGroup()) +
                                    shipping.get(product.getCode()).prize * shipping.get(product.getCode()).qty);
                }
                total += shipping.getTotalPrize();
            }
        }
        if (total > 0) {
            for (Store store: storesInChart) {
                Group[] groupsInStore = Group.getGroupsFromStore(store);
                List<Float> groupTotals = new ArrayList<Float>();
                double storeTotal = 0;
                for (int g = 0; g < groupsInStore.length; g++) {
                    float groupTotal = (float)(double)(totalShippingsMappedByGroup.get(groupsInStore[g]));
                    if (groupTotal > 0) {
                        groupTotals.add(groupTotal);
                        storeTotal += groupTotal;
                        colors.add(groupsInStore[g].color(activity, false));
                        legend.add(new Legend(groupsInStore[g].color(activity, false),
                                groupsInStore[g].title() +" [" +String.format("%.2f", groupTotal) +" €]"));
                    }
                }
                float[] groupTotalsArray = new float[groupTotals.size()];
                for (int g = 0; g < groupTotals.size(); g++)
                    groupTotalsArray[g] = groupTotals.get(g);
                entries.add(new BarEntry(store.ordinal(), groupTotalsArray));
                switch (store) {
                    case MERCADONA:
                        holder.mercadonaTotal.setText(String.format("%.2f", storeTotal) +" €");
                        break;
                    case ESTANCO:
                        holder.estancoTotal.setText(String.format("%.2f", storeTotal) +" €");
                        break;
                }
            }
            holder.totalTotal.setText(String.format("%.2f", total) +" €");

            holder.principalChartLegend.setLayoutManager(new LinearLayoutManager(activity));
            holder.principalChartLegend.setAdapter(new LegendListAdapter(legend, activity));
        }

        BarDataSet dataSet = new BarDataSet(entries, "");

        dataSet.setDrawIcons(false);
        dataSet.setDrawValues(false);
//        dataSet.setValueTextSize(10);

        if (!colors.isEmpty())
            dataSet.setColors(colors);

        BarData data = new BarData(dataSet);

        chart.setData(data);

        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMinimum(-1);
        xAxis.setAxisMaximum(storesInChart.size());
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value == 0 || value == 1 ? Store.vals()[(int)value].title() : "";
            }
        });
        // undo all highlights
        chart.highlightValues(null);
        chart.setExtraBottomOffset(-5); //elimina espacio entre el grafico y la leyenda

        chart.invalidate();
        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
    }

    private void setTotalSecondaryChartData(ViewHolderTotalCharts holder, final Store store, boolean highContrastOn) {
        BarChart chart = holder.secondaryChart;
        totalSecondaryGraphStoreSelected = store;
        List<BarEntry> entries = new ArrayList<BarEntry>();
        List<Integer> colors = new ArrayList<Integer>();
        List<Legend> legend = new ArrayList<Legend>();

        if (store != null) {
            List<Shipping> shippingsInStore =
                    shippingRepository.getShippingsInMonthInStore(store, end);

            Map<Group, Double> totalShippingsMappedByGroup = new HashMap<Group, Double>();
            Group[] groupsInStore = Group.getGroupsFromStore(store);
            for (Group group : groupsInStore) {
                totalShippingsMappedByGroup.put(group, 0d);
//                colors.add(group.color(activity, highContrastOn));
            }
            double total = 0;
            for (Shipping shipping : shippingsInStore) {
                for (Product product : shipping.getProducts()) {
                    totalShippingsMappedByGroup.put(product.getGroup(),
                            totalShippingsMappedByGroup.get(product.getGroup()) +
                                    shipping.get(product.getCode()).prize * shipping.get(product.getCode()).qty);
                }
                total += shipping.getTotalPrize();
            }

            if (total > 0) {
                for (int g = 0; g < groupsInStore.length; g++) {
                    float groupTotal = (float)(double)totalShippingsMappedByGroup.get(groupsInStore[g]);
                    if (groupTotal > 0) {
                        float[] groupTotalArray = {groupTotal};
                        entries.add(new BarEntry(g, groupTotalArray));
                        colors.add(groupsInStore[g].color(activity, highContrastOn));
                        legend.add(new Legend(groupsInStore[g].color(activity, highContrastOn),
                                groupsInStore[g].title() +" [" +String.format("%.2f", groupTotal) +" €]"));
                    }
                }
                holder.secondaryChartLegend.setLayoutManager(new LinearLayoutManager(activity));
                holder.secondaryChartLegend.setAdapter(new LegendListAdapter(legend, activity));
            }
        }

        BarDataSet dataSet = new BarDataSet(entries, "");

        dataSet.setDrawIcons(false);
        dataSet.setDrawValues(false);
//        dataSet.setValueTextSize(10);

        if (!colors.isEmpty())
            dataSet.setColors(colors);

        BarData data = new BarData(dataSet);
//        data.setValueFormatter(new IValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                return String.format("%.1f", value) +" %";
//            }
//        });
        chart.setData(data);

        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMinimum(-1);
        xAxis.setAxisMaximum(Group.getGroupsFromStore(store).length);
        xAxis.setLabelRotationAngle(90f);
        xAxis.setLabelCount((int) xAxis.getAxisMaximum() + 1);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value != -1 && value < Group.getGroupsFromStore(store).length ?
                        Group.getGroupsFromStore(store)[(int)value].title() : "";
            }
        });
        // undo all highlights
        chart.highlightValues(null);
//        chart.setExtraBottomOffset(-5); //elimina espacio entre el grafico y la leyenda

        chart.invalidate();
        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
    }

    Store weeklytotalSecondaryGraphStoreSelected = null;

    private void configureHolderWeeklyTotalCharts(final ViewHolderWeeklyTotalCharts holder) {

        configureBarChart(holder.principalChart);
        configureBarChart(holder.secondaryChart);
        setWeeklyTotalPrincipalChartData(holder);
        holder.secondaryChartContainer.setVisibility(View.GONE);

        holder.mercadonaAvgContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureBarChart(holder.secondaryChart);
                setWeeklyTotalSecondaryChartData(holder, Store.MERCADONA, holder.secondaryChartHighContrastCheckBox.isChecked());
                holder.secondaryChartContainer.setVisibility(View.VISIBLE);
                holder.mercadonaAvgContainer.setAlpha(1f);
                holder.estancoAvgContainer.setAlpha(0.4f);
            }
        });

        holder.estancoAvgContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureBarChart(holder.secondaryChart);
                setWeeklyTotalSecondaryChartData(holder, Store.ESTANCO, holder.secondaryChartHighContrastCheckBox.isChecked());
                holder.secondaryChartContainer.setVisibility(View.VISIBLE);
                holder.mercadonaAvgContainer.setAlpha(0.4f);
                holder.estancoAvgContainer.setAlpha(1f);
            }
        });

        holder.secondaryChartHighContrastCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (weeklytotalSecondaryGraphStoreSelected != null)
                    setWeeklyTotalSecondaryChartData(holder, weeklytotalSecondaryGraphStoreSelected, checked);
            }
        });

        holder.unexpandSecondaryChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.secondaryChartContainer.setVisibility(View.GONE);
                holder.mercadonaAvgContainer.setAlpha(1f);
                holder.estancoAvgContainer.setAlpha(1f);
            }
        });
    }

    private void setWeeklyTotalPrincipalChartData(ViewHolderWeeklyTotalCharts holder) {
        BarChart chart = holder.principalChart;
        List<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        List<Store> storesInGraph = new ArrayList<Store>();

        Map<Store, List<Shipping>> shippingsMappedByStore =
                shippingRepository.getShippingsInMonthMappedByStore(end);
        Map<Store, List<Map<Group, Double>>> totalShippingsMappedByStoreDayOfWeekAndGroup =
                new HashMap<Store, List<Map<Group, Double>>>();
        for (Store store: Store.vals()) {
            List<Map<Group, Double>> emptyStore = new ArrayList<Map<Group, Double>>();
            for (int d = 0; d < 7; d++) {
                Map<Group, Double> emptyDay = new HashMap<Group, Double>();
                for (Group group : Group.getGroupsFromStore(store))
                    emptyDay.put(group, 0d);
                emptyStore.add(emptyDay);
            }
            totalShippingsMappedByStoreDayOfWeekAndGroup.put(store, emptyStore);

            for (Shipping shipping : shippingsMappedByStore.get(store)) {
                if (!storesInGraph.contains(store))
                    storesInGraph.add(store);
                Calendar shippingDate = Calendar.getInstance();
                shippingDate.setTime(shipping.getDate());
                int dayOfWeek = shippingDate.get(Calendar.DAY_OF_WEEK) != 1 ?
                        shippingDate.get(Calendar.DAY_OF_WEEK) - 2 : 6;
                for (Product product : shipping.getProducts()) {
                    double tmpTotal = totalShippingsMappedByStoreDayOfWeekAndGroup.get(store).get(dayOfWeek).get(product.getGroup());
                    tmpTotal += shipping.get(product.getCode()).qty * shipping.get(product.getCode()).prize;
                    totalShippingsMappedByStoreDayOfWeekAndGroup.get(store).get(dayOfWeek).put(product.getGroup(), tmpTotal);
                }
            }
        }

        int nStores = storesInGraph.size();
        for (int s = 0; s < nStores; s++) {
            Store store = storesInGraph.get(s);
            List<BarEntry> entries = new ArrayList<BarEntry>();
            List<Integer> colors = new ArrayList<Integer>();
            for (int d = 0; d < 7; d++) {
                double offset = 0;
                if(nStores % 2 == 0) {
                    int half = nStores / 2;
                    offset = 1.0 / nStores * ((s + 0.5 - half) + (s > half ? 1 : 0));
                }
                else {
                    double half = nStores / 2.0;
                    offset = 1.0 / nStores * (s + 0.5 - half);
                }

                List<Float> totalStoreDay = new ArrayList<Float>();
                for (Group group : Group.getGroupsFromStore(store)) {
                    if (totalShippingsMappedByStoreDayOfWeekAndGroup.get(store).get(d).get(group) > 0) {
                        totalStoreDay.add((float) (double) totalShippingsMappedByStoreDayOfWeekAndGroup.get(store).get(d).get(group));
                        colors.add(group.color(activity, false));
                    }
                }
                
                if (!totalStoreDay.isEmpty()) {
                    float[] storeDayValues = new float[totalStoreDay.size()];
                    for (int g = 0; g < totalStoreDay.size(); g++) {
                        storeDayValues[g] = totalStoreDay.get(g);
                    }
                    entries.add(new BarEntry(d + (float) offset, storeDayValues));
                }
                else {
                    entries.add(new BarEntry(d + (float) offset, new float[]{0}));
                    colors.add(activity.getResources().getColor(R.color.black));
                }
            }
            BarDataSet dataSet = new BarDataSet(entries, store.title());
            dataSet.setColors(colors);
            dataSet.setDrawIcons(false);
            dataSet.setDrawValues(false);
            dataSets.add(dataSet);
        }
//        List<BarEntry> dayNames = new ArrayList<BarEntry>();
        double total = 0, totalMercadona = 0, totalEstanco = 0;
        for (int d = 0; d < 7; d++) {
            float totalDay = 0;
            for (int ds = 0; ds < dataSets.size(); ds++) {
                totalDay += dataSets.get(ds).getEntryForIndex(d).getPositiveSum();
                if (ds == 0)
                    totalMercadona += dataSets.get(ds).getEntryForIndex(d).getPositiveSum();
                else
                    totalEstanco += dataSets.get(ds).getEntryForIndex(d).getPositiveSum();
            }
//            dayNames.add(new BarEntry(d, 0));
            total += totalDay;
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMinimum(-1);
        xAxis.setAxisMaximum(7);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String[] daysOfWeek = {"lun", "mar", "mie", "jue", "vie", "sab", "dom"};
                return value != -1 && value != 7 ? daysOfWeek[(int)value] : "";
            }
        });

        holder.dayAvg.setText(String.format("%.2f", total / 7) +" €");
        holder.mercadonaAvg.setText(String.format("%.2f", totalMercadona / 7) +" €");
        holder.estancoAvg.setText(String.format("%.2f", totalEstanco / 7) +" €");
//        BarDataSet dayNamesDataSet = new BarDataSet(dayNames, "");
//        dataSets.add(0, dayNamesDataSet);

        BarData data = new BarData(dataSets);
        data.setBarWidth(1f / storesInGraph.size());

        chart.setData(data);
//        chart.groupBars(0, 0.51f, 0f);
        chart.setFitBars(true);
        // undo all highlights
        chart.highlightValues(null);
        chart.setExtraBottomOffset(-5); //elimina espacio entre el grafico y la leyenda

        List<Legend> legend = new ArrayList<Legend>();
        for (Store store: storesInGraph)
            legend.add(new Legend(store.color(activity), store.title()));

        holder.principalChartLegend.setLayoutManager(new LinearLayoutManager(activity));
        holder.principalChartLegend.setAdapter(new LegendListAdapter(legend, activity));

        chart.invalidate();
        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
    }

    private void setWeeklyTotalSecondaryChartData(ViewHolderWeeklyTotalCharts holder, Store store, boolean highContrastOn) {
        BarChart chart = holder.secondaryChart;
        weeklytotalSecondaryGraphStoreSelected = store;

        List<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//        List<Legend> legend = new ArrayList<Legend>();
        List<Group> groupsInChart = new ArrayList<Group>();

        Map<Store, List<Shipping>> shippingsMappedByStore =
                shippingRepository.getShippingsInMonthMappedByStore(end);
        Map<Store, List<Map<Group, Double>>> totalShippingsMappedByStoreDayOfWeekAndGroup =
                new HashMap<Store, List<Map<Group, Double>>>();
        List<Map<Group, Double>> emptyStore = new ArrayList<Map<Group, Double>>();
        for (int d = 0; d < 7; d++) {
            Map<Group, Double> emptyDay = new HashMap<Group, Double>();
            for (Group group: Group.getGroupsFromStore(store))
                emptyDay.put(group, 0d);
            emptyStore.add(emptyDay);
        }
        totalShippingsMappedByStoreDayOfWeekAndGroup.put(store, emptyStore);

        for (Shipping shipping: shippingsMappedByStore.get(store)) {
            Calendar shippingDate = Calendar.getInstance();
            shippingDate.setTime(shipping.getDate());
            int dayOfWeek = shippingDate.get(Calendar.DAY_OF_WEEK) != 1 ?
                    shippingDate.get(Calendar.DAY_OF_WEEK) - 2 : 6;
            for (Product product: shipping.getProducts()) {
                double tmpTotal = totalShippingsMappedByStoreDayOfWeekAndGroup.get(store).get(dayOfWeek).get(product.getGroup());
                tmpTotal += shipping.get(product.getCode()).qty * shipping.get(product.getCode()).prize;
                totalShippingsMappedByStoreDayOfWeekAndGroup.get(store).get(dayOfWeek).put(product.getGroup(), tmpTotal);
                if (!groupsInChart.contains(product.getGroup()))
                    groupsInChart.add(product.getGroup());

            }
        }
        int nGroups = groupsInChart.size();
        for (int g = 0; g < nGroups; g++) {
            Group group = groupsInChart.get(g);
            List<BarEntry> entries = new ArrayList<BarEntry>();
            double offset = 0;
            if(nGroups % 2 == 0) {
                int half = nGroups / 2;
                offset = 1.0 / nGroups * ((g - half) + (g > half ? 1 : 0));
            }
            else {
                double half = nGroups / 2.0;
                offset = 1.0 / nGroups * (g + 0.5 - half);
            }
            for (int d = 0; d < 7; d++) {
                float totalDayGroup = (float)(double) totalShippingsMappedByStoreDayOfWeekAndGroup.get(store).get(d).get(group);
                if (totalDayGroup > 0) {
                    entries.add(new BarEntry(d + (float) offset, new float[]{totalDayGroup}));
                }
            }
            BarDataSet dataSet = new BarDataSet(entries, group.title());
            dataSet.setColor(group.color(activity, highContrastOn));
            dataSet.setDrawIcons(false);
            dataSet.setDrawValues(false);
            dataSets.add(dataSet);
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMinimum(-1);
        xAxis.setAxisMaximum(7);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String[] daysOfWeek = {"lun", "mar", "mie", "jue", "vie", "sab", "dom"};
                return value != -1 && value < 7 ? daysOfWeek[(int)value] : "";
            }
        });

        BarData data = new BarData(dataSets);
        data.setBarWidth(1f / nGroups);
        chart.setData(data);
//        chart.groupBars(0, 0.51f, 0f);
        chart.setFitBars(true);
        // undo all highlights
        chart.highlightValues(null);
        chart.setExtraBottomOffset(-5); //elimina espacio entre el grafico y la leyenda

        List<Legend> legend = new ArrayList<Legend>();
        for (Group group: groupsInChart)
            legend.add(new Legend(group.color(activity,highContrastOn), group.title()));

        holder.secondaryChartLegend.setLayoutManager(new LinearLayoutManager(activity));
        holder.secondaryChartLegend.setAdapter(new LegendListAdapter(legend, activity));

        chart.invalidate();
        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolderPercentCharts extends RecyclerView.ViewHolder {

        private CardView cardContainer;
        private PieChart principalChart, secondaryChart;
        private LinearLayout mercadonaPercentContainer, estancoPercentContainer, rollPercentContainer,
                secondaryChartContainer;
        private ImageView unexpandSecondaryChartButton;
        private RecyclerView principalChartLegend, secondaryChartLegend;
        private CheckBox secondaryChartHighContrastCheckBox;
        private TextView mercadonaPercent, estancoPercent;

        public ViewHolderPercentCharts(View v) {
            super(v);
            cardContainer = v.findViewById(R.id.shipping_graphs_percent_charts_card_card_view);
            principalChart = v.findViewById(R.id.shipping_graphs_percent_charts_principal_chart);
            secondaryChart = v.findViewById(R.id.shipping_graphs_percent_charts_secondary_chart);
            mercadonaPercentContainer = v.findViewById(R.id.shipping_graphs_percent_charts_mercadona_percent_container);
            estancoPercentContainer = v.findViewById(R.id.shipping_graphs_percent_charts_estanco_percent_container);
            rollPercentContainer = v.findViewById(R.id.shipping_graphs_percent_charts_roll_percent_container);
            secondaryChartContainer = v.findViewById(R.id.shipping_graphs_percent_charts_secondary_chart_container);
            unexpandSecondaryChartButton = v.findViewById(R.id.shipping_graphs_percent_charts_secondary_chart_unexpand_button);
            principalChartLegend = v.findViewById(R.id.shipping_graphs_percent_charts_principal_chart_legend);
            secondaryChartLegend = v.findViewById(R.id.shipping_graphs_percent_charts_secondary_chart_legend);
            secondaryChartHighContrastCheckBox = v.findViewById(R.id.shipping_graphs_percent_charts_secondary_chart_high_contrast_checkbox);
            mercadonaPercent = v.findViewById(R.id.shipping_graphs_percent_charts_percent_mercadona);
            estancoPercent = v.findViewById(R.id.shipping_graphs_percent_charts_percent_estanco);
        }
    }

    public class ViewHolderTotalCharts extends RecyclerView.ViewHolder {

        private CardView cardContainer;
        private BarChart principalChart, secondaryChart;
        private LinearLayout mercadonaTotalContainer, estancoTotalContainer, rollTotalContainer,
                secondaryChartContainer;
        private ImageView unexpandSecondaryChartButton;
        private RecyclerView principalChartLegend, secondaryChartLegend;
        private CheckBox secondaryChartHighContrastCheckBox;
        private TextView totalTotal, mercadonaTotal, estancoTotal;

        public ViewHolderTotalCharts(View v) {
            super(v);
            cardContainer = v.findViewById(R.id.shipping_graphs_total_charts_card_card_view);
            principalChart = v.findViewById(R.id.shipping_graphs_total_charts_principal_chart);
            secondaryChart = v.findViewById(R.id.shipping_graphs_total_charts_secondary_chart);
            mercadonaTotalContainer = v.findViewById(R.id.shipping_graphs_total_charts_mercadona_total_container);
            estancoTotalContainer = v.findViewById(R.id.shipping_graphs_total_charts_estanco_total_container);
            rollTotalContainer = v.findViewById(R.id.shipping_graphs_total_charts_roll_total_container);
            secondaryChartContainer = v.findViewById(R.id.shipping_graphs_total_charts_secondary_chart_container);
            unexpandSecondaryChartButton = v.findViewById(R.id.shipping_graphs_total_charts_secondary_chart_unexpand_button);
            principalChartLegend = v.findViewById(R.id.shipping_graphs_total_charts_principal_chart_legend);
            secondaryChartLegend = v.findViewById(R.id.shipping_graphs_total_charts_secondary_chart_legend);
            secondaryChartHighContrastCheckBox = v.findViewById(R.id.shipping_graphs_total_charts_secondary_chart_high_contrast_checkbox);
            totalTotal = v.findViewById(R.id.shipping_graphs_total_charts_principal_chart_total);
            mercadonaTotal = v.findViewById(R.id.shipping_graphs_total_charts_total_mercadona);
            estancoTotal = v.findViewById(R.id.shipping_graphs_total_charts_total_estanco);
        }
    }

    public class ViewHolderWeeklyTotalCharts extends RecyclerView.ViewHolder {

        private CardView cardContainer;
        private BarChart principalChart, secondaryChart;
        private LinearLayout mercadonaAvgContainer, estancoAvgContainer, rollTotalContainer,
                secondaryChartContainer;
        private ImageView unexpandSecondaryChartButton;
        private RecyclerView principalChartLegend, secondaryChartLegend;
        private CheckBox secondaryChartHighContrastCheckBox;
        private TextView dayAvg, mercadonaAvg, estancoAvg;

        public ViewHolderWeeklyTotalCharts(View v) {
            super(v);
            cardContainer = v.findViewById(R.id.shipping_graphs_weekly_total_charts_card_card_view);
            principalChart = v.findViewById(R.id.shipping_graphs_weekly_total_charts_principal_chart);
            secondaryChart = v.findViewById(R.id.shipping_graphs_weekly_total_charts_secondary_chart);
            mercadonaAvgContainer = v.findViewById(R.id.shipping_graphs_weekly_total_charts_mercadona_avg_container);
            estancoAvgContainer = v.findViewById(R.id.shipping_graphs_weekly_total_charts_estanco_avg_container);
            rollTotalContainer = v.findViewById(R.id.shipping_graphs_weekly_total_charts_roll_total_container);
            secondaryChartContainer = v.findViewById(R.id.shipping_graphs_weekly_total_charts_secondary_chart_container);
            unexpandSecondaryChartButton = v.findViewById(R.id.shipping_graphs_weekly_total_charts_secondary_chart_unexpand_button);
            principalChartLegend = v.findViewById(R.id.shipping_graphs_weekly_total_charts_principal_chart_legend);
            secondaryChartLegend = v.findViewById(R.id.shipping_graphs_weekly_total_charts_secondary_chart_legend);
            secondaryChartHighContrastCheckBox = v.findViewById(R.id.shipping_graphs_weekly_total_charts_secondary_chart_high_contrast_checkbox);
            dayAvg = v.findViewById(R.id.shipping_graphs_weekly_total_charts_day_avg);
            mercadonaAvg = v.findViewById(R.id.shipping_graphs_weekly_total_charts_mercadona_avg);
            estancoAvg = v.findViewById(R.id.shipping_graphs_weekly_total_charts_estanco_avg);
        }
    }
}
