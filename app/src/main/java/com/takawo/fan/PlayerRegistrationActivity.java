package com.takawo.fan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

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

    @OnClick(R.id.button_player_registration)
    void onClickRegist(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Player登録")
        .setMessage("登録しますか？")
        .setPositiveButton("はい",
            new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
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

        //ToolBar設定
        setSupportActionBar(toolbar);
    }
}
