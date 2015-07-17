package com.riasayu.gosuke.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.riasayu.gosuke.sunshine.data.WeatherContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    ForecastAdapter mForecastAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // onCreateView�ɒu���Ă��L���������BonCreate��onCreateView�̍��͉��H
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String locationSetting = Utility.getPreferredLocation(getActivity());
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + "ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(locationSetting, System.currentTimeMillis());
        Cursor cur = getActivity().getContentResolver().query(weatherForLocationUri, null, null, null, sortOrder);

        mForecastAdapter = new ForecastAdapter(getActivity(), cur, 0);
        ListView forecastListView = (ListView)rootView.findViewById(R.id.listview_forecast);
        forecastListView.setAdapter(mForecastAdapter);
        forecastListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // �ȉ�3�ʂ�̂ǂ�ł�����
//                String forecast = (String) mForecastAdapter.getItem(position);
                //String forecast = (String )parent.getItemAtPosition(position);
                //String forecast = (String) ((TextView)view).getText().toString();

                // ������Class�̐��̂����Ȃ̂��m�F���Ă݂�B
                //String forecast = parent.getClass().getSimpleName(); // ListView������
                // String forecast = parent.getItemAtPosition(position).getClass().getSimpleName(); // String Class������
                // String forecast = view.getClass().getSimpleName(); // AppCompatTextView Class������
                //Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();

//                Intent detailIntent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, forecast);
//                startActivity(detailIntent);

            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        weatherUpdate();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            weatherUpdate();
        }
        else if(id == R.id.action_location){
            showPreferredLocationOnMap();
        }
        return super.onOptionsItemSelected(item);
    }

    private void weatherUpdate(){
        String location = Utility.getPreferredLocation(getActivity());
        new FetchWeatherTask(getActivity()).execute(location);
    }
    private void showPreferredLocationOnMap(){
        String location = Utility.getPreferredLocation(getActivity());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + location));
        if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
        else{
            Toast.makeText(getActivity(), "No map app!", Toast.LENGTH_SHORT).show();
        }
    }
}

