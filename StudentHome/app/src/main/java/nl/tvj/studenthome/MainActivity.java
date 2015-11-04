package nl.tvj.studenthome;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    Database dbm = new Database();
    Gebruiker gebruiker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread mythread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    gebruiker = dbm.getGebruikerVanGebruikersnaam("uber");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        mythread.start();
        setUserLocation();
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
}
