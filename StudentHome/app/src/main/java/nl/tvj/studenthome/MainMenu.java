package nl.tvj.studenthome;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

public class MainMenu extends AppCompatActivity {
    private Gebruiker ingelogdeGebruiker;
    private Studentenhuis studentenhuis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //  Gebruiker en studentenhuis uitlezen uit SharedPreferences
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String g = sharedPref.getString("user", "");
        String s = sharedPref.getString("home", "");
        ingelogdeGebruiker = gson.fromJson(g, Gebruiker.class);
        studentenhuis = gson.fromJson(s, Studentenhuis.class);

        //  TODO Verander 'MainMenu' tekst in action bar naar studentenhuis naam
        //  TODO Gebruik 'menu slider' waar je naar 'AvondetenActivity' of 'ra nking' kan gaan en kan uitloggen
        //  TODO Laat in het midden van huidige scherm info van het huidige studentenhuis zien
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
}
