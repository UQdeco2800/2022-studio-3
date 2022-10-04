package com.deco2800.game.components.friendlyunits;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

public class AvatarIconComponent extends RenderComponent {

    private Texture texture;
    public String texturePath;


    public AvatarIconComponent(String texturePath) {
        this(ServiceLocator.getResourceService().getAsset(texturePath, Texture.class));
        this.texturePath = texturePath;
    }

    public AvatarIconComponent(Texture texture) {
        this.texture = texture;
    }

    @Override
    protected void draw(SpriteBatch batch) {

    }
}
