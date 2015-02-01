package com.takawo.fan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.takawo.fan.db.FandbPlayer;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Takawo on 2015/01/20.
 */
public class PlayerRegistrationActivity extends ActionBarActivity {
    public PlayerRegistrationActivity() {
        super();
    }

    @InjectView(R.id.tool_bar)
    Toolbar toolbar;

    @InjectView(R.id.inputPlayerName)
    EditText inputPlayerName;

    @InjectView(R.id.inputGameEvent)
    EditText inputGameEvent;

    @InjectView(R.id.inputPlayerCategory)
    EditText inputPlayerCategory;

    @InjectView(R.id.inputPlayerResultType)
    RadioGroup inputPlayerResultType;

    @InjectView(R.id.inputPlayerComment)
    EditText inputPlayerComment;

    @OnClick(R.id.button_player_registration)
    void onClickRegist(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Player登録")
        .setMessage("登録しますか？")
        .setPositiveButton("はい",
            new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    registPlayer();
                    Intent intent = new Intent(PlayerRegistrationActivity.this, MainActivity.class);
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
        setContentView(R.layout.player_regist);
        ButterKnife.inject(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PlayerRegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

        );
        //ToolBar設定
        setSupportActionBar(toolbar);
    }

    /**
     * Player登録
     */
    private void registPlayer(){
        FandbPlayer player = new FandbPlayer(
                0,  //id,autoincrementだからnullを渡したい
                inputPlayerName.getText().toString(),
                inputGameEvent.getText().toString(),
                getResultType(),
                inputPlayerCategory.getText().toString(),
                null,
                null,
                inputPlayerComment.getText().toString());
        MyApplication app = (MyApplication)getApplication();
        app.getDaoSession().getFandbPlayerDao().insert(player);
    }

    /**
     * ResultType取得
     * 0:スコア、1:タイム
     * @return
     */
    private long getResultType(){
        int resultTypeId = inputPlayerResultType.getCheckedRadioButtonId();
        long resultType = 0;
        if (resultTypeId == R.id.playerResultTypeRB0){
            resultType = 0;
        }else if (resultTypeId == R.id.playerResultTypeRB1){
            resultType = 1;
        }
        return resultType;
    }
}
