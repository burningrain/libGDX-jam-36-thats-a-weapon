package com.github.br.libgdx.jam36.android;

import android.os.Bundle;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.crashinvaders.basisu.gdx.Ktx2TextureLoader;
import com.github.br.libgdx.jam36.Main;
import com.github.br.libgdx.jam36.PlatformConfigurator;

import workaround.ktx.tiled.Ktx2AtlasTmxMapLoader;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true; // Recommended, but not required.
        initialize(new Main(new PlatformConfigurator() {
            @Override
            public void configureLoaders(AssetManager assetManager, FileHandleResolver fileHandleResolver) {
                assetManager.setLoader(Texture.class, ".ktx2", new Ktx2TextureLoader(fileHandleResolver));
                assetManager.setLoader(TiledMap.class, new Ktx2AtlasTmxMapLoader(fileHandleResolver));
            }
        }), configuration);
    }
}
