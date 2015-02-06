package com.takawo.fan.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.takawo.fan.FanMaster;
import com.takawo.fan.KeyValuePair;
import com.takawo.fan.MyApplication;
import com.takawo.fan.R;
import com.takawo.fan.adaptor.KeyValuePairArrayAdapter;
import com.takawo.fan.db.FandbPlayer;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Takawo on 2015/01/20.
 */
public class GameRegistrationActivity extends ActionBarActivity {
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
    EditText inputGameStartTime;
    @InjectView(R.id.inputGameEndTime)
    EditText inputGameEndTime;
    @InjectView(R.id.inputGameOpposition)
    EditText inputGameOpposition;
    @InjectView(R.id.inputGameResult)
    EditText inputGameResult;
    @InjectView(R.id.inputGameResultScore)
    EditText inputGameResultScore;
    @InjectView(R.id.inputGameResultTime)
    EditText inputGameResultTime;
    @InjectView(R.id.inputGameComment)
    EditText inputGameComment;

    @OnClick(R.id.button_player_registration)
    void onClickRegist(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("試合情報登録")
        .setMessage("登録しますか？")
        .setPositiveButton("はい",
            new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    registGame();
                    Intent intent = new Intent(GameRegistrationActivity.this, GameActivity.class);
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

        setToolbar();  //ToolBar設定
        setSpinnerGameType();
    }

    private void setToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_done_grey600_36dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     Intent intent = new Intent(GameRegistrationActivity.this, GameActivity.class);
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
            Toast.makeText(GameRegistrationActivity.this, item.getKey().toString(), Toast.LENGTH_LONG).show();
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * Game登録
     */
    private void registGame(){
    }

}
