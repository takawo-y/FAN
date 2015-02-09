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

import com.takawo.fan.util.FanConst;
import com.takawo.fan.util.FanMaster;
import com.takawo.fan.util.FanUtil;
import com.takawo.fan.util.KeyValuePair;
import com.takawo.fan.MyApplication;
import com.takawo.fan.R;
import com.takawo.fan.adaptor.KeyValuePairArrayAdapter;
import com.takawo.fan.db.FandbGame;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Takawo on 2015/01/20.
 */
public class GameRegistrationActivity extends ActionBarActivity {
    private Long playerId;
    private String playerName;
    private String playerImage;
    private long resultType;

    public GameRegistrationActivity() {
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

    @OnClick(R.id.button_game_registration)
    void onClickRegist(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("試合情報登録")
        .setMessage("登録しますか？")
        .setPositiveButton("はい",
            new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    registGame();
                    Intent intent = new Intent(GameRegistrationActivity.this, GameActivity.class);
                    intent.putExtra(FanConst.INTENT_PLAYER_ID, playerId);
                    intent.putExtra(FanConst.INTENT_PLAYER_NAME, playerName);
                    intent.putExtra(FanConst.INTENT_PLAYER_IMAGE, playerImage);
                    intent.putExtra(FanConst.INTENT_RESULT_TYPE, resultType);
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
        setContentView(R.layout.game_regist);
        ButterKnife.inject(this);
        getValueFromIntent();

        setToolbar();  //ToolBar設定
        setSpinnerGameType();
        setTimePicker();
        if(resultType == 0){
            //スコア
            tableResultTime.setVisibility(View.GONE);
        }else if(resultType == 1){
            //タイム
            tableResultScore.setVisibility(View.GONE);
        }
    }

    /**
     * Intentから値の取得
     */
    private void getValueFromIntent(){
        Intent intent = getIntent();
        playerId = intent.getLongExtra(FanConst.INTENT_PLAYER_ID, 0);
        playerName = intent.getStringExtra(FanConst.INTENT_PLAYER_NAME);
        playerImage = intent.getStringExtra(FanConst.INTENT_PLAYER_IMAGE);
        resultType = intent.getLongExtra(FanConst.INTENT_RESULT_TYPE, 0);
    }

    /**
     * Toolbar設定
     */
    private void setToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_done_grey600_36dp);
        if(playerImage == null || playerImage.isEmpty()){
            toolbar.setLogo(R.drawable.no_image);
        }else{
            toolbar.setLogo(new BitmapDrawable(getResources(), FanUtil.resizeImage(playerImage, 100)));
        }
        toolbar.setTitle(playerName);
        toolbar.setSubtitle(R.string.game_registration_view_name);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     Intent intent = new Intent(GameRegistrationActivity.this, GameActivity.class);
                                                     intent.putExtra(FanConst.INTENT_PLAYER_ID, playerId);
                                                     intent.putExtra(FanConst.INTENT_PLAYER_NAME, playerName);
                                                     intent.putExtra(FanConst.INTENT_PLAYER_IMAGE, playerImage);
                                                     intent.putExtra(FanConst.INTENT_RESULT_TYPE, resultType);
                                                     startActivity(intent);
                                                 }
                                             }

        );
    }

    private void setSpinnerGameType(){
        inputGameType.setOnItemSelectedListener(onItemSelectedListener);
        KeyValuePairArrayAdapter adapter = new KeyValuePairArrayAdapter(this, android.R.layout.simple_spinner_item, FanMaster.getGameType());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputGameType.setAdapter(adapter);
        inputGameType.setSelection(adapter.getPosition(0));

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
    private void registGame(){
        FandbGame game = new FandbGame(
                playerId,
                null,
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
        app.getDaoSession().getFandbGameDao().insert(game);
    }

}
