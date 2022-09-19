package com.deco2800.game.components.worker.duration;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class DurationBar extends ProgressBar{
    public DurationBar(int width, int height){
        super(0f, 1f, 0.01f, false, new ProgressBarStyle());

        Pixmap bg_pixmap = new Pixmap(width, height, Format.RGBA8888);
        bg_pixmap.setColor(Color.RED);
        bg_pixmap.fill();
        TextureRegionDrawable bg_drawable = new TextureRegionDrawable(new TextureRegion(new Texture(bg_pixmap)));
        bg_pixmap.dispose();
        getStyle().background = bg_drawable;

        Pixmap knob_pixmap = new Pixmap(width, height, Format.RGBA8888);
        knob_pixmap.setColor(Color.GREEN);
        knob_pixmap.fill();
        TextureRegionDrawable knob_drawable = new TextureRegionDrawable(new TextureRegion(new Texture(knob_pixmap)));
        knob_pixmap.dispose();
        getStyle().knob = knob_drawable;

        Pixmap knob_before_pixmap = new Pixmap(width, height, Format.RGBA8888);
        knob_before_pixmap.setColor(Color.GREEN);
        knob_before_pixmap.fill();
        TextureRegionDrawable knob_before_drawable = new TextureRegionDrawable(new TextureRegion(new Texture(knob_before_pixmap)));
        knob_before_pixmap.dispose();
        getStyle().knobBefore = knob_before_drawable;

        setWidth(width);
		setHeight(height);
		setAnimateDuration(0.0f);
		setValue(1f);
		setAnimateDuration(0.25f);
    }
}
