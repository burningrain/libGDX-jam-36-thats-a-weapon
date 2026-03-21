package com.github.br.libgdx.jam36.context;

import com.badlogic.gdx.utils.Array;
import com.github.br.libgdx.jam36.screens.phase.Phase;

public class GameContext {

    private boolean gameOverAndNeedChangePhases;
    private TabletContext tabletContext;
    private Array<Phase> currentPhases;

    private Array<Phase> phases;
    private Array<Phase> gameOverPhases;
    private int currentPhase = 0;

    private EventsBlock eventsBlock = new EventsBlock();

    public GameContext() {
        tabletContext = new TabletContext();
    }

    public Array<Phase> getCurrentPhases() {
        return currentPhases;
    }

    public void setCurrentPhases(Array<Phase> currentPhases) {
        this.currentPhases = currentPhases;
    }

    public Array<Phase> getGameOverPhases() {
        return gameOverPhases;
    }

    public void setGameOverPhases(Array<Phase> gameOverPhases) {
        this.gameOverPhases = gameOverPhases;
    }

    public void setPhases(Array<Phase> phases) {
        this.phases = phases;
    }

    public Array<Phase> getPhases() {
        return phases;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(int currentPhase) {
        this.currentPhase = currentPhase;
    }

    public TabletContext getTabletContext() {
        return tabletContext;
    }

    public boolean isGameOverAndNeedChangePhases() {
        return gameOverAndNeedChangePhases;
    }

    public void setGameOverAndNeedChangePhases(boolean gameOverAndNeedChangePhases) {
        this.gameOverAndNeedChangePhases = gameOverAndNeedChangePhases;
    }

    // listener
    public interface Listener {
        void update(GameContext context);
    }

    private Array<Listener> listeners = new Array<>();

    public Array<Listener> getListeners() {
        return listeners;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.removeValue(listener, true);
    }

    public void notifyListener() {
        for (Listener listener : listeners) {
            listener.update(this);
        }
    }

    public EventsBlock getEventsBlock() {
        return eventsBlock;
    }

    public void setEventsBlock(EventsBlock eventsBlock) {
        this.eventsBlock = eventsBlock;
    }
}
