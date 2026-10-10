package com.github.br.libgdx.jam36.screens.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.github.br.libgdx.jam36.Resources;
import com.github.br.libgdx.jam36.screens.StageActors;
import com.github.br.libgdx.structure.screen.ui.ActorFactory;
import com.github.br.libgdx.structure.screen.ui.AnimatedImage;
import com.github.br.libgdx.structure.screen.ui.DefaultActorFactory;
import com.github.tommyettinger.textra.TypingLabel;

public class UserActorFactory implements ActorFactory {


    private final DefaultActorFactory delegate;

    private final Skin skin;
    private final AssetManager assetManager;

    public UserActorFactory(Skin skin, AssetManager assetManager) {
        this.delegate = new DefaultActorFactory(skin, assetManager);
        this.skin = skin;
        this.assetManager = assetManager;
    }


    @Override
    public Actor getActor(MapObject object) {
        String name = object.getName();
        MapProperties properties = object.getProperties();

        String actorType = properties.get(DefaultActorFactory.ACTOR_TYPE, String.class);
        switch (name) {
            // daily
            case StageActors.HERO_PHONE_CALLER:
                return createHeroCallerText(object);

            case StageActors.TABLET_LEFT_BUTTON:
            case StageActors.TABLET_RIGHT_BUTTON:
                return createTabletButton(object);
            case StageActors.TABLET_TEXT:
                return createTabletText(object);
            case StageActors.SIGN_BUTTON:
                return createSignButton(object);

            // watch
            case StageActors.WEEK_DAY:
                return createCalendarDay(object);
            case StageActors.WATCH_ARROW:
                return createWatchHourArrow(object);


            case StageActors.TEXT_WINDOW:
                return createTextWindow(object);

            // table
            case StageActors.HERO_DICTOPHONE:
                return createHeroDictophone(object);
            case StageActors.HR_DICTOPHONE:
                return createHrDictophone(object);

            case StageActors.HERO_STRESS_LEVEL:
                return createHeroStressLevel(object);
            case StageActors.HR_STRESS_LEVEL:
                return createHrStressLevel(object);

            case StageActors.THOUGHT:
                return createThought(object);

            // hell
            case StageActors.FIRE_1:
            case StageActors.FIRE_2:
            case StageActors.FIRE_3:
            case StageActors.FIRE_4:
            case StageActors.FIRE_5:
                return createFire(object);
        }

        return delegate.getActor(object);
    }

    private Actor createHeroCallerText(MapObject object) {
        TypingLabel label = new TypingLabel("Вставь сюда текст", skin, "talking");
        label.setAlignment(Align.center);

        MapProperties properties = object.getProperties();
        Float width = properties.get("width", float.class);
        Float height = properties.get("height", float.class);

        label.setWidth(width);
        label.setHeight(height);

        label.setWrap(true);

        return label;
    }

    public AnimatedImage createAnimationThought() {
        return delegate.createAnimatedImage(
            Resources.ANIMATION_ATLAS,
            Resources.Animation.THOUGHT,
            Animation.PlayMode.NORMAL,
            0.033f
        );
    }

    public FloatingTextButton createFloatingThought(int id, String text, float floatAmplitude, float speed, float phase) {
        FloatingTextButton floatingButton = new FloatingTextButton(
            skin, "thought", text , floatAmplitude, speed, phase
        );
        floatingButton.setName("" + id);

        return floatingButton;
    }

    public Actor createThought(MapObject object) {
        return new ImageTextButton("Впиши текст", skin, "thought");
    }

    public Actor createWatchHourArrow(MapObject object) {
        TextureAtlas textureAtlas = assetManager.get(Resources.ANIMATION_ATLAS, TextureAtlas.class);
        TextureAtlas.AtlasRegion region = textureAtlas.findRegion(Resources.Animation.WATCH_ARROW);

        return new Image(region);
    }

