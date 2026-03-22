package com.github.br.libgdx.jam36.screens.phase.game;

import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.screens.phase.Phase;

public class GamePhase implements Phase {

    private GameContext gameContext;
    private CustomOrthogonalTiledMapRenderer renderer;

    private Watch watch;

    public GamePhase(Watch watch) {
        this.watch = watch;
    }

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        this.gameContext = gameContext;
        this.renderer = renderer;
    }

    @Override
    public void draw(float deltaTime) {
        watch.update(deltaTime);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
