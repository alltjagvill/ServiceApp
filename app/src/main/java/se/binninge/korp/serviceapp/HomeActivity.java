package se.binninge.korp.serviceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolBar);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.app_bar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_search:
                Toast.makeText(this, "Search button selected", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.action_adds:
                Toast.makeText(this, "Adds button selected", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.action_nearby:
                Toast.makeText(this, "Nearby button selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_settings:
                Toast.makeText(this, "Settings button selected", Toast.LENGTH_SHORT).show();
                return true;

                default: return super.onOptionsItemSelected(item);
        }


    }
}
