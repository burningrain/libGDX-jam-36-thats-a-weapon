package com.github.br.libgdx.jam36.screens.phase.phone;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.context.PhoneContext;
import com.github.br.libgdx.jam36.screens.TiledLayers;
import com.github.br.libgdx.jam36.screens.phase.Phase;

public class HeroBigPhone2UpPhase implements Phase, PhoneContext.Listener {

    //TODO грязь, но времени уже просто нет
    public boolean isMoveToUp;

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

        MapLayer heroTablePhone = renderer.getLayer(TiledLayers.SMALL_HERO_PHONE_ON_TABLE);
        heroTablePhone.setVisible(false);

        // установить позицию вне экрана
        currentY = INITIAL_OFFSET_Y;
        renderer.updateOffsetsForGroupLayer(TiledLayers.HERO_BIG_PHONE, 0, currentY);
        isMoveToUp = true;
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
        if (isMoveToUp) {
            if (currentY >= 0) {
                // приехали вверх
                isMoveToUp = false;
                if (currentY > 0) {
                    renderer.updateOffsetsForGroupLayer(
                        TiledLayers.HERO_BIG_PHONE,
                        0,
                        -currentY
                    );
                }
                setFinished(true);
            } else {
                moveTabletUp(DELTA_Y, deltaTime);
            }
        }
    }

    private void cleanListeners() {
    }

    private void moveTabletUp(float deltaY, float deltaTime) {
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
