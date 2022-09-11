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

        //Check for selected units
        for (Entity entity: ServiceLocator.getEntityService().getEntities()) {
            TextureRenderComponent TRC = entity.getComponent(TextureRenderComponent.class);
            if (TRC != null){
                SelectableComponent selectedComponent = entity.getComponent(SelectableComponent.class);
                if (selectedComponent != null && selectedComponent.isSelected()) {
                    TRC.setTexture(texture);
                }
                else {
                    TRC.setTexture(TRC.getTextureOG());
                }
            }
        }
    }
}
