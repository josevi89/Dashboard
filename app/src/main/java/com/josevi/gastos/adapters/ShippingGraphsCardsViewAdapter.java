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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
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
        double total = 0;
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
                    holder.mercadonaPercent.setText(String.format("%.1f", storeTotal) +" %");
                    break;
                case ESTANCO:
                    holder.estancoPercent.setText(String.format("%.1f", storeTotal) +" %");
                    break;
            }
        }
        if (total > 0) {
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
        chart.getXAxis().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        chart.invalidate();
        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private void setTotalPrincipalChartData(ViewHolderTotalCharts holder) {
        BarChart chart = holder.principalChart;
        List<BarEntry> entries = new ArrayList<BarEntry>();
        List<Integer> colors = new ArrayList<Integer>();
        List<Legend> legend = new ArrayList<Legend>();

        Map<Store, List<Shipping>> shippingsMappedByStore =
                shippingRepository.getShippingsInMonthMappedByStore(end);
        Map<Group, Double> totalShippingsMappedByGroup = new HashMap<Group, Double>();
        for (Group group: Group.vals()) {
            totalShippingsMappedByGroup.put(group, 0d);
//            colors.add(group.color(activity, false));
        }
        double total = 0;
        for (Store store: Store.vals()) {
            for (Shipping shipping: shippingsMappedByStore.get(store)) {
                for (Product product: shipping.getProducts()) {
                    totalShippingsMappedByGroup.put(product.getGroup(),
                            totalShippingsMappedByGroup.get(product.getGroup()) +
                                    shipping.get(product.getCode()).prize * shipping.get(product.getCode()).qty);
                }
                total += shipping.getTotalPrize();
            }
        }
        if (total > 0) {
            for (Store store: Store.vals()) {
                Group[] groupsInStore = Group.getGroupsFromStore(store);
                List<Float> groupTotals = new ArrayList<Float>();
                double storeTotal = 0;
                for (int g = 0; g < groupsInStore.length; g++) {
                    float groupTotal = (float)(double)(totalShippingsMappedByGroup.get(groupsInStore[g]));
                    if (groupTotal > 00) {
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
        // undo all highlights
        chart.highlightValues(null);
        chart.setExtraBottomOffset(-5); //elimina espacio entre el grafico y la leyenda

        chart.invalidate();
        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
    }

    private void setTotalSecondaryChartData(ViewHolderTotalCharts holder, Store store, boolean highContrastOn) {
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
                        entries.add(new BarEntry(entries.size(), groupTotalArray));
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
        // undo all highlights
        chart.highlightValues(null);
//        chart.setExtraBottomOffset(-5); //elimina espacio entre el grafico y la leyenda

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
}
