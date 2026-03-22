package com.github.br.libgdx.jam36.screens.phase;

import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.EventsBlock;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.context.TabletContext;

import java.time.LocalDate;

public class GameOverStatisticsPhase implements Phase {

    //TODO заменить на английский !!!

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        TabletContext tabletContext = gameContext.getTabletContext();
        tabletContext.setPages(
            0,
            "Дата увольнения: " + LocalDate.now() +
                "\nПричина: " + getFaultCause(gameContext)
        );
        tabletContext.setSignButtonText("Начать заново?");
    }

    private String getFaultCause(GameContext gameContext) {
        EventsBlock eventsBlock = gameContext.getEventsBlock();
        if (eventsBlock.isDocumentsSign()) {
            return "по собственному желанию";
        } else if (eventsBlock.isAlcoholDrinker()) {
            return "за нетрезвое состояние на рабочем месте";
        }

        return "Неизвестная причина";
    }

    @Override
    public void draw(float deltaTime) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
