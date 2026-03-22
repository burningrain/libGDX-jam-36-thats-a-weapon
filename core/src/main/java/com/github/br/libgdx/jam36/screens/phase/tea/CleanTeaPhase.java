package com.github.br.libgdx.jam36.screens.phase.tea;

import com.badlogic.gdx.maps.MapLayer;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.screens.TiledLayers;
import com.github.br.libgdx.jam36.screens.phase.Phase;

public class CleanTeaPhase implements Phase {

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        MapLayer layer = renderer.getLayer(TiledLayers.TEA);
        layer.setVisible(false);
    }

    @Override
    public void draw(float deltaTime) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
