package com.github.br.libgdx.jam36.screens.phase.phone;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.context.PhoneContext;
import com.github.br.libgdx.jam36.screens.StageActors;
import com.github.br.libgdx.jam36.screens.TiledLayers;
import com.github.br.libgdx.jam36.screens.phase.ContextChanger;
import com.github.br.libgdx.jam36.screens.phase.Phase;
import com.github.tommyettinger.textra.TypingLabel;

@Deprecated
public class HeroBigPhonePhase implements Phase, PhoneContext.Listener {

    //TODO грязь, но времени уже просто нет
    public boolean isMoveToUp;
    public boolean isNeedToMove;

    public static final int INITIAL_OFFSET_Y = -1500;
    public static final float DELTA_Y = 1000f;
    private float currentY;

    private CustomOrthogonalTiledMapRenderer renderer;
    private GameContext gameContext;

    private TypingLabel phoneCallerText;

    private boolean isFinished;

    private final ContextChanger changerAfterSign;

    public HeroBigPhonePhase(ContextChanger changerAfterSign) {
        this.changerAfterSign = changerAfterSign;
    }

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        this.gameContext = gameContext;
        this.gameContext.getPhoneContext().setListener(this);

        this.renderer = renderer;

        phoneCallerText = renderer.getActor(TiledLayers.ACTORS_LAYER_HERO_BIG_PHONE, StageActors.HERO_PHONE_CALLER, TypingLabel.class);

        phoneCallerText.setText(gameContext.getTabletContext().getCurrentPage());
        phoneCallerText.setWrap(true);

        MapGroupLayer layer = (MapGroupLayer) renderer.getLayer(TiledLayers.HERO_BIG_PHONE);
        layer.setVisible(true);

        // установить позицию вне экрана
        currentY = INITIAL_OFFSET_Y;
        renderer.updateOffsetsForGroupLayer(TiledLayers.TABLET, 0, currentY);
        isMoveToUp = true;
        isNeedToMove = true;
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
        if (isNeedToMove) {
            if (isMoveToUp) {
                if (currentY >= 0) {
                    // приехали вверх
                    isMoveToUp = false;
                    isNeedToMove = false;
                    if (currentY > 0) {
                        renderer.updateOffsetsForGroupLayer(
                            TiledLayers.TABLET,
                            0,
                            -currentY
                        );
                    }
                } else {
                    moveTabletUp(DELTA_Y, deltaTime);
                }
            } else {
                if (currentY <= INITIAL_OFFSET_Y) {
                    resetUI();
                    cleanListeners();
                    setFinished(true);
                } else {
                    moveTabletUp(-DELTA_Y, deltaTime);
                }
            }
        }
    }

    private void cleanListeners() {

    }

    private void moveTabletUp(float deltaY, float deltaTime) {
        float dy = deltaY * deltaTime;
        renderer.updateOffsetsForGroupLayer(
            TiledLayers.TABLET,
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
