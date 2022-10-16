package com.deco2800.game.components.friendlyunits.task;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.components.friendlyunits.SelectableComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnitIdleTask extends DefaultTask implements PriorityTask{

    private static final Logger logger = LoggerFactory.getLogger(UnitIdleTask.class);
    private boolean idling = false;
    private UnitMovementTask movementTask;
    private Vector2 startPos;
    private List<GridPoint2> path;

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
    public void start() {
        super.start();
        startPos = owner.getEntity().getPosition();
        movementTask = new UnitMovementTask(startPos);
        movementTask.create(owner);
        movementTask.start();
        owner.getEntity().getEvents().addListener("unitWalk", this::startMoving);

        selectableComponent = owner.getEntity().getComponent(SelectableComponent.class);
    }

    @Override
    public void update() {

        if (selectableComponent != null) {
            isSelected = selectableComponent.isSelected();
            isHovered = selectableComponent.isHovered();
        }


        if (movementTask.isMoving()) {
            movementTask.update();
        } else if (!idling) {
            // Start idling again
            if(isHovered || isSelected){
                owner.getEntity().getEvents().trigger("unitHighlightedIdleAnimate");
            }else{
                owner.getEntity().getEvents().trigger("unitIdleAnimate");
            }
            idling = true;
        } else if(idling){
            if(isHovered || isSelected){
                owner.getEntity().getEvents().trigger("unitHighlightedIdleAnimate");
            }else{
                owner.getEntity().getEvents().trigger("unitIdleAnimate");
            }
        }
    }

    /**
     * Moves the worker to the target
     *
     * @param target target of the worker
     */
    public void startMoving(Vector2 target) {
        logger.debug("Starting moving to {}", target);
        movementTask.stop();
        movementTask.setTarget(target);
        movementTask.start();

        // Get the vector of the target and the worker and trigger movement animation
        if (target.x > owner.getEntity().getPosition().x && target.y > owner.getEntity().getPosition().y){
            owner.getEntity().getEvents().trigger("unitBackwardRightMoveAnimate");
        } else if (target.x < owner.getEntity().getPosition().x && target.y > owner.getEntity().getPosition().y) {
            owner.getEntity().getEvents().trigger("unitBackwardLeftMoveAnimate");
        } else if (target.x < owner.getEntity().getPosition().x && target.y < owner.getEntity().getPosition().y) {
            owner.getEntity().getEvents().trigger("unitLeftMoveAnimate");
        } else if (target.x > owner.getEntity().getPosition().x && target.y < owner.getEntity().getPosition().y) {
            owner.getEntity().getEvents().trigger("unitRightMoveAnimate");
        } else {
            owner.getEntity().getEvents().trigger("unitIdleAnimate");
        }
        idling = false;
    }

    /**
     * Return the current movement task
     *
     * @return the movement task
     */
    public UnitMovementTask getMovementTask() {
        return this.movementTask;
    }

    /**
     * Return whether the worker is currently idling.
     *
     * @return True if the worker is stationary. False otherwise.
     */
    public boolean isIdling() {
        return this.idling;
    }
    
}
