package com.github.br.libgdx.jam36.screens.phase.predicate;

import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.screens.phase.Phase;

public class PredicatePhase implements Phase {

    private final PhasePredicate predicate;
    private final Phase phase;

    private boolean isFinished;

    public PredicatePhase(PhasePredicate predicate, Phase phase) {
        this.predicate = predicate;
        this.phase = phase;
    }


    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        if (!predicate.isCanBeApplied(gameContext)) {
            isFinished = true;
        } else {
            phase.initUI(gameContext, renderer);
        }
    }

    @Override
    public void draw(float deltaTime) {
        if (isFinished) {
            return;
        }

        phase.draw(deltaTime);
    }

    @Override
    public boolean isFinished() {
        if (isFinished) {
            return true;
        }

        return phase.isFinished();
    }

}
