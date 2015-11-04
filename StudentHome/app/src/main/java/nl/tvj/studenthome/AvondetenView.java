package nl.tvj.studenthome;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;

public class AvondetenView extends AppCompatActivity {
    private int avondetenId;
    private Avondeten geselecteerdAvondeten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avondeten_view);

        //  TODO hier moet de geselecteerde activiteit uit het vorige scherm ('AvondetenActivity') getoond worden met beoordeling etc. als de host van de activiteit de huidige Gordon Ramsey is moet er een mogelijkheid worden gegeven om een CookOff aan te gaan.
        SharedPreferences sharedPref = getSharedPreferences("User", MODE_PRIVATE);
        avondetenId = sharedPref.getInt("avondeten", -1);

        new getActiviteit().execute((Void[])null);
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

    public class getActiviteit extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //geselecteerdAvondeten = db.getAvondEtenByID(avondetenId);
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {

        }
    }
}
