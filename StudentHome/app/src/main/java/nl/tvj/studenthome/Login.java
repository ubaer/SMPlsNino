package nl.tvj.studenthome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.sql.SQLException;

public class Login extends AppCompatActivity {
    private Database db;
    private boolean connected;
    EditText editUsername;
    EditText editPassword;
    String username;
    String password;
    Gebruiker g;
    Studentenhuis s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new Database();
        connected = false;
        editUsername = (EditText) findViewById(R.id.tbUsername);
        editPassword = (EditText) findViewById(R.id.tbPassword);
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
        username = editUsername.getText().toString();
        password = editPassword.getText().toString();
        new showConnectionResult().execute((Void[]) null);
    }

    private class showConnectionResult extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            db.connect();

            try {
                connected = db.checkLoginGegevens(username, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
             g = null;
            try {
                g = db.getGebruikerVanGebruikersnaam(username);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            s = null;
            try {
                s = db.getStudentenHuis(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            if (connected) {
                Toast.makeText(Login.this, "Logged in!", Toast.LENGTH_SHORT).show();
                System.out.println("Database connectie geslaagd");

                //  Gebruiker en studentenhuis ophalen uit de database

                //  Omzetten naar JSON objecten zodat deze in de Shared Preferences opgeslagen kunnen
                //  worden
                if (g != null && s != null)
                {
                    Gson gson = new Gson();
                    String ingelogdeGebruiker = gson.toJson(g);
                    String studentenhuis = gson.toJson(s);

                    //  In shared preferences de session (ingelogde gebruiker en bijbehorende studentenhuis)
                    //  opslaan
                    SharedPreferences sharedPref  = getSharedPreferences("User", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("logedInUser", ingelogdeGebruiker);
                    editor.putString("home", studentenhuis);
                    editor.commit();
                }

                //  Open 'main menu' scherm van een studentenhuis
                Intent mainActivity = new Intent(Login.this, MainActivity.class);
                System.out.println("Opened main menu");
                Login.this.startActivity(mainActivity);
            }
            else
            {
                Toast.makeText(Login.this, "Login informatie verkeerd", Toast.LENGTH_SHORT).show();
                System.out.println("Database connectie gefaald");
            }
        }
    }
}
