package nl.tvj.studenthome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class AvondetenOverzicht extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avondeten);

        //  TODO haal alle avondeten activiteiten op (via de Studentenhuis klasse) en laat een aantal avondeten activiteiten uit het verleden zien
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
}
