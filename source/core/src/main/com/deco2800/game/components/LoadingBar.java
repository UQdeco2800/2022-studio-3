package com.deco2800.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.GdxGame;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadingBar extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(HealthBarComponent.class);
    private Image background;
    private Image frame;
    private ProgressBar bar;
    private Label loading;
    private int progress;
    private boolean loaded;

    @Override
    public void create() {
        super.create();
        addActors();
        progress = 0;
        loaded = false;
    }

    public void addActors() {
        background = new Image(ServiceLocator.getResourceService().getAsset("images/loading_screen.png", Texture.class));
        background.setTouchable(Touchable.disabled);

        frame = new Image(ServiceLocator.getResourceService().getAsset("images/loading bar_4.png", Texture.class));
        frame.setScale(1.2f, 0.9f);
        frame.setTouchable(Touchable.disabled);

        TextureRegionDrawable drawable = new TextureRegionDrawable(new Texture("images/loading bar_5.png"));
        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
        style.knobBefore = drawable;

        bar = new ProgressBar(0f, 100f, 1f, false, style);
        bar.setHeight(frame.getHeight() * frame.getScaleY());
        bar.setWidth(frame.getWidth() * frame.getScaleX());
        bar.setRound(true);

        loading = new Label("", skin);

        stage.addActor(background);
        stage.addActor(bar);
        stage.addActor(loading);
        stage.addActor(frame);
    }

    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public void update() {
        logger.info("Loading... {}%", ServiceLocator.getResourceService().getProgress());
        if (ServiceLocator.getResourceService().loadForMillis(10)) {
            loaded = true;
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
        progress = ServiceLocator.getResourceService().getProgress();
//        progress = 50;
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        background.setSize(width, height);

        float y = height / 5f;
        float x = (width - frame.getWidth() * frame.getScaleX()) / 2f;

        frame.setPosition(x, y);
        bar.setPosition(x, y);
        loading.setPosition(x + frame.getWidth() * frame.getScaleX() / 3f, y + frame.getHeight() * frame.getScaleY() / 2f);
        loading.setText(String.format("Loading... %02d%%", ServiceLocator.getResourceService().getProgress()));
        bar.setValue(progress);
    }

    @Override
    public void dispose() {
        super.dispose();
        background.remove();
        frame.remove();
        bar.remove();
        loading.remove();
        logger.debug("Loading Bar Actors Disposed");
    }
}
