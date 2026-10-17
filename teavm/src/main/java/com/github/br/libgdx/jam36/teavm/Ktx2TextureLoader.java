package com.github.br.libgdx.jam36.teavm;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.crashinvaders.basisu.gdx.BasisuGdxUtils;
import com.crashinvaders.basisu.gdx.BasisuTextureFormatSelector;
import com.crashinvaders.basisu.gdx.Ktx2TextureData;

/**
 * Исправленный синхронный лоадер KTX2 для TeaVM WasmGC.
 * Предотвращает утечку/затирание TextureData в асинхронных потоках.
 */
public class Ktx2TextureLoader extends SynchronousAssetLoader<Texture, TextureLoader.TextureParameter> {

    public Ktx2TextureLoader(FileHandleResolver resolver) {
        super(resolver);
        // We need to make sure this one is first time called
        // on the main thread and not during async texture loading.
        BasisuGdxUtils.initSupportedGlTextureFormats();
    }

    @Override
    public Texture load(AssetManager assetManager, String fileName, FileHandle fileHandle, TextureLoader.TextureParameter parameter) {
        Ktx2TextureData data;
        if (parameter instanceof Ktx2TextureParameter) {
            Ktx2TextureParameter basisParameter = (Ktx2TextureParameter) parameter;
            data = new Ktx2TextureData(fileHandle);
            data.setUseMipMaps(basisParameter.useMipmaps);
            if (basisParameter.formatSelector != null) {
                data.setTextureFormatSelector(basisParameter.formatSelector);
            }
        } else {
            data = new Ktx2TextureData(fileHandle);
        }
        data.prepare();

        Texture texture = new Texture(data);
        if (parameter != null) {
            // data.useMipMaps() can end up false even if requested true (e.g. a non-power-of-two
            // texture on WebGL1), only 1 level is actually uploaded then - a mipmap filter would
            // make the texture GL-incomplete (silently sampled as black, no error).
            Texture.TextureFilter minFilter = (!data.useMipMaps() && parameter.minFilter.isMipMap())
                ? Texture.TextureFilter.Linear : parameter.minFilter;
            texture.setFilter(minFilter, parameter.magFilter);
            texture.setWrap(parameter.wrapU, parameter.wrapV);
        }

        return texture;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TextureLoader.TextureParameter parameter) {
        return null;
    }

    public static class Ktx2TextureParameter extends TextureLoader.TextureParameter {
        // public int layerIndex = 0; // Not yet supported.
        public boolean useMipmaps = true;
        public BasisuTextureFormatSelector formatSelector = null;

        public Ktx2TextureParameter() {
        }
    }

}
