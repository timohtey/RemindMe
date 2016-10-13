package com.example.android.remindme;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import database.RemindMeDataSource;
import json.HttpHandler;
import model.ToDoEntry;

public class MainActivity extends AppCompatActivity {
    // JSON related variables
    private ProgressDialog pDialog;
    private static String jsonUrl = "https://dl.dropboxusercontent.com/u/6890301/tasks.json";

    // Datasource
    private RemindMeDataSource remindMeDataSource;

    private Toolbar toolbar;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Database
        remindMeDataSource = new RemindMeDataSource(this);
        remindMeDataSource.open();

        // If no existing to do entries in the database
        if(remindMeDataSource.retrieveAllToDoEntries().isEmpty()) {
            // Retrieve To Do Entries from JSON URL
            new GetToDoEntries().execute();
        }

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Fragment for the Pending tasks
        PendingFragment pendingFragment = new PendingFragment();
        pendingFragment.setRemindMeDataSource(remindMeDataSource);
        viewPagerAdapter.addFragments(pendingFragment, "Pending");

        // Fragment for the Done tasks
        DoneFragment doneFragment = new DoneFragment();
        doneFragment.setRemindMeDataSource(remindMeDataSource);
        viewPagerAdapter.addFragments(doneFragment, "Done");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetToDoEntries extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Show progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading data...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(jsonUrl);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray toDoEntriesData = jsonObj.getJSONArray("data");

                    // Loop through all of the Data
                    for (int i = 0; i < toDoEntriesData.length(); i++) {
                        JSONObject c = toDoEntriesData.getJSONObject(i);

                        int id = c.getInt("id");
                        String name = c.getString("name");
                        int state = c.getInt("state");

                        ToDoEntry toDoEntry = new ToDoEntry(name, state);
                        toDoEntry.setToDoEntryId(id);

                        // Insert retrieved data to the database
                        remindMeDataSource.addToDoEntry(toDoEntry);
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Check internet connection.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            // Force Refresh
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }
}
