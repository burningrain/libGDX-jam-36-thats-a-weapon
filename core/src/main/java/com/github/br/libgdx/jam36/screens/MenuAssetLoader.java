package com.github.br.libgdx.jam36.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.github.br.libgdx.jam36.Resources;
import structure.screen.loading.AssetsLoader;

public class MenuAssetLoader implements AssetsLoader {

    @Override
    public void loadAssets(AssetManager assetManager) {
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();

        // Используем мип-мап фильтры для лучшего качества при уменьшении
        //params.generateMipMaps = true; // Включаем генерацию мип-мапов
        params.textureMinFilter = Texture.TextureFilter.Linear; // Трилинейная фильтрация
        params.textureMagFilter = Texture.TextureFilter.Linear;

        assetManager.load(Resources.MENU, TiledMap.class, params);
    }

    @Override
    public void unloadAssets(AssetManager assetManager) {

    }

}
