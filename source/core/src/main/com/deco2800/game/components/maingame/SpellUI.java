package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.components.mainmenu.MainMenuDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.utils.random.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class SpellUI extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);


    private Image spellBoxImage;
    private Image spellBtnImage;

    private Image castImage;

    AnimationRenderComponent animator;

    Entity spell;

    Timer timer;

    float initialHeight;
    float initialWidth;

    ImageButton spellBtn;

    Array<Entity> enemyEntities;

    private boolean pseudoCast;

    int screenX;

    int screenY;

    @Override
    public void create() {
        this.pseudoCast = false;
        super.create();
        addActors();
        entity.getEvents().addListener("update pointer", this::updatePointer);
        entity.getEvents().addListener("release spell", this::onRelease);
        this.timer = new Timer(10000, 10001);
    }

    public void updatePointer(int screenX, int screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
    }


    @Override
    public void update() {
        if(timer.isTimerExpired()) {
            spellBtn.setVisible(true);
        }
        if(spell != null && spell.getComponent(AnimationRenderComponent.class).isFinished()){
            spell.getComponent(AnimationRenderComponent.class).stopAnimation();
            spell.setEnabled(false);
        }
    }

    private void addActors() {
        spellBoxImage = new Image(ServiceLocator.getResourceService().getAsset("images/spellbox-zeus.png", Texture.class));

        this.initialHeight = spellBoxImage.getHeight();
        this.initialWidth = spellBoxImage.getWidth();
        spellBoxImage.setWidth((float) (initialWidth * 0.7));
        spellBoxImage.setHeight((float) (initialHeight * 0.7));
        spellBoxImage.setPosition(0f, Gdx.graphics.getHeight() - spellBoxImage.getHeight());

        spellBtnImage = new Image(ServiceLocator.getResourceService().getAsset("images/spell-btn-unclickable.png", Texture.class));
        spellBtnImage.setPosition((int) (initialWidth/2.24), Gdx.graphics.getHeight() - spellBoxImage.getHeight());
        spellBtnImage.setWidth(spellBoxImage.getWidth() - (float) (initialWidth/2.24));
        spellBtnImage.setHeight((float) (spellBoxImage.getHeight()/2.6));

        Texture spellBtnTexture = new Texture(Gdx.files.internal("images/spell-btn.png"));
        spellBtn = new ImageButton(new TextureRegionDrawable(spellBtnTexture));
        spellBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Spell Release button clicked");
                        pseudoCast = true;
                    }
                });
        spellBtn.setHeight((float) (spellBoxImage.getHeight()/2.6));
        spellBtn.setWidth(spellBoxImage.getWidth() - (float) (initialWidth/2.24));
        spellBtn.setPosition((int) (initialWidth/2.24), Gdx.graphics.getHeight() - spellBoxImage.getHeight());

        castImage = new Image(ServiceLocator.getResourceService().getAsset("images/SpellIndicator/spelliso.png", Texture.class));
        castImage.setHeight(1000);
        castImage.setWidth(1000);

        stage.addActor(spellBoxImage);
        stage.addActor(spellBtnImage);
        stage.addActor(spellBtn);

    }

    public void updateTables() {
        enemyEntities = new Array<>();

        String[] enemy = {"blueJoker","snake","titan","wolf"};
        List enemyList = Arrays.asList(enemy);

        for (Entity entity: ServiceLocator.getEntityService().getEntities()) {
            String name = entity.getEntityName();
            if (name != null && enemyList.contains(name)) {
                enemyEntities.add(entity);
            }
        }

        if (pseudoCast) {
            stage.addActor(castImage);
            castImage.setPosition(screenX - castImage.getImageWidth()/2, Gdx.graphics.getHeight() - screenY - castImage.getImageHeight()/2);
        } else {
            castImage.remove();
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
        updateTables();
    }

    @Override
    public void dispose() {
        spellBoxImage.remove();
        spellBtnImage.remove();
        spellBtn.remove();
    }

    private void onRelease() {
        if (pseudoCast) {
            logger.info("Releasing Spell");
            spellBtn.setVisible(false);
            this.timer = new Timer(15000, 15001);
            for (Entity entity : ServiceLocator.getEntityService().getEntities()) {
                if (entity.getEntityName() == "Explosion") {
                    spell = entity;
                    break;
                }
            }
//            spell.setEnabled(true);

            spell.setPosition(screenToWorldPosition(screenX - 613, screenY + 345));

            spell.getComponent(AnimationRenderComponent.class).startAnimation("spell_effect");

            for (Entity entity : enemyEntities) {
                entity.dispose();
            }
            pseudoCast = false;
        }
    }

    public Vector2 screenToWorldPosition(int screenX, int screenY) {
        Vector3 worldPos = ServiceLocator.getEntityService().getCamera().unproject(new Vector3(screenX, screenY, 0));
        return new Vector2(worldPos.x, worldPos.y);
    }
}
