package com.github.br.libgdx.jam36.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.libgdx.jam36.Constants;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.Resources;
import com.github.br.libgdx.jam36.screens.phase.*;
import structure.screen.AbstractGameScreen;

public class MenuScreen extends AbstractGameScreen {

    private TiledMap tiledMap;
    private CustomOrthogonalTiledMapRenderer renderer;

    private OrthographicCamera camera;
    private Viewport viewport;

    private ActorFactory actorFactory;

    private PhaseManager phaseManager;
    private GameContext gameContext;

    @Override
    public void show() {
        AssetManager assetManager = getGameManager().assetManager;
        tiledMap = assetManager.get(Resources.MENU);

        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        centerCamera();

        Skin gameSkin = assetManager.get(Resources.SKIN);
        actorFactory = new ActorFactory(gameSkin, assetManager);
        renderer = new CustomOrthogonalTiledMapRenderer(actorFactory, viewport, tiledMap, 1f);

        gameContext = new GameContext();
        phaseManager = new PhaseManager(gameContext, renderer);
        initPhases(phaseManager);

        Gdx.input.setInputProcessor(renderer.getInputProcessor()); //TODO должна переехать в фазы конкретные
    }

    private void initPhases(PhaseManager phaseManager) {
        phaseManager.addPhase(new ChangeContextPhase(gameContext -> {
            gameContext.getTabletContext().setPages(
                1,
                "это первая страница",
                "это вторая страница",
                "это третья страница",
                "это четвертая страница"
            );
        }));

        phaseManager.addPhase(new TabletPhase()); // выбор языка, имени-фамилии и прочее
        phaseManager.addPhase(new HrDialogPhase());
//        phaseManager.addPhase(new HrMeetingPhase());
//        phaseManager.addPhase(new ThoughtPhase());

        phaseManager.initFirstPhase();
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

        phaseManager.act(delta);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        renderer.resize(width, height);
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
