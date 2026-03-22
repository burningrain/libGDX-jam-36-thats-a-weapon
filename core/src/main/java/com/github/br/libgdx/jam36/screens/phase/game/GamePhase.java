//package com.github.br.libgdx.jam36.screens.phase.game;
//
//import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
//import com.github.br.libgdx.jam36.context.GameContext;
//import com.github.br.libgdx.jam36.screens.phase.Phase;
//
//public class GamePhase implements Phase {
//
//    private GameContext gameContext;
//    private CustomOrthogonalTiledMapRenderer renderer;
//
//    private Watch watch;
//
//    private GamePhaseManager phaseManager;
//
//    public GamePhase(Watch watch) {
//        this.watch = watch;
//    }
//
//    @Override
//    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
//        this.gameContext = gameContext;
//        this.renderer = renderer;
//        phaseManager = new GamePhaseManager(renderer, createInnerGameContext(gameContext));
//        phaseManager.initFirstPhase();
//    }
//
//    @Override
//    public void draw(float deltaTime) {
//        watch.update(deltaTime);
//        phaseManager.act(deltaTime);
//    }
//
//    @Override
//    public boolean isFinished() {
//        boolean finished = phaseManager.isFinished();
//        if (finished) {
//            GameContext innerContext = finished.getInnerContext();
//            gameContext.setHeroStressLevel(innerContext.getHeroStressLevel());
//            gameContext.setHrStressLevel(innerContext.getHrStressLevel());
//        }
//        return finished;
//    }
//
//    private GameContext createInnerGameContext(GameContext parentContext) {
//        GameContext newGameContext = new GameContext();
//        newGameContext.setHeroStressLevel(parentContext.getHeroStressLevel());
//        newGameContext.setHrStressLevel(parentContext.getHrStressLevel());
//        newGameContext.setCurrentPhase(0);
//        newGameContext.setPhases(createPhases());
//
//        return newGameContext;
//    }
//
//}
