package com.deco2800.game.components.maingame;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

public class Explosion {
    AnimationRenderComponent animator ;
    private static final String RELEASE = "spell_effect";
    Entity entity;

    public Explosion() {
        entity = new Entity();
        animator = new AnimationRenderComponent(ServiceLocator.getResourceService().getAsset("images/spell.atlas", TextureAtlas.class));
        animator.addAnimation(RELEASE,0.1f, Animation.PlayMode.NORMAL);
        entity.setScale(20f,12f);
        entity.addComponent(animator);
        entity.setEntityName("Explosion");
    }

    public void create() {
    }

    public void dispose() {
        entity.dispose();
    }

    public Entity getEntity() { return this.entity; }

}
