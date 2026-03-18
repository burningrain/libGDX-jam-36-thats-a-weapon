package structure.screen.statemachine;

import structure.screen.AbstractGameScreen;
import structure.screen.loading.AssetsLoader;

public class GameScreenState {

    public final AbstractGameScreen screen;
    public final AssetsLoader assetsLoader;

    public GameScreenState(AbstractGameScreen screen, AssetsLoader assetsLoader) {
        this.screen = screen;
        this.assetsLoader = assetsLoader;
    }

}
