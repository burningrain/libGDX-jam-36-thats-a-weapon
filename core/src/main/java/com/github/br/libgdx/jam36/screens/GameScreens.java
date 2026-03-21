package com.github.br.libgdx.jam36.screens;


import structure.screen.statemachine.GameScreenState;

public interface GameScreens {

    GameScreenState MENU = new GameScreenState(new MainScreen(), new MainAssetLoader());
    //GameScreenState LEVEL_1 = new GameScreenState(new Level1Screen(), new Level1AssetLoader());

}
