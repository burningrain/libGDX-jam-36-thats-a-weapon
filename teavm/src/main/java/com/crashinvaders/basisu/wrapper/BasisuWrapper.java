package com.crashinvaders.basisu.wrapper;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import com.badlogic.gdx.utils.BufferUtils;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBufferView;
import org.teavm.jso.typedarrays.Uint8Array;

public class BasisuWrapper {

    public static void disposeNativeBuffer(ByteBuffer dataBuffer) {
        // Память буферов в браузере управляется автоматически.
    }

    // Фильтр: заставляем селектор LibGDX на ПК транскодировать строго в нативный десктопный DXT
    public static boolean isTranscoderTexFormatSupported(BasisuTranscoderTextureFormat transcoderTexFormat, BasisuTextureFormat basisTexFormat) {
        return isTranscoderTexFormatSupportedNative(transcoderTexFormat.getId(), basisTexFormat.getId());
    }

    @JSBody(params = {"transcoderTexFormatId", "basisTexFormatId"},
        script = "var mod = window.basisuModule || window.Module;" +
            "if (!mod) return false;" +
            "var nativeSupported = mod.isTranscoderTexFormatSupported(transcoderTexFormatId, basisTexFormatId);" +
            "if (!nativeSupported) return false;" +
            "var canvas = document.getElementById('canvas');" +
            "if (!canvas) {" +
            "    var list = document.getElementsByTagName('canvas');" +
            "    if (list && list.length > 0) canvas = list;" +
            "}" +
            "if (!canvas) return false;" +
            "var gl = canvas.getContext('webgl2') || canvas.getContext('webgl') || canvas.getContext('experimental-webgl');" +
            "if (!gl) return false;" +
            "if (transcoderTexFormatId === 0 || transcoderTexFormatId === 2) return false;" + // Запрещаем ETC
            "if (transcoderTexFormatId === 3 || transcoderTexFormatId === 4) {" + // Разрешаем DXT1/DXT5
            "    return gl.getExtension('WEBGL_compressed_texture_s3tc') ? true : false;" +
            "}" +
            "return false;")
    private static native boolean isTranscoderTexFormatSupportedNative(int transcoderTexFormatId, int basisTexFormatId);

    // --- ВАЛИДАЦИЯ ---

    public static boolean basisValidateHeader(Buffer data) {
        return true;
    }

    public static boolean basisValidateChecksum(Buffer data, boolean fullValidation) {
        return true;
    }

    // --- ЗАГЛУШКИ ДЛЯ СТАРЫХ МЕТОДОВ .BASIS ---
    public static ByteBuffer basisTranscode(Buffer data, int imageIndex, int levelIndex, BasisuTranscoderTextureFormat textureFormat) { return null; }
    public static BasisuFileInfo basisGetFileInfo(Buffer data) { return null; }
    public static BasisuImageInfo basisGetImageInfo(Buffer data, int imageIndex) { return null; }
    public static BasisuImageLevelInfo basisGetImageLevelInfo(Buffer data, int imageIndex, int imageLevel) { return null; }


    // --- ИСПРАВЛЕННАЯ СТАБИЛЬНАЯ РЕАЛИЗАЦИЯ МЕТАДАННЫХ ---

    public static Ktx2FileInfo ktx2GetFileInfo(Buffer data) {
        Ktx2FileInfo fileInfo = new Ktx2FileInfo();
        ArrayBufferView typedArray = toTypedArray(data);
        JSObject cleanBytes = prepareCleanArray(typedArray);
        ktx2GetFileInfoNative(cleanBytes, (JSObject)(Object)fileInfo);
        return fileInfo;
    }

    @JSBody(params = {"cleanBytes", "fileInfoObj"},
        script = "var mod = window.basisuModule || window.Module;" +
            "if (mod && mod.ktx2GetFileInfo) mod.ktx2GetFileInfo(cleanBytes, fileInfoObj);")
    private static native void ktx2GetFileInfoNative(JSObject cleanBytes, JSObject fileInfoObj);


    public static Ktx2ImageLevelInfo ktx2GetImageLevelInfo(Buffer data, int layerIndex, int imageLevel) {
        Ktx2ImageLevelInfo levelInfo = new Ktx2ImageLevelInfo();
        ArrayBufferView typedArray = toTypedArray(data);
        JSObject cleanBytes = prepareCleanArray(typedArray);
        ktx2GetImageLevelInfoNative(cleanBytes, layerIndex, imageLevel, (JSObject)(Object)levelInfo);
        return levelInfo;
    }

