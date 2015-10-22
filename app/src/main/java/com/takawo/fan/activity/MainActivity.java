package com.takawo.fan.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.takawo.fan.MyApplication;
import com.takawo.fan.adapter.PlayerAdapter;
import com.takawo.fan.db.FandbGame;
import com.takawo.fan.db.FandbGameDao;
import com.takawo.fan.util.FanConst;
import com.takawo.fan.util.FanUtil;
import com.takawo.fan.util.GameSearchKey;
import com.takawo.fan.util.MyItemDecoration;
import com.takawo.fan.R;
import com.takawo.fan.db.FandbPlayer;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.dao.query.QueryBuilder;


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

        SharedPreferences sharePre = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        sharePre.edit().putString(FanConst.SHARED_GAME_SEARCH_KEY, gson.toJson(new GameSearchKey())).commit();  //検索キー保持をクリア

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

        Calendar today = Calendar.getInstance();
        String todayYear = Integer.toString(today.get(Calendar.YEAR));

        layoutManagerPlayer = new LinearLayoutManager(this);
        recyclerViewPlayer.setLayoutManager(layoutManagerPlayer);
        recyclerViewPlayer.setAdapter(new PlayerAdapter(this, playerList, todayYear, getPlayerCount(playerList, todayYear),
                getPlayerWatchGameCount(playerList, todayYear)));

    }

    /**
     * Player毎のGame数
     *
     * @param playerList
     * @return
     */
    private Map<Long, Integer> getPlayerCount(List<FandbPlayer> playerList, String searchKeyYear){
        Map map = new HashMap();

        for(FandbPlayer player: playerList){
            QueryBuilder countQB = ((MyApplication)getApplication()).getDaoSession().getFandbGameDao().queryBuilder();
            countQB.where(FandbGameDao.Properties.PlayerId.eq(player.getId()));
            Date from = new Date(searchKeyYear+"/01/01");
            Date to = new Date(searchKeyYear+"/12/31");
            countQB.where(FandbGameDao.Properties.GameDay.ge(from));
            countQB.where(FandbGameDao.Properties.GameDay.lt(to));
            List<FandbGame> gameList = countQB.list();
            map.put(player.getId(), new Integer(gameList.size()));
        }

        return map;
    }

    /**
     * Player毎の現地観戦数
     *
     * @param playerList
     * @return
     */
    private Map<Long, Integer> getPlayerWatchGameCount(List<FandbPlayer> playerList, String searchKeyYear){
        Map map = new HashMap();

        for(FandbPlayer player: playerList){
            QueryBuilder countQB = ((MyApplication)getApplication()).getDaoSession().getFandbGameDao().queryBuilder();
            countQB.where(FandbGameDao.Properties.PlayerId.eq(player.getId()),
                    FandbGameDao.Properties.GameType.eq(new Integer(0)) );
            Date from = new Date(searchKeyYear+"/01/01");
            Date to = new Date(searchKeyYear+"/12/31");
            countQB.where(FandbGameDao.Properties.GameDay.ge(from));
            countQB.where(FandbGameDao.Properties.GameDay.lt(to));
            List<FandbGame> gameList = countQB.list();
            map.put(player.getId(), new Integer(gameList.size()));
        }

        return map;
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
                //データエクスポート
                String outDir = Environment.getExternalStorageDirectory().getPath()+"/data/fan/dbExport/";
                String outFile = "fandb.sqlite";
                if(exportDBFile(outDir, outFile)){
                    Toast.makeText(this, "Completed download Data File.\r\n"+outDir+outFile, Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_up:
                //sqliteファイル取込
                final String inDir = Environment.getExternalStorageDirectory().getPath()+"/data/fan/dbImport/";
                final String inFile = "fandb.sqlite";

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final Context context = this;
                builder.setTitle("データの取込")
                        .setMessage("下記DBファイルを取り込みますか。\r\n実行前に現在のデータをエクスポートしておいてください。\r\n"+
                        inDir+inFile)
                        .setPositiveButton("はい",
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which){
                                        if(importDBFile(inDir, inFile)){
                                            Toast.makeText(context, "Completed Import Data File.\r\n"+inDir+inFile, Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                }
                        )
                        .setNegativeButton("いいえ",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }
                        )
                        .show();

        }
        return false;
    }

    /**
     * DBファイル出力
     *
     * @return
     */
    private boolean exportDBFile(String outDirString, String outFile){

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

    private boolean importDBFile(String inDirString, String inFileString){
        String dbFile = ((MyApplication)getApplication()).getDaoSession().getDatabase().getPath();
        File inFile = new File(inDirString+inFileString);
        if(inFile.exists() == false){
            Toast.makeText(this, "Import Error:該当ファイルが存在しません\r\n"+inFile.getPath(), Toast.LENGTH_LONG).show();
            return false;
        }
        String err = FanUtil.filecopy(inDirString+inFileString, dbFile);
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
                    Toast.makeText(MainActivity.this, "もう1度押すと、終了します。", Toast.LENGTH_SHORT).show();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mConfirmExit = false;
                        }
                    }, 1000);
                    mConfirmExit = true;
                } else {
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory(Intent.CATEGORY_HOME);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainActivity.this.startActivity(homeIntent);
                }
                return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }
}
