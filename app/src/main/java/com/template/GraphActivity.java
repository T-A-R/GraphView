package com.template;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphActivity extends SmartActivity {

    private List<Coordinates> coordinates = null;
    private List<Coordinates> oldCoordinates = null;
    private SharedPreferences sharedPrefs;
    private LineGraphSeries<DataPoint> series = null;
    private GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        readCoordinates();
        initViews();
    }

    private void initViews() {
        Button pastCalc = findViewById(R.id.btn_past);
        pastCalc.setOnClickListener(v -> loadPast());

        graph = findViewById(R.id.graph);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Prize");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Number of spins");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(getResources().getColor(R.color.primary_dark));
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getResources().getColor(R.color.primary_dark));
        graph.getGridLabelRenderer().setPadding(30);

        graph.removeAllSeries();
        series = new LineGraphSeries<DataPoint>();
        series.appendData(new DataPoint(0, 0), true, 2000);

        for (Coordinates item : coordinates) {
            double x = item.getX();
            double y = item.getY();
            series.appendData(new DataPoint(x, y), true, 2000);
            Log.d("T-L.GraphActivity", "initViews: " +x +"/" +y);
        }
        double maxX = coordinates.get(coordinates.size()-1).getX();
        double maxY = coordinates.get(coordinates.size()-1).getY();
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(maxY);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(maxX);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        graph.addSeries(series);
    }

    private void loadPast() {
        series = new LineGraphSeries<DataPoint>();
        series.appendData(new DataPoint(0, 0), false, 2000);
        for (Coordinates item : oldCoordinates) {
            double x = item.getX();
            double y = item.getY();
            series.appendData(new DataPoint(x, y), false, 2000);
            Log.d("T-L.GraphActivity", "initViews: " +x +"/" +y);
        }
        graph.removeAllSeries();
        double maxX = oldCoordinates.size() > 0 ? oldCoordinates.get(oldCoordinates.size()-1).getX() + 10 : 10;
        double maxY = oldCoordinates.size() > 0 ? oldCoordinates.get(oldCoordinates.size()-1).getY() + 10 : 10;
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(maxY);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(maxX);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        graph.addSeries(series);
    }

    private void readCoordinates() {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            Gson gson = new Gson();
            String json = sharedPrefs.getString("COORDINATES", "");
            Type type = new TypeToken<List<Coordinates>>() {
            }.getType();
            coordinates = gson.fromJson(json, type);
            oldCoordinates = gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        if (coordinates == null || coordinates.size() == 0) {
            coordinates = new ArrayList<>();
            coordinates.add(new Coordinates(getRandomX(), getRandomX()));
            oldCoordinates = new ArrayList<>();
        } else {
            coordinates.add(new Coordinates(coordinates.get(coordinates.size()-1).getX() + getRandomX(),
                    coordinates.get(coordinates.size()-1).getY() + getRandomY()));
        }
        Log.d("T-L.GraphActivity", "readCoordinates: " + coordinates.size() + " / " + oldCoordinates.size());
        Log.d("T-L.GraphActivity", "readCoordinates: ");
        saveCoordinates();
    }

    private void saveCoordinates() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(coordinates);

        editor.putString("COORDINATES", json);
        editor.apply();
    }

    private int getRandomX() {
        return new Random().nextInt(10) + 1;
    }

    private int getRandomY() {
        return new Random().nextInt(12) - 2;
    }

    @Override
    public void onBackPressed() {
        startActivity(MainActivity.class);

    }
}