package com.takawo.fan.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TimePicker;

import com.takawo.fan.MyApplication;
import com.takawo.fan.R;
import com.takawo.fan.adaptor.KeyValuePairArrayAdapter;
import com.takawo.fan.db.FandbGame;
import com.takawo.fan.db.FandbGameDao;
import com.takawo.fan.db.FandbPlayer;
import com.takawo.fan.util.FanConst;
import com.takawo.fan.util.FanMaster;
import com.takawo.fan.util.FanUtil;
import com.takawo.fan.util.KeyValuePair;

import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Takawo on 2015/01/20.
 */
public class GameUpdateActivity extends ActionBarActivity {
    private Long playerId;
    private Long gameId;
    private FandbPlayer playerData;

    public GameUpdateActivity() {
        super();
    }

    @InjectView(R.id.tool_bar)
    Toolbar toolbar;

    @InjectView(R.id.inputGameType)
    Spinner inputGameType;
    @InjectView(R.id.inputGameCategory)
    EditText inputGameCategory;
    @InjectView(R.id.inputGameInfo)
    EditText inputGameInfo;
    @InjectView(R.id.inputGamePlace)
    EditText inputGamePlace;
    @InjectView(R.id.inputGameWeather)
    EditText inputGameWeather;
    @InjectView(R.id.inputGameTemperature)
    EditText inputGameTemperature;
    @InjectView(R.id.inputGameDate)
    DatePicker inputGameDate;
    @InjectView(R.id.inputGameStartTime)
    TimePicker inputGameStartTime;
    @InjectView(R.id.inputGameEndTime)
    TimePicker inputGameEndTime;
    @InjectView(R.id.inputGameOpposition)
    EditText inputGameOpposition;
    @InjectView(R.id.inputGameResult)
    EditText inputGameResult;
    @InjectView(R.id.inputRowResultScore)
    TableRow tableResultScore;
    @InjectView(R.id.inputRowResultTime)
    TableRow tableResultTime;
    @InjectView(R.id.inputGameResultScore)
    EditText inputGameResultScore;
    @InjectView(R.id.inputGameResultTime)
    EditText inputGameResultTime;
    @InjectView(R.id.inputGameComment)
    EditText inputGameComment;

    @OnClick(R.id.button_game_update)
    void onClickRegist(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("試合情報更新")
        .setMessage("更新しますか？")
        .setPositiveButton("はい",
            new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    updateGame();
                    Intent intent = new Intent(GameUpdateActivity.this, GameActivity.class);
                    intent.putExtra(FanConst.INTENT_PLAYER_ID, playerId);
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
        setContentView(R.layout.activity_game_update);
        ButterKnife.inject(this);
        playerId = getIntent().getLongExtra(FanConst.INTENT_PLAYER_ID, 0);
        playerData = ((MyApplication)getApplication()).getDaoSession().getFandbPlayerDao().load(playerId);
        gameId = getIntent().getLongExtra(FanConst.INTENT_GAME_ID, 0);

        setToolbar();  //ToolBar設定
        setData();

        setTimePicker();
        if(playerData.getResultType() == 0){
            //スコア
            tableResultTime.setVisibility(View.GONE);
        }else if(playerData.getResultType() == 1){
            //タイム
            tableResultScore.setVisibility(View.GONE);
        }
    }

    /**
     * Toolbar設定
     */
    private void setToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_done_grey600_36dp);
        if(playerData.getPlayerImagePath() == null || playerData.getPlayerImagePath().isEmpty()){
            toolbar.setLogo(R.drawable.no_image);
        }else{
            toolbar.setLogo(new BitmapDrawable(getResources(), FanUtil.resizeImage(playerData.getPlayerImagePath(), 100)));
        }
        toolbar.setTitle(playerData.getPlayerName());
        toolbar.setSubtitle(R.string.game_registration_view_name);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     Intent intent = new Intent(GameUpdateActivity.this, GameActivity.class);
                                                     intent.putExtra(FanConst.INTENT_PLAYER_ID, playerId);
                                                     startActivity(intent);
                                                 }
                                             }

        );
    }

    private FandbGame getGameData(){
        MyApplication app = (MyApplication)getApplication();
        return app.getDaoSession().getFandbGameDao().queryBuilder()
                .where(FandbGameDao.Properties.PlayerId.eq(playerId), FandbGameDao.Properties.Id.eq(gameId)).unique();
    }

    private void setData(){
        FandbGame gameData = getGameData();
        setSpinnerGameType(gameData.getGameType());  //Gameタイプ
        inputGameCategory.setText(gameData.getGameCategory());  //Gameカテゴリ
        inputGameInfo.setText(gameData.getGameInfo());  //Gameインフォメーション



    }

    /**
     * GameTypeセット
     *
     * @param type
     */
    private void setSpinnerGameType(Long type){
        inputGameType.setOnItemSelectedListener(onItemSelectedListener);
        KeyValuePairArrayAdapter adapter = new KeyValuePairArrayAdapter(this, android.R.layout.simple_spinner_item, FanMaster.getGameType());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputGameType.setAdapter(adapter);
        inputGameType.setSelection(adapter.getPosition(type.intValue()));

    }

    /**
     * @brief スピナーのOnItemSelectedListener
     */
    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            KeyValuePair item = (KeyValuePair)inputGameType.getSelectedItem();
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    private void setTimePicker(){
        inputGameStartTime.setIs24HourView(true);
        inputGameEndTime.setIs24HourView(true);
    }

    /**
     * Game登録
     */
    private void updateGame(){
        FandbGame game = new FandbGame(
                playerId,
                gameId,
                new Long(((KeyValuePair)inputGameType.getSelectedItem()).getKey()),
                inputGameCategory.getText().toString(),
                inputGameInfo.getText().toString(),
                inputGamePlace.getText().toString(),
                inputGameWeather.getText().toString(),
                inputGameTemperature.getText().toString(),
                FanUtil.getDate(inputGameDate),
                FanUtil.getTimeString(inputGameStartTime),
                FanUtil.getTimeString(inputGameEndTime),
                inputGameOpposition.getText().toString(),
                inputGameResult.getText().toString(),
                inputGameResultScore.getText().toString(),
                inputGameResultTime.getText().toString(),
                inputGameComment.getText().toString()
        );
        MyApplication app = (MyApplication)getApplication();
        app.getDaoSession().getFandbGameDao().update(game);
    }

}
