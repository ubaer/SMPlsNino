package nl.tvj.studenthome;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class AvondetenOverzicht extends AppCompatActivity {
    private Gebruiker ingelogdeGebruiker;
    private Studentenhuis studentenhuis;
    private Database db;
    private ArrayList<Activiteit> activiteiten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avondeten);

        //  Gebruiker en studentenhuis uitlezen uit SharedPreferences
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String g = sharedPref.getString("user", "");
        String s = sharedPref.getString("home", "");
        ingelogdeGebruiker = gson.fromJson(g, Gebruiker.class);
        studentenhuis = gson.fromJson(s, Studentenhuis.class);

        //  TODO haal alle avondeten activiteiten op (via de Studentenhuis klasse) en laat een aantal avondeten activiteiten uit het verleden zien
        db = new Database();

        new getAllAvondEten().execute((Void[]) null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_avondeten, menu);
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

    public void btnNieuwAvondeten_Click(View view) {
    }

    //  TODO 'setOnItemClickListerner' event aanmaken voor lvAvondeten, het is de bedoeling dat je avondeten kan terugkijken met een gemiddelde beoordeling, in het nieuwe scherm waar je dan komt kun je een cook-off aanvragen, mits de gebruiker de huidige gordon ramsey is

    private class getAllAvondEten extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                activiteiten = db.getAllAvondEten();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            if (activiteiten == null) {
                ListView lv = (ListView)findViewById(R.id.lvAvondeten);

                //  String array aanmaken met verschillende waarden om deze vervolgens
                //  aan een array van strings toe te voegen.
                String [] errorString = { "Geen avondeten activiteiten beschikbaar" };
                ArrayList<String> allStrings = new ArrayList<>();
                allStrings.addAll(Arrays.asList(errorString));

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AvondetenOverzicht.this, R.layout.support_simple_spinner_dropdown_item, allStrings);

                lv.setAdapter(adapter);
            }
            else {
                ListView lv = (ListView)findViewById(R.id.lvAvondeten);

                //  String array aanmaken met verschillende waarden om deze vervolgens
                //  aan een array van strings toe te voegen.
                ArrayList<Activiteit> allStrings = new ArrayList<>();
                allStrings.addAll(activiteiten);

                ArrayAdapter<Activiteit> adapter = new ArrayAdapter<>(AvondetenOverzicht.this, R.layout.support_simple_spinner_dropdown_item, allStrings);

                lv.setAdapter(adapter);
            }
        }
    }
}
