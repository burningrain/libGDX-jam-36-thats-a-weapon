package com.github.br.libgdx.jam36.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.libgdx.jam36.Constants;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.Resources;
import com.github.br.libgdx.jam36.context.EventsBlock;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.context.TabletContext;
import com.github.br.libgdx.jam36.screens.phase.*;
import com.github.br.libgdx.jam36.screens.phase.game.Watch;
import com.github.br.libgdx.jam36.screens.phase.hell.CloseHellDoorPhase;
import com.github.br.libgdx.jam36.screens.phase.hell.GoFromTheHellPhase;
import com.github.br.libgdx.jam36.screens.phase.hell.GoToTheHellPhase;
import com.github.br.libgdx.jam36.screens.phase.hell.OpenHellDoorPhase;
import com.github.br.libgdx.jam36.screens.phase.hr.HideHrPhase;
import com.github.br.libgdx.jam36.screens.phase.hr.ShowHrPhase;
import com.github.br.libgdx.jam36.screens.phase.mind.MindChooserPhase;
import com.github.br.libgdx.jam36.screens.phase.phone.*;
import com.github.br.libgdx.jam36.screens.phase.predicate.PredicatePhase;
import com.github.br.libgdx.jam36.screens.phase.stress.ShowStressLevelsPhase;
import com.github.br.libgdx.jam36.screens.phase.tea.ShowTeaPhase;
import com.github.br.libgdx.jam36.ui.AnimatedImage;
import structure.screen.AbstractGameScreen;

public class MainScreen extends AbstractGameScreen {

    private TiledMap tiledMap;
    private CustomOrthogonalTiledMapRenderer renderer;

    private OrthographicCamera camera;
    private Viewport viewport;

    private ActorFactory actorFactory;

    private PhaseManager phaseManager;
    private GameContext gameContext;

    private Watch watch;

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

        // часы
        Image arrow = renderer.getActor(TiledLayers.ACTORS_LAYER_WATCH, StageActors.WATCH_ARROW, Image.class);
        // Устанавливаем точку вращения в нижний центр
        arrow.setOrigin(arrow.getWidth() / 2f, 0);

        AnimatedImage weekDay = renderer.getActor(TiledLayers.ACTORS_LAYER_WATCH, StageActors.WEEK_DAY, AnimatedImage.class);
        watch = new Watch(arrow, weekDay);
        watch.setTime(14.5f); // игра начинается в 14:30 дня


        // игровой движок
        gameContext = createGameContext();
        phaseManager = new PhaseManager(renderer);
        phaseManager.initFirstPhase(gameContext);

