//package com.github.br.libgdx.jam36.screens.phase.game;
//
//import com.badlogic.gdx.utils.Array;
//import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
//import com.github.br.libgdx.jam36.context.GameContext;
//import com.github.br.libgdx.jam36.screens.phase.Phase;
//
//public class GamePhaseManager {
//
//    private final CustomOrthogonalTiledMapRenderer renderer;
//
//    private GameContext gameContext;
//    private boolean isFinished = false;
//
//    public GamePhaseManager(CustomOrthogonalTiledMapRenderer renderer, GameContext gameContext) {
//        this.renderer = renderer;
//        this.gameContext = gameContext;
//    }
//
//    public void act(float deltaTime) {
//        int currentPhase = gameContext.getCurrentPhase();
//        Array<Phase> currentPhases = gameContext.getCurrentPhases();
//        Phase phase = currentPhases.get(currentPhase);
//
//        phase.draw(deltaTime);
//        if (phase.isFinished()) {
//            if (gameContext.isGameOverAndNeedChangePhases()) {
//                gameContext.setGameOverAndNeedChangePhases(false);
//                gameContext.setCurrentPhases(gameContext.getGameOverPhases());
//                gameContext.setCurrentPhase(0);
//
//                Phase nextPhase = gameContext.getCurrentPhases().get(gameContext.getCurrentPhase());
//                nextPhase.initUI(gameContext, renderer);
//            } else {
//                currentPhase++;
//                gameContext.setCurrentPhase(currentPhase);
//                Phase nextPhase = gameContext.getCurrentPhases().get(currentPhase);
//                nextPhase.initUI(gameContext, renderer);
//            }
//        }
//    }
//
//    public void initFirstPhase() {
//        gameContext.setCurrentPhases(gameContext.getPhases());
//        Array<Phase> currentPhases = gameContext.getCurrentPhases();
//        Phase phase = currentPhases.get(0);
//        phase.initUI(gameContext, renderer);
//    }
//
//    public boolean isFinished() {
//        return isFinished;
//    }
//
//}
