package com.github.br.libgdx.jam36.screens;


import com.github.br.libgdx.common.structure.screen.statemachine.GameScreenState;
import com.github.br.libgdx.jam36.Resources;

public interface GameScreens {

    GameScreenState MENU = new GameScreenState(
        new MainScreen(Resources.SKIN, true), new MainAssetLoader()
    );
    //GameScreenState LEVEL_1 = new GameScreenState(new Level1Screen(), new Level1AssetLoader());

}
