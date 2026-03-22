package com.github.br.libgdx.jam36.screens.phase.stress;

import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.screens.StageActors;
import com.github.br.libgdx.jam36.screens.TiledLayers;
import com.github.br.libgdx.jam36.screens.phase.Phase;
import com.github.br.libgdx.jam36.ui.AnimatedImage;

public class HideStressLevelsPhase implements Phase {

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        AnimatedImage hrStress = renderer.getActor(TiledLayers.ACTORS_LAYER_STRESS_LEVELS, StageActors.HR_STRESS_LEVEL, AnimatedImage.class);
        hrStress.setVisible(false);

        AnimatedImage heroStress = renderer.getActor(TiledLayers.ACTORS_LAYER_STRESS_LEVELS, StageActors.HERO_STRESS_LEVEL, AnimatedImage.class);
        heroStress.setVisible(false);
    }

    @Override
    public void draw(float deltaTime) {

    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
