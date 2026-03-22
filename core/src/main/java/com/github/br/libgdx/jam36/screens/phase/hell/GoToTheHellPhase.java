package com.github.br.libgdx.jam36.screens.phase.hell;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.screens.StageActors;
import com.github.br.libgdx.jam36.screens.TiledLayers;
import com.github.br.libgdx.jam36.screens.phase.Phase;
import com.github.br.libgdx.jam36.ui.AnimatedImage;

public class GoToTheHellPhase implements Phase {

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        gameContext.getEventsBlock().setInTheHell(true);

        MapLayer dailyLayer = renderer.getLayer(TiledLayers.DAILY);
        dailyLayer.setVisible(false);

        MapLayer hellDailyLayer = renderer.getLayer(TiledLayers.DAILY_HELL_VERSION);
        hellDailyLayer.setVisible(true);

        MapLayer environmentLayer = renderer.getLayer(TiledLayers.ENVIRONMENT);
        environmentLayer.setVisible(false);

        MapLayer environmentHellLayer = renderer.getLayer(TiledLayers.HELL_ENVIRONMENT);
        environmentHellLayer.setVisible(true);

        MapLayer blueDoorLayer = ((MapGroupLayer) environmentHellLayer).getLayers().get(
            TiledLayers.HELL_ENVIRONMENT__DOOR_BLUE_LAYER
        );
        blueDoorLayer.setVisible(false);

        MapLayer redDoorLayer = ((MapGroupLayer) environmentHellLayer).getLayers().get(
            TiledLayers.HELL_ENVIRONMENT__DOOR_RED_LAYER
        );
        redDoorLayer.setVisible(true);

        // turn on fires
        AnimatedImage fire1 = renderer.getActor(TiledLayers.HELL_TABLE_FIRES_LAYER, StageActors.FIRE_1, AnimatedImage.class);
        fire1.play();

        AnimatedImage fire2 = renderer.getActor(TiledLayers.HELL_TABLE_FIRES_LAYER, StageActors.FIRE_2, AnimatedImage.class);
        fire2.play();

        AnimatedImage fire3 = renderer.getActor(TiledLayers.HELL_TABLE_FIRES_LAYER, StageActors.FIRE_3, AnimatedImage.class);
        fire3.play();

        AnimatedImage fire4 = renderer.getActor(TiledLayers.HELL_TABLE_FIRES_LAYER, StageActors.FIRE_4, AnimatedImage.class);
        fire4.play();

        AnimatedImage fire5 = renderer.getActor(TiledLayers.HELL_TABLE_FIRES_LAYER, StageActors.FIRE_5, AnimatedImage.class);
        fire5.play();
    }

    @Override
    public void draw(float deltaTime) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
