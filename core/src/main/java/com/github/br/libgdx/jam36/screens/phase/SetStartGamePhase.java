package com.github.br.libgdx.jam36.screens.phase;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.github.br.libgdx.common.structure.screen.ui.AnimatedImage;
import com.github.br.libgdx.common.structure.screen.ui.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.screens.CustomVfxEffectPooledFactoryImpl;
import com.github.br.libgdx.jam36.screens.StageActors;
import com.github.br.libgdx.jam36.screens.TiledLayers;

public class SetStartGamePhase implements Phase {

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        MapLayer thoughtsLayer = renderer.getTiledLayer(TiledLayers.THOUGHTS);
        thoughtsLayer.setVisible(false);

        MapLayer heroBigPhoneLayer = renderer.getTiledLayer(TiledLayers.MAIN_VFX, TiledLayers.HERO_BIG_PHONE);
        heroBigPhoneLayer.setVisible(false);

        MapLayer tabletLayer = renderer.getTiledLayer(TiledLayers.MAIN_VFX, TiledLayers.TABLET);
        tabletLayer.setVisible(false);

        MapLayer teaLayer = renderer.getTiledLayer(TiledLayers.MAIN_VFX, TiledLayers.TEA);
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


        MapLayer dailyLayer = renderer.getTiledLayer(TiledLayers.MAIN_VFX, TiledLayers.DAILY);
        dailyLayer.setVisible(true);

        MapLayer dailyHellLayer = renderer.getTiledLayer(TiledLayers.MAIN_VFX, TiledLayers.DAILY_HELL_VERSION);
        dailyHellLayer.setVisible(false);

        MapLayer hellEnvironmentLayer = renderer.getTiledLayer(TiledLayers.MAIN_VFX, TiledLayers.HELL_ENVIRONMENT);
        hellEnvironmentLayer.setVisible(false);

        MapLayer environmentLayer = renderer.getTiledLayer(TiledLayers.MAIN_VFX, TiledLayers.ENVIRONMENT);
        environmentLayer.setVisible(true);

        MapLayer tiledLayer = renderer.getTiledLayer(TiledLayers.MAIN_VFX);
        MapProperties properties = tiledLayer.getProperties();

        properties.put(CustomOrthogonalTiledMapRenderer.IS_VFX_EFFECT_ENABLED, Boolean.TRUE);
        properties.put(CustomOrthogonalTiledMapRenderer.VFX_LAYER_EFFECT, CustomVfxEffectPooledFactoryImpl.Title.MAIN_VFX);
    }

    @Override
    public void draw(float deltaTime) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
