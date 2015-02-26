package com.takawo.fan.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.takawo.fan.adapter.GameAdapter;
import com.takawo.fan.adapter.KeyValuePairArrayAdapter;
import com.takawo.fan.db.FandbImage;
import com.takawo.fan.db.FandbPlayer;
import com.takawo.fan.util.FanConst;
import com.takawo.fan.MyApplication;
import com.takawo.fan.util.FanMaster;
import com.takawo.fan.util.FanUtil;
import com.takawo.fan.util.KeyValuePair;
import com.takawo.fan.util.MyItemDecoration;
import com.takawo.fan.R;
import com.takawo.fan.db.FandbGame;
import com.takawo.fan.db.FandbGameDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by 9004027600 on 2015/02/04.
 */
public class GameActivity extends ActionBarActivity {

    private Long id;
    private FandbPlayer playerData;
    private List<FandbGame> gameList;
    private RecyclerView.LayoutManager layoutManagerGame;

    private final String FILTER_NOTHING = "すべて";

    @InjectView(R.id.tool_bar_game_list)
    Toolbar toolbar;

    @InjectView(R.id.list_game)
    RecyclerView recyclerViewGame;

    @InjectView(R.id.searchKeyDate)
    Spinner searchKeyDate;
    @InjectView(R.id.searchKeyCategory)
    Spinner searchKeyCategory;
    @InjectView(R.id.searchKeyType)
    Spinner searchKeyType;

    @OnClick(R.id.fab_game)
    void onClickAdd(){
        //Game新規登録画面を開く
        Intent intent = new Intent(GameActivity.this, GameRegistrationActivity.class);
        intent.putExtra(FanConst.INTENT_PLAYER_ID, id);
        startActivity(intent);
    }

    @OnClick(R.id.button_player_update)
    void onClickUpdate(){
        //Player更新画面を開く
        Intent intent = new Intent(GameActivity.this, PlayerUpdateActivity.class);
        intent.putExtra(FanConst.INTENT_PLAYER_ID, id);
        startActivity(intent);
    }

