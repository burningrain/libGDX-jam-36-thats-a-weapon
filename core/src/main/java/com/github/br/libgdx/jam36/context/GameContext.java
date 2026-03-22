package com.github.br.libgdx.jam36.context;

import com.badlogic.gdx.utils.Array;
import com.github.br.libgdx.jam36.screens.phase.Phase;

public class GameContext {

    private boolean gameOverAndNeedChangePhases;
    private PhoneContext phoneContext;
    private TabletContext tabletContext;
    private Array<Phase> currentPhases;

    private Array<Phase> phases;
    private Array<Phase> gameOverPhases;
    private int currentPhase = 0;

    private EventsBlock eventsBlock = new EventsBlock();
    private boolean needToShowRules;
    private int difficultyLevel;

    public GameContext() {
        phoneContext = new PhoneContext();
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

    public PhoneContext getPhoneContext() {
        return phoneContext;
    }

    public void setPhoneContext(PhoneContext phoneContext) {
        this.phoneContext = phoneContext;
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

    public EventsBlock getEventsBlock() {
        return eventsBlock;
    }

    public void setEventsBlock(EventsBlock eventsBlock) {
        this.eventsBlock = eventsBlock;
    }

    public void setNeedToShowRules(boolean needToShowRules) {
        this.needToShowRules = needToShowRules;
    }

    public boolean isNeedToShowRules() {
        return needToShowRules;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
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

}
