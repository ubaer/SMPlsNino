package nl.tvj.studenthome;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AvondetenNieuw extends AppCompatActivity {

    //
    private TextView etDate;
    private TextView ettime;
    private EditText totaleKosten;
    private EditText omschrijving;
    private Database dbm;
    private Date date;
    double kosten ;
    String eventOmschrijving;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avondeten_nieuw);
        ettime = (TextView) findViewById(R.id.tvTijd);
        etDate = (TextView) findViewById(R.id.tvDate);
        totaleKosten = (EditText) findViewById(R.id.etTotaleKosten);
        omschrijving = (EditText) findViewById(R.id.etOmschrijving);
        dbm = new Database();
        date = new Date();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_avondeten_nieuw, menu);
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

    public void btnSetDate(View view) {
        DatePickerDialog.OnDateSetListener myDatePickerCallback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                etDate.setText(String.valueOf(year) +"-"+ String.valueOf(monthOfYear) +"-"+ String.valueOf(dayOfMonth));
                date.setYear(year);
                date.setMonth(monthOfYear);
                date.setDate(dayOfMonth);
            }
        };
        new DatePickerDialog(this,
                DatePickerDialog.THEME_DEVICE_DEFAULT_DARK,
                myDatePickerCallback,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
    }



    public void btnSetTime(View view) {
        TimePickerDialog.OnTimeSetListener myTimePickerCallback = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // do stuff with the time from the picker
                // the hour and minute are in 24-hour format
                String hour = Integer.toString(hourOfDay);
                String min = Integer.toString(minute);

                if(hourOfDay < 10)
                {
                    hour = "0"+ Integer.toString(hourOfDay);
                }
                if(minute < 10)
                {
                    min = "0"+ Integer.toString(minute);
                }
                ettime.setText(hour+":"+min);
                date.setHours(hourOfDay);
                date.setMinutes(minute);
            }
        };

        new TimePickerDialog(this,
                TimePickerDialog.THEME_TRADITIONAL,
                myTimePickerCallback,
                Calendar.getInstance().get(Calendar.HOUR),
                Calendar.getInstance().get(Calendar.MINUTE),
                false).show();
    }

    public void btnCreateEvent(View view) {
        //Het ophalen van de gebruiker en het studentenhuis uit de shared preferences
        kosten = Double.parseDouble(totaleKosten.getText().toString());
        eventOmschrijving = omschrijving.getText().toString();
        new createEvent().execute((Void[]) null);
    }
    private class createEvent extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences sharedPref = getSharedPreferences("User", Context.MODE_PRIVATE);
            String g = sharedPref.getString("logedInUser", "");
            String s = sharedPref.getString("home", "");
            Gson gson = new Gson();
            Gebruiker host = gson.fromJson(g, Gebruiker.class);
            Studentenhuis studentenhuis = gson.fromJson(s, Studentenhuis.class);
            Avondeten avondeten = new Avondeten(kosten,eventOmschrijving, host,date );
            try {
                dbm.addActiviteit(studentenhuis,avondeten);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            Intent avondetenOverzicht = new Intent(AvondetenNieuw.this, AvondetenOverzicht.class);
            Toast.makeText(AvondetenNieuw.this, "Avondeten toegevoegd", Toast.LENGTH_SHORT).show();
            AvondetenNieuw.this.startActivity(avondetenOverzicht);
        }
    }
}
