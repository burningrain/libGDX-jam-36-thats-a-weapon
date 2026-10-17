package com.crashinvaders.basisu.gdx;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

import java.nio.Buffer;

/**
 * Мост для WebGL расширений сжатых текстур под TeaVM (бэкенд gdx-teavm).
 * Полностью заменяет GWT-зависимости на прямые JS-вызовы к контексту WebGL.
 */
public class BasisuGdxGl {

    public static int[] getSupportedTextureFormats() {
        JSObject glContext = getGlContext();
        if (glContext == null) {
            return new int[0];
        }
        return getSupportedTextureFormatsNative(glContext);
    }

    public static void glCompressedTexImage2D(int target, int level, int internalFormat,
                                              int width, int height, int border,
                                              int imageSize, Buffer pixels) {
        JSObject glContext = getGlContext();
        if (glContext == null) return;

        // Передаем Buffer в JS как Object, чтобы избежать проблем со строгой типизацией Java.
        // Метод извлечет из структуры данных правильное ArrayBufferView окно (Uint8Array)
        JSObject arrayBufferView = getWebGLBufferArray(pixels);

        glCompressedTexImage2DNative(glContext, target, level, internalFormat, width, height, border, imageSize, arrayBufferView);
    }

    @JSBody(params = { "gl" }, script =
        // 1. Активируем необходимые WebGL расширения сжатых текстур в контексте браузера
        "gl.getExtension('WEBGL_compressed_texture_astc');" +
            "gl.getExtension('WEBGL_compressed_texture_atc');" +
            "gl.getExtension('WEBGL_compressed_texture_etc');" +
            "gl.getExtension('WEBGL_compressed_texture_etc1');" +
            "gl.getExtension('WEBGL_compressed_texture_pvrtc');" +
            "gl.getExtension('WEBGL_compressed_texture_s3tc');" +

            // 2. Запрашиваем результирующий массив поддерживаемых GPU токенов форматов
            "var supportedFormats = gl.getParameter(0x86A3);" + // GL_COMPRESSED_TEXTURE_FORMATS
            "if (!supportedFormats) return [];" +
            "var resultValues = new Array(supportedFormats.length);" +
            "for (var i = 0; i < supportedFormats.length; i++) {" +
            "    resultValues[i] = supportedFormats[i];" +
            "}" +
            "return resultValues;"
    )
    private static native int[] getSupportedTextureFormatsNative(JSObject gl);

    @JSBody(params = { "gl", "target", "level", "internalFormat", "width", "height", "border", "imageSize", "pixels" },
        script = "gl.compressedTexImage2D(target, level, internalFormat, width, height, border, pixels);")
    private static native void glCompressedTexImage2DNative(JSObject gl, int target, int level, int internalFormat,
                                                            int width, int height, int border, int imageSize, JSObject pixels);

    /**
     * Безопасное динамическое извлечение текущего контекста WebGL из рантайма gdx-teavm.
     */
    @JSBody(params = {}, script =
        // 1. Если контекст уже был где-то кэширован глобально, возвращаем его
        "if (typeof window !== 'undefined' && window.gl) return window.gl;" +

            // 2. Ищем холст строго по вашему id="canvas", затем по тегу
            "var canvas = document.getElementById('canvas') || document.querySelector('canvas');" +
            "if (canvas) {" +
            "    var gl = canvas.getContext('webgl2') || canvas.getContext('webgl') || canvas.getContext('experimental-webgl');" +
            "    if (gl) {" +
            // 3. Лайфхак для gdx-teavm: если бэкенд xpenatan внутри себя будет искать window.gl,
            // сохраняем его туда, чтобы предотвратить появление ошибок "getContext is not defined" в рантайме
            "        if (typeof window !== 'undefined') window.gl = gl;" +
            "        return gl;" +
            "    }" +
            "}" +
            "return null;"
    )
    private static native JSObject getGlContext();

    /**
     * Извлекает корректный JS-массив Uint8Array с учетом текущей позиции Java-буфера (смещение и размер).
     */
    @JSBody(params = { "bufferObj" },
        script = "if (!bufferObj) return null;" +
            "if (bufferObj.typedArray) return bufferObj.typedArray;" +
            "if (bufferObj.array && bufferObj.array.buffer) {" +
            "    var pos = bufferObj.position || 0;" +
            "    var rem = typeof bufferObj.remaining === 'function' ? bufferObj.remaining() : (bufferObj.array.length - pos);" +
            "    return new Uint8Array(bufferObj.array.buffer, bufferObj.array.byteOffset + pos, rem);" +
            "}" +
            "if (bufferObj.buffer && bufferObj.buffer instanceof ArrayBuffer) return new Uint8Array(bufferObj.buffer);" +
            "return bufferObj;")
    private static native JSObject getWebGLBufferArray(Object bufferObj);
}
