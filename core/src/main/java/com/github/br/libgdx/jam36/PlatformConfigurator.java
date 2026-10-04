package com.github.br.libgdx.jam36;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;

public interface PlatformConfigurator {

    void configureLoaders(AssetManager manager, FileHandleResolver fileHandleResolver);

}
