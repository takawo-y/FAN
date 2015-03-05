package com.takawo.fan.activity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Environment;
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
import com.takawo.fan.util.FanUtil;
import com.takawo.fan.util.MyItemDecoration;
import com.takawo.fan.R;
import com.takawo.fan.db.FandbPlayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
                String outDir = Environment.getExternalStorageDirectory().getPath()+"/data/fan/db/";
                String outFile = "fandb.sqlite";
                if(writeDBFile(outDir, outFile)){
                    Toast.makeText(this, "Completed download Data File.\r\n"+outDir+outFile, Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return false;
    }

    /**
     * DBファイルコピー
     *
     * @return
     */
    private boolean writeDBFile(String outDirString, String outFile){

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) == false){
            Toast.makeText(this, "Export Error:SDカードをマウントしてください", Toast.LENGTH_LONG).show();
            return false;
        }

        String dbFile = ((MyApplication)getApplication()).getDaoSession().getDatabase().getPath();
        File outDir = new File(outDirString);
        if(outDir.exists() == false){
            if(outDir.mkdirs() == false){
                Toast.makeText(this, "Export Error:ディレクトリ作成に失敗しました\r\n"+outDir.getPath(), Toast.LENGTH_LONG).show();
                return false;
            }
        }

        String err = FanUtil.filecopy(dbFile, outDirString+outFile);
        if(err.isEmpty() == false){
            Toast.makeText(this, err, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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
