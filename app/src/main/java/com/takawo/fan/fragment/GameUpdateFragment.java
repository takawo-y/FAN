package com.takawo.fan.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.takawo.fan.MyApplication;
import com.takawo.fan.R;
import com.takawo.fan.adapter.KeyValuePairArrayAdapter;
import com.takawo.fan.db.FandbGame;
import com.takawo.fan.db.FandbGameDao;
import com.takawo.fan.db.FandbPlayer;
import com.takawo.fan.util.FanConst;
import com.takawo.fan.util.FanMaster;
import com.takawo.fan.util.KeyValuePair;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Takawo on 2015/02/15.
 */
public class GameUpdateFragment extends Fragment {

    private Long playerId;
    private Long gameId;
    private FandbPlayer playerData;

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
                getActivity(),
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
                getActivity(),
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
                getActivity(),
                new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        inputGameEndTime.setText(String.format("%02d", hourOfDay)+":"+String.format("%02d", minute));
                    }
                },
                nowHour, nowMinute, true);
        timePickerDialog.show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_update, container, false);
        playerId = getArguments().getLong(FanConst.INTENT_PLAYER_ID);
        gameId = getArguments().getLong(FanConst.INTENT_GAME_ID);
        playerData = ((MyApplication)getActivity().getApplication()).getDaoSession().getFandbPlayerDao().load(playerId);

        ButterKnife.inject(this, view);

        setData();
        if(playerData.getResultType() == 0){
            //スコア
            tableResultTime.setVisibility(View.GONE);
        }else if(playerData.getResultType() == 1){
            //タイム
            tableResultScore.setVisibility(View.GONE);
        }
        return view;
    }

    private void setData(){
        FandbGame data = ((MyApplication)getActivity().getApplication()).getDaoSession().getFandbGameDao().queryBuilder()
                .where(FandbGameDao.Properties.PlayerId.eq(playerId), FandbGameDao.Properties.Id.eq(gameId)).unique();

        setSpinnerGameType(data.getGameType());  //Gameタイプ
        inputGameCategory.setText(data.getGameCategory());  //Gameカテゴリ
        inputGameInfo.setText(data.getGameInfo());  //Gameインフォメーション
        inputGameDate.setText(new SimpleDateFormat("yyyy/MM/dd").format(data.getGameDay()));  //試合日
        inputGameStartTime.setText(data.getStartTime());  //開始時間
        inputGameEndTime.setText(data.getEndTime());  //終了時間
        inputGameOpposition.setText(data.getOpposition());  //対戦相手
        inputGameResult.setText(data.getResult());  //試合結果
        if(data.getResultScore() != null && data.getResultScore().isEmpty() == false){
            inputGameResultScore.setText(data.getResultScore());
        }
        if(data.getResultTime() != null && data.getResultTime().isEmpty() == false){
            inputGameResultTime.setText(data.getResultTime());
        }
        inputGamePlace.setText(data.getPlace());  //会場
        inputGameWeather.setText(data.getWeather());  //天気
        inputGameTemperature.setText(data.getTemperature());  //気温
        inputGameComment.setText(data.getComment());  //備考

    }

    /**
     * GameTypeセット
     *
     * @param type
     */
    private void setSpinnerGameType(Long type){
        inputGameType.setOnItemSelectedListener(onItemSelectedListener);
        KeyValuePairArrayAdapter adapter = new KeyValuePairArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, FanMaster.getGameType());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputGameType.setAdapter(adapter);
        inputGameType.setSelection(adapter.getPosition(type.intValue()));
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
     * Game更新
     */
    public void updateGame(){
        FandbGame game = new FandbGame(
                playerId,
                gameId,
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
                inputGameResult.getText().toString(),
                inputGameResultScore.getText().toString(),
                inputGameResultTime.getText().toString(),
                inputGameComment.getText().toString()
        );
        ((MyApplication)getActivity().getApplication()).getDaoSession().getFandbGameDao().update(game);
    }

}
