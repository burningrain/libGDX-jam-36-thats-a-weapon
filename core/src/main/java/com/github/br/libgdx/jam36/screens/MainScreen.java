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

        phases.add(new SetStartGamePhase());   // initial scene settings
        phases.add(new HideHrPhase());
        phases.add(new DelayPhase(2f));
        phases.add(new HeroBigPhone1CallPhase("lawyer"));
        phases.add(new HeroBigPhone2UpPhase());
        phases.add(new DelayPhase(1f));
        phases.add(new HeroBigPhone3GetCallPhase());

        phases.add(new HrDialogPhase("Before they start putting pressure on you, tell me, do you know the rules?"));
        phases.add(
            new MindChooserPhase(
                actorFactory,
                (answer, gameContext) -> {
                    if (answer == 2) {
                        gameContext.setNeedToShowRules(true);
                    }
                },
                new Choose(1, "Yes"),
                new Choose(2, "No")
            ));

        // if rules explanation is needed
        phases.add(new PredicatePhase(
            gameContext -> gameContext.isNeedToShowRules(),
            new HrDialogPhase("You've been there for a couple of years. Legally, they have nothing on you. They'll try to break you psychologically."))
        );
        phases.add(new PredicatePhase(
            gameContext -> gameContext.isNeedToShowRules(),
            new HrDialogPhase("Strict schedule monitoring, competency tests, and other dirty tricks."))
        );
        phases.add(new PredicatePhase(
            gameContext -> gameContext.isNeedToShowRules(),
            new HrDialogPhase("But you have weapons in this fight too, don't forget that!"))
        );
        phases.add(new PredicatePhase(
            gameContext -> gameContext.isNeedToShowRules(),
            new HrDialogPhase("Record their violations on an audio device. The case will be won; you just need to build a file."))
        );
        phases.add(new PredicatePhase(
            gameContext -> gameContext.isNeedToShowRules(),
            new MindChooserPhase(
                actorFactory,
                (answer, gameContext) -> {
                },
                new Choose(1, "And this is my weapon?"),
                new Choose(2, "I just have to\n ENDURE this?!")
            )
        ));
        phases.add(new PredicatePhase(
            gameContext -> gameContext.isNeedToShowRules(),
            new HrDialogPhase("Exactly. Remember the most important thing: sign nothing! Inaction is also an action!")
        ));

        // if refusing rules explanation
        phases.add(new PredicatePhase(
            gameContext -> !gameContext.isNeedToShowRules(),
            new HrDialogPhase("Then we'll save some time."))
        );

        phases.add(new HrDialogPhase("Now, choose your difficulty level."));
        phases.add(new MindChooserPhase(
                actorFactory,
                (answer, gameContext) -> {
                    // difficulty level changes time allotted for response
                    gameContext.setDifficultyLevel(answer);
                    watch.setSecondsPerHour(watch.getSecondsPerHour() / gameContext.getDifficultyLevel());
                },
                new Choose(1, "Friendly Chat"),
                new Choose(2, "Competitive Match"),
                new Choose(4, "Flayed\n Alive")
            )
        );

        phases.add(new HeroBigPhone3CancelCallPhase());
        phases.add(new HeroBigPhone4DownPhase());

        phases.add(new ShowHrPhase());
        phases.add(new HrDialogPhase("Hey, waiting for me? Here I am! Just going around the office with document updates. Take a look."));
        phases.add(new ChangeContextPhase(context -> {
            TabletContext tabletContext = context.getTabletContext();
            tabletContext.setPages(
                0,
                "You are signing the following documents:\n" +
                    "\n- Information security policy refinements" +
                    "\n- Consent to participate in the corporate lottery" +
                    "\n- Amendments to the internal labor regulations" +
                    "\n- Changes to the code of business ethics" +
                    "\n- Additions to the conflict of interest policy" +
                    "\n- Regulation on the color of socks and ties"
                ,
                "- Non-disclosure of the non-disclosure agreement agreement" +
                    "\n- Policy on the proper conclusion of the working day" +
                    "\n- Ban on setting desktop wallpapers not approved by the Art Director" +
                    "\n- Act on the non-refundable consumption of cookies from the corporate pantry" +
                    "\n- Consent to surveillance via corporate webcam" +
                    "\n- Handover act for the only extended power outlet (with inheritance rights)"
                ,
                "- Job description: \"Synergy Specialist\"" +
                    "\n- Code for emoji usage in the work chat" +
                    "\n- Instruction on protection against drafts" +
                    "\n- Voluntary resignation letter" +
                    "\n- Memo on counteracting fear in reports to superiors" +
                    "\n- Ban on using keyboard RGB-lighting in \"disco\" mode after midnight"
                ,
                "- Obligation not to feed the hamsters powering the production server" +
                    "\n- Waiver of claims regarding the lack of power outlets" +
                    "\n- Obligation not to commit code after 03:00 AM without psychiatric clearance" +
                    "\n- Handover act for insomnia (appendix to schedule management)" +
                    "\n- Financial liability for the management's mood" +
                    "\n- Obligation not to bring board games longer than 30 minutes into the office"
            );
            tabletContext.setSignButtonText("Review\nand Sign");
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

        // if signed, it's game over
        phases.add(new PredicatePhase(
            gameContext -> gameContext.getEventsBlock().isDocumentsSign(),
            new HrDialogPhase("You signed your own resignation? What a good boy! I thought this would be much harder!"))
        );
        phases.add(new PredicatePhase(
            gameContext -> gameContext.getEventsBlock().isDocumentsSign(),
            new GameOverPhase()
        ));

        // if not signed, character refuses to sign
        phases.add(new HrDialogPhase("Oh, you put the documents aside? Is something wrong?"));
        phases.add(
            new MindChooserPhase(
                actorFactory,
                (answer, gameContext) -> {
                },
                new Choose(1, "I'm not signing\nanything"),
                new Choose(2, "I need to\nconsult with\nmy lawyer"),
                new Choose(3, "I'll show these\npapers to my\ncat first!")
            ));
        phases.add(new HrDialogPhase("Then I must inform you that this is a refusal to cooperate on your part!"));
        phases.add(new HrDialogPhase("What time did you get to work today? Actually, never mind. Review your new schedule."));
        phases.add(new ChangeContextPhase(context -> {
            TabletContext tabletContext = context.getTabletContext();
            tabletContext.setPages(
                0,
                "Monday: 9:00 AM - 6:00 PM" +
                    "\nTuesday: 10:00 AM - 7:00 PM" +
                    "\nWednesday: 11:00 AM - 8:00 PM" +
                    "\nThursday: 9:30 AM - 6:30 PM" +
                    "\nFriday: 10:30 AM - 7:30 PM"
            );
            tabletContext.setSignButtonText("Review\nand Sign");
        }));
        TabletPhase workCalendarTablet = new TabletPhase(gameContext -> {});
        phases.add(workCalendarTablet);

        //phases.add(new GamePhase(watch));
        //phases.add(new ShowStressLevelsPhase());
        phases.add(new HrDialogPhase("Let's begin your internal labor regulations competency test! Review this!!!"));
        phases.add(new ChangeContextPhase(context -> {
            TabletContext tabletContext = context.getTabletContext();
            tabletContext.setPages(
                0,
                "MANAGEMENT DIRECTIVES\n" +
                    "\n1. All employees must pass a company mission test. Retakes are at your own expense (salary deduction). " +
                    "The third retake will raise the question of job suitability." +
                    "\n2. All calls must begin with a formal acknowledgment of the CEO's busy schedule." +
                    "\n2. Use of the word 'problem' is strictly prohibited." +
                    "\n3. All employees are required to arrive at work 15 minutes before the official start of the workday."
                ,
                "6. Room #317 is designated for personal calls; it is equipped with glass walls and a microphone." +
                    "\n4. Discussing salaries is strictly prohibited." +
                    "\n5. Each employee must have no fewer than three and no more than five items of corporate merch on their desk." +
                    "\n6. Employee birthdays are no longer an excuse for celebrations during working hours." +
                    "\n7. Each employee is required to fill the management's gas tank once a quarter at their own expense."
                ,
                "8. Conversations in the open space exceeding 65 decibels are prohibited." +
                    "\n9. Every two weeks, each employee must fill out a surveillance form for three randomly assigned colleagues." +
                    "\n10. Every two weeks, each employee must fill out a surveillance form for three randomly assigned colleagues." +
                    "\n11. Coffee in the breakroom has been replaced with chicory." +
                    "\n12. Each employee must state how many times they have doubted management's decisions (precise count required)."
            );
            tabletContext.setSignButtonText("Review\nand Sign");
        }));

        TabletPhase test1 = new TabletPhase(gameContext -> {
            gameContext.getEventsBlock().setDocumentsSign(true);
        });
        phases.add(new TimerPhase(
            test1,
            15f,
            gameContext -> {
            }));

        // Day 2
        phases.add(new ShowTeaPhase());
        phases.add(new HrDialogPhase("Look, I completely understand you! It's always so hard... so very hard! " +
            "That's why I brought us some tea. Would you like some?"));
        phases.add(new MindChooserPhase(
                actorFactory,
                (answer, gameContext) -> {
                    EventsBlock eventsBlock = gameContext.getEventsBlock();
                    if (answer == 1 || answer == 4) {
                        eventsBlock.setAlcoholDrinker(true);
                    }
                },
                new Choose(1, "I will,\nthank you!"),
                new Choose(2, "No,\nthank you"),
                new Choose(3, "I refuse"),
                new Choose(4, "Sure,\nwhy not")
            )
        );

        // если согласился пить чай
        phases.add(new PredicatePhase(
                gameContext -> gameContext.getEventsBlock().isAlcoholDrinker(),
                new HrDialogPhase("So, do you like it?")
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
                    new Choose(1, "Is this... \nalcohol?"),
                    new Choose(2, "Is there cognac \nin this tea??")
                )
            )
        );
        phases.add(new PredicatePhase(
                gameContext -> gameContext.getEventsBlock().isAlcoholDrinker(),
                new HrDialogPhase("Of course! Obviously! And it's very soothing for the nerves, you know!")
            )
        );
        phases.add(new PredicatePhase(
                gameContext -> gameContext.getEventsBlock().isAlcoholDrinker(),
                new HrDialogPhase(
                    "However, this is a serious violation! And I've already prepared the report and the order. Sign here, please!"
                )
            )
        );
        phases.add(new PredicatePhase(
                gameContext -> gameContext.getEventsBlock().isAlcoholDrinker(),
                new MindChooserPhase(
                    actorFactory,
                    (answer, gameContext) -> {
                    },
                    new Choose(1, "BUT YOU'RE DRUNK \nYOURSELF!")
                )
            )
        );
        phases.add(new PredicatePhase(
                gameContext -> gameContext.getEventsBlock().isAlcoholDrinker(),
                new HrDialogPhase(
                    "YES, BUT I'M NOT THE ONE GETTING FIRED HERE! Now, sign it!"
                )
            )
        );
        phases.add(new PredicatePhase(
                gameContext -> gameContext.getEventsBlock().isAlcoholDrinker(),
                new GameOverPhase()
            )
        );

        // if refused to drink tea
        phases.add(new HrDialogPhase(
            "I'm tired of these games... then sign the mutual separation agreement!")
        );
        phases.add(new GoToTheHellPhase());
        phases.add(new HrDialogPhase(
            "If you want to keep your job, sign it!")
        );
        phases.add(new HrDialogPhase(
            "I said SIGN IT!")
        );
        phases.add(new OpenHellDoorPhase());
        phases.add(new HrDialogPhase(
            "Hey, gorgeous, let's go to lunch!")
        );
        phases.add(new HrDialogPhase(
            "See, I can't get anything done because of you!!! Fine, let's just say you're fired!")
        );
        phases.add(new CloseHellDoorPhase());
        phases.add(new HrDialogPhase(
            "Thanks for playing!")
        );
        phases.add(new GameOverPhase());


        // GAME OVER
        Array<Phase> gameOverPhases = new Array<>();
        gameOverPhases.add(new HrDialogPhase(
            "Well, congratulations on your termination! And don't forget your exit clearance form! I wish you luck and all the best!")
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

        phaseManager.act(gameContext, delta);

        camera.update();
        renderer.setView(camera);
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
