package com.github.br.libgdx.jam36.screens.phase.game;

public enum WeekDay {

    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5)
    ;

    private int day;

    WeekDay(int day) {
        this.day = day;
    }

    public int getDayNumber() {
        return day;
    }

}
