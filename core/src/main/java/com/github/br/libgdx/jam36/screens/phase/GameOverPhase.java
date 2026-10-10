package com.github.br.libgdx.jam36.screens.phase;

import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.structure.screen.ui.CustomOrthogonalTiledMapRenderer;

public class GameOverPhase implements Phase {

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        gameContext.setGameOverAndNeedChangePhases(true);
    }

    @Override
    public void draw(float deltaTime) {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
