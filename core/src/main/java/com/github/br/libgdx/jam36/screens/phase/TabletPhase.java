package com.github.br.libgdx.jam36.screens.phase;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.context.TabletContext;
import com.github.br.libgdx.jam36.screens.StageActors;
import com.github.br.libgdx.jam36.screens.TiledLayers;

public class TabletPhase implements Phase, TabletContext.Listener {

    public static final int INITIAL_OFFSET_Y = -1500;
    public static final float DELTA_Y = 1000f;
    private float currentY;
    private boolean isMoveToUp;
    private boolean isNeedToMove;

    private CustomOrthogonalTiledMapRenderer renderer;
    private GameContext gameContext;

    private Label tabletText;
    private ImageButton leftButton;
    private ImageButton rightButton;
    private ImageTextButton signButton;

    private boolean isFinished;

    private ChangeListener signButtonListener;

    private ChangeListener buttonLeftListener;
    private ChangeListener buttonRightListener;

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        this.gameContext = gameContext;
        this.gameContext.getTabletContext().setListener(this);

        this.renderer = renderer;

        tabletText = renderer.getActor(TiledLayers.ACTORS_LAYER_TABLET, StageActors.TABLET_TEXT, Label.class);
        leftButton = renderer.getActor(TiledLayers.ACTORS_LAYER_TABLET, StageActors.TABLET_LEFT_BUTTON, ImageButton.class);
        rightButton = renderer.getActor(TiledLayers.ACTORS_LAYER_TABLET, StageActors.TABLET_RIGHT_BUTTON, ImageButton.class);
        signButton = renderer.getActor(TiledLayers.ACTORS_LAYER_TABLET, StageActors.SIGN_BUTTON, ImageTextButton.class);

        tabletText.setText(gameContext.getTabletContext().getCurrentPage());
        tabletText.setWrap(true);
        signButtonListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                isNeedToMove = true;
            }
        };
        signButton.addListener(signButtonListener);

        buttonLeftListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                gameContext.getTabletContext().goToPrevPage();
            }
        };
        leftButton.addListener(buttonLeftListener);

        buttonRightListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                gameContext.getTabletContext().goToNextPage();
            }
        };
        rightButton.addListener(buttonRightListener);

        MapGroupLayer layer = (MapGroupLayer) renderer.getLayer(TiledLayers.TABLET);
        layer.setVisible(true);

        // установить позицию вне экрана
        currentY = INITIAL_OFFSET_Y;
        renderer.updateOffsetsForGroupLayer(TiledLayers.TABLET, 0, currentY);
        isMoveToUp = true;
        isNeedToMove = true;
    }

    @Override
    public void update(TabletContext context) {
        tabletText.setText(context.getCurrentPage());
    }

    @Override
    public void draw(float deltaTime) {
        if (isNeedToMove) {
            if (isMoveToUp) {
                if (Math.abs(currentY - 0) < 12f) {
                    // приехали вверх
                    isMoveToUp = false;
                    isNeedToMove = false;
                } else {
                    moveTabletUp(DELTA_Y, deltaTime);
                }
            } else {
                if (Math.abs(currentY - INITIAL_OFFSET_Y) < 12f) {
                    cleanListeners();
                    setFinished(true);
                } else {
                    moveTabletUp(-DELTA_Y, deltaTime);
                }
            }
        }
    }

    private void cleanListeners() {
        leftButton.removeListener(buttonLeftListener);
        rightButton.removeListener(buttonRightListener);
        signButton.removeListener(signButtonListener);
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
