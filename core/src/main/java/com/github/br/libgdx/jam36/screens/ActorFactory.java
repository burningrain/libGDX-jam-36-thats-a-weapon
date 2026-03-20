package com.github.br.libgdx.jam36.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.github.br.libgdx.jam36.Resources;
import com.github.br.libgdx.jam36.ui.AnimatedImage;

public class ActorFactory {

    private final Skin skin;
    private final AssetManager assetManager;

    public ActorFactory(Skin skin, AssetManager assetManager) {
        this.skin = skin;
        this.assetManager = assetManager;
    }

    public Actor getActor(MapObject object) {
        String name = object.getName();
        object.getProperties();

        switch (name) {
            // daily
            case "tablet_left_button":
                return createTabletButton(object);
            case "tablet_text":
                return createTabletText(object);
            case "sign_button":
                return createSignButton(object);

            case "clock":
                return createClock(object);
            case "text_window":
                return createTextWindow(object);

            case "hero_dictophone":
                return createHeroDictophone(object);
            case "hr_dictophone":
                return createHrDictophone(object);

            case "hero_stress_level":
                return createHeroStressLevel(object);
            case "hr_stress_level":
                return createHrStressLevel(object);

           // hell
            case "fire":
                return createFire(object);

            default:
                throw new IllegalArgumentException("unknown stage2d actor: " + name);
        }
    }

    private Actor createHrStressLevel(MapObject object) {
        TextureAtlas textureAtlas = assetManager.get(Resources.ANIMATION_ATLAS, TextureAtlas.class);
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.findRegions(Resources.Animation.HR_STRESS_LEVEL);
        Animation<TextureRegion> animation = new Animation<>(
            0.033f, regions, Animation.PlayMode.LOOP_PINGPONG
        );

        return new AnimatedImage(animation);
    }

    private Actor createHeroStressLevel(MapObject object) {
        TextureAtlas textureAtlas = assetManager.get(Resources.ANIMATION_ATLAS, TextureAtlas.class);
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.findRegions(Resources.Animation.HERO_STRESS_LEVEL);
        Animation<TextureRegion> animation = new Animation<>(
            0.033f, regions, Animation.PlayMode.LOOP_PINGPONG
        );

        return new AnimatedImage(animation);
    }

    private Actor createHeroDictophone(MapObject object) {
        TextureAtlas textureAtlas = assetManager.get(Resources.ANIMATION_ATLAS, TextureAtlas.class);
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.findRegions(Resources.Animation.HERO_DICTOPHONE);
        Animation<TextureRegion> animation = new Animation<>(
            0.033f, regions, Animation.PlayMode.LOOP_PINGPONG
        );

        return new AnimatedImage(animation);
    }

    private Actor createHrDictophone(MapObject object) {
        TextureAtlas textureAtlas = assetManager.get(Resources.ANIMATION_ATLAS, TextureAtlas.class);
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.findRegions(Resources.Animation.HR_DICTOPHONE);
        Animation<TextureRegion> animation = new Animation<>(
            0.033f, regions, Animation.PlayMode.LOOP_PINGPONG
        );

        return new AnimatedImage(animation);
    }

    private Actor createFire(MapObject object) {
        TextureAtlas textureAtlas = assetManager.get(Resources.ANIMATION_ATLAS, TextureAtlas.class);
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.findRegions(Resources.Animation.FIRE);
        Animation<TextureRegion> animation = new Animation<>(
            0.033f, regions, Animation.PlayMode.LOOP_PINGPONG
        );

        return new AnimatedImage(animation);
    }

    private Actor createTextWindow(MapObject object) {
        Label label = new Label("Хватит испытывать наше терпение! У тебя больше нет права на ошибку! Один неверный ответ и ты нас покидаешь!", skin, "talking");
        label.setAlignment(Align.topLeft);

        MapProperties properties = object.getProperties();
        Float width = properties.get("width", float.class);
        Float height = properties.get("height", float.class);

        label.setWidth(width);
        label.setHeight(height);

        label.setWrap(true);

        return label;
    }

    private Actor createClock(MapObject object) {
        //TODO переделать
        Label label = new Label("текст", skin, "document_text");
        return label;
    }

    private Actor createSignButton(MapObject object) {
        Button button = new ImageTextButton("Ознакомиться\nи подписать", skin);
        return button;
    }

    private Actor createTabletText(MapObject object) {
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

    private Actor createTabletButton(MapObject object) {
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
