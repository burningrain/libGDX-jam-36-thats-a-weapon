package com.github.br.libgdx.jam36.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.libgdx.jam36.Constants;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.Resources;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.context.TabletContext;
import com.github.br.libgdx.jam36.screens.phase.*;
import com.github.br.libgdx.jam36.screens.phase.predicate.PredicatePhase;
import structure.screen.AbstractGameScreen;

public class MainScreen extends AbstractGameScreen {

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

        gameContext = createGameContext();


        phaseManager = new PhaseManager(renderer);
        phaseManager.initFirstPhase(gameContext);

        Gdx.input.setInputProcessor(renderer.getInputProcessor()); //TODO должна переехать в фазы конкретные
    }

    private GameContext createGameContext() {
        Array<Phase> phases = new Array<>();

        phases.add(new HrDialogPhase("Раз уж пересеклись, подпиши уже, пожалуйста, свои документы"));
        phases.add(new ChangeContextPhase(context -> {
            TabletContext tabletContext = context.getTabletContext();
            tabletContext.setPages(
                0,
                "Вы подписываете следующие документы:\n" +
                    "\n- Политика информационной безопасности" +
                    "\n- Согласие на участие в корпоративной лотерее" +
                    "\n- Дополнения в правилах внутреннего трудового распорядка" +
                    "\n- Изменения в кодексе деловой этики" +
                    "\n- Дополнения в политике конфликта интересов" +
                    "\n- Положение о цвете носков и галстуков"
                ,
                "- Согласие на слежку через корпоративную веб-камеру" +
                    "\n- Положение по надлежащему завершению рабочего дня" +
                    "\n- Запрет на установку обоев рабочего стола, не прошедших утверждение арт-директором" +
                    "\n- Акт о невозвратном потреблении печенек из корпоративной кладовки" +
                    "\n- Соглашение о неразглашении рецепта кофе из кофемашины" +

                    "\n- Акт приема-передачи единственной удлиненной розетки (с правом наследования)"
                ,
                "- Должностная инструкция \"Специалист по синергии\"" +
                    "\n- Кодекс использования эмодзи в рабочем чате" +
                    "\n- Инструктаж по защите от сквозняков" +
                    "\n- Заявление на увольнение по собственному желанию" +
                    "\n- Памятка по противодействию страху в отчетах наверх" +
                    "\n- Запрет на использование RGB-подсветки клавиатуры в режиме \"дискотека\" после полуночи"
                ,
                "- Обязательство не кормить хомяков, на которых крутится сервер продакшена" +
                    "\n- Отказ от претензий по поводу нехватки розеток " +
                    "\n- Обязательство не коммитить код после 03:00 без подтверждения дежурным психиатром" +
                    "\n- Акт приема-передачи бессонницы (приложение к график-менеджменту)" +
                    "\n- Материальная ответственность за настроение начальства" +
                    "\n- Обязательство не приносить в офис настольные игры длиннее 30 минут"
            );
            tabletContext.setSignButtonText("Ознакомиться\nи подписать");
        }));

        phases.add(new TabletPhase()); // выбор языка, имени-фамилии и прочее
        phases.add(new HrDialogPhase("А знаешь что? Давай мы начнем заново. Будешь чай?"));
        phases.add(new MindChooserPhase(
                actorFactory,
                new Choose(1, "Буду,\nблагодарю!"),
                new Choose(2, "Спасибо,\nне нужно"),
                new Choose(3, "Я отказываюсь"),
                new Choose(4, "Сразу после вас")
            )
        );
        phases.add(new GoToTheHellPhase());
        phases.add(new GameOverPhase());


//        phaseManager.addPhase(new HrMeetingPhase());
//        phaseManager.addPhase(new ThoughtPhase());


        Array<Phase> gameOverPhases = new Array<>();
        gameOverPhases.add(new HrDialogPhase(
            "Боюсь, что теперь ты уволен! И не забудь подписать обходной лист! Пока.")
        );
        gameOverPhases.add(new ChangeContextPhase(context -> {
            TabletContext tabletContext = context.getTabletContext();
            tabletContext.setPages(
                0,
                "Дата увольнения: {такой-то} день\nПричина увольнения: {такая-то}"
            );
            tabletContext.setSignButtonText("Начать заново?");
        }));
        gameOverPhases.add(new TabletPhase()); // рестарт игры
        gameOverPhases.add(new PredicatePhase(
            gameContext -> gameContext.getEventsBlock().isInTheHell(),
            new GoFromTheHellPhase())
        );
        gameOverPhases.add(new ChangeContextPhase(context -> {
            MainScreen.this.gameContext = createGameContext();
            phaseManager.initFirstPhase(gameContext);
        }));

        GameContext gameContext = new GameContext();
        gameContext.setPhases(phases);
        gameContext.setGameOverPhases(gameOverPhases);

        return gameContext;
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

        phaseManager.act(gameContext, delta);
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
