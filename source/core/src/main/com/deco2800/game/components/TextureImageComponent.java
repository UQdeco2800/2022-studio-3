package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Texture;
import com.deco2800.game.services.ServiceLocator;

/**
 * A very simple component that stores refernece to the buildings
 * texture, this is used by the building UI to display an image
 * of the UI, if the building uses an atlas file rather than
 * a texture
 */
public class TextureImageComponent extends Component {
    private Texture asset;

    public TextureImageComponent(String assetPath) {
        asset = ServiceLocator.getResourceService().getAsset(assetPath, Texture.class);
    }

    /**
     * Returns the UI display texture of the building
     * @return UI display texture.
     */
    public Texture getAsset() {
        return asset;
    }
}
