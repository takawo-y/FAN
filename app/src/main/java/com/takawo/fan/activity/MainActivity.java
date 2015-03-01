package com.takawo.fan.activity;

import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.takawo.fan.MyApplication;
import com.takawo.fan.adapter.PlayerAdapter;
import com.takawo.fan.util.MyItemDecoration;
import com.takawo.fan.R;
import com.takawo.fan.db.FandbPlayer;

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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        recyclerViewPlayer.setAdapter(new PlayerAdapter(this, playerList));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_dl:
                Toast.makeText(this, "Download", Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
    }

}
