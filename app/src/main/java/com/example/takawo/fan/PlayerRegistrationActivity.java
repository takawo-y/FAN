package com.example.takawo.fan;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

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
