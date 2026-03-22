package com.github.br.libgdx.jam36.screens.phase;

import com.badlogic.gdx.maps.MapLayer;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.screens.StageActors;
import com.github.br.libgdx.jam36.screens.TiledLayers;
import com.github.br.libgdx.jam36.ui.AnimatedImage;

public class SetStartGamePhase implements Phase {

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        MapLayer thoughtsLayer = renderer.getLayer(TiledLayers.THOUGHTS);
        thoughtsLayer.setVisible(false);

        MapLayer heroBigPhoneLayer = renderer.getLayer(TiledLayers.HERO_BIG_PHONE);
        heroBigPhoneLayer.setVisible(false);

        MapLayer tabletLayer = renderer.getLayer(TiledLayers.TABLET);
        tabletLayer.setVisible(false);

        MapLayer teaLayer = renderer.getLayer(TiledLayers.TEA);
        teaLayer.setVisible(false);

        // clean up daily table
        AnimatedImage hrDictophone = renderer.getActor(TiledLayers.ACTORS_LAYER_DICTOPHONES, StageActors.HR_DICTOPHONE, AnimatedImage.class);
        hrDictophone.setVisible(false);

        AnimatedImage heroDictophone = renderer.getActor(TiledLayers.ACTORS_LAYER_DICTOPHONES, StageActors.HERO_DICTOPHONE, AnimatedImage.class);
        heroDictophone.setVisible(false);

        AnimatedImage hrStress = renderer.getActor(TiledLayers.ACTORS_LAYER_STRESS_LEVELS, StageActors.HR_STRESS_LEVEL, AnimatedImage.class);
        hrStress.setVisible(false);

        AnimatedImage heroStress = renderer.getActor(TiledLayers.ACTORS_LAYER_STRESS_LEVELS, StageActors.HERO_STRESS_LEVEL, AnimatedImage.class);
        heroStress.setVisible(false);


        MapLayer dailyLayer = renderer.getLayer(TiledLayers.DAILY);
        dailyLayer.setVisible(true);

        MapLayer dailyHellLayer = renderer.getLayer(TiledLayers.DAILY_HELL_VERSION);
        dailyHellLayer.setVisible(false);

        MapLayer hellEnvironmentLayer = renderer.getLayer(TiledLayers.HELL_ENVIRONMENT);
        hellEnvironmentLayer.setVisible(false);

        MapLayer environmentLayer = renderer.getLayer(TiledLayers.ENVIRONMENT);
        environmentLayer.setVisible(true);
    }

    @Override
    public void draw(float deltaTime) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
