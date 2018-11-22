package com.morova.budgettracker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.morova.budgettracker.data.entities.CashMovementItem;
import com.morova.budgettracker.data.entities.Category;
import com.morova.budgettracker.data.viewmodels.CashMovementItemViewModel;
import com.morova.budgettracker.data.viewmodels.CategoryViewModel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiagramsActivity extends AppCompatActivity {

    private LineChart mainLineChart;

    private CashMovementItemViewModel cashMovementItemViewModel;
    private CategoryViewModel categoryViewModel;

    private List<CashMovementItem> itemList = new ArrayList<>();
    private HashMap<Long, Category> categoryMap = new HashMap<>();

    private Category.Direction typeOfChartToShow = Category.Direction.EXPENSE;
    private boolean categoryMapInitalized = false;

    //TODO filter functions
    //TODO style diagram

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagrams);

        initContentView();

        categoryViewModel = ViewModelProviders.of(this)
                .get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, new DiagramsActivity.CategoryObserver());

        cashMovementItemViewModel = ViewModelProviders.of(this)
                .get(CashMovementItemViewModel.class);
        cashMovementItemViewModel.getAllItems().observe(this, new DiagramsActivity.CashMovementItemsObserver());
    }

    private void updateChart(Category.Direction direction) {
        //mainLineChart.clear();
        //mainLineChart.clearValues();

        List<Entry> entries = new ArrayList<>();
        int sumAmountOfDirection = 0;
        int itemTOAddIdx = 0;
        for (int i = 0; i < itemList.size(); ++i) {
            CashMovementItem item = itemList.get(i);

            if (categoryMap.get(item.getCategoryId()).getDirection()
                    .equals(direction)) {

                sumAmountOfDirection += item.getAmount();

                entries.add(new Entry(itemTOAddIdx, sumAmountOfDirection));
                itemTOAddIdx++;
            }
        }

        XAxis xAxis = mainLineChart.getXAxis();
        xAxis.setValueFormatter(new XAxisDateFormatter(itemList, direction));
        xAxis.setGranularity(1f);

        String label = direction.equals(Category.Direction.EXPENSE) ?
                Category.Direction.EXPENSE.toString() : Category.Direction.INCOME.toString();

        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setColor(
                direction.equals(Category.Direction.EXPENSE) ?
                        R.color.expense_color : R.color.income_color);
        LineData lineData = new LineData(dataSet);
        mainLineChart.setData(lineData);
        mainLineChart.notifyDataSetChanged();
        mainLineChart.invalidate();

    }

    private class XAxisDateFormatter implements IAxisValueFormatter {

        private List<String> labels = new ArrayList<>();

        private XAxisDateFormatter(List<CashMovementItem> cashMovementItems, Category.Direction direction) {

            String formatString = "%d %s %d:%d";
            //int labelIdx = 0;
            for (int i = 0; i < cashMovementItems.size(); ++i) {
                CashMovementItem item = cashMovementItems.get(i);
                Category.Direction itemDirection = categoryMap.get(item.getCategoryId()).getDirection();

                if (itemDirection.equals(direction)) {
                    LocalDateTime actualDateTime = item.getDateTime();
                    String month = actualDateTime.getMonth().toString();
                    int day = actualDateTime.getDayOfMonth();
                    int hour = actualDateTime.getHour();
                    int minute = actualDateTime.getMinute();

                    String label = String.format(formatString,
                            day, month, hour, minute);

                    labels.add(label);
                }

            }
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            int index = (int) value;
            if (index >= 0 && index < labels.size()) {
                return labels.get(index);
            }
            return "NO DATE";
        }
    }

//    private void updateGraphView() {
//
//        DataPoint[] dataPoints = getDataPointsFromList(itemList, Category.Direction.EXPENSE);
//
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(
//                dataPoints
//        );
//        mainGraphView.addSeries(series);
//
//        mainGraphView.getGridLabelRenderer().setLabelFormatter(
//                new DateAsXAxisLabelFormatter(this)
//        );
//    }

    private DataPoint[] getDataPointsFromList(List<CashMovementItem> cashMovementItems, Category.Direction direction) {
        List<DataPoint> series = new ArrayList<>();
        int sumDirection = 0;

        for (CashMovementItem item : cashMovementItems) {

            if (categoryMap.get(item.getCategoryId()).getDirection()
                    .equals(direction)) {

                sumDirection += item.getAmount();

                series.add(new DataPoint(
                        convertToDate(item.getDateTime()),
                        sumDirection));
            }
        }

        return series.toArray(new DataPoint[series.size()]);
    }

    private java.util.Date convertToDate(LocalDateTime dateTime) {
        return java.util.Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private void initContentView() {
       // mainGraphView = findViewById(R.id.MainGraphView);
        mainLineChart = findViewById(R.id.MainLineChart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_diagrams);
        setSupportActionBar(toolbar);
    }

    private void setItemList(List<CashMovementItem> cashMovementItems) {
        itemList.clear();
        itemList.addAll(cashMovementItems);
    }

    private void setCategoryMap(List<Category> categories) {
        categoryMap.clear();
        for (Category category : categories) {
            categoryMap.put(category.getId(), category);
        }
    }

    private class CashMovementItemsObserver implements Observer<List<CashMovementItem>> {
        @Override
        public void onChanged(@Nullable List<CashMovementItem> cashMovementItems) {
            setItemList(cashMovementItems);
           // updateGraphView();
            if (categoryMapInitalized) {
                updateChart(typeOfChartToShow);
            }
        }
    }

    private class CategoryObserver implements Observer<List<Category>> {
        @Override
        public void onChanged(@Nullable List<Category> categories) {
            setCategoryMap(categories);
            categoryMapInitalized = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diagrams, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_expenses_diagram:
                typeOfChartToShow = Category.Direction.EXPENSE;
                updateChart(typeOfChartToShow);
                return true;
            case R.id.action_income_diagram:
                typeOfChartToShow = Category.Direction.INCOME;
                updateChart(typeOfChartToShow);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
