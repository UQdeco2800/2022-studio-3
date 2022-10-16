package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Texture;
import com.deco2800.game.services.ServiceLocator;

public class TextureImageComponent extends Component {
    private Texture asset;

    public TextureImageComponent(String assetPath) {
        asset = ServiceLocator.getResourceService().getAsset(assetPath, Texture.class);
    }

    public Texture getAsset() {
        return asset;
    }
}