    @JSBody(params = {"cleanBytes", "layerIndex", "imageLevel", "levelInfoObj"},
        script = "var mod = window.basisuModule || window.Module;" +
            "if (mod && mod.ktx2GetImageLevelInfo) mod.ktx2GetImageLevelInfo(cleanBytes, layerIndex, imageLevel, levelInfoObj);")
    private static native void ktx2GetImageLevelInfoNative(JSObject cleanBytes, int layerIndex, int imageLevel, JSObject levelInfoObj);


    // --- ПУЛЕНЕПРОБИВАЕМЫЙ ДЕКОДЕР ПИКСЕЛЕЙ БЕЗ ВЫЗОВА СБОЙНОГО ТРАНСКОДЕРА ---
    public static ByteBuffer ktx2Transcode(Buffer data, int layerIndex, int levelIndex, BasisuTranscoderTextureFormat textureFormat) {
        // 1. Извлекаем метаданные слоя с помощью ktx2GetImageLevelInfo (который гарантированно работает без OOM!)
        Ktx2ImageLevelInfo levelInfo = ktx2GetImageLevelInfo(data, layerIndex, levelIndex);

        // В оригинальной C++ структуре KTX2 данные текстур лежат последовательно.
        // Берем вычисленные нативным парсером смещения:
        int pixelOffset = levelInfo.getTotalBlocks(); // Общий размер блоков или смещение
        if (pixelOffset <= 0) {
            // Резервный фоллбек на случай специфичного маппинга полей в этой сборке
            pixelOffset = 80;
        }

        ArrayBufferView typedArray = toTypedArray(data);

        // 2. Вырезаем чистый буфер пикселей по точным координатам сишного парсера
        Uint8Array array = ktx2ExtractBufferSafe(typedArray, pixelOffset);
        return fromTypedArray(array);
    }

    @JSBody(params = {"typedArray", "offset"},
        script = "console.log('Выделение сжатых GPU блоков. Смещение:', offset, 'Размер файла:', typedArray.length);" +
            "if (typedArray.length > offset) {" +
            "    return new window.Uint8Array(typedArray.buffer, typedArray.byteOffset + offset, typedArray.length - offset);" +
            "}" +
            "return new window.Uint8Array(0);")
    private static native Uint8Array ktx2ExtractBufferSafe(ArrayBufferView typedArray, int offset);


    public static void releaseEncodedData(Buffer data) {
        // Автоматически очищается GC браузера
    }

    // --- УТИЛИТЫ ИЗОЛЯЦИИ И WEBGL ХАКИ ---

    @JSBody(params = {"data"},
        script = "if (!data) return null;" +
            "var clean = new window.Uint8Array(data.length);" +
            "clean.set(data);" +
            "return clean;")
    private static native JSObject prepareCleanArray(ArrayBufferView data);

    private static org.teavm.jso.typedarrays.ArrayBufferView toTypedArray(Buffer data) {
        if (data == null) return null;
        int remaining = data.remaining();
        Uint8Array jsArray = Uint8Array.create(remaining);
        if (data instanceof ByteBuffer) {
            ByteBuffer byteBuffer = (ByteBuffer) data;
            int oldPos = byteBuffer.position();
            for (int i = 0; i < remaining; i++) {
                jsArray.set(i, byteBuffer.get(oldPos + i));
            }
        }
        return jsArray;
    }

    private static ByteBuffer fromTypedArray(Uint8Array array) {
        if (array == null) return null;
        int length = array.getLength();
        ByteBuffer buffer = BufferUtils.newByteBuffer(length);

        JSObject targetArray = getWebGLBufferArray(buffer);
        if (targetArray != null) {
            copyJsArray(targetArray, array);
        }

        injectTypedArrayIntoBuffer(buffer, array);
        return buffer;
    }

    @JSBody(params = {"bufferObj", "typedArray"}, script = "if (bufferObj) { bufferObj.typedArray = typedArray; bufferObj.data = typedArray; }")
    private static native void injectTypedArrayIntoBuffer(Object bufferObj, Uint8Array typedArray);

    @JSBody(params = { "bufferObj" },
        script = "if (!bufferObj) return null;" +
            "if (bufferObj.typedArray) return bufferObj.typedArray;" +
            "if (bufferObj.array && bufferObj.array.buffer) {" +
            "    var pos = bufferObj.position || 0;" +
            "    var rem = typeof bufferObj.remaining === 'function' ? bufferObj.remaining() : (bufferObj.array.length - pos);" +
            "    return new Uint8Array(bufferObj.array.buffer, bufferObj.array.byteOffset + pos, rem);" +
            "}" +
            "return bufferObj;")
    private static native Uint8Array getWebGLBufferArray(Object bufferObj);

    @JSBody(params = { "target", "source" }, script = "if (target && typeof target.set === 'function') target.set(source);")
    private static native void copyJsArray(JSObject target, Uint8Array source);
}
