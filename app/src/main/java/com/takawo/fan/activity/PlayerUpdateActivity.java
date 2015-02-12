package com.takawo.fan.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.squareup.picasso.Picasso;
import com.takawo.fan.MyApplication;
import com.takawo.fan.R;
import com.takawo.fan.db.FandbPlayer;
import com.takawo.fan.util.FanConst;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Takawo on 2015/02/11.
 */
public class PlayerUpdateActivity extends ActionBarActivity {

    public PlayerUpdateActivity() {super();}

    private int RESULT_PICK_FILENAME = 1;
    private SharedPreferences sharePre;
    private final String SHARE_IMAGE_PATH_KEY = "imagePath";

    @InjectView(R.id.tool_bar)
    Toolbar toolbar;

    @InjectView(R.id.inputPlayerImage)
    ImageView inputPlayerImage;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_regist);
        ButterKnife.inject(this);

        setToolbar();  //ToolBar設定
        setData();  //初期表示
    }

    private void setToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_done_grey600_36dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     Intent intent = new Intent(PlayerUpdateActivity.this, MainActivity.class);
                                                     startActivity(intent);
                                                 }
                                             }

        );
    }

    private void setData(){
        FandbPlayer data = getPlayerData();
        if(data.getPlayerImagePath() == null || data.getPlayerImagePath().isEmpty()){
            Picasso.with(this).load(R.drawable.no_image).into(inputPlayerImage);
        }else{
            Picasso.with(this).load(new File(data.getPlayerImagePath())).into(inputPlayerImage);
        }
        inputPlayerName.setText(data.getPlayerName());
        inputGameEvent.setText(data.getGameEvent());
        inputPlayerCategory.setText(data.getCategory());
        Long resultType = data.getResultType();
        switch (resultType.intValue()){
            case 0:
                inputPlayerResultType.check(R.id.playerResultTypeRB0);
            case 1:
                inputPlayerResultType.check(R.id.playerResultTypeRB1);
        }
        inputPlayerComment.setText(data.getPlayerComment());
    }

    private FandbPlayer getPlayerData(){
        Intent intent = getIntent();
        Long id = intent.getLongExtra(FanConst.INTENT_PLAYER_ID, 0);

        MyApplication app = (MyApplication)getApplication();
        return app.getDaoSession().getFandbPlayerDao().load(id);

    }
}
