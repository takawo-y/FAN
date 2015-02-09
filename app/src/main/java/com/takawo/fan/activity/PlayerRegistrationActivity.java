package com.takawo.fan.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.takawo.fan.MyApplication;
import com.takawo.fan.R;
import com.takawo.fan.db.FandbPlayer;

import java.io.File;

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

    @OnClick(R.id.inputPlayerImage)
    void onClickImage(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_PICK_FILENAME);
    }

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

        setToolbar();  //ToolBar設定
        sharePre = PreferenceManager.getDefaultSharedPreferences(this);
        sharePre.edit().clear().commit();
    }

    private void setToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_done_grey600_36dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     Intent intent = new Intent(PlayerRegistrationActivity.this, MainActivity.class);
                                                     startActivity(intent);
                                                 }
                                             }

        );
    }

    /**
     * Player登録
     */
    private void registPlayer(){
        sharePre = PreferenceManager.getDefaultSharedPreferences(this);
        String path = sharePre.getString(SHARE_IMAGE_PATH_KEY, "");
        FandbPlayer player = new FandbPlayer(
                null,
                inputPlayerName.getText().toString(),
                inputGameEvent.getText().toString(),
                getResultType(),
                inputPlayerCategory.getText().toString(),
                null,
                path,
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
            //inputPlayerImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            Picasso.with(this).load(new File(picturePath)).into(inputPlayerImage);
            sharePre = PreferenceManager.getDefaultSharedPreferences(this);
            sharePre.edit().putString(SHARE_IMAGE_PATH_KEY, picturePath).commit();
        }
    }

}
