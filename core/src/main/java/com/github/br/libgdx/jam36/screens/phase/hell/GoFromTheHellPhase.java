package com.github.br.libgdx.jam36.screens.phase.hell;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.screens.StageActors;
import com.github.br.libgdx.jam36.screens.TiledLayers;
import com.github.br.libgdx.jam36.screens.phase.Phase;
import com.github.br.libgdx.jam36.ui.AnimatedImage;

public class GoFromTheHellPhase implements Phase {

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        gameContext.getEventsBlock().setInTheHell(false);

        MapLayer dailyLayer = renderer.getLayer(TiledLayers.DAILY);
        dailyLayer.setVisible(true);

        MapLayer hellDailyLayer = renderer.getLayer(TiledLayers.DAILY_HELL_VERSION);
        hellDailyLayer.setVisible(false);

        MapLayer environmentLayer = renderer.getLayer(TiledLayers.ENVIRONMENT);
        environmentLayer.setVisible(true);

        MapLayer environmentHellLayer = renderer.getLayer(TiledLayers.HELL_ENVIRONMENT);
        environmentHellLayer.setVisible(false);

        MapLayer blueDoorLayer = ((MapGroupLayer) environmentHellLayer).getLayers().get(
            TiledLayers.HELL_ENVIRONMENT__DOOR_BLUE_LAYER
        );
        blueDoorLayer.setVisible(false);

        MapLayer redDoorLayer = ((MapGroupLayer) environmentHellLayer).getLayers().get(
            TiledLayers.HELL_ENVIRONMENT__DOOR_RED_LAYER
        );
        redDoorLayer.setVisible(false);

        // turn on fires
        AnimatedImage fire1 = renderer.getActor(TiledLayers.HELL_TABLE_FIRES_LAYER, StageActors.FIRE_1, AnimatedImage.class);
        fire1.pause();

        AnimatedImage fire2 = renderer.getActor(TiledLayers.HELL_TABLE_FIRES_LAYER, StageActors.FIRE_2, AnimatedImage.class);
        fire2.pause();

        AnimatedImage fire3 = renderer.getActor(TiledLayers.HELL_TABLE_FIRES_LAYER, StageActors.FIRE_3, AnimatedImage.class);
        fire3.pause();

        AnimatedImage fire4 = renderer.getActor(TiledLayers.HELL_TABLE_FIRES_LAYER, StageActors.FIRE_4, AnimatedImage.class);
        fire4.pause();

        AnimatedImage fire5 = renderer.getActor(TiledLayers.HELL_TABLE_FIRES_LAYER, StageActors.FIRE_5, AnimatedImage.class);
        fire5.pause();
        //

        // clean up daily table
        AnimatedImage hrDictophone = renderer.getActor(TiledLayers.ACTORS_LAYER_DICTOPHONES, StageActors.HR_DICTOPHONE, AnimatedImage.class);
        hrDictophone.setVisible(true);

        AnimatedImage heroDictophone = renderer.getActor(TiledLayers.ACTORS_LAYER_DICTOPHONES, StageActors.HERO_DICTOPHONE, AnimatedImage.class);
        heroDictophone.setVisible(true);

        AnimatedImage hrStress = renderer.getActor(TiledLayers.ACTORS_LAYER_STRESS_LEVELS, StageActors.HR_STRESS_LEVEL, AnimatedImage.class);
        hrStress.setVisible(true);

        AnimatedImage heroStress = renderer.getActor(TiledLayers.ACTORS_LAYER_STRESS_LEVELS, StageActors.HERO_STRESS_LEVEL, AnimatedImage.class);
        heroStress.setVisible(true);
    }

    @Override
    public void draw(float deltaTime) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
