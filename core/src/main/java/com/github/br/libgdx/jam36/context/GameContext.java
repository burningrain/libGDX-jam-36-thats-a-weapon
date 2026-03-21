package com.github.br.libgdx.jam36.context;

public class GameContext {

    public interface Listener {
        void update(GameContext context);
    }

    private final TabletContext tabletContext = new TabletContext();

    public TabletContext getTabletContext() {
        return tabletContext;
    }



    // listener
    private Listener listener;

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void notifyListener() {
        listener.update(this);
    }

}
