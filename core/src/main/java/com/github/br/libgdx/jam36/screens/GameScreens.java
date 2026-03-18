package com.github.br.libgdx.jam36.screens;


import structure.screen.statemachine.GameScreenState;

public interface GameScreens {

    GameScreenState MENU = new GameScreenState(new MenuScreen(), new MenuAssetLoader());
    //GameScreenState LEVEL_1 = new GameScreenState(new Level1Screen(), new Level1AssetLoader());

}
