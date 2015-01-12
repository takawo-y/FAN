package com.example.takawo.fan;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.couchbase.lite.Query;
import com.example.takawo.fan.db.DBManager;
import com.example.takawo.fan.db.adaptor.PlayerAdaptor;
import com.example.takawo.fan.db.view.Player;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private RecyclerView recyclerViewPlayer;
    private RecyclerView.Adapter adapterPlayer;
    private RecyclerView.LayoutManager layoutManagerPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar =(Toolbar) findViewById(R.id.activity_my_toolbar);
        toolbar.setTitle("FAN --toolbar");
        setSupportActionBar(toolbar);

        recyclerViewPlayer = (RecyclerView)findViewById(R.id.list_player);
        recyclerViewPlayer.setHasFixedSize(true);

        layoutManagerPlayer = new LinearLayoutManager(this);
        recyclerViewPlayer.setLayoutManager(layoutManagerPlayer);

        DBManager dbManager = new DBManager(this);
        Player playerView = new Player();
        Query playerList = playerView.getQuery(dbManager.createDB());

//        adapterPlayer = new PlayerAdaptor(new ArrayList<String>(), )
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
