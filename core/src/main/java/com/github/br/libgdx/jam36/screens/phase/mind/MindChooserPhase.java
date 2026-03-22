package com.github.br.libgdx.jam36.screens.phase.mind;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.github.br.libgdx.jam36.Constants;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.screens.ActorFactory;
import com.github.br.libgdx.jam36.screens.TiledLayers;
import com.github.br.libgdx.jam36.screens.phase.Choose;
import com.github.br.libgdx.jam36.screens.phase.Phase;
import com.github.br.libgdx.jam36.ui.AnimatedImage;
import com.github.br.libgdx.jam36.ui.FloatingButton;

public class MindChooserPhase implements Phase {

    // Параметры плавания
    private static final float FLOAT_AMPLITUDE = 105f;    // Амплитуда в пикселях
    private static final float FLOAT_SPEED = 1.2f;        // Скорость (радиан/сек)
    private static final float FLOAT_SPEED_VARIATION = 0.5f; // Вариация скорости

    private static final float REPULSION_FORCE = 500f;  // Сила отталкивания
    private static final float MIN_DISTANCE = 140f;     // Минимальное расстояние между кнопками

    private final ActorFactory actorFactory;
    private final Choose[] chooses;

    private Array<FloatingButton> floatingButtons = new Array<>();

    private GameContext gameContext;
    private CustomOrthogonalTiledMapRenderer renderer;

    private boolean isFinished;

    private boolean isFadeIn;
    private boolean isFadeOut;

    private boolean isAnimationInProgress;

    private Array<AnimatedImage> animatedThoughts = new Array<>();
    private Stage stage;

    private MindChooserHandler mindChooserHandler;


    public MindChooserPhase(ActorFactory actorFactory, MindChooserHandler mindChooserHandler, Choose... chooses) {
        this.actorFactory = actorFactory;
        this.mindChooserHandler = mindChooserHandler;
        this.chooses = chooses;
    }

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        this.gameContext = gameContext;
        this.renderer = renderer;

        MapLayer layer = renderer.getLayer(TiledLayers.THOUGHTS);
        layer.setOpacity(0);
        layer.setVisible(true);

        isFadeIn = true;
        stage = renderer.getStageByLayerName(TiledLayers.ACTORS_LAYER_THOUGHTS);
    }

    @Override
    public void draw(float deltaTime) {
        MapLayer layer = renderer.getLayer(TiledLayers.THOUGHTS);
        float opacity = layer.getOpacity();

        if (isFadeIn) {
            if (opacity == 1f) {
                createButtons(renderer);
                isFadeIn = false;
            } else {
                opacity += deltaTime / 1.25f;
                if (opacity > 1f) {
                    opacity = 1f;
                }
                layer.setOpacity(opacity);
            }
        }

        if (isFadeOut) {
            if (!isAnimationInProgress) {
                for (FloatingButton button : floatingButtons) {
                    float x = button.getX();
                    float y = button.getY();

                    AnimatedImage animationThought = actorFactory.createAnimationThought();
                    animationThought.setX(x);
                    animationThought.setY(y);
                    animationThought.play();

                    animatedThoughts.add(animationThought);
                    stage.addActor(animationThought);

                    button.remove();
                }
                isAnimationInProgress = true;
            }

            for (AnimatedImage animationThought : animatedThoughts) {
                if (animationThought.isAnimationEnd()) {
                    animationThought.remove();
                }
            }

            if (opacity == 0f) {
                isFadeOut = false;
                finishPhase();
            } else {
                opacity -= deltaTime / 1.25f;
                if (opacity < 0f) {
                    opacity = 0f;
                }
                layer.setOpacity(opacity);
            }
        }
    }

    private void finishPhase() {
        MapLayer layer = renderer.getLayer(TiledLayers.THOUGHTS);
        layer.setVisible(false);
        isFinished = true;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    private void createButtons(CustomOrthogonalTiledMapRenderer renderer) {
        Stage stage = renderer.getStageByLayerName(TiledLayers.ACTORS_LAYER_THOUGHTS);
        for (int i = 0; i < chooses.length; i++) {
            Choose choose = chooses[i];

            // Создаем плавающую кнопку с уникальной фазой
            float phase = (float) (i * Math.PI * 2 / chooses.length); // Равномерное распределение фаз
            float speed = FLOAT_SPEED + (float) Math.random() * FLOAT_SPEED_VARIATION;

            FloatingButton thought = actorFactory.createFloatingThought(
                choose.getId(),
                "{WAVE=1.0;1.0;0.4}" + choose.getText() + "{ENDWAVE}",
                FLOAT_AMPLITUDE,
                speed,
                phase
            );
            thought.setX(MathUtils.random(100, Constants.WORLD_WIDTH - 250));
            thought.setY(MathUtils.random(700, Constants.WORLD_HEIGHT - 250));

            thought.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    int answerId = Integer.parseInt(actor.getName());
                    mindChooserHandler.handle(answerId, gameContext);
                    isFadeOut = true;
                    // Можно добавить эффект при выборе
                    thought.resetPosition();
                }
            });

            stage.addActor(thought);
            floatingButtons.add(thought);
        }
        separateButtons();
    }

    private void separateButtons() {
        int maxIterations = 50;
        float step = 0.1f;

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            boolean moved = false;

            for (int i = 0; i < floatingButtons.size; i++) {
                FloatingButton btn1 = floatingButtons.get(i);
                Rectangle rect1 = new Rectangle(btn1.getX(), btn1.getY(),
                    btn1.getWidth(), btn1.getHeight());

                for (int j = i + 1; j < floatingButtons.size; j++) {
                    FloatingButton btn2 = floatingButtons.get(j);
                    Rectangle rect2 = new Rectangle(btn2.getX(), btn2.getY(),
                        btn2.getWidth(), btn2.getHeight());

                    if (rect1.overlaps(rect2)) {
                        moved = true;

                        // Вычисляем направление отталкивания
                        float dx = btn1.getX() - btn2.getX();
                        float dy = btn1.getY() - btn2.getY();
                        float distance = Vector2.dst(btn1.getX(), btn1.getY(),
                            btn2.getX(), btn2.getY());

                        if (distance < 0.01f) {
                            dx = MathUtils.random(-1f, 1f);
                            dy = MathUtils.random(-1f, 1f);
                            distance = 1;
                        }

                        float overlap = MIN_DISTANCE - distance;
                        float force = overlap * REPULSION_FORCE * step;

                        float moveX = (dx / distance) * force;
                        float moveY = (dy / distance) * force;

                        // Отталкиваем обе кнопки
                        btn1.setPosition(btn1.getX() + moveX, btn1.getY() + moveY);
                        btn2.setPosition(btn2.getX() - moveX, btn2.getY() - moveY);

                        // Ограничиваем область
                        float minX = 200;
                        float maxX = Constants.WORLD_WIDTH - btn1.getWidth() - 200;
                        float minY = 600;
                        float maxY = Constants.WORLD_HEIGHT - btn1.getHeight() - 200;

                        btn1.setPosition(
                            MathUtils.clamp(btn1.getX(), minX, maxX),
                            MathUtils.clamp(btn1.getY(), minY, maxY)
                        );
                        btn2.setPosition(
                            MathUtils.clamp(btn2.getX(), minX, maxX),
                            MathUtils.clamp(btn2.getY(), minY, maxY)
                        );
                    }
                }
            }

            if (!moved) break;
        }
    }


}
