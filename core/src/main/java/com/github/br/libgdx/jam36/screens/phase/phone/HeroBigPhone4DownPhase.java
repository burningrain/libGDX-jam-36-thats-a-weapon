package com.github.br.libgdx.jam36.screens.phase.phone;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.context.PhoneContext;
import com.github.br.libgdx.jam36.screens.StageActors;
import com.github.br.libgdx.jam36.screens.TiledLayers;
import com.github.br.libgdx.jam36.screens.phase.Phase;
import com.github.br.libgdx.jam36.ui.AnimatedImage;

public class HeroBigPhone4DownPhase implements Phase, PhoneContext.Listener {

    //TODO грязь, но времени уже просто нет
    public boolean isMoveToDown;

    public static final int INITIAL_OFFSET_Y = -1500;
    public static final float DELTA_Y = 1000f;
    private float currentY;

    private CustomOrthogonalTiledMapRenderer renderer;
    private GameContext gameContext;

    private boolean isFinished;

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        this.gameContext = gameContext;
        this.gameContext.getPhoneContext().setListener(this);

        this.renderer = renderer;

        MapGroupLayer layer = (MapGroupLayer) renderer.getLayer(TiledLayers.HERO_BIG_PHONE);
        layer.setVisible(true);

        // установить позицию вне экрана
        currentY = 0;
        renderer.updateOffsetsForGroupLayer(TiledLayers.TABLET, 0, currentY);
        isMoveToDown = true;
    }

    private void resetUI() {
        // скрываем и возвращаем планшет назад на экран
        MapGroupLayer layer = (MapGroupLayer) renderer.getLayer(TiledLayers.HERO_BIG_PHONE);
        layer.setVisible(false);
        renderer.updateOffsetsForGroupLayer(TiledLayers.HERO_BIG_PHONE, 0, -currentY);
    }

    @Override
    public void update(PhoneContext context) {
        //TODO ???
        //tabletText.setText(context.getCurrentPage());
    }

    @Override
    public void draw(float deltaTime) {
        if (isMoveToDown) {
            if (currentY <= INITIAL_OFFSET_Y) {
                resetUI();
                preFinish();
                setFinished(true);
            } else {
                movePhoneDown(-DELTA_Y, deltaTime);
            }
        }
    }

    private void preFinish() {
        MapLayer heroTablePhone = renderer.getLayer(TiledLayers.SMALL_HERO_PHONE_ON_TABLE);
        heroTablePhone.setVisible(true);

        AnimatedImage dictophone = renderer.getActor(TiledLayers.ACTORS_LAYER_DICTOPHONES, StageActors.HERO_DICTOPHONE, AnimatedImage.class);
        dictophone.setVisible(false);
    }

    private void movePhoneDown(float deltaY, float deltaTime) {
        float dy = deltaY * deltaTime;
        renderer.updateOffsetsForGroupLayer(
            TiledLayers.HERO_BIG_PHONE,
            0,
            dy
        );
        currentY += dy;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

}
