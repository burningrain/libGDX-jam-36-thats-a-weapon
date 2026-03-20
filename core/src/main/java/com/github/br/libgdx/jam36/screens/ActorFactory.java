package com.github.br.libgdx.jam36.screens;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

public class ActorFactory {

    private final Skin skin;

    public ActorFactory(Skin skin) {
        this.skin = skin;
    }

    public Actor getActor(MapObject object) {
        String name = object.getName();
        object.getProperties();

        switch (name) {
            case "tablet_left_button":
                return createTabletButton(object);
            case "tablet_text":
                return createTabletText(object);
            case "sign_button":
                return createSignButton(object);

            case "clock":
                return createClock(object);
            case "fire":
                return createFire(object);
            case "text_window":
                return createTextWindow(object);
            default:
                throw new IllegalArgumentException("unknown stage2d actor: " + name);
        }
    }

    private Actor createFire(MapObject object) {
        return new Label("", skin, "talking");
    }

    private Actor createTextWindow(MapObject object) {
        Label label = new Label("У тебя больше нет права на ошибку!\nВсего один неверный ответ и ты нас покидаешь!", skin, "talking");
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
        Boolean isFlip  = (Boolean) properties.get("flip");
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
