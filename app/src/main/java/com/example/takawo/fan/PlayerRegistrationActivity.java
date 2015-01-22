package com.example.takawo.fan;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Takawo on 2015/01/20.
 */
public class PlayerRegistrationActivity extends ActionBarActivity {
    public PlayerRegistrationActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_regist);

        //ToolBar設定
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        ImageView registImg = (ImageView)findViewById(R.id.button_player_registration);
        registImg.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );

    }
}
