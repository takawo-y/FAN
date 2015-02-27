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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.takawo.fan.MyApplication;
import com.takawo.fan.R;
import com.takawo.fan.db.FandbImage;
import com.takawo.fan.db.FandbPlayer;
import com.takawo.fan.util.BitmapTransformation;
import com.takawo.fan.util.FanConst;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Takawo on 2015/01/20.
 */
public class GameImageRegistrationActivity extends ActionBarActivity {
    public GameImageRegistrationActivity() {
        super();
    }

    private int RESULT_PICK_FILENAME = 1;
    private SharedPreferences sharePre;
    private final String SHARE_IMAGE_PATH_KEY = "imagePath";
    private Long playerId;
    private Long gameId;
    private FandbPlayer playerData;

    @InjectView(R.id.tool_bar)
    Toolbar toolbar;

    @InjectView(R.id.inputGameImage)
    ImageView inputGameImage;
    @InjectView(R.id.inputGameImageTitle)
    EditText inputGameImageTitle;
    @InjectView(R.id.inputGameImageComment)
    EditText inputGameImageComment;

    @OnClick(R.id.inputGameImage)
    void onClickImage(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_PICK_FILENAME);
    }

    @OnClick(R.id.button_game_image_registration)
    void onClickRegist(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("画像登録")
        .setMessage("登録しますか？")
        .setPositiveButton("はい",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        registGameImage();
                        Intent intent = new Intent(GameImageRegistrationActivity.this, GameUpdateActivity.class);
                        intent.putExtra(FanConst.INTENT_PLAYER_ID, playerId);
                        intent.putExtra(FanConst.INTENT_GAME_ID, gameId);
                        intent.putExtra(FanConst.INTENT_FRAGMENT_POSITION, 1);  //フラグメント指定用
                        startActivity(intent);
                    }
                }
        )
        .setNegativeButton("いいえ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }
        )
        .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_image_regist);
        ButterKnife.inject(this);

        playerId = getIntent().getLongExtra(FanConst.INTENT_PLAYER_ID, 0);
        gameId = getIntent().getLongExtra(FanConst.INTENT_GAME_ID, 0);
        playerData = ((MyApplication)getApplication()).getDaoSession().getFandbPlayerDao().load(playerId);

        setToolbar();  //ToolBar設定
        sharePre = PreferenceManager.getDefaultSharedPreferences(this);
        sharePre.edit().clear().commit();
    }

    private void setToolbar(){
        toolbar.setLogo(R.drawable.no_image);
        if(playerData.getPlayerImagePath() != null && playerData.getPlayerImagePath().isEmpty() == false){
            for (int i = 0; i < toolbar.getChildCount(); i++) {
                View child = toolbar.getChildAt(i);
                if (child != null)
                    if (child.getClass() == ImageView.class) {
                        ImageView logoView = (ImageView) child;
                        if(logoView.getId() == -1){
                            Picasso.with(this)
                                    .load(new File(playerData.getPlayerImagePath()))
                                    .transform(new BitmapTransformation())
                                    .resize(100, 100)
                                    .centerInside()
                                    .into(logoView);
                        }
                    }
            }
        }
        toolbar.setBackgroundColor(new Integer(playerData.getPlayerColor()));
        toolbar.setTitleTextColor(new Integer(playerData.getPlayerFontColor()));
        toolbar.setSubtitleTextColor(new Integer(playerData.getPlayerFontColor()));
        toolbar.setTitle(playerData.getPlayerName());
        toolbar.setSubtitle(R.string.game_image_regist_view_name);
        toolbar.setNavigationIcon(R.drawable.ic_done_grey600_36dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     Intent intent = new Intent(GameImageRegistrationActivity.this, GameUpdateActivity.class);
                                                     intent.putExtra(FanConst.INTENT_PLAYER_ID, playerId);
                                                     intent.putExtra(FanConst.INTENT_GAME_ID, gameId);
                                                     intent.putExtra(FanConst.INTENT_FRAGMENT_POSITION, 1);
                                                     startActivity(intent);
                                                 }
                                             }

        );
    }

    /**
     * 画像登録
     */
    private void registGameImage(){
        sharePre = PreferenceManager.getDefaultSharedPreferences(this);
        String path = sharePre.getString(SHARE_IMAGE_PATH_KEY, "");
        FandbImage image = new FandbImage(
                playerId,
                gameId,
                null,
                path,
                inputGameImageTitle.getText().toString(),
                inputGameImageComment.getText().toString());
        ((MyApplication)getApplication()).getDaoSession().getFandbImageDao().insert(image);
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
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(
                    selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Toast.makeText(this, picturePath, Toast.LENGTH_LONG).show();
            Picasso.with(this).load(new File(picturePath)).resize(300, 300).centerInside().into(inputGameImage);
            sharePre = PreferenceManager.getDefaultSharedPreferences(this);
            sharePre.edit().putString(SHARE_IMAGE_PATH_KEY, picturePath).commit();
        }
    }

}
