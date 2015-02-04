package com.takawo.fan;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.takawo.fan.adaptor.PlayerAdaptor;
import com.takawo.fan.db.data.DBHelper;
import com.takawo.fan.db.data.PlayerDemoData;
import com.takawo.fan.db.DaoMaster;
import com.takawo.fan.db.DaoSession;
import com.takawo.fan.db.FandbPlayer;
import com.takawo.fan.db.FandbPlayerDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    private RecyclerView recyclerViewPlayer;
    private RecyclerView.Adapter adapterPlayer;
    private RecyclerView.LayoutManager layoutManagerPlayer;

    @InjectView(R.id.tool_bar)
    Toolbar toolbar;

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

        //ToolBar設定
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setTitle(R.string.player_list_view_name);

        //DB接続
        MyApplication app = (MyApplication)getApplication();
        List<FandbPlayer> playerList = app.getDaoSession().getFandbPlayerDao().loadAll(); //Player検索

        recyclerViewPlayer = (RecyclerView)findViewById(R.id.list_player);
        recyclerViewPlayer.setHasFixedSize(true);
        recyclerViewPlayer.addItemDecoration(new MyItemDecoration(this));

        layoutManagerPlayer = new LinearLayoutManager(this);
        recyclerViewPlayer.setLayoutManager(layoutManagerPlayer);
        //recyclerViewPlayer.setAdapter(new PlayerDemoAdaptor(this, getDemoPlayerList()));  //デモ用Adaptor
        recyclerViewPlayer.setAdapter(new PlayerAdaptor(this, playerList));  //本番Adaptor

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

    private List<PlayerDemoData> getDemoPlayerList(){
        /***********************************
         データ作成
         **********************************/
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.no_image);
        PlayerDemoData player1 = new PlayerDemoData();
        player1.setPlayerId(1);
        player1.setPlayerImg(img);
        player1.setPlayerName("柏レイソル");
        player1.setGameEvent("サッカー");
        PlayerDemoData player2 = new PlayerDemoData();
        player2.setPlayerId(2);
        player2.setPlayerImg(img);
        player2.setPlayerName("Takumi");
        player2.setGameEvent("水泳");
        PlayerDemoData player3 = new PlayerDemoData();
        player3.setPlayerId(3);
        player3.setPlayerImg(img);
        player3.setPlayerName("西武ライオンズ");
        player3.setGameEvent("野球");
        ArrayList<PlayerDemoData> results = new ArrayList<>();
        results.add(player1);
        results.add(player2);
        results.add(player3);

        return results;
    }
}
