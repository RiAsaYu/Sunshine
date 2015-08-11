package com.riasayu.gosuke.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.riasayu.gosuke.sunshine.data.WeatherContract;
import com.riasayu.gosuke.sunshine.data.WeatherContract.WeatherEntry;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";

    private static final String FORECAST_SHARE_HASHTAG = "#SunshineApp";

    private ShareActionProvider mShareActionProvider;
    private String mForecast;
    private Uri mUri;

    private static final int DETAIL_LOADER = 0;

    private static final String[] DETAIL_COLUMS = {
            WeatherEntry.TABLE_NAME + "." + WeatherEntry._ID,
            WeatherEntry.COLUMN_DATE,
            WeatherEntry.COLUMN_SHORT_DESC,
            WeatherEntry.COLUMN_MAX_TEMP,
            WeatherEntry.COLUMN_MIN_TEMP,
            WeatherEntry.COLUMN_HUMIDITY,
            WeatherEntry.COLUMN_PRESSURE,
            WeatherEntry.COLUMN_WIND_SPEED,
            WeatherEntry.COLUMN_DEGREES,
            WeatherEntry.COLUMN_WEATHER_ID,
            // This works because the WeatherProvider returns location data joined with
            // weather data, even though they're stored in two different tables.
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING
    };

    private static final int COL_WEATHER_ID = 0;
    private static final int COL_WEATHER_DATE = 1;
    private static final int COL_WEATHER_DESC = 2;
    private static final int COL_WEATHER_MAX_TEMP = 3;
    private static final int COL_WEATHER_MIN_TEMP = 4;
    private static final int COL_WEATHER_HUMIDITY = 5;
    private static final int COL_WEATHER_PRESSURE = 6;
    private static final int COL_WEATHER_WIND_SPEED = 7;
    private static final int COL_WEATHER_DEGRESS = 8;
    private static final int COL_WEATHER_CONDITION_ID = 9;

    private ImageView mIconView;
    private TextView mFriendlyDateView;
    private TextView mDateView;
    private TextView mDescriptionView;
    private TextView mHighTempView;
    private TextView mLowTempView;
    private TextView mHumidityView;
    private TextView mPressureView;
    private TextView mWindView;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        // 使うたびに繰り返しViewを取得するのは無駄なのでここでまとめて取得しておく。
        mIconView = (ImageView)rootView.findViewById(R.id.detail_icon);
        mFriendlyDateView = (TextView)rootView.findViewById(R.id.detail_day_textView);
        mDateView = (TextView)rootView.findViewById(R.id.detail_date_textView);
        mDescriptionView = (TextView)rootView.findViewById(R.id.detail_forecast_textView);
        mHighTempView = (TextView)rootView.findViewById(R.id.detail_high_textView);
        mLowTempView = (TextView)rootView.findViewById(R.id.detail_low_textview);
        mHumidityView = (TextView)rootView.findViewById(R.id.detail_humidity_textView);
        mPressureView = (TextView)rootView.findViewById(R.id.detail_pressure_textView);
        mWindView = (TextView)rootView.findViewById(R.id.detail_wind_textView);

        Bundle arguments = getArguments();
        if(arguments != null){
            mUri = arguments.getParcelable(DetailActivityFragment.DETAIL_URI);
        }
        return rootView;
    }

    void onLocationChanged(String newLocation){
        Uri uri = mUri;
        if(null != uri){
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
            Uri updateUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation, date);
            mUri = updateUri;
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mForecast != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    private Intent createShareForecastIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "In onActivityCreate");
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "In onCreateLoader");
        if(null != mUri){
            // Now create and return a Cursorloader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "In onLoaderFinished");
        if(data == null || !data.moveToFirst()){return;}

        int weatherId = data.getInt(COL_WEATHER_CONDITION_ID);
        mIconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));

        String friendlyDateText = Utility.getDayName(getActivity(), data.getLong(COL_WEATHER_DATE));
        String dateText = Utility.getFormattedMonthDay(getActivity(), data.getLong(COL_WEATHER_DATE));
        mFriendlyDateView.setText(friendlyDateText);
        mDateView.setText(dateText);

        String description = data.getString(COL_WEATHER_DESC);
        mDescriptionView.setText(description);

        boolean isMetric = Utility.isMetric(getActivity());

        String high = Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
        mHighTempView.setText(high);
        String low = Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);
        mLowTempView.setText(low);

        float humidity = data.getFloat(COL_WEATHER_HUMIDITY);
        mHumidityView.setText(getActivity().getString(R.string.format_humidity, humidity));

        float windSpeedStr = data.getFloat(COL_WEATHER_WIND_SPEED);
        float windDirStr = data.getFloat(COL_WEATHER_DEGRESS);
        mWindView.setText(Utility.getFormattedWind(getActivity(), windSpeedStr, windDirStr));

        float pressure = data.getFloat(COL_WEATHER_PRESSURE);
        mPressureView.setText(getActivity().getString(R.string.format_pressure, pressure));

        mForecast = String.format("%s - %s -%s/%s", dateText, description, high, low);

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        if(mShareActionProvider != null){
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "In onLoaderReset");
    }
}
