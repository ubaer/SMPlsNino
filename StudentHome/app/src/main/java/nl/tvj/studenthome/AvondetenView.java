package nl.tvj.studenthome;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class AvondetenView extends AppCompatActivity {
    private int avondetenId;
    private Gebruiker ingelogdeGebruiker;
    private Studentenhuis studentenhuis;
    private Database db;
    private Avondeten geselecteerdAvondeten;

    private double beoordeling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avondeten_view);

        //  TODO hier moet de geselecteerde activiteit uit het vorige scherm ('AvondetenActivity') getoond worden met beoordeling etc. als de host van de activiteit de huidige Gordon Ramsey is moet er een mogelijkheid worden gegeven om een CookOff aan te gaan.
        SharedPreferences sharedPref = getSharedPreferences("User", MODE_PRIVATE);
        avondetenId = sharedPref.getInt("avondeten", -1);
        Gson gson = new Gson();
        String g = sharedPref.getString("logedInUser", "");
        String s = sharedPref.getString("home", "");
        ingelogdeGebruiker = gson.fromJson(g, Gebruiker.class);
        studentenhuis = gson.fromJson(s, Studentenhuis.class);

        db = new Database();

        new getActiviteit().execute((Void[])null);

        RatingBar rb = (RatingBar)findViewById(R.id.rbGemiddeldeBeoordeling);
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                beoordeling = ((RatingBar)findViewById(R.id.rbGemiddeldeBeoordeling)).getRating();

                new addBeoordeling().execute((Void[])null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_avondeten_view, menu);
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

    public void btnDaagUit_Click(View view) {

        Thread mythread = new Thread(new Runnable() {
            @Override
            public void run() {
                geselecteerdAvondeten.addDeelnemer(ingelogdeGebruiker);
            }
        });
        mythread.start();
            Toast.makeText(AvondetenView.this, "Aangemeld!", Toast.LENGTH_LONG).show();
            new getActiviteit().execute((Void[]) null);
    }

    public class getActiviteit extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                geselecteerdAvondeten = db.getAvondEtenByID(avondetenId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            //  Controls op activity worden gevuld
            TextView omschrijving = (TextView)findViewById(R.id.tvOmschrijving);
            omschrijving.setText(geselecteerdAvondeten.omschrijving);

            TextView host = (TextView)findViewById(R.id.tvHost);
            host.setText(geselecteerdAvondeten.host.getNaam());

//            Drawable progress = ((RatingBar)findViewById(R.id.rbGemiddeldeBeoordeling)).getProgressDrawable();
//            DrawableCompat.setTint(progress, Color.alpha());

            TextView datum = (TextView)findViewById(R.id.tvDatum);
            datum.setText(geselecteerdAvondeten.starttijd.toString());

            TextView bedrag = (TextView)findViewById(R.id.tvTotaalbedrag);
            bedrag.setText(String.valueOf(geselecteerdAvondeten.totaalbedrag));

            ArrayList<Gebruiker> alDeelnemers = geselecteerdAvondeten.deelnemers;

            if (alDeelnemers.isEmpty()) {
                ListView lvDeelnemers = (ListView)findViewById(R.id.lvDeelnemers);

                String [] aErrorString = { "Geen deelnemers bekend" };
                ArrayList<String> errorString = new ArrayList<>();
                errorString.addAll(Arrays.asList(aErrorString));

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AvondetenView.this, R.layout.support_simple_spinner_dropdown_item, errorString);

                lvDeelnemers.setAdapter(adapter);
            }
            else {
                ListView lvDeelnemers = (ListView)findViewById(R.id.lvDeelnemers);

                ArrayList<Gebruiker> deelnemers = new ArrayList<>();
                deelnemers.addAll(alDeelnemers);

                ArrayAdapter<Gebruiker> adapter = new ArrayAdapter<>(AvondetenView.this, R.layout.support_simple_spinner_dropdown_item, deelnemers);

                lvDeelnemers.setAdapter(adapter);
            }

            //  TODO bepaal of button clickable is (kijk of host gordon ramsey van 't moment is)
        }
    }

    public class addBeoordeling extends AsyncTask<Void, Void, Void> {
        boolean gelukt = false;

        @Override
        protected Void doInBackground(Void... params) {
         //   gelukt = studentenhuis.addBeoordeling(geselecteerdAvondeten, beoordeling, ingelogdeGebruiker);
            Beoordeling beoordelingFull = new Beoordeling(ingelogdeGebruiker, beoordeling);
            try {
                db.addBeoordeling(geselecteerdAvondeten, beoordelingFull);
             //   geselecteerdAvondeten.addBeoordeling(beoordelingFull);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            if (gelukt) {
                Toast.makeText(AvondetenView.this, "Beoordeling toegevoegen gelukt!", Toast.LENGTH_LONG);
            }
            else
            {
                Toast.makeText(AvondetenView.this, "Beoordeling toevoegen niet gelukt!", Toast.LENGTH_LONG);
            }
        }
    }
}
