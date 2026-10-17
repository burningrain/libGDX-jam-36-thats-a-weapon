package com.crashinvaders.basisu.wrapper;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import com.badlogic.gdx.utils.BufferUtils;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBufferView;
import org.teavm.jso.typedarrays.Uint8Array;

public class BasisuWrapper {

    public static void disposeNativeBuffer(ByteBuffer dataBuffer) {}

    public static boolean isTranscoderTexFormatSupported(BasisuTranscoderTextureFormat transcoderTexFormat, BasisuTextureFormat basisTexFormat) {
        return isTranscoderTexFormatSupportedNative(transcoderTexFormat.getId(), basisTexFormat.getId());
    }

    @JSBody(params = {"transcoderTexFormatId", "basisTexFormatId"},
        script = "var mod = window.basisuModule || window.Module;" +
            "if (!mod) return false;" +
            "var nativeSupported = mod.isTranscoderTexFormatSupported(transcoderTexFormatId, basisTexFormatId);" +
            "if (!nativeSupported) return false;" +
            "var canvas = document.getElementById('canvas') || document.getElementsByTagName('canvas');" +
            "if (!canvas) return false;" +
            "var gl = canvas.getContext('webgl2') || canvas.getContext('webgl') || canvas.getContext('experimental-webgl');" +
            "if (!gl) return false;" +

            "if (transcoderTexFormatId === 0) {" +
            "    return !!(gl.getExtension('WEBGL_compressed_texture_etc1') || gl.getExtension('WEBGL_compressed_texture_etc'));" +
            "}" +
            "if (transcoderTexFormatId === 2) {" +
            "    return !!gl.getExtension('WEBGL_compressed_texture_etc');" +
            "}" +
            "if (transcoderTexFormatId === 3 || transcoderTexFormatId === 4) {" +
            "    var s3tc = gl.getExtension('WEBGL_compressed_texture_s3tc') || " +
            "               gl.getExtension('MOZ_WEBGL_compressed_texture_s3tc') || " +
            "               gl.getExtension('WEBKIT_WEBGL_compressed_texture_s3tc') || " +
            "               gl.getExtension('WEBGL_compressed_texture_s3tc_srgb');" +
            "    return !!s3tc;" +
            "}" +
            "if (transcoderTexFormatId === 7) {" +
            "    return !!gl.getExtension('WEBGL_compressed_texture_astc');" +
            "}" +
            "return transcoderTexFormatId === 14;")
    private static native boolean isTranscoderTexFormatSupportedNative(int transcoderTexFormatId, int basisTexFormatId);

    public static boolean basisValidateHeader(Buffer data) { return true; }
    public static boolean basisValidateChecksum(Buffer data, boolean fullValidation) { return true; }
    public static ByteBuffer basisTranscode(Buffer data, int imageIndex, int levelIndex, BasisuTranscoderTextureFormat textureFormat) { return null; }
    public static BasisuFileInfo basisGetFileInfo(Buffer data) { return null; }
    public static BasisuFileInfo basisGetImageInfo(Buffer data, int imageIndex) { return null; }
    public static BasisuImageLevelInfo basisGetImageLevelInfo(Buffer data, int imageIndex, int imageLevel) { return null; }

    public static Ktx2FileInfo ktx2GetFileInfo(Buffer data) {
        Ktx2FileInfo fileInfo = new Ktx2FileInfo();
        ArrayBufferView typedArray = toTypedArray(data);
        JSObject cleanBytes = prepareCleanArray(typedArray);
        ktx2GetFileInfoNative(cleanBytes, (JSObject)(Object)fileInfo);
        return fileInfo;
    }

    @JSBody(params = {"cleanBytes", "fileInfoObj"}, script = "if(window.basisuModule) window.basisuModule.ktx2GetFileInfo(cleanBytes, fileInfoObj);")
    private static native void ktx2GetFileInfoNative(JSObject cleanBytes, JSObject fileInfoObj);

    public static Ktx2ImageLevelInfo ktx2GetImageLevelInfo(Buffer data, int layerIndex, int imageLevel) {
        Ktx2ImageLevelInfo levelInfo = new Ktx2ImageLevelInfo();
        ArrayBufferView typedArray = toTypedArray(data);
        JSObject cleanBytes = prepareCleanArray(typedArray);
        ktx2GetImageLevelInfoNative(cleanBytes, layerIndex, imageLevel, (JSObject)(Object)levelInfo);
        return levelInfo;
    }

    @JSBody(params = {"cleanBytes", "layerIndex", "imageLevel", "levelInfoObj"}, script = "if(window.basisuModule) window.basisuModule.ktx2GetImageLevelInfo(cleanBytes, layerIndex, imageLevel, levelInfoObj);")
    private static native void ktx2GetImageLevelInfoNative(JSObject cleanBytes, int layerIndex, int imageLevel, JSObject levelInfoObj);

    public static ByteBuffer ktx2Transcode(Buffer data, int layerIndex, int levelIndex, BasisuTranscoderTextureFormat textureFormat) {
        ArrayBufferView typedArray = toTypedArray(data);
        Uint8Array array = ktx2TranscodeNative(typedArray, layerIndex, levelIndex, textureFormat.getId());
        return fromTypedArray(array);
    }

    @JSBody(params = {"data", "layerIndex", "levelIndex", "textureFormat"},
        script = "var mod = window.basisuModule || window.Module;" +
            "if (!mod || !mod.ktx2Transcode) return new window.Uint8Array(0);" +
            "var cleanBytes = new window.Uint8Array(data.length);" +
            "cleanBytes.set(data);" +
            "return mod.ktx2Transcode(cleanBytes, layerIndex, levelIndex, textureFormat);")
    private static native Uint8Array ktx2TranscodeNative(ArrayBufferView data, int layerIndex, int levelIndex, int textureFormat);

    public static void releaseEncodedData(Buffer data) {}

    @JSBody(params = {"data"}, script = "var c = new window.Uint8Array(data.length); c.set(data); return c;")
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
        if (targetArray != null) copyJsArray(targetArray, array);
        injectTypedArrayIntoBuffer(buffer, targetArray);
        return buffer;
    }

    @JSBody(params = {"bufferObj", "typedArray"}, script = "if(bufferObj){ bufferObj.typedArray = typedArray; bufferObj.data = typedArray; }")
    private static native void injectTypedArrayIntoBuffer(Object bufferObj, JSObject typedArray);

    @JSBody(params = { "bufferObj" },
        script = "if(!bufferObj) return null;" +
            "if(bufferObj.typedArray) return bufferObj.typedArray;" +
            "if(bufferObj.array && bufferObj.array.buffer) {" +
            "    return new Uint8Array(bufferObj.array.buffer, bufferObj.array.byteOffset + (bufferObj.position || 0), bufferObj.array.length - (bufferObj.position || 0));" +
            "}" +
            "return bufferObj;")
    private static native JSObject getWebGLBufferArray(Object bufferObj);

    @JSBody(params = { "target", "source" }, script = "if(target && typeof target.set === 'function') target.set(source);")
    private static native void copyJsArray(JSObject target, Uint8Array source);
}
