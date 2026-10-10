package com.github.br.libgdx.jam36.screens.phase;

import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.structure.screen.ui.CustomOrthogonalTiledMapRenderer;

public class DelayPhase implements Phase {

    private final float delaySeconds;
    private float accumulator = 0;

    public DelayPhase(float delaySeconds) {
        this.delaySeconds = delaySeconds;
    }

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
    }

    @Override
    public void draw(float deltaTime) {
        accumulator += deltaTime;
    }

    @Override
    public boolean isFinished() {
        return (accumulator >= delaySeconds);
    }

}
