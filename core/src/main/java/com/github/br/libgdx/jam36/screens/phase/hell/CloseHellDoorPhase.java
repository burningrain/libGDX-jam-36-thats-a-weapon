package com.github.br.libgdx.jam36.screens.phase.hell;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.screens.TiledLayers;
import com.github.br.libgdx.jam36.screens.phase.Phase;

public class CloseHellDoorPhase implements Phase {

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        MapLayer layer = renderer.getLayer(TiledLayers.HELL_ENVIRONMENT);
        MapGroupLayer environmentHellLayer = (MapGroupLayer) layer;

        MapLayer blueDoorLayer = environmentHellLayer.getLayers().get(
            TiledLayers.HELL_ENVIRONMENT__DOOR_BLUE_LAYER
        );
        blueDoorLayer.setVisible(false);

        MapLayer redDoorLayer = environmentHellLayer.getLayers().get(
            TiledLayers.HELL_ENVIRONMENT__DOOR_RED_LAYER
        );
        redDoorLayer.setVisible(true);
    }

    @Override
    public void draw(float deltaTime) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
