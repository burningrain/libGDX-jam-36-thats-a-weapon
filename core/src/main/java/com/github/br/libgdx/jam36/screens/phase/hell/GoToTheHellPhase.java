package com.github.br.libgdx.jam36.screens.phase.hell;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.github.br.libgdx.common.structure.screen.ui.AnimatedImage;
import com.github.br.libgdx.common.structure.screen.ui.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.screens.CustomVfxEffectPooledFactoryImpl;
import com.github.br.libgdx.jam36.screens.StageActors;
import com.github.br.libgdx.jam36.screens.TiledLayers;
import com.github.br.libgdx.jam36.screens.phase.Phase;

public class GoToTheHellPhase implements Phase {

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        gameContext.getEventsBlock().setInTheHell(true);

        MapLayer dailyLayer = renderer.getTiledLayer(TiledLayers.MAIN_VFX, TiledLayers.DAILY);
        dailyLayer.setVisible(false);

        MapLayer hellDailyLayer = renderer.getTiledLayer(TiledLayers.MAIN_VFX, TiledLayers.DAILY_HELL_VERSION);
        hellDailyLayer.setVisible(true);

        MapLayer environmentLayer = renderer.getTiledLayer(TiledLayers.MAIN_VFX, TiledLayers.ENVIRONMENT);
        environmentLayer.setVisible(false);

        MapLayer environmentHellLayer = renderer.getTiledLayer(TiledLayers.MAIN_VFX, TiledLayers.HELL_ENVIRONMENT);
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

        MapLayer tiledLayer = renderer.getTiledLayer(TiledLayers.MAIN_VFX);
        MapProperties properties = tiledLayer.getProperties();

        properties.put(CustomOrthogonalTiledMapRenderer.IS_VFX_EFFECT_ENABLED, Boolean.TRUE);
        properties.put(CustomOrthogonalTiledMapRenderer.VFX_LAYER_EFFECT, CustomVfxEffectPooledFactoryImpl.Title.HELL_VFX);
    }

    @Override
    public void draw(float deltaTime) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