    @OnClick(R.id.button_player_delete)
    void onClickDelete(){
        //Player削除する
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Player削除")
                .setMessage("Playerデータを削除しますか？\n紐づく試合情報も全て削除されます。")
                .setPositiveButton("はい",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                ((MyApplication)getApplication()).getDaoSession().getFandbPlayerDao().deleteByKey(id);
                                FandbGame gameEntity = new FandbGame();
                                gameEntity.setPlayerId(id);
                                ((MyApplication)getApplication()).getDaoSession().getFandbGameDao().delete(gameEntity);
                                FandbImage imageEntity = new FandbImage();
                                imageEntity.setPlayerId(id);
                                ((MyApplication)getApplication()).getDaoSession().delete(imageEntity);
                                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                                intent.putExtra(FanConst.INTENT_PLAYER_ID, id);
                                startActivity(intent);
                            }
                        }
                )
                .setNegativeButton("いいえ",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){}
                        }
                )
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        id = intent.getLongExtra(FanConst.INTENT_PLAYER_ID, 0);
        playerData = ((MyApplication)getApplication()).getDaoSession().getFandbPlayerDao().load(id);

        setToolbar();  //ToolBar設定
        setList();  //一覧取得
        setSerachKeyDate();  //検索キー設定
        setSerachKeyCategory();  //検索キー設定
        setSerachKeyType();  //検索キー設定

    }

    /**
     * Toolbar設定
     */
    private void setToolbar(){
        if(playerData.getPlayerImagePath() == null || playerData.getPlayerImagePath().isEmpty()){
            toolbar.setLogo(R.drawable.no_image);
        }else{
            toolbar.setLogo(new BitmapDrawable(getResources(), FanUtil.resizeImage(playerData.getPlayerImagePath(), 100)));
        }
        toolbar.setTitle(playerData.getPlayerName());
        toolbar.setSubtitle(R.string.game_list_view_name);
        toolbar.setNavigationIcon(R.drawable.ic_done_grey600_36dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     Intent intent = new Intent(GameActivity.this, MainActivity.class);
                                                     startActivity(intent);
                                                 }
                                             }

        );
    }

    /**
     * Listセット
     */
    private void setList(){
        gameList = ((MyApplication)getApplication()).getDaoSession().getFandbGameDao().queryBuilder()
                .where(FandbGameDao.Properties.PlayerId.eq(id)).orderDesc(FandbGameDao.Properties.GameDay).list();

        recyclerViewGame.setHasFixedSize(true);
        recyclerViewGame.addItemDecoration(new MyItemDecoration(this));

        layoutManagerGame = new LinearLayoutManager(this);
        recyclerViewGame.setLayoutManager(layoutManagerGame);
        recyclerViewGame.setAdapter(new GameAdapter(this, gameList, new Integer(playerData.getPlayerColor())));
    }
    private void setQueryList(){
        String gameDay = searchKeyDate.getSelectedItem().toString();
        String category = searchKeyCategory.getSelectedItem().toString();
        int type = ((KeyValuePair)searchKeyType.getSelectedItem()).getKey();

        QueryBuilder qb = ((MyApplication)getApplication()).getDaoSession().getFandbGameDao().queryBuilder()
                .where(FandbGameDao.Properties.PlayerId.eq(id));

        if(FILTER_NOTHING.equals(gameDay) == false){
            Log.d("ReSearchGame", "試合日：「"+gameDay+"」");
            Date from = new Date(gameDay+"/01");
            Calendar cal = Calendar.getInstance();
            cal.setTime(from);
            cal.add(Calendar.MONTH, 1);
            Date to = cal.getTime();
            qb.where(FandbGameDao.Properties.GameDay.ge(from));
            qb.where(FandbGameDao.Properties.GameDay.lt(to));
        }
        if(FILTER_NOTHING.equals(category) == false){
            qb.where(FandbGameDao.Properties.GameCategory.eq(category));
        }
        if(type != 99){
            qb.where(FandbGameDao.Properties.GameType.eq(type));
        }
        List<FandbGame> list = qb.orderDesc(FandbGameDao.Properties.GameDay).list();
        recyclerViewGame.setHasFixedSize(true);
        recyclerViewGame.addItemDecoration(new MyItemDecoration(this));

        layoutManagerGame = new LinearLayoutManager(this);
        recyclerViewGame.setLayoutManager(layoutManagerGame);
        recyclerViewGame.setAdapter(new GameAdapter(this, list, new Integer(playerData.getPlayerColor())));
    }

    /**
     * 検索キーセット
     */
    private void setSerachKeyDate(){
        ArrayAdapter<String> dateAdapter =
                (new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getGameDateGroup(gameList)));
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchKeyDate.setAdapter(dateAdapter);
        searchKeyDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //初回起動時の動作
                if (searchKeyDate.isFocusable() == false) {
                    searchKeyDate.setFocusable(true);
                    return;
                }
                setQueryList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchKeyDate.setFocusable(false);
    }
    private void setSerachKeyCategory(){
        ArrayAdapter<String> categoryAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getGameCategoryGroup(gameList));
        searchKeyCategory.setAdapter(categoryAdapter);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchKeyCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //初回起動時の動作
                if (searchKeyCategory.isFocusable() == false) {
                    searchKeyCategory.setFocusable(true);
                    return;
                }
                setQueryList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchKeyCategory.setFocusable(false);
        searchKeyCategory.setPromptId(R.string.game_list_view_filter_category);
    }
    private void setSerachKeyType(){
        searchKeyType.setOnItemSelectedListener(onItemSelectedListenerOfType);
        KeyValuePairArrayAdapter typeAdapter = new KeyValuePairArrayAdapter(this, android.R.layout.simple_spinner_item, FanMaster.getGameTypeForSearch());
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchKeyType.setAdapter(typeAdapter);
        searchKeyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //初回起動時の動作
                if (searchKeyType.isFocusable() == false) {
                    searchKeyType.setFocusable(true);
                    return;
                }
                setQueryList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchKeyType.setFocusable(false);
        searchKeyType.setPromptId(R.string.game_list_view_filter_type);
    }

    private AdapterView.OnItemSelectedListener onItemSelectedListenerOfType = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            KeyValuePair item = (KeyValuePair)searchKeyType.getSelectedItem();

        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * 試合日の集約(yyyy/mm)
     *
     * @param result
     * @return
     */
    private List<String> getGameDateGroup(List<FandbGame> result){
        List<FandbGame> list = result;
        HashSet<String> dateList = new HashSet<>();
        for(FandbGame data: list){
            SimpleDateFormat formatA = new SimpleDateFormat("yyyy/MM/dd");
            String formatDate = formatA.format(data.getGameDay());
            dateList.add(formatDate.substring(0,7));
        }

        List<String> rtnList = new ArrayList<>();
        rtnList.add(FILTER_NOTHING);

        List<String> tmpList = new ArrayList<>();
        for(String value: dateList){
            tmpList.add(value);
        }
        Collections.sort(tmpList);
        rtnList.addAll(tmpList);

        return rtnList;
    }

    /**
     * カテゴリの集約
     *
     * @param result
     * @return
     */
    private List<String> getGameCategoryGroup(List<FandbGame> result){
        List<FandbGame> list = result;
        HashSet<String> categoryList = new HashSet<>();
        for(FandbGame data: list){
            categoryList.add(data.getGameCategory());
        }

        List<String> rtnList = new ArrayList<>();
        rtnList.add(FILTER_NOTHING);

        List<String> tmpList = new ArrayList<>();
        for(String value: categoryList){
            tmpList.add(value);
        }
        Collections.sort(tmpList);
        rtnList.addAll(tmpList);

        return rtnList;
    }
}
