package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;


/** Render a static texture. */
public class HighlightedTextureRenderComponent extends RenderComponent {
    private Texture texture;

    Array<Entity> selectedEntities2 = new Array<>();
    /**
     * @param texturePath Internal path of static texture to render.
     *                    Will be scaled to the entity's scale.
     */
    public HighlightedTextureRenderComponent(String texturePath) {
        this(ServiceLocator.getResourceService().getAsset(texturePath, Texture.class));
    }

//...
    /** @param texture Static texture to render. Will be scaled to the entity's scale. */
    public HighlightedTextureRenderComponent(Texture texture) {
        this.texture = texture;
    }


    @Override
    protected void draw(SpriteBatch batch) {

        Array<Entity> selectedEntities = new Array<>();
        //Check for selected units
        for (Entity entity: ServiceLocator.getEntityService().getEntities()) {
            SelectableComponent selectedComponent = entity.getComponent(SelectableComponent.class);
            if (selectedComponent != null && selectedComponent.isSelected()) {
                selectedEntities.add(entity);
            }
        }

        if (!selectedEntities.isEmpty()) {
            selectedEntities2 = selectedEntities;
            for (Entity entity: selectedEntities) {
                TextureRenderComponent TRC = entity.getComponent(TextureRenderComponent.class);
                TRC.setTexture(texture);
            }
        }
        else {
            for (Entity entity: selectedEntities2) {
                TextureRenderComponent TRC = entity.getComponent(TextureRenderComponent.class);
                TRC.setTexture(TRC.getTextureOG());
            }
        }
    }
}
