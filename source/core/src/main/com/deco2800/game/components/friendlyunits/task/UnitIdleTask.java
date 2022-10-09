package com.deco2800.game.components.friendlyunits.task;

import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.components.friendlyunits.SelectableComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnitIdleTask extends DefaultTask implements PriorityTask{

    private static final Logger logger = LoggerFactory.getLogger(UnitIdleTask.class);
    private boolean idling = false;

    // Selection
    private boolean isSelected = false;
    private boolean isHovered = false;
    private SelectableComponent selectableComponent;

    public UnitIdleTask() {}

    @Override
    public int getPriority() {
        return 1; // Low priority task
    }

    @Override
    public void start(){
        super.start();
        selectableComponent = owner.getEntity().getComponent(SelectableComponent.class);
        idling = true;
        owner.getEntity().getEvents().trigger("unitIdleAnimate");
    }

    @Override
    public void update() {

        if (selectableComponent != null) {
            isSelected = selectableComponent.isSelected();
            isHovered = selectableComponent.isHovered();
        }


        // if (movementTask.isMoving()) {
        //     movementTask.update();
        // } else if (!idling) {
            // Start idling again
            if(isHovered || isSelected){
                owner.getEntity().getEvents().trigger("unitHighlightedIdleAnimate");
            }else{
                owner.getEntity().getEvents().trigger("unitIdleAnimate");
            }
            idling = true;
        // }
    }

    public boolean isIdling() {
        return this.idling;
    }
    
}
