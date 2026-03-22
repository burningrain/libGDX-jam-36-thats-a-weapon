package com.github.br.libgdx.jam36.screens.phase;

import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;

public class TimerPhase implements Phase {

    private final Phase targetPhase;
    private final ContextChanger changerAfterTime;
    private final float time;

    private GameContext gameContext;

    private float accumulator;

    public TimerPhase(Phase targetPhase, float time, ContextChanger changerAfterTime) {
        this.targetPhase = targetPhase;
        this.time = time;
        this.changerAfterTime = changerAfterTime;
    }

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        this.gameContext = gameContext;
        targetPhase.initUI(gameContext, renderer);
    }

    @Override
    public void draw(float deltaTime) {
        targetPhase.draw(deltaTime);
        accumulator += deltaTime;
        if (accumulator >= time) {
            changerAfterTime.change(gameContext);
        }
    }

    @Override
    public boolean isFinished() {
        return targetPhase.isFinished();
    }

}
