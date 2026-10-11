package com.github.br.libgdx.jam36.screens;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.crashinvaders.vfx.effects.BloomEffect;
import com.crashinvaders.vfx.effects.ChainVfxEffect;
import com.crashinvaders.vfx.effects.ChromaticAberrationEffect;
import com.crashinvaders.vfx.effects.CrtEffect;
import com.crashinvaders.vfx.effects.FilmGrainEffect;
import com.crashinvaders.vfx.effects.FisheyeEffect;
import com.crashinvaders.vfx.effects.FxaaEffect;
import com.crashinvaders.vfx.effects.GaussianBlurEffect;
import com.crashinvaders.vfx.effects.LensFlareEffect;
import com.crashinvaders.vfx.effects.LevelsEffect;
import com.crashinvaders.vfx.effects.MotionBlurEffect;
import com.crashinvaders.vfx.effects.NfaaEffect;
import com.crashinvaders.vfx.effects.OldTvEffect;
import com.crashinvaders.vfx.effects.RadialBlurEffect;
import com.crashinvaders.vfx.effects.RadialDistortionEffect;
import com.crashinvaders.vfx.effects.VignettingEffect;
import com.crashinvaders.vfx.effects.WaterDistortionEffect;
import com.crashinvaders.vfx.effects.ZoomEffect;
import com.crashinvaders.vfx.effects.util.GammaThresholdEffect;
import com.crashinvaders.vfx.effects.util.MixEffect;
import com.github.br.libgdx.common.structure.screen.ui.vfx.VfxEffectPooledFactoryImpl;
import com.github.br.libgdx.common.structure.screen.ui.vfx.effect.SmoothVignetteEffect;

public class CustomVfxEffectPooledFactoryImpl extends VfxEffectPooledFactoryImpl {

    public interface Title {

        String MAIN_VFX = "MAIN_VFX";
        String THINKING_VFX = "THINKING_VFX";

    }

    private float timeAccumulator = 0f;

    private void updateMainVfx(float deltaTime) {
        chromaticAberrationEffect.setMaxDistortion(0.1f);
    }

    private void updateThinking(float deltaTime) {
        // Настройки вибрации:
        float baseShift = 1.0f;      // Минимальный/базовый сдвиг в пикселях
        float amplitude = 1.0f;      // Насколько сильно отклоняется (в пикселях)
        float speed = 1.5f;         // Скорость вибрации (частота)

        // 1. Вариант: Плавная пульсация (синусоида)
        // Математика выдаст значение от (baseShift) до (baseShift + amplitude)
        timeAccumulator += deltaTime;
        float currentShift = baseShift + MathUtils.sin(timeAccumulator * speed) * amplitude;
        chromaticAberrationEffect.setMaxDistortion(MathUtils.clamp(currentShift, 0.1f, 1.2f));
    }

    public CustomVfxEffectPooledFactoryImpl() {
        this.putEffects(Title.MAIN_VFX, new Array<ChainVfxEffect>() {{
            this.add(chromaticAberrationEffect);
            this.add(vignettingEffect);
        }});
        this.putEffects(Title.THINKING_VFX, new Array<ChainVfxEffect>() {{
            //this.add(radialBlurEffect);
            this.add(chromaticAberrationEffect);
            this.add(vignettingEffect);
            this.add(gaussianBlurEffect);
        }});
    }

    @Override
    public void update(float deltaTime, String effectTitle) {
        switch (effectTitle) {
            case Title.MAIN_VFX:
                updateMainVfx(deltaTime);
                break;
            case Title.THINKING_VFX:
                updateThinking(deltaTime);
                break;
        }
    }

    public GaussianBlurEffect gaussianBlurEffect = new GaussianBlurEffect();
    public MotionBlurEffect motionBlurEffect = new MotionBlurEffect(
        Pixmap.Format.RGBA8888,
        MixEffect.Method.MIX,
        0.5f
    );
    public FisheyeEffect fisheyeEffect = new FisheyeEffect();
    public OldTvEffect oldTvEffect = new OldTvEffect();
    public ChromaticAberrationEffect chromaticAberrationEffect = new ChromaticAberrationEffect(10);
    public BloomEffect bloomEffect = new BloomEffect();
    public NfaaEffect nfaaEffect = new NfaaEffect(true);
    public CrtEffect crtEffect = new CrtEffect();
    public LensFlareEffect lensFlareEffect = new LensFlareEffect();
    public FxaaEffect fxaaEffect = new FxaaEffect();
    public WaterDistortionEffect waterDistortionEffect = new WaterDistortionEffect(12f, 12f);
    public VignettingEffect vignettingEffect = new VignettingEffect(false);
    public SmoothVignetteEffect smoothVignetteEffect = new SmoothVignetteEffect();
    public ZoomEffect zoomEffect = new ZoomEffect();
    public GammaThresholdEffect gammaThresholdEffect = new GammaThresholdEffect(GammaThresholdEffect.Type.RGBA);
    public LevelsEffect levelsEffect = new LevelsEffect();
    public RadialBlurEffect radialBlurEffect = new RadialBlurEffect(2);
    public FilmGrainEffect filmGrainEffect = new FilmGrainEffect();
    public RadialDistortionEffect radialDistortionEffect = new RadialDistortionEffect();




}
