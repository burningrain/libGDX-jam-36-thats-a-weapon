package com.github.br.libgdx.jam36.screens.phase.phone;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.screens.StageActors;
import com.github.br.libgdx.jam36.screens.TiledLayers;
import com.github.br.libgdx.jam36.screens.phase.Phase;
import com.github.tommyettinger.textra.TypingLabel;

public class HeroBigPhone1CallPhase implements Phase {

    private final String callerName;

    public HeroBigPhone1CallPhase(String caller) {
        this.callerName = caller;
    }

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        TypingLabel caller = renderer.getActor(TiledLayers.ACTORS_LAYER_HERO_BIG_PHONE, StageActors.HERO_PHONE_CALLER, TypingLabel.class);
        caller.setText("{JUMP=0.4;1.0;1.0}" + callerName + "{ENDJUMP}");
        caller.setVisible(true);
        gameContext.getPhoneContext().setCallerName(callerName);

        MapLayer layer = renderer.getLayer(TiledLayers.HERO_BIG_PHONE);
        MapGroupLayer heroBigPhoneLayer = (MapGroupLayer) layer;

        MapLayer callHumanPic = heroBigPhoneLayer.getLayers().get(TiledLayers.HERO_BIG_PHONE_CALL_HUMAN_PIC);
        callHumanPic.setVisible(true);

        MapLayer getCallLayer = heroBigPhoneLayer.getLayers().get(TiledLayers.HERO_BIG_PHONE_GET_CALL);
        getCallLayer.setVisible(true);

        MapLayer cancelCallLayer = heroBigPhoneLayer.getLayers().get(TiledLayers.HERO_BIG_PHONE_CANCEL_CALL);
        cancelCallLayer.setVisible(false);

        MapLayer dynamicBtnLayer = heroBigPhoneLayer.getLayers().get(TiledLayers.HERO_BIG_PHONE_DYNAMIC_BUTTON);
        dynamicBtnLayer.setVisible(false);

        MapLayer callNumberLayer = heroBigPhoneLayer.getLayers().get(TiledLayers.HERO_BIG_PHONE_CALL_NUMBER_BUTTON);
        callNumberLayer.setVisible(false);

        MapLayer muteMicrophoneLayer = heroBigPhoneLayer.getLayers().get(TiledLayers.HERO_BIG_PHONE_MUTE_MICROPHONE_BUTTON);
        muteMicrophoneLayer.setVisible(false);
    }

    @Override
    public void draw(float deltaTime) {

    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
