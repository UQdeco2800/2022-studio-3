package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DialogueBoxDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(DialogueBoxDisplay.class);
    private Label dialogue;
    private Label title;
    private Image backgroundTexture;
    private Image image;
    private TextButton dismissBtn;

    private boolean hidden;

    @Override
    public void create() {

        super.create();
        addActors();
    }

    public void addActors() {

        this.backgroundTexture = new Image(
                ServiceLocator
                        .getResourceService()
                        .getAsset("images/dialogue_box_pattern2_background.png", Texture.class)
        );
        this.backgroundTexture.setWidth(1085f);
        this.backgroundTexture.setHeight(143f * 1.5f);
        this.backgroundTexture.setPosition(358f * 1.5f + 5f, 0f);

        this.title = new Label("EXAMPLE TITLE", skin);
        this.title.setPosition(this.backgroundTexture.getX() + 30f, this.backgroundTexture.getHeight() - this.title.getHeight() - 25f);

        this.dialogue = new Label("EXAMPLE DIALOGUE TEXT", skin);
        this.dialogue.setPosition(this.backgroundTexture.getX() + 30f, (this.backgroundTexture.getHeight() / 2f) - 25f);

        this.dismissBtn = new TextButton("", new Skin(Gdx.files.internal("atlantis/exitButtonSkin.json")));
        this.dismissBtn.setWidth(45f);
        this.dismissBtn.setHeight(25f);
        this.dismissBtn.setPosition(this.backgroundTexture.getX() + this.backgroundTexture.getWidth() - this.dismissBtn.getWidth(),
                this.backgroundTexture.getHeight() - this.dismissBtn.getHeight());
        this.dismissBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                entity.getEvents().trigger("dialogueDismiss");
            }
        });

        this.image = new Image(
                ServiceLocator
                        .getResourceService()
                        .getAsset("images/dialogue_box_image_default.png", Texture.class)
        );
        this.image.setWidth(128f);
        this.image.setHeight(128f);
        this.image.setPosition(this.backgroundTexture.getX() + this.backgroundTexture.getWidth() - this.image.getWidth() - 30f, 35f);

        stage.addActor(this.backgroundTexture);
        stage.addActor(this.dialogue);
        stage.addActor(this.title);
        stage.addActor(this.image);
        stage.addActor(this.dismissBtn);

        this.show();
    }

    // TODO: update render to display new text
    public void setDialogue(String dialogue) {

        this.dialogue = new Label(dialogue, skin);
    }

    // TODO: update render to display new text
    public void setTitle(String title) {

        this.title = new Label(title, skin);
    }

    public void setImage(String imagePath) {

        this.image = new Image(new Texture(Gdx.files.internal(imagePath)));
    }

    public boolean isHidden() {

        return this.hidden;
    }

    public void hide() {

        if (this.isHidden())
            return;

        this.hidden = true;
    }

    public void show() {

        if (!this.isHidden())
            return;

        this.hidden = true;
    }

    @Override
    public void draw(SpriteBatch batch) {

        this.backgroundTexture.setVisible(!this.isHidden());
        this.dialogue.setVisible(!this.isHidden());
        this.title.setVisible(!this.isHidden());
        this.image.setVisible(!this.isHidden());
        this.dismissBtn.setVisible(!this.isHidden());
    }

    @Override
    public void dispose() {

        super.dispose();
        this.title.remove();
        this.dialogue.remove();
        this.backgroundTexture.remove();
        this.image.remove();
    }
}
