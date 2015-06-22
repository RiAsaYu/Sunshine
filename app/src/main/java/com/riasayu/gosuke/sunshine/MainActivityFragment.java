package com.riasayu.gosuke.sunshine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    ArrayAdapter mForecastAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String [] forecasetArray ={
                "Today - Sunny - 98/63",
                "Tomorrow -Foggy - 70/40",
                "Weds - Cloudy - 72/63",
                "Thurs - Asteroids = 75/65",
                "Fri -Heavy Rain -65/56",
                "Sat - HELP TRAPPED IN WEATHERESTATION - 60/51",
                "Sun -Sunny - 80/68"
        };
        List<String> weekForecast = new ArrayList<String>(Arrays.asList(forecasetArray));

        mForecastAdapter = new ArrayAdapter(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weekForecast);
        ListView forecastListView = (ListView)rootView.findViewById(R.id.listview_forecast);
        forecastListView.setAdapter(mForecastAdapter);

        return rootView;
    }
}
