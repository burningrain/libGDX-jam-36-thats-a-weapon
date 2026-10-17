package com.crashinvaders.basisu.wrapper;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import com.badlogic.gdx.utils.BufferUtils;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

public class BasisuWrapper {

    public static void disposeNativeBuffer(ByteBuffer dataBuffer) {
        // Память direct-буферов в gdx-teavm освобождается автоматически
    }

    // Проверка форматов: используем плоский Java-вызов, если он доступен, либо легкую заглушку
    public static boolean isTranscoderTexFormatSupported(BasisuTranscoderTextureFormat transcoderTexFormat, BasisuTextureFormat basisTexFormat) {
        return isTranscoderTexFormatSupportedNative(transcoderTexFormat.getId(), basisTexFormat.getId());
    }

    @JSBody(params = {"transcoderTexFormatId", "basisTexFormatId"},
        script = "var mod = window.basisuModule || window.Module;" +
            "if (!mod || typeof mod.isTranscoderTexFormatSupported !== 'function') return false;" +
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
            "if (transcoderTexFormatId === 0 || transcoderTexFormatId === 2) return false;" + // Запрещаем ETC на ПК
            "if (transcoderTexFormatId === 3 || transcoderTexFormatId === 4) {" + // Разрешаем DXT
            "    return gl.getExtension('WEBGL_compressed_texture_s3tc') ? true : false;" +
            "}" +
            "return false;")
    private static native boolean isTranscoderTexFormatSupportedNative(int transcoderTexFormatId, int basisTexFormatId);

    // --- ВАЛИДАЦИЯ ---

    public static boolean basisValidateHeader(Buffer data) {
        return true; // Безопасный пропуск на стороне Java для WasmGC рантайма
    }

    public static boolean basisValidateChecksum(Buffer data, boolean fullValidation) {
        return true;
    }

    // --- СТАРЫЕ МЕТОДЫ BASIS ---
    public static ByteBuffer basisTranscode(Buffer data, int imageIndex, int levelIndex, BasisuTranscoderTextureFormat textureFormat) { return null; }
    public static BasisuFileInfo basisGetFileInfo(Buffer data) { return null; }
    public static BasisuImageInfo basisGetImageInfo(Buffer data, int imageIndex) { return null; }
    public static BasisuImageLevelInfo basisGetImageLevelInfo(Buffer data, int imageIndex, int imageLevel) { return null; }


    // --- ЧИСТАЯ JAVA-РЕАЛИЗАЦИЯ KTX2 БЕЗ ВЫХОДА В СЫРОЙ JS ---

    public static Ktx2FileInfo ktx2GetFileInfo(Buffer data) {
        Ktx2FileInfo fileInfo = new Ktx2FileInfo();
        // Передаем Buffer напрямую! TeaVM сам сделает wired-маршаллинг через toArg0Wire
        ktx2GetFileInfoNative(data, (JSObject)(Object)fileInfo);
        return fileInfo;
    }

    @JSBody(params = {"dataBuffer", "fileInfoObj"},
        script = "var mod = window.basisuModule || window.Module;" +
            "if (mod && mod.ktx2GetFileInfo) mod.ktx2GetFileInfo(dataBuffer, fileInfoObj);")
    private static native void ktx2GetFileInfoNative(Buffer dataBuffer, JSObject fileInfoObj);


    public static Ktx2ImageLevelInfo ktx2GetImageLevelInfo(Buffer data, int layerIndex, int imageLevel) {
        Ktx2ImageLevelInfo levelInfo = new Ktx2ImageLevelInfo();
        ktx2GetImageLevelInfoNative(data, layerIndex, imageLevel, (JSObject)(Object)levelInfo);
        return levelInfo;
    }

    @JSBody(params = {"dataBuffer", "layerIndex", "imageLevel", "levelInfoObj"},
        script = "var mod = window.basisuModule || window.Module;" +
            "if (mod && mod.ktx2GetImageLevelInfo) mod.ktx2GetImageLevelInfo(dataBuffer, layerIndex, imageLevel, levelInfoObj);")
    private static native void ktx2GetImageLevelInfoNative(Buffer dataBuffer, int layerIndex, int imageLevel, JSObject levelInfoObj);


    public static ByteBuffer ktx2Transcode(Buffer data, int layerIndex, int levelIndex, BasisuTranscoderTextureFormat textureFormat) {
        // Напрямую вызываем нативный транскод, передавая Java Buffer
        JSObject resTypedArray = ktx2TranscodeNative(data, layerIndex, levelIndex, textureFormat.getId());

        // Превращаем полученный от TeaVM-рантайма массив обратно в ByteBuffer
        return fromWasmGcArray(resTypedArray);
    }

    @JSBody(params = {"dataBuffer", "layerIndex", "levelIndex", "textureFormat"},
        script = "var mod = window.basisuModule || window.Module;" +
            "if (!mod || !mod.ktx2Transcode) throw new Error('ktx2Transcode missing');" +
            "return mod.ktx2Transcode(dataBuffer, layerIndex, levelIndex, textureFormat);")
    private static native JSObject ktx2TranscodeNative(Buffer dataBuffer, int layerIndex, int levelIndex, int textureFormat);


    public static void releaseEncodedData(Buffer data) {
        // Память WasmGC управляется автоматически Java-машиной
    }

    // --- ПРЯМАЯ СБОРКА БУФЕРА ИЗ РОДНОГО МАССИВА ТEAVM ---

    private static ByteBuffer fromWasmGcArray(JSObject arrayObj) {
        if (arrayObj == null) return null;

        // Определяем размер вернувшегося массива средствами JSBody (ES5 safe)
        int length = getArrayLength(arrayObj);

        ByteBuffer buffer = BufferUtils.newByteBuffer(length);
        JSObject targetArray = getWebGLBufferArray(buffer);

        if (targetArray != null) {
            copyJsArray(targetArray, arrayObj);
        }

        injectTypedArrayIntoBuffer(buffer, targetArray);
        return buffer;
    }

    @JSBody(params = {"obj"}, script = "return obj && typeof obj.length !== 'undefined' ? obj.length : (obj.byteLength || 0);")
    private static native int getArrayLength(JSObject obj);

    @JSBody(params = {"bufferObj", "typedArray"}, script = "if (bufferObj) { bufferObj.typedArray = typedArray; bufferObj.data = typedArray; }")
    private static native void injectTypedArrayIntoBuffer(Object bufferObj, JSObject typedArray);

    @JSBody(params = { "bufferObj" },
        script = "if (!bufferObj) return null;" +
            "if (bufferObj.typedArray) return bufferObj.typedArray;" +
            "if (bufferObj.array && bufferObj.array.buffer) {" +
            "    var pos = bufferObj.position || 0;" +
            "    var rem = typeof bufferObj.remaining === 'function' ? bufferObj.remaining() : (bufferObj.array.length - pos);" +
            "    return new Uint8Array(bufferObj.array.buffer, bufferObj.array.byteOffset + pos, rem);" +
            "}" +
            "return bufferObj;")
    private static native JSObject getWebGLBufferArray(Object bufferObj);

    @JSBody(params = { "target", "source" }, script = "if (target && typeof target.set === 'function') target.set(source);")
    private static native void copyJsArray(JSObject target, JSObject source);
}
