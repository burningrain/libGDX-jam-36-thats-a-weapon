package com.github.br.libgdx.jam36.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.libgdx.jam36.Constants;
import com.github.br.libgdx.jam36.Resources;
import structure.screen.AbstractGameScreen;

public class MenuScreen extends AbstractGameScreen {

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Viewport viewport;

    @Override
    public void show() {
        tiledMap = getGameManager().assetManager.get(Resources.MENU);

        for (MapLayer layer : tiledMap.getLayers()) {
            if (layer instanceof TiledMapImageLayer) {
                ((TiledMapImageLayer) layer).getTextureRegion().getTexture().setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
            }
        }

        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        centerCamera();

        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1f);
    }

    private void centerCamera() {
        camera.position.set(Constants.WORLD_WIDTH / 2f, Constants.WORLD_HEIGHT / 2f, 0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        renderer.setView(camera);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        // Всегда центрируем на контенте
        centerCamera();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
