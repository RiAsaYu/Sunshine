package com.riasayu.gosuke.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements ForecastFragment.Callback {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String DETAILFRAGMENT_TAG = "DFTAG";

    private boolean mTwoPane;
    private  String mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        mLocation = Utility.getPreferredLocation(this);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.weather_detail_container) != null){
            mTwoPane = true;
            if(savedInstanceState == null){ // Fragmentが再生成された場合はnullではないらしい。Fragmentで自動的にonSaveInstanceState()が呼ばれているからのようだ。
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.weather_detail_container, new DetailActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        }
        else{
            mTwoPane = false;
        }
    }

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume()");
        super.onResume();
        String location = Utility.getPreferredLocation(this);
        // update the location in our second pane using the fragment manager
        if(location != null && !location.equals(mLocation)){
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            if(null != ff){
                ff.onLocationChanged();
            }
            DetailActivityFragment df = (DetailActivityFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if(null != df){
                df.onLocationChanged(location);
            }
            mLocation = location;
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the detail; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Uri contentUri){
        if(mTwoPane){
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction
            Bundle args = new Bundle();
            args.putParcelable(DetailActivityFragment.DETAIL_URI, contentUri);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        }
        else{
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(contentUri);
            startActivity(intent);
        }
    }
}