        Gdx.input.setInputProcessor(renderer.getInputProcessor()); //TODO должна переехать в фазы конкретные
    }

    private GameContext createGameContext() {
        Array<Phase> phases = new Array<>();

        phases.add(new SetStartGamePhase());   // стартовые настройки сцены
        phases.add(new HideHrPhase());
        phases.add(new DelayPhase(2f));
        phases.add(new HeroBigPhone1CallPhase("юрист"));
        phases.add(new HeroBigPhone2UpPhase());
        phases.add(new DelayPhase(1f));
        phases.add(new HeroBigPhone3GetCallPhase());

        phases.add(new HrDialogPhase("Пока тебя не начали прессовать, скажи мне, ты знаешь правила?"));
        phases.add(
            new MindChooserPhase(
                actorFactory,
                (answer, gameContext) -> {
                    if (answer == 2) {
                        gameContext.setNeedToShowRules(true);
                    }
                },
                new Choose(1, "Да"),
                new Choose(2, "Нет")
            ));
        // если нужно рассказать правила
        phases.add(new PredicatePhase(
            gameContext -> gameContext.isNeedToShowRules(),
            new HrDialogPhase("Ты там уже пару лет. По закону у них ничего на тебя нет. Они будут давить " +
                "психологически"))
        );
        phases.add(new PredicatePhase(
            gameContext -> gameContext.isNeedToShowRules(),
            new HrDialogPhase("Контроль рабочего графика, тесты на профессиональную пригодность и прочая грязь"))
        );
        phases.add(new PredicatePhase(
            gameContext -> gameContext.isNeedToShowRules(),
            new HrDialogPhase("В этой битве у тебя тоже есть оружие, не забывай об этом!")
        ));
        phases.add(new PredicatePhase(
            gameContext -> gameContext.isNeedToShowRules(),
            new HrDialogPhase("фиксируй их нарушения на аудиоустройство. Дело будет выиграно, нужно просто собрать базу")
        ));
        phases.add(new PredicatePhase(
            gameContext -> gameContext.isNeedToShowRules(),
            new MindChooserPhase(
                actorFactory,
                (answer, gameContext) -> {
                },
                new Choose(1, "И это мое оружие?"),
                new Choose(2, "Мне нужно\n ТЕРПЕТЬ?!")
            )
        ));
        phases.add(new PredicatePhase(
            gameContext -> gameContext.isNeedToShowRules(),
            new HrDialogPhase("Именно так. Запомни главное: ничего не подписывай! Бездействие - это тоже действие!")
        ));

        // если отказывается от правил
        phases.add(new PredicatePhase(
            gameContext -> !gameContext.isNeedToShowRules(),
            new HrDialogPhase("Значит сэкономим время"))
        );
        //
        phases.add(new HrDialogPhase("Теперь выбери уровень сложности"));
        phases.add(new MindChooserPhase(
                actorFactory,
                (answer, gameContext) -> {
                    // уровень сложности меняет время, отведенное на ответ
                    gameContext.setDifficultyLevel(answer);
                    watch.setSecondsPerHour(watch.getSecondsPerHour() / gameContext.getDifficultyLevel());
                },
                new Choose(1, "Дружеская беседа"),
                new Choose(2, "Спортивное состязание"),
                new Choose(4, "Сдирание кожи\n заживо")
            )
        );

        phases.add(new HeroBigPhone3CancelCallPhase());
        phases.add(new HeroBigPhone4DownPhase());

        phases.add(new ShowHrPhase());
        phases.add(new HrDialogPhase("Привет, заждался? А я к тебе! Обхожу весь отдел с обновлениями документов. Глянь"));
        phases.add(new ChangeContextPhase(context -> {
            TabletContext tabletContext = context.getTabletContext();
            tabletContext.setPages(
                0,
                "Вы подписываете следующие документы:\n" +
                    "\n- Уточнения в политике информационной безопасности" +
                    "\n- Согласие на участие в корпоративной лотерее" +
                    "\n- Дополнения в правилах внутреннего трудового распорядка" +
                    "\n- Изменения в кодексе деловой этики" +
                    "\n- Дополнения в политике конфликта интересов" +
                    "\n- Положение о цвете носков и галстуков"
                ,
                "- Соглашение о неразглашении неразглашения о соглашении" +
                    "\n- Положение по надлежащему завершению рабочего дня" +
                    "\n- Запрет на установку обоев рабочего стола, не прошедших утверждение арт-директором" +
                    "\n- Акт о невозвратном потреблении печенек из корпоративной кладовки" +
                    "\n- Согласие на слежку через корпоративную веб-камеру" +
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
        TabletPhase signDocuments = new TabletPhase(gameContext -> {
            gameContext.getEventsBlock().setDocumentsSign(true);
        });
        phases.add(new TimerPhase(
            signDocuments,
            15f,
            gameContext -> {
                signDocuments.isNeedToMove = true;
                signDocuments.isMoveToUp = false;
            }));

        // если подписал, то все кончено
        phases.add(new PredicatePhase(
            gameContext -> gameContext.getEventsBlock().isDocumentsSign(),
            new HrDialogPhase("Подписал по собственному? Ну какой же ты молодец! Я думала, будет намного сложнее!"))
        );
        phases.add(new PredicatePhase(
            gameContext -> gameContext.getEventsBlock().isDocumentsSign(),
            new GameOverPhase()
        ));

        // если не подписал, то говорит, что не будет ничего подписывать
        phases.add(new HrDialogPhase("Ой, ты отложил документы? Что-то не так?"));
        phases.add(
            new MindChooserPhase(
                actorFactory,
                (answer, gameContext) -> {
                },
                new Choose(1, "Я не буду ничего\nподписывать"),
                new Choose(2, "Мне нужно\nпосоветоваться\nс юристом"),
                new Choose(3, "Сперва покажу\nэти бумаги\nсвоему коту!")
            ));
        phases.add(new HrDialogPhase("Тогда я вынуждена сообщить, что это отказ сотрудничать с твоей стороны!"));
        phases.add(new HrDialogPhase("Ты сегодня во сколько пришел на работу? Впрочем неважно. Ознакомься со своим новым расписанием"));
        phases.add(new ChangeContextPhase(context -> {
            TabletContext tabletContext = context.getTabletContext();
            tabletContext.setPages(
                0,
                "Понедельник: 9:00 - 18:00" +
                    "\nВторник: 10:00 - 19:00" +
                    "\nСреда: 11:00 - 20:00" +
                    "\nЧетверг: 9:30 - 18:30" +
                    "\nПятница: 10:30 - 19:30"
            );
            tabletContext.setSignButtonText("Ознакомиться\nи подписать");
        }));
        TabletPhase workCalendarTablet = new TabletPhase(gameContext -> {});
        phases.add(workCalendarTablet);

        //phases.add(new GamePhase(watch));
        //phases.add(new ShowStressLevelsPhase());
        phases.add(new HrDialogPhase("Давай начнем твое тестирование на знание внутренних трудовых распорядков! Ознакомься!!!"));
        phases.add(new ChangeContextPhase(context -> {
            TabletContext tabletContext = context.getTabletContext();
            tabletContext.setPages(
                0,
                "РАСПОРЯЖЕНИЯ НАЧАЛЬСТВА\n" +
                    "\n1. Все сотрудники обязаны пройти тест на знание миссии компании. Пересдача — за свой счет (удержание из зарплаты). " +
                    "Третья пересдача — вопрос о соответствии занимаемой должности" +
                    "\n2. Все созвоны начинаются с уважения к занятости генерального директора" +
                    "\n2. Запрещается использовать слово 'проблема'" +
                    "\n3. Все сотрудники обязаны приходить на работу за 15 минут до начала рабочего дня"
                ,
                "6. Для личных звонков выделена специальная комната №317, оборудованная стеклянными стенами и микрофоном" +
                    "\n4. Запрещается обсуждать зарплаты" +
                    "\n5. Каждый сотрудник обязан иметь на рабочем столе не менее трех и не более пяти предметов корпоративного мерча" +
                    "\n6. День рождения сотрудника больше не является поводом для поздравлений в рабочее время" +
                    "\n7. Каждый сотрудник обязан раз в квартал заполнить бензобак начальства за свой счет"
                ,
                "8. В опенспейсе запрещены разговоры громче 65 децибел" +
                    "\n9. Каждый сотрудник раз в две недели заполняет форму наблюдения за тремя случайно назначенными коллегами" +
                    "\n10. Каждый сотрудник раз в две недели заполняет форму наблюдения за тремя случайно назначенными коллегами" +
                    "\n11. Кофе в кулере заменен на цикорий" +
                    "\n12. Каждый сотрудник обязан указать, сколько раз он усомнился в решениях руководства (с точностью до раза)"
            );
            tabletContext.setSignButtonText("Ознакомиться\nи подписать");
        }));
        TabletPhase test1 = new TabletPhase(gameContext -> {
            gameContext.getEventsBlock().setDocumentsSign(true);
        });
        phases.add(new TimerPhase(
            test1,
            15f,
            gameContext -> {
            }));

        // 2 день
        phases.add(new ShowTeaPhase());
        phases.add(new HrDialogPhase("Пойми, я прекрасно понимаю тебя! Это всегда очень тяжело...так тяжело! " +
            "Поэтому я и принесла нам чай. Будешь?"));
        phases.add(new MindChooserPhase(
                actorFactory,
                (answer, gameContext) -> {
                    EventsBlock eventsBlock = gameContext.getEventsBlock();
                    if (answer == 1 || answer == 4) {
                        eventsBlock.setAlcoholDrinker(true);
                    }
                },
                new Choose(1, "Буду,\nблагодарю!"),
                new Choose(2, "Спасибо,\nно нет"),
                new Choose(3, "Я отказываюсь"),
                new Choose(4, "Давайте")
            )
        );

        // если согласился пить чай
        phases.add(new PredicatePhase(
                gameContext -> gameContext.getEventsBlock().isAlcoholDrinker(),
                new HrDialogPhase("Ну как, нравится?")
            )
        );
        phases.add(new PredicatePhase(
                gameContext -> gameContext.getEventsBlock().isAlcoholDrinker(),
                new MindChooserPhase(
                    actorFactory,
                    (answer, gameContext) -> {
                        EventsBlock eventsBlock = gameContext.getEventsBlock();
                        if (answer == 1 || answer == 4) {
                            eventsBlock.setAlcoholDrinker(true);
                        }
                    },
                    new Choose(1, "Это что-то\nалкогольное?"),
                    new Choose(2, "Этот чай с коньяком??")
                )
            )
        );
        phases.add(new PredicatePhase(
                gameContext -> gameContext.getEventsBlock().isAlcoholDrinker(),
                new HrDialogPhase("Конечно да! Разумеется! И очень успокаивает нервы, знаешь ли!")
            )
        );
        phases.add(new PredicatePhase(
                gameContext -> gameContext.getEventsBlock().isAlcoholDrinker(),
                new HrDialogPhase(
                    "Однако это серьезное нарушение! И я уже составила на тебя акт и приказ. Подпиши, пожалуйста!"
                )
            )
        );
        phases.add(new PredicatePhase(
                gameContext -> gameContext.getEventsBlock().isAlcoholDrinker(),
                new MindChooserPhase(
                    actorFactory,
                    (answer, gameContext) -> {
                    },
                    new Choose(1, "НО ВЕДЬ ВЫ ЖЕ САМИ ПЬЯНЫ!")
                )
            )
        );
        phases.add(new PredicatePhase(
                gameContext -> gameContext.getEventsBlock().isAlcoholDrinker(),
                new HrDialogPhase(
                    "ДА, НО ВЕДЬ НЕ МЕНЯ ЖЕ ЗДЕСЬ УВОЛЬНЯЮТ! На, подписывай!"
                )
            )
        );
        phases.add(new PredicatePhase(
                gameContext -> gameContext.getEventsBlock().isAlcoholDrinker(),
                new GameOverPhase()
            )
        );

        // если отказался пить чай
        phases.add(new HrDialogPhase(
            "Мне надоели эти игры... тогда подписывай расторжение по соглашению сторон!")
        );
        phases.add(new GoToTheHellPhase());
        phases.add(new HrDialogPhase(
            "Если хочешь остаться работать, подписывай!")
        );
        phases.add(new HrDialogPhase(
            "Кому говорю, подписывай!")
        );
        phases.add(new OpenHellDoorPhase());
        phases.add(new HrDialogPhase(
            "Эй, красавица, пошли на обед!")
        );
        phases.add(new HrDialogPhase(
            "Видишь, я ничего не успеваю доделать!!! Давай тогда будем считать, что ты уволен!")
        );
        phases.add(new CloseHellDoorPhase());
        phases.add(new HrDialogPhase(
            "Спасибо за игру!")
        );
        phases.add(new GameOverPhase());


        // GAME OVER, игра проиграна!
        Array<Phase> gameOverPhases = new Array<>();
        gameOverPhases.add(new HrDialogPhase(
            "Что ж, поздравляю тебя с увольнением! И не забудь про обходной лист! Желаю удачи и всего наилучшего!")
        );
        gameOverPhases.add(new GameOverStatisticsPhase()); // заполнение экрана статистики
        gameOverPhases.add(new TabletPhase(gameContext -> { // рестарт игры
            // do nothing
        }));
        gameOverPhases.add(
            new ChangeContextPhase(gameContext -> {
                // скрыть столик с чаем
                MapLayer layer = renderer.getLayer(TiledLayers.TEA);
                layer.setVisible(false);
            })
        );
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
