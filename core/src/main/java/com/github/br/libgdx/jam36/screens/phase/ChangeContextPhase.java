package com.github.br.libgdx.jam36.screens.phase;

import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.structure.screen.ui.CustomOrthogonalTiledMapRenderer;

public class ChangeContextPhase implements Phase {

    private final ContextChanger contextChanger;

    public ChangeContextPhase(ContextChanger contextChanger) {
        this.contextChanger = contextChanger;
    }

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        contextChanger.change(gameContext);
    }

    @Override
    public void draw(float deltaTime) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
