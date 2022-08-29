package com.deco2800.game.worker.components.duration;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.deco2800.game.rendering.RenderComponent;

public class DurationBarComponent extends RenderComponent{
    private SpriteBatch durationBarBatch;
    private Batch originalBatch;
    private ProgressBar durationBar;

    public DurationBarComponent(int width, int height){
        durationBar = new DurationBar(width, height);
    }

    @Override
    public void create(){
        super.create();
        var entity_position = entity.getPosition();
        durationBar.setPosition(entity_position.x + 10, entity_position.y + 10);
    }

    @Override
    public void draw(SpriteBatch batch){
        var entity_position = this.entity.getPosition();
        durationBar.setPosition(entity_position.x + 10, entity_position.y + 10);
        durationBar.draw(batch, 1);
    }

}
