package com.takawo.fan.util;

/**
 * Created by 9004027600 on 2015/03/13.
 */
public class GameSearchKey {
    private boolean isSet = false;
    private String gameYear;
    private String gameYearMonth;
    private String category;
    private int type;

    public boolean isSet() {
        return isSet;
    }

    public void setIsSet(boolean isSet) {
        this.isSet = isSet;
    }

    public String getGameYear() {
        return gameYear;
    }

    public void setGameYear(String gameYear) {
        this.gameYear = gameYear;
    }

    public String getGameYearMonth() {
        return gameYearMonth;
    }

    public void setGameYearMonth(String gameYearMonth) {
        this.gameYearMonth = gameYearMonth;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
