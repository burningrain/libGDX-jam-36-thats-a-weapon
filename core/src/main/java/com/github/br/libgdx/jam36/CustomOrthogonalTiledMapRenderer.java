package com.github.br.libgdx.jam36;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.libgdx.jam36.screens.ActorFactory;

import java.util.Iterator;

public class CustomOrthogonalTiledMapRenderer extends OrthogonalTiledMapRenderer {

    private final ObjectMap<String, Stage> stages = new ObjectMap<>();
    private final Viewport viewport;
    private final ActorFactory actorFactory;
    private final InputMultiplexer inputMultiplexer;

    public CustomOrthogonalTiledMapRenderer(
        ActorFactory actorFactory, Viewport viewport, TiledMap map, float unitScale
    ) {
        super(map, unitScale);
        this.viewport = viewport;
        this.actorFactory = actorFactory;

        clampToEdgeTextures(map);

        inputMultiplexer = new InputMultiplexer();
        createStageActors(inputMultiplexer, map.getLayers());
    }

    @Override
    public void renderObjects(MapLayer layer) {
        String name = layer.getName();
        Stage stage = stages.get(name);
        if (stage == null) {
            throw new IllegalArgumentException("stage [" + name + "] is not found");
        }

        // ЗАКРЫВАЕМ батч рендерера
        getBatch().flush();
        endRender(); // закрывает batch рендерера

        // Рендерим Stage
        stage.getViewport().apply();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        // Снова открываем батч рендерера
        beginRender();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        for (Stage stage : stages.values()) {
            stage.getViewport().update(width, height, true);
        }
    }

    private void clampToEdgeTextures(TiledMap tiledMap) {
        for (MapLayer layer : tiledMap.getLayers()) {
            if (layer instanceof TiledMapImageLayer) {
                ((TiledMapImageLayer) layer).getTextureRegion().getTexture().setWrap(
                    Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge
                );
            }
        }
    }

    private void createStageActors(InputMultiplexer inputMultiplexer, MapLayers mapLayers) {
        for (MapLayer layer : mapLayers) {
            if (layer instanceof MapGroupLayer) {
                MapLayers layers = ((MapGroupLayer) layer).getLayers();
                createStageActors(inputMultiplexer, layers);
            }

            MapObjects objects = layer.getObjects();
            if (objects == null || objects.getCount() == 0) {
                continue;
            }

            Stage stage = new Stage(viewport, getBatch());
            for (MapObject object : objects) {
                Actor actor = actorFactory.getActor(object);
                MapProperties properties = object.getProperties();
                float x = properties.get("x", float.class);
                float y = properties.get("y", float.class);
                float width = properties.get("width", Float.class);
                float height = properties.get("height", Float.class);
                actor.setBounds(x, y, width, height);

                stage.addActor(actor);
            }

            stages.put(layer.getName(), stage);
            inputMultiplexer.addProcessor(stage);
        }
    }

    public InputProcessor getInputProcessor() {
        return inputMultiplexer;
    }

}
