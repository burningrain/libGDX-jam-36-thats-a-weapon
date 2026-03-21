package com.github.br.libgdx.jam36.screens.phase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.screens.StageActors;
import com.github.br.libgdx.jam36.screens.TiledLayers;

public class HrDialogPhase implements Phase {

    private GameContext gameContext;
    private CustomOrthogonalTiledMapRenderer renderer;

    private boolean isFinished;

    private final String text;

    public HrDialogPhase(String text) {
        this.text = text;
    }

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        this.gameContext = gameContext;
        this.renderer = renderer;

        Label hrTextWindow = renderer.getActor(TiledLayers.HR_TEXT_WINDOW, StageActors.TEXT_WINDOW, Label.class);
        hrTextWindow.setText(text);
        hrTextWindow.setWrap(true);

        MapLayer layer = renderer.getLayer(TiledLayers.HR_TEXT_WINDOW);
        layer.setVisible(true);
    }

    @Override
    public void draw(float deltaTime) {
        if (Gdx.input.justTouched()) {
            MapLayer layer = renderer.getLayer(TiledLayers.HR_TEXT_WINDOW);
            layer.setVisible(false);
            isFinished = true;
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

}
