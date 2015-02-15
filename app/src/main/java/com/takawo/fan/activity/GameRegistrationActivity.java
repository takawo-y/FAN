package com.takawo.fan.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TextView;
import android.widget.TimePicker;

import com.takawo.fan.db.FandbPlayer;
import com.takawo.fan.util.FanConst;
import com.takawo.fan.util.FanMaster;
import com.takawo.fan.util.FanUtil;
import com.takawo.fan.util.KeyValuePair;
import com.takawo.fan.MyApplication;
import com.takawo.fan.R;
import com.takawo.fan.adaptor.KeyValuePairArrayAdapter;
import com.takawo.fan.db.FandbGame;

import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Takawo on 2015/01/20.
 */
public class GameRegistrationActivity extends ActionBarActivity {
    private Long playerId;
    private FandbPlayer playerData;

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
    TextView inputGameDate;
    @InjectView(R.id.inputGameStartTime)
    TextView inputGameStartTime;
    @InjectView(R.id.inputGameEndTime)
    TextView inputGameEndTime;
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

    @OnClick(R.id.inputGameDate)
    void onClickGameDate(){
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        inputGameDate.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    @OnClick(R.id.inputGameStartTime)
    void onClickStartTime(){
        final Calendar calendar = Calendar.getInstance();
        final int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int nowMinute = calendar.get(Calendar.MINUTE);

        final TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        inputGameStartTime.setText(String.format("%02d", hourOfDay)+":"+String.format("%02d", minute));
                    }
                },
                nowHour, nowMinute, true);
        timePickerDialog.show();
    }

    @OnClick(R.id.inputGameEndTime)
    void onClickEndTime(){
        final Calendar calendar = Calendar.getInstance();
        final int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int nowMinute = calendar.get(Calendar.MINUTE);

        final TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        inputGameEndTime.setText(String.format("%02d", hourOfDay)+":"+String.format("%02d", minute));
                    }
                },
                nowHour, nowMinute, true);
        timePickerDialog.show();
    }
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
        setContentView(R.layout.activity_game_regist);
        ButterKnife.inject(this);
        playerId = getIntent().getLongExtra(FanConst.INTENT_PLAYER_ID, 0);
        playerData = ((MyApplication)getApplication()).getDaoSession().getFandbPlayerDao().load(playerId);

        setToolbar();  //ToolBar設定
        setSpinnerGameType();
        if(playerData.getResultType()== 0){
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
                                                     Intent intent = new Intent(GameRegistrationActivity.this, GameActivity.class);
                                                     intent.putExtra(FanConst.INTENT_PLAYER_ID, playerId);
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
                new Date(inputGameDate.getText().toString()),
                inputGameStartTime.getText().toString(),
                inputGameEndTime.getText().toString(),
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
