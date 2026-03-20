package com.github.br.libgdx.jam36.ui;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class AnimatedImage extends Image {

    public AnimatedImage(Animation<TextureRegion> animation) {
        super(new AnimatedDrawable(animation));
        this.setSize(this.getWidth(), this.getHeight());
        this.setOrigin(this.getWidth() / 2f, this.getHeight() / 2f);
    }

}
