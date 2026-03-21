package com.github.br.libgdx.jam36.screens.phase;

import com.badlogic.gdx.utils.Array;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;

public class PhaseManager {

    private final GameContext gameContext;
    private final CustomOrthogonalTiledMapRenderer renderer;

    private final Array<Phase> phases = new Array<>();
    private int currentPhase = 0;

    public PhaseManager(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        this.gameContext = gameContext;
        this.renderer = renderer;
    }

    public void act(float deltaTime) {
        Phase phase = phases.get(currentPhase);

        phase.draw(deltaTime);
        if (phase.isFinished()) {
            currentPhase++;
            Phase nextPhase = phases.get(currentPhase);
            nextPhase.initUI(gameContext, renderer);
        }
    }

    public void addPhase(Phase phase) {
        phases.add(phase);
    }

    public void initFirstPhase() {
        Phase phase = phases.get(0);
        phase.initUI(gameContext, renderer);
    }

}
