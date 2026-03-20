package com.github.br.libgdx.jam36.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.br.libgdx.jam36.Resources;
import structure.screen.loading.AssetsLoader;

public class MenuAssetLoader implements AssetsLoader {

    @Override
    public void loadAssets(AssetManager assetManager) {
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();

        // Трилинейная фильтрация для убирания лесенок из картинок
        params.textureMinFilter = Texture.TextureFilter.Linear;
        params.textureMagFilter = Texture.TextureFilter.Linear;
        assetManager.load(Resources.MENU, TiledMap.class, params);

        // skin
        assetManager.load(Resources.SKIN_ATLAS, TextureAtlas.class);
        assetManager.load(Resources.SKIN, Skin.class, new SkinLoader.SkinParameter(Resources.SKIN_ATLAS));

        // animation
        assetManager.load(Resources.ANIMATION_ATLAS, TextureAtlas.class);
    }

    @Override
    public void unloadAssets(AssetManager assetManager) {

    }

}
