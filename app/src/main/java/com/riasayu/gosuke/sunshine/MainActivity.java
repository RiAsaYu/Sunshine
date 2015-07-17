package com.riasayu.gosuke.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        Log.d("MainActivity", "onStart()");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d("MainActivity", "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("MainActivity", "onStop()");
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.d("MainActivity", "onResume()");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d("MainActivity", "onDestroy()");
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

}
