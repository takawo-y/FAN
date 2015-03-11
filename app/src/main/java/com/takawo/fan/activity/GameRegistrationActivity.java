package com.takawo.fan.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.takawo.fan.db.FandbPlayer;
import com.takawo.fan.util.BitmapTransformation;
import com.takawo.fan.util.FanConst;
import com.takawo.fan.util.FanMaster;
import com.takawo.fan.util.FanUtil;
import com.takawo.fan.util.KeyValuePair;
import com.takawo.fan.MyApplication;
import com.takawo.fan.R;
import com.takawo.fan.adapter.KeyValuePairArrayAdapter;
import com.takawo.fan.db.FandbGame;

import java.io.File;
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

    private int RESULT_PICK_FILENAME = 1;
    private SharedPreferences sharePre;
    private final String SHARE_IMAGE_PATH_KEY = "imagePath";

    public GameRegistrationActivity() {
        super();
    }

    @InjectView(R.id.tool_bar)
    Toolbar toolbar;
    @InjectView(R.id.button_game_registration)
    ImageView button_game_registration;

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
    @InjectView(R.id.inputGameOppositionImage)
    ImageView inputGameOppositionImage;
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

    @OnClick(R.id.inputGameOppositionImage)
    void onClickImage(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_PICK_FILENAME);
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

        sharePre = PreferenceManager.getDefaultSharedPreferences(this);
        sharePre.edit().clear().commit();
    }

    /**
     * Toolbar設定
     */
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
        toolbar.setSubtitle(R.string.game_registration_view_name);
        button_game_registration.setImageResource(FanUtil.getPlayerIconDone(playerData.getPlayerIconColor().intValue()));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(FanUtil.getPlayerIconBack(playerData.getPlayerIconColor().intValue()));
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
        sharePre = PreferenceManager.getDefaultSharedPreferences(this);
        String path = sharePre.getString(SHARE_IMAGE_PATH_KEY, "");
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
                path,
                inputGameResult.getText().toString(),
                inputGameResultScore.getText().toString(),
                inputGameResultTime.getText().toString(),
                inputGameComment.getText().toString()
        );
        MyApplication app = (MyApplication)getApplication();
        app.getDaoSession().getFandbGameDao().insert(game);
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

            String picturePath = "";
            if(selectedImage != null &&
                    "content".equals(selectedImage.getScheme())){
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(
                        selectedImage,
                        filePathColumn,
                        null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
            }else{
                picturePath = selectedImage.getPath();
            }

            Toast.makeText(this, picturePath, Toast.LENGTH_LONG).show();
            Picasso.with(this).load(new File(picturePath)).resize(200, 200).centerInside().into(inputGameOppositionImage);
            sharePre = PreferenceManager.getDefaultSharedPreferences(this);
            sharePre.edit().putString(SHARE_IMAGE_PATH_KEY, picturePath).commit();
        }
    }

}
