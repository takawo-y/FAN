package com.takawo.fan.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TimePicker;

import com.takawo.fan.MyApplication;
import com.takawo.fan.R;
import com.takawo.fan.activity.GameActivity;
import com.takawo.fan.adaptor.KeyValuePairArrayAdapter;
import com.takawo.fan.db.FandbGame;
import com.takawo.fan.db.FandbGameDao;
import com.takawo.fan.db.FandbPlayer;
import com.takawo.fan.util.FanConst;
import com.takawo.fan.util.FanMaster;
import com.takawo.fan.util.FanUtil;
import com.takawo.fan.util.KeyValuePair;

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
    DatePicker inputGameDate;
    @InjectView(R.id.inputGameStartTime)
    TimePicker inputGameStartTime;
    @InjectView(R.id.inputGameEndTime)
    TimePicker inputGameEndTime;
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

    @OnClick(R.id.button_game_update)
    void onClickRegist(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("試合情報更新")
                .setMessage("更新しますか？")
                .setPositiveButton("はい",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                updateGame();
//                                Intent intent = new Intent(GameUpdateActivity.this, GameActivity.class);
//                                intent.putExtra(FanConst.INTENT_PLAYER_ID, playerId);
//                                startActivity(intent);
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        playerId = getArguments().getLong(FanConst.INTENT_PLAYER_ID);
        gameId = getArguments().getLong(FanConst.INTENT_GAME_ID);
        playerData = ((MyApplication)getActivity().getApplication()).getDaoSession().getFandbPlayerDao().load(playerId);

        setData();

        setTimePicker();
        if(playerData.getResultType() == 0){
            //スコア
            tableResultTime.setVisibility(View.GONE);
        }else if(playerData.getResultType() == 1){
            //タイム
            tableResultScore.setVisibility(View.GONE);
        }
        return inflater.inflate(R.layout.fragment_game_update, null);
    }

    private FandbGame getGameData(){
        return ((MyApplication)getActivity().getApplication()).getDaoSession().getFandbGameDao().queryBuilder()
                .where(FandbGameDao.Properties.PlayerId.eq(playerId), FandbGameDao.Properties.Id.eq(gameId)).unique();
    }

    private void setData(){
        FandbGame gameData = getGameData();
        setSpinnerGameType(gameData.getGameType());  //Gameタイプ
        inputGameCategory.setText(gameData.getGameCategory());  //Gameカテゴリ
        inputGameInfo.setText(gameData.getGameInfo());  //Gameインフォメーション
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

    private void setTimePicker(){
        inputGameStartTime.setIs24HourView(true);
        inputGameEndTime.setIs24HourView(true);
    }

    /**
     * Game登録
     */
    private void updateGame(){
        FandbGame game = new FandbGame(
                playerId,
                gameId,
                new Long(((KeyValuePair)inputGameType.getSelectedItem()).getKey()),
                inputGameCategory.getText().toString(),
                inputGameInfo.getText().toString(),
                inputGamePlace.getText().toString(),
                inputGameWeather.getText().toString(),
                inputGameTemperature.getText().toString(),
                FanUtil.getDate(inputGameDate),
                FanUtil.getTimeString(inputGameStartTime),
                FanUtil.getTimeString(inputGameEndTime),
                inputGameOpposition.getText().toString(),
                inputGameResult.getText().toString(),
                inputGameResultScore.getText().toString(),
                inputGameResultTime.getText().toString(),
                inputGameComment.getText().toString()
        );
        ((MyApplication)getActivity().getApplication()).getDaoSession().getFandbGameDao().update(game);
    }


}
