package com.takawo.fan.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.takawo.fan.MyApplication;
import com.takawo.fan.R;
import com.takawo.fan.db.FandbPlayer;
import com.takawo.fan.util.FanConst;
import com.takawo.fan.util.FanUtil;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Takawo on 2015/02/11.
 */
public class PlayerUpdateActivity extends ActionBarActivity {

    public PlayerUpdateActivity() {super();}

    private int RESULT_PICK_FILENAME = 1;
    private SharedPreferences sharePre;
    private final String SHARE_IMAGE_PATH_KEY = "imagePath";

    private Long playerId;
    private Long gameId;

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

    @OnClick(R.id.inputPlayerImage)
    void onClickImage(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_PICK_FILENAME);
    }

    @OnClick(R.id.button_player_update)
    void onClickRegist(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Player更新")
                .setMessage("更新しますか？")
                .setPositiveButton("はい",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                updatePlayer();
                                Intent intent = new Intent(PlayerUpdateActivity.this, PlayerUpdateActivity.class);
                                intent.putExtra(FanConst.INTENT_PLAYER_ID, playerId);
                                intent.putExtra(FanConst.INTENT_GAME_ID, gameId);
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
        setContentView(R.layout.activity_player_update);
        ButterKnife.inject(this);

        playerId = getIntent().getLongExtra(FanConst.INTENT_PLAYER_ID, 0);
        gameId = getIntent().getLongExtra(FanConst.INTENT_GAME_ID, 0);
        setData();  //初期表示
        setToolbar();  //ToolBar設定
    }

    private void setToolbar(){
        toolbar.setTitle("Player 更新");
        toolbar.setNavigationIcon(R.drawable.ic_done_grey600_36dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     Intent intent = new Intent(PlayerUpdateActivity.this, GameActivity.class);
                                                     intent.putExtra(FanConst.INTENT_PLAYER_ID, playerId);
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
                break;
            case 1:
                inputPlayerResultType.check(R.id.playerResultTypeRB1);
        }
        inputPlayerComment.setText(data.getPlayerComment());
    }

    /**
     * Player検索
     *
     * @return
     */
    private FandbPlayer getPlayerData(){
        MyApplication app = (MyApplication)getApplication();
        return app.getDaoSession().getFandbPlayerDao().load(playerId);

    }

    /**
     * 画像選択後
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_PICK_FILENAME
                && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(
                    selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Toast.makeText(this, picturePath, Toast.LENGTH_LONG).show();
            //inputPlayerImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            Picasso.with(this).load(new File(picturePath)).into(inputPlayerImage);
            sharePre = PreferenceManager.getDefaultSharedPreferences(this);
            sharePre.edit().putString(SHARE_IMAGE_PATH_KEY, picturePath).commit();
        }
    }

    /**
     * Player登録
     */
    private void updatePlayer(){
        sharePre = PreferenceManager.getDefaultSharedPreferences(this);
        String path = sharePre.getString(SHARE_IMAGE_PATH_KEY, "");
        FandbPlayer player = new FandbPlayer(
                playerId,
                inputPlayerName.getText().toString(),
                inputGameEvent.getText().toString(),
                FanUtil.getResultType(inputPlayerResultType.getCheckedRadioButtonId()),
                inputPlayerCategory.getText().toString(),
                null,
                path,
                inputPlayerComment.getText().toString());
        MyApplication app = (MyApplication)getApplication();
        app.getDaoSession().getFandbPlayerDao().update(player);
    }

}
