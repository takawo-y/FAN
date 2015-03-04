package com.takawo.fan.activity;

import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.takawo.fan.MyApplication;
import com.takawo.fan.adapter.PlayerAdapter;
import com.takawo.fan.db.FandbGame;
import com.takawo.fan.db.FandbImage;
import com.takawo.fan.util.MyItemDecoration;
import com.takawo.fan.R;
import com.takawo.fan.db.FandbPlayer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
        List<FandbPlayer> playerList = ((MyApplication)getApplication()).getDaoSession().getFandbPlayerDao().loadAll(); //Player検索

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
                downloadPlayer();
                downloadGame();
                downloadImage();
                Toast.makeText(this, "Completed downloads All Data\r\n"+"/data/data/com.takawo.fan/files", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    private void downloadPlayer(){
        FileOutputStream fs = null;
        BufferedWriter out = null;
        List<FandbPlayer> playerList = ((MyApplication)getApplication()).getDaoSession().getFandbPlayerDao().loadAll();
        try{
            fs = this.openFileOutput("fandb_player.csv", 0);
            out = new BufferedWriter(new OutputStreamWriter(fs));
            for (FandbPlayer data: playerList){
                out.write(data.getId()+","
                        +data.getPlayerName()+","
                        +data.getGameEvent()+","
                        +data.getResultType()+","
                        +data.getCategory()+","
                        +data.getPlayerColor()+","
                        +data.getPlayerFontColor()+","
                        +data.getPlayerIconColor()+","
                        +data.getPlayerImagePath()+","
                        +data.getPlayerComment());
            }
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Download Error\r\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void downloadGame(){
        FileOutputStream fs = null;
        BufferedWriter out = null;
        List<FandbGame> gameList = ((MyApplication)getApplication()).getDaoSession().getFandbGameDao().loadAll();
        try{
            fs = this.openFileOutput("fandb_game.csv", 0);
            out = new BufferedWriter(new OutputStreamWriter(fs));
            for (FandbGame data: gameList){
                out.write(data.getPlayerId()+","
                        +data.getId()+","
                        +data.getGameType()+","
                        +data.getGameCategory()+","
                        +data.getGameInfo()+","
                        +data.getPlace()+","
                        +data.getWeather()+","
                        +data.getTemperature()+","
                        +data.getGameDay()+","
                        +data.getStartTime()+","
                        +data.getEndTime()+","
                        +data.getOpposition()+","
                        +data.getOppositionImagePath()+","
                        +data.getResult()+","
                        +data.getResultScore()+","
                        +data.getResultTime()+","
                        +data.getComment());
            }
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Download Error\r\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void downloadImage(){
        FileOutputStream fs = null;
        BufferedWriter out = null;
        List<FandbImage> imageList = ((MyApplication)getApplication()).getDaoSession().getFandbImageDao().loadAll();
        try{
            fs = this.openFileOutput("fandb_image.csv", 0);
            out = new BufferedWriter(new OutputStreamWriter(fs));
            for (FandbImage data: imageList){
                out.write(data.getPlayerId()+","
                        +data.getGameId()+","+
                        +data.getId()+","
                        +data.getPath()+","
                        +data.getTitle()+","
                        +data.getComment());
            }
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Download Error\r\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private volatile boolean mConfirmExit;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN) {
            if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                if(!mConfirmExit) {
                    Toast.makeText(MainActivity.this, "もう1度押すと終了します", Toast.LENGTH_SHORT).show();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mConfirmExit = false;
                        }
                    }, 1000);
                    mConfirmExit = true;
                } else {
                    finish();
                }
                return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }

}
