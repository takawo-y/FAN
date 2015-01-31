package com.takawo.fan.db.data;

import android.graphics.Bitmap;

/**
 * Created by Takawo on 2015/01/14.
 */
public class PlayerDemoData {
    private int playerId;
    private Bitmap playerImg;
    private String playerName;
    private String gameEvent;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public Bitmap getPlayerImg() {
        return playerImg;
    }

    public void setPlayerImg(Bitmap playerImg) {
        this.playerImg = playerImg;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getGameEvent() {
        return gameEvent;
    }

    public void setGameEvent(String gameEvent) {
        this.gameEvent = gameEvent;
    }
}
