package nl.tvj.studenthome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

public class Login extends AppCompatActivity {
    private Database db;
    private boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new Database();
        connected = false;

        new showConnectionResult().execute((Void[]) null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void btnLoginClick(View view) {

    }

    private class showConnectionResult extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            connected = db.connect();
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            if (connected) {
                Toast.makeText(Login.this, "Database connectie geslaagd", Toast.LENGTH_SHORT).show();
                System.out.println("Database connectie geslaagd");

                //  Gebruiker en studentenhuis ophalen uit de database
                Gebruiker g = null;
                Studentenhuis s = null;

                //  Omzetten naar JSON objecten zodat deze in de Shared Preferences opgeslagen kunnen
                //  worden
                if (g != null && s != null)
                {
                    Gson gson = new Gson();
                    String ingelogdeGebruiker = gson.toJson(g);
                    String studentenhuis = gson.toJson(s);

                    //  In shared preferences de session (ingelogde gebruiker en bijbehorende studentenhuis)
                    //  opslaan
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Login.this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("user", ingelogdeGebruiker);
                    editor.putString("home", studentenhuis);
                }

                //  Open 'main menu' scherm van een studentenhuis
                Intent mainMenu = new Intent(Login.this, MainMenu.class);
                System.out.println("Opened main menu");
                Login.this.startActivity(mainMenu);
            }
            else
            {
                Toast.makeText(Login.this, "Database connectie gefaald", Toast.LENGTH_SHORT).show();
                System.out.println("Database connectie gefaald");
            }
        }
    }
}
