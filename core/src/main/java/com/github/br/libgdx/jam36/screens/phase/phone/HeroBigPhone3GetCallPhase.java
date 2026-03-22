package com.github.br.libgdx.jam36.screens.phase.phone;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.screens.StageActors;
import com.github.br.libgdx.jam36.screens.TiledLayers;
import com.github.br.libgdx.jam36.screens.phase.Phase;
import com.github.tommyettinger.textra.TypingLabel;

public class HeroBigPhone3GetCallPhase implements Phase {

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        String callerName = gameContext.getPhoneContext().getCallerName();

        TypingLabel caller = renderer.getActor(TiledLayers.ACTORS_LAYER_HERO_BIG_PHONE, StageActors.HERO_PHONE_CALLER, TypingLabel.class);
        caller.setText(callerName);
        caller.restart();
        caller.setVisible(true);


        MapLayer layer = renderer.getLayer(TiledLayers.HERO_BIG_PHONE);
        MapGroupLayer heroBigPhoneLayer = (MapGroupLayer) layer;

        MapLayer callHumanPic = heroBigPhoneLayer.getLayers().get(TiledLayers.HERO_BIG_PHONE_CALL_HUMAN_PIC);
        callHumanPic.setVisible(true);

        MapLayer getCallLayer = heroBigPhoneLayer.getLayers().get(TiledLayers.HERO_BIG_PHONE_GET_CALL);
        getCallLayer.setVisible(false);

        MapLayer cancelCallLayer = heroBigPhoneLayer.getLayers().get(TiledLayers.HERO_BIG_PHONE_CANCEL_CALL);
        cancelCallLayer.setVisible(true);

        MapLayer dynamicBtnLayer = heroBigPhoneLayer.getLayers().get(TiledLayers.HERO_BIG_PHONE_DYNAMIC_BUTTON);
        dynamicBtnLayer.setVisible(true);

        MapLayer callNumberLayer = heroBigPhoneLayer.getLayers().get(TiledLayers.HERO_BIG_PHONE_CALL_NUMBER_BUTTON);
        callNumberLayer.setVisible(true);

        MapLayer muteMicrophoneLayer = heroBigPhoneLayer.getLayers().get(TiledLayers.HERO_BIG_PHONE_MUTE_MICROPHONE_BUTTON);
        muteMicrophoneLayer.setVisible(true);
    }

    @Override
    public void draw(float deltaTime) {

    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
