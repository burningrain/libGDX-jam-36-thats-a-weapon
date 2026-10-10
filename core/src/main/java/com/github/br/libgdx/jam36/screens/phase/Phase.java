package com.github.br.libgdx.jam36.screens.phase;

import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.structure.screen.ui.CustomOrthogonalTiledMapRenderer;

public interface Phase {

    // видимые слои и анимации
    void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer);

    void draw(float deltaTime);

    boolean isFinished();

}
