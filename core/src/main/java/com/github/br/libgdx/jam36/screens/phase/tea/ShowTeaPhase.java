package com.github.br.libgdx.jam36.screens.phase.tea;

import com.badlogic.gdx.maps.MapLayer;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.screens.TiledLayers;
import com.github.br.libgdx.jam36.screens.phase.Phase;

public class ShowTeaPhase implements Phase {

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        // показать столик с чаем
        MapLayer layer = renderer.getLayer(TiledLayers.TEA);
        layer.setVisible(true);
    }

    @Override
    public void draw(float deltaTime) {

    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
