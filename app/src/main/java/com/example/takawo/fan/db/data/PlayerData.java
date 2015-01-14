package com.example.takawo.fan.db.data;

/**
 * Created by Takawo on 2015/01/14.
 */
public class PlayerData {
    private int playerId;
    private String playerName;
    private String gameEvent;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
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
