package nl.tvj.studenthome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    Database dbm = new Database();
    Gebruiker gebruiker;
    Studentenhuis studentenhuis;
    ArrayList<Gebruiker>wieIsThuis = new ArrayList<>();
    SwipeRefreshLayout  swipeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Het ophalen van de gebruiker en het studentenhuis uit de shared preferences
        SharedPreferences sharedPref = getSharedPreferences("User", Context.MODE_PRIVATE);
        String g = sharedPref.getString("logedInUser", "");
        String s = sharedPref.getString("home", "");
        Gson gson = new Gson();
        gebruiker = gson.fromJson(g, Gebruiker.class);
        studentenhuis = gson.fromJson(s, Studentenhuis.class);
        //Userlokatie update starten
        setUserLocation();
        setTitle(studentenhuis.getNaam());
        new addListThuis().execute((Void[]) null);
        //swipelayout
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new addListThuis().execute((Void[]) null);
            }
        });
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
    public void setUserLocation()
    {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE); //Access the location manager (register to recieve updates)
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(gebruiker != null) {
                    gebruiker.lastLong = location.getLongitude();
                    gebruiker.lastLat = location.getLatitude();
                    Thread mythread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dbm.setGebruikerLocatie(gebruiker.id, gebruiker.lastLong, gebruiker.lastLat);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    mythread.start();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };

        try{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    private class addListThuis extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                wieIsThuis = dbm.getAanwezigeGebruikers(studentenhuis.id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            final ArrayAdapter<Gebruiker> itemsAdapter = new ArrayAdapter<Gebruiker>(MainActivity.this, android.R.layout.simple_list_item_1, wieIsThuis);
            final ListView lvWieIsThuis = (ListView) findViewById(R.id.lvAanwezigeGebruikers);
            lvWieIsThuis.setAdapter(itemsAdapter);
            swipeLayout.setRefreshing(false);
        }
    }
}
