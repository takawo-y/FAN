package com.takawo.fan.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.takawo.fan.MyApplication;
import com.takawo.fan.MyItemDecoration;
import com.takawo.fan.R;
import com.takawo.fan.adaptor.PlayerAdaptor;
import com.takawo.fan.db.data.PlayerDemoData;
import com.takawo.fan.db.FandbPlayer;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    private RecyclerView.LayoutManager layoutManagerPlayer;

    @InjectView(R.id.tool_bar)
    Toolbar toolbar;

    @InjectView(R.id.list_player)
    RecyclerView recyclerViewPlayer;

    @OnClick(R.id.fab_player)
    void onClickAdd(){
        Intent intent = new Intent(MainActivity.this, PlayerRegistrationActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setToolbar();  //ToolBar設定
        setList();  //一覧取得
    }

    private void setToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setTitle(R.string.player_list_view_name);
    }

    private void setList(){
        MyApplication app = (MyApplication)getApplication();
        List<FandbPlayer> playerList = app.getDaoSession().getFandbPlayerDao().loadAll(); //Player検索

        recyclerViewPlayer.setHasFixedSize(true);
        recyclerViewPlayer.addItemDecoration(new MyItemDecoration(this));

        layoutManagerPlayer = new LinearLayoutManager(this);
        recyclerViewPlayer.setLayoutManager(layoutManagerPlayer);
        recyclerViewPlayer.setAdapter(new PlayerAdaptor(this, playerList));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
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
