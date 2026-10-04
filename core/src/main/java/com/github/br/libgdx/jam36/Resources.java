package com.github.br.libgdx.jam36;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public interface Resources {

    String SKIN_ATLAS = "skin/export/export.atlas";
    String SKIN = "skin/export/export.json";

    public interface Tmx {
        String MENU = "menu.tmx";
    }

    static String getTmx(String tmx) {
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            return "tiled-packed/" + tmx;
        }
        return "tiled-packed-ktx/" + tmx;
    }

    String ANIMATION_ATLAS = "animation/jam36_animation.atlas";

    interface Animation {

        String HERO_DICTOPHONE = "hero_dictophone";
        String HR_DICTOPHONE = "hr_dictophone";

        String FIRE = "fire";
        String HR_STRESS_LEVEL = "hr_stress";
        String HERO_STRESS_LEVEL = "my_stress";

        String CALENDAR_DAY = "watch_work_day";

        String WATCH_ARROW = "watch_arrow";

        String THOUGHT = "thought";

    }

}
