package com.crashinvaders.basisu.wrapper;

import static com.crashinvaders.basisu.wrapper.UniqueIdUtils.findOrThrow;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

import java.io.Closeable;

public class Ktx2FileInfo implements Closeable {

    private JSObject fileInfoJs;

    // Разрешаем дефолтный конструктор, инициализируя его безопасной JS-пустышкой.
    // Это предотвратит UnsupportedOperationException, когда оригинальная библиотека вызывает new Ktx2FileInfo().
    public Ktx2FileInfo() {
        this.fileInfoJs = createEmptyJsObject();
    }

    public Ktx2FileInfo(Object fileInfoJs) {
        this.fileInfoJs = (JSObject) fileInfoJs;
    }

    // Хелпер для внутренней привязки объекта после нативного вызова
    public void setJsObject(JSObject fileInfoJs) {
        this.fileInfoJs = fileInfoJs;
    }

    @Override
    public void close() {
        // Процедурный JS-объект не требует вызова .delete(), память очистит GC браузера.
        fileInfoJs = null;
    }

    public int getTotalLayers() {
        return getIntProperty(fileInfoJs, "totalLayers");
    }

    public int getTotalMipmapLevels() {
        return getIntProperty(fileInfoJs, "totalMipmapLevels");
    }

    public int getImageWidth() {
        return getIntProperty(fileInfoJs, "imageWidth");
    }

    public int getImageHeight() {
        return getIntProperty(fileInfoJs, "imageHeight");
    }

    public boolean hasAlpha() {
        return getBoolProperty(fileInfoJs, "hasAlpha");
    }

    public BasisuTextureFormat getTextureFormat() {
        int textureFormatId = getIntProperty(fileInfoJs, "textureFormat");
        return findOrThrow(BasisuTextureFormat.values(), textureFormatId);
    }

    // --- ES5-совместимые нативные методы доступа к свойствам плоского JS-объекта ---

    @JSBody(script = "return { totalLayers: 1, totalMipmapLevels: 1, imageWidth: 0, imageHeight: 0, hasAlpha: false, textureFormat: 0 };")
    private static native JSObject createEmptyJsObject();

    @JSBody(params = {"obj", "propName"}, script = "return obj && typeof obj[propName] !== 'undefined' ? obj[propName] : 0;")
    private static native int getIntProperty(JSObject obj, String propName);

    @JSBody(params = {"obj", "propName"}, script = "return obj && typeof obj[propName] !== 'undefined' ? !!obj[propName] : false;")
    private static native boolean getBoolProperty(JSObject obj, String propName);
}
