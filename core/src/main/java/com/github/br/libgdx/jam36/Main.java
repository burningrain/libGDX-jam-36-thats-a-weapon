package com.github.br.libgdx.jam36;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.*;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.libgdx.jam36.screens.GameScreens;
import com.github.tommyettinger.textra.FWSkinLoader;
import structure.AbstractSimpleGame;
import structure.GameSettings;
import structure.screen.statemachine.GameScreenState;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends AbstractSimpleGame<UserFactoryImpl> {

    private AssetManager assetManager;
    private Viewport viewport;

    @Override
    protected UserFactoryImpl createUserFactory() {
        return new UserFactoryImpl();
    }

    @Override
    protected GameScreenState createStartState() {
        return GameScreens.MENU;
    }

    @Override
    protected void initLoaders(AssetManager assetManager, InternalFileHandleResolver fileHandleResolver) {
        // графика
        assetManager.setLoader(Texture.class, new TextureLoader(fileHandleResolver));
        assetManager.setLoader(TextureAtlas.class, new TextureAtlasLoader(fileHandleResolver));
        //assetManager.setLoader(Skin.class, new FreeTypeSkinLoader(fileHandleResolver));

        // 1. Регистрируем лоадер для FWSkin
        assetManager.setLoader(Skin.class, new FWSkinLoader(assetManager.getFileHandleResolver()));

        // эффекты частиц
        assetManager.setLoader(ParticleEffect.class, ".p", new ParticleEffectLoader(fileHandleResolver));

        // звук
        assetManager.setLoader(Sound.class, new SoundLoader(fileHandleResolver));
        assetManager.setLoader(Music.class, new MusicLoader(fileHandleResolver));

        // карты редакторов уровней
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(fileHandleResolver));
        assetManager.setLoader(TiledMap.class, new AtlasTmxMapLoader(fileHandleResolver));

        // шрифты
        assetManager.setLoader(BitmapFont.class, new FreetypeFontLoader(fileHandleResolver));
    }

    @Override
    protected void fillGameSettings(GameSettings.Builder builder) {
    }

}
