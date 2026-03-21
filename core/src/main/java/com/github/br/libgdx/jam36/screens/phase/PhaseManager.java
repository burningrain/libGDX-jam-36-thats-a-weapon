package com.github.br.libgdx.jam36.screens.phase;

import com.badlogic.gdx.utils.Array;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;

public class PhaseManager {

    private final CustomOrthogonalTiledMapRenderer renderer;

    public PhaseManager(CustomOrthogonalTiledMapRenderer renderer) {
        this.renderer = renderer;
    }

    public void act(GameContext gameContext, float deltaTime) {
        int currentPhase = gameContext.getCurrentPhase();
        Array<Phase> currentPhases = gameContext.getCurrentPhases();
        Phase phase = currentPhases.get(currentPhase);

        phase.draw(deltaTime);
        if (phase.isFinished()) {
            if (gameContext.isGameOverAndNeedChangePhases()) {
                gameContext.setGameOverAndNeedChangePhases(false);
                gameContext.setCurrentPhases(gameContext.getGameOverPhases());
                gameContext.setCurrentPhase(0);

                Phase nextPhase = gameContext.getCurrentPhases().get(gameContext.getCurrentPhase());
                nextPhase.initUI(gameContext, renderer);
            } else {
                currentPhase++;
                gameContext.setCurrentPhase(currentPhase);
                Phase nextPhase = gameContext.getCurrentPhases().get(currentPhase);
                nextPhase.initUI(gameContext, renderer);
            }
        }
    }

    public void initFirstPhase(GameContext gameContext) {
        gameContext.setCurrentPhases(gameContext.getPhases());
        Array<Phase> currentPhases = gameContext.getCurrentPhases();
        Phase phase = currentPhases.get(0);
        phase.initUI(gameContext, renderer);
    }

}
