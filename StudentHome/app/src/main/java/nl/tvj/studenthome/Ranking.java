package nl.tvj.studenthome;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class Ranking extends AppCompatActivity {
    SwipeRefreshLayout  swipeLayout;
    ArrayList<Gebruiker> listRankings = new ArrayList<>();
    Database dbm = new Database();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        //swipelayout
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_containerRanking);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new addRankings().execute((Void[]) null);
            }
        });
        new addRankings().execute((Void[]) null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ranking, menu);
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
    private class addRankings extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                listRankings = dbm.getGebruikersAVGRating();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            final ArrayAdapter<Gebruiker> itemsAdapter = new ArrayAdapter<Gebruiker>(Ranking.this, android.R.layout.simple_list_item_1, listRankings);
            final ListView lvRankings = (ListView) findViewById(R.id.lvRankings);
            lvRankings.setAdapter(itemsAdapter);

            final String[] laatWaterVerbranden = { "Nino score: 1,5" };
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(Ranking.this, android.R.layout.simple_list_item_1, laatWaterVerbranden);
            final ListView lvLWV = (ListView)findViewById(R.id.listView);
            lvLWV.setAdapter(adapter);

            swipeLayout.setRefreshing(false);
        }
    }
}
