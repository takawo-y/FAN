package com.takawo.fan.activity;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.takawo.fan.MyApplication;
import com.takawo.fan.R;
import com.takawo.fan.adapter.KeyValuePairArrayAdapter;
import com.takawo.fan.db.FandbPlayer;
import com.takawo.fan.util.FanConst;
import com.takawo.fan.util.FanMaster;
import com.takawo.fan.util.FanUtil;
import com.takawo.fan.util.KeyValuePair;

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

    int mColor = 0xffffffff;
    int mTextColor = 0xffc0c0c0;

    LayoutInflater inflater;
    View dialogView;

    @InjectView(R.id.tool_bar)
    Toolbar toolbar;

    @InjectView(R.id.inputPlayerImage)
    ImageView inputPlayerImage;
    @InjectView(R.id.inputPlayerName)
    EditText inputPlayerName;
    @InjectView(R.id.inputPlayerColor)
    TextView inputPlayerColor;
    @InjectView(R.id.inputPlayerFontColor)
    TextView inputPlayerFontColor;
    @InjectView(R.id.inputPlayerIcon)
    Spinner inputPlayerIcon;
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

    @OnClick(R.id.inputPlayerColor)
    void onClickColor(){
      colorDialog(inputPlayerColor);
    }

    @OnClick(R.id.inputPlayerFontColor)
    void onClickFontColor(){
        colorDialog(inputPlayerFontColor);
    }

    @OnClick(R.id.button_player_registration)
    void onClickRegist(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Player登録")
        .setMessage("登録しますか？")
        .setPositiveButton("はい",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        registPlayer();
                        Intent intent = new Intent(PlayerRegistrationActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_player_regist);
        ButterKnife.inject(this);

        setToolbar();  //ToolBar設定
        sharePre = PreferenceManager.getDefaultSharedPreferences(this);
        sharePre.edit().clear().commit();
    }

    private void setToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_done_grey600_36dp);
        toolbar.setTitle(R.string.player_registration_view_name);
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

    private void setSpinnerGameType(){
        inputPlayerIcon.setOnItemSelectedListener(onItemSelectedListener);
        KeyValuePairArrayAdapter adapter = new KeyValuePairArrayAdapter(this, android.R.layout.simple_spinner_item, FanMaster.getPlayerIcon());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputPlayerIcon.setAdapter(adapter);
        inputPlayerIcon.setSelection(adapter.getPosition(0));

    }
    /**
     * @brief スピナーのOnItemSelectedListener
     */
    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            KeyValuePair item = (KeyValuePair)inputPlayerIcon.getSelectedItem();
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

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
                FanUtil.getResultType(inputPlayerResultType.getCheckedRadioButtonId()),
                inputPlayerCategory.getText().toString(),
                inputPlayerColor.getText().toString(),
                inputPlayerFontColor.getText().toString(),
                new Long(((KeyValuePair)inputPlayerIcon.getSelectedItem()).getKey()),
                path,
                inputPlayerComment.getText().toString());
        MyApplication app = (MyApplication)getApplication();
        app.getDaoSession().getFandbPlayerDao().insert(player);
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
            //Picasso.with(this).load(new File(picturePath)).transform(new BitmapTransformation()).into(inputPlayerImage);
            Picasso.with(this).load(new File(picturePath)).resize(200, 200).centerInside().into(inputPlayerImage);
            sharePre = PreferenceManager.getDefaultSharedPreferences(this);
            sharePre.edit().putString(SHARE_IMAGE_PATH_KEY, picturePath).commit();
        }
    }

    private void colorDialog(View targetView) {
        final String rString = getResources().getString(R.string.color_picker_red);
        final String gString = getResources().getString(R.string.color_picker_green);
        final String bString = getResources().getString(R.string.color_picker_blue);

        inflater = LayoutInflater.from(this);
        dialogView = inflater.inflate(R.layout.dialog_picker, null);

        final TextView textR = ButterKnife.findById(dialogView, R.id.colorPickerTextR);
        textR.setText(String.format("%s(%02X)", rString, (mColor & 0x00ff0000) >> 16));
        final TextView textG = ButterKnife.findById(dialogView, R.id.colorPickerTextG);
        textG.setText(String.format("%s(%02X)", gString, (mColor & 0x0000ff00) >> 8));
        final TextView textB = ButterKnife.findById(dialogView, R.id.colorPickerTextB);
        textB.setText(String.format("%s(%02X)", bString, (mColor & 0x000000ff)));
        final TextView viewer = ButterKnife.findById(dialogView, R.id.colorPickerViewer);
        viewer.setBackgroundColor(mColor);
        viewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });

        final SeekBar seekBarR = (SeekBar)dialogView.findViewById(R.id.colorPickerSeekBarR);
        seekBarR.setProgress((mColor & 0x00ff0000) >>16);
        seekBarR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = seekBar.getProgress();
                textR.setText(String.format("%s(%02X)", rString, value));
                mColor = (mColor & 0xff00ffff) | value << 16;
                viewer.setBackgroundColor(mColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                int value = seekBar.getProgress();
                textR.setText(String.format("%s(%02X)", rString, value));
                mColor = (mColor & 0xff00ffff) | value << 16;
                viewer.setBackgroundColor(mColor);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                int value = seekBar.getProgress();
                textR.setText(String.format("%s(%02X)", rString, value));
                mColor = (mColor & 0xff00ffff) | value << 16;
                viewer.setBackgroundColor(mColor);
            }
        });
        final SeekBar seekBarG = (SeekBar)dialogView.findViewById(R.id.colorPickerSeekBarG);
        seekBarG.setProgress((mColor & 0x0000ff00) >> 8);
        seekBarG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = seekBar.getProgress();
                textG.setText(String.format("%s(%02X)", gString, value));
                mColor = (mColor & 0xffff00ff) | value << 8;
                viewer.setBackgroundColor(mColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                int value = seekBar.getProgress();
                textG.setText(String.format("%s(%02X)", gString, value));
                mColor = (mColor & 0xffff00ff) | value << 8;
                viewer.setBackgroundColor(mColor);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                int value = seekBar.getProgress();
                textG.setText(String.format("%s(%02X)", gString, value));
                mColor = (mColor & 0xffff00ff) | value << 8;
                viewer.setBackgroundColor(mColor);
            }
        });
        final SeekBar seekBarB = (SeekBar)dialogView.findViewById(R.id.colorPickerSeekBarB);
        seekBarB.setProgress((mColor & 0x000000ff));
        seekBarB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = seekBar.getProgress();
                textB.setText(String.format("%s(%02X)", bString, value));
                mColor = (mColor & 0xffffff00) | value;
                viewer.setBackgroundColor(mColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                int value = seekBar.getProgress();
                textB.setText(String.format("%s(%02X)", bString, value));
                mColor = (mColor & 0xffffff00) | value;
                viewer.setBackgroundColor(mColor);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                int value = seekBar.getProgress();
                textB.setText(String.format("%s(%02X)", bString, value));
                mColor = (mColor & 0xffffff00) | value;
                viewer.setBackgroundColor(mColor);
            }
        });
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(dialogView);
        final View targetV = targetView;
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int idx) {
                mTextColor = mColor;
                final View textView0 = targetV;
                textView0.setBackgroundColor(mTextColor);
                ((TextView)textView0).setText(Integer.toString(mColor));
            }
        });
        alert.show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == 0) {
            final String rString = getResources().getString(R.string.color_picker_red);
            final String gString = getResources().getString(R.string.color_picker_green);
            final String bString = getResources().getString(R.string.color_picker_blue);

            LayoutInflater inflater1 = LayoutInflater.from(this);
            final View dialogView1 = inflater1.inflate(R.layout.dialog_selecter, null);

            final GridView gridView = (GridView)dialogView1.findViewById(R.id.colorSelectGridView);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,   android.R.layout.simple_list_item_1) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    view.setBackgroundColor(FanConst.COLOR_TABLE[position]);
                    return view;
                }
            };
            for(int i = 0; i<FanConst.COLOR_TABLE.length; i++) {
                adapter.add("");
            }
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    mColor = FanConst.COLOR_TABLE[arg2];
                    final TextView textR = (TextView)dialogView.findViewById(R.id.colorPickerTextR);
                    textR.setText(String.format("%s(%02X)", rString, (mColor & 0x00ff0000) >> 16));
                    final TextView textG = (TextView)dialogView.findViewById(R.id.colorPickerTextG);
                    textG.setText(String.format("%s(%02X)", gString, (mColor & 0x0000ff00) >> 8));
                    final TextView textB = (TextView)dialogView.findViewById(R.id.colorPickerTextB);
                    textB.setText(String.format("%s(%02X)", bString, (mColor & 0x000000ff)));
                    final TextView viewer = (TextView)dialogView.findViewById(R.id.colorPickerViewer);
                    viewer.setBackgroundColor(mColor);
                    final SeekBar seekBarR = (SeekBar)dialogView.findViewById(R.id.colorPickerSeekBarR);
                    seekBarR.setProgress((mColor & 0x00ff0000) >>16);
                    final SeekBar seekBarG = (SeekBar)dialogView.findViewById(R.id.colorPickerSeekBarG);
                    seekBarG.setProgress((mColor & 0x0000ff00) >> 8);
                    final SeekBar seekBarB = (SeekBar)dialogView.findViewById(R.id.colorPickerSeekBarB);
                    seekBarB.setProgress((mColor & 0x000000ff));
                    dismissDialog(0);
                }
            });

            return new AlertDialog.Builder(this)
                    .setView(dialogView1)
                    .setNegativeButton("Cancel", null)
                    .create();
        }
        return super.onCreateDialog(id);
    }
}