    public Actor createHrStressLevel(MapObject object) {
        TextureAtlas textureAtlas = assetManager.get(Resources.ANIMATION_ATLAS, TextureAtlas.class);
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.findRegions(Resources.Animation.HR_STRESS_LEVEL);
        Animation<TextureRegion> animation = new Animation<>(
            0.033f, regions, Animation.PlayMode.LOOP_PINGPONG
        );

        return new AnimatedImage(animation);
    }

    public Actor createHeroStressLevel(MapObject object) {
        TextureAtlas textureAtlas = assetManager.get(Resources.ANIMATION_ATLAS, TextureAtlas.class);
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.findRegions(Resources.Animation.HERO_STRESS_LEVEL);
        Animation<TextureRegion> animation = new Animation<>(
            0.033f, regions, Animation.PlayMode.LOOP_PINGPONG
        );

        return new AnimatedImage(animation);
    }

    public Actor createHeroDictophone(MapObject object) {
        TextureAtlas textureAtlas = assetManager.get(Resources.ANIMATION_ATLAS, TextureAtlas.class);
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.findRegions(Resources.Animation.HERO_DICTOPHONE);
        Animation<TextureRegion> animation = new Animation<>(
            0.033f, regions, Animation.PlayMode.LOOP_PINGPONG
        );

        return new AnimatedImage(animation);
    }

    public Actor createHrDictophone(MapObject object) {
        TextureAtlas textureAtlas = assetManager.get(Resources.ANIMATION_ATLAS, TextureAtlas.class);
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.findRegions(Resources.Animation.HR_DICTOPHONE);
        Animation<TextureRegion> animation = new Animation<>(
            0.033f, regions, Animation.PlayMode.LOOP_PINGPONG
        );

        return new AnimatedImage(animation);
    }

    public Actor createFire(MapObject object) {
        TextureAtlas textureAtlas = assetManager.get(Resources.ANIMATION_ATLAS, TextureAtlas.class);
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.findRegions(Resources.Animation.FIRE);
        Animation<TextureRegion> animation = new Animation<>(
            0.033f, regions, Animation.PlayMode.LOOP_PINGPONG
        );

        return new AnimatedImage(animation);
    }

    public Actor createTextWindow(MapObject object) {
        TypingLabel label = new TypingLabel("Вставь сюда текст", skin, "talking");
        label.setAlignment(Align.topLeft);

        MapProperties properties = object.getProperties();
        Float width = properties.get("width", float.class);
        Float height = properties.get("height", float.class);

        label.setWidth(width);
        label.setHeight(height);

        label.setWrap(true);

        return label;
    }

    public Actor createCalendarDay(MapObject object) {
        TextureAtlas textureAtlas = assetManager.get(Resources.ANIMATION_ATLAS, TextureAtlas.class);
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.findRegions(Resources.Animation.CALENDAR_DAY);
        Animation<TextureRegion> animation = new Animation<>(
            0.033f, regions, Animation.PlayMode.LOOP_PINGPONG
        );

        return new AnimatedImage(animation);
    }

    public Actor createSignButton(MapObject object) {
        return new ImageTextButton("Ознакомиться\nи подписать", skin);
    }

    public Actor createTabletText(MapObject object) {
        MapProperties properties = object.getProperties();

        Label label = new Label("текст", skin, "document_text");
        label.setAlignment(Align.topLeft);

        Float width = properties.get("width", float.class);
        Float height = properties.get("height", float.class);

        label.setWidth(width);
        label.setHeight(height);

        label.setWrap(true);

        return label;
    }

    public Actor createTabletButton(MapObject object) {
        ImageButton button = new ImageButton(skin);
        MapProperties properties = object.getProperties();
        Boolean isFlip = (Boolean) properties.get("flip");
        if (isFlip != null && isFlip) {
            button.setTransform(true); // Разрешаем трансформацию
            button.setScale(-1, 1);    // Отражаем по горизонтали
            button.setOrigin(Align.center); // Устанавливаем центр для отражения

            // Корректировка хитбокса (важно для нажатий!)
            // Из-за scale(-1) координаты нажатий будут неправильными, если не скорректировать
        }

        return button;
    }

}
