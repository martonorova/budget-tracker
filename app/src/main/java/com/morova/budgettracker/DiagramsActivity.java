package com.morova.budgettracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class DiagramsActivity extends AppCompatActivity {

    private GraphView mainGraphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagrams);

        initContentView();
        initGraphView();
    }

    private void initGraphView() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(
                new DataPoint[] {
                        new DataPoint(0, 1),
                        new DataPoint(1, 5),
                        new DataPoint(2, 3),
                        new DataPoint(3, 2),
                        new DataPoint(4, 6)
                }
        );

        mainGraphView.addSeries(series);
    }

    private void initContentView() {
        mainGraphView = findViewById(R.id.MainGraphView);
    }


}
