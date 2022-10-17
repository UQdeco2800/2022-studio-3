package com.deco2800.game.soldiers.movement;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.components.friendlyunits.SelectableComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoldierIdleTask extends DefaultTask implements PriorityTask{

    private static final Logger logger = LoggerFactory.getLogger(SoldierIdleTask.class);
    private boolean idling = false;
    private SoldierMovementTask movementTask;
    private Vector2 startPos;
    private List<GridPoint2> path;

    // Selection
    private boolean isSelected = false;
    private boolean isHovered = false;
    private SelectableComponent selectableComponent;

    public SoldierIdleTask() {}

    @Override
    public int getPriority() {
        return 1; // Low priority task
    }

    @Override
    public void start() {
        super.start();
        startPos = owner.getEntity().getPosition();
        movementTask = new SoldierMovementTask(startPos);
        movementTask.create(owner);
        movementTask.start();
        owner.getEntity().getEvents().addListener("soldierWalk", this::startMoving);

        selectableComponent = owner.getEntity().getComponent(SelectableComponent.class);

        // Add attack animation
        owner.getEntity().getEvents().addListener("attackEast", this::attackRight);
        owner.getEntity().getEvents().addListener("attackWest", this::attackLeft);
        owner.getEntity().getEvents().addListener("attackNorth", this::attackRight);
        owner.getEntity().getEvents().addListener("attackSouth", this::attackRight);
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
                owner.getEntity().getEvents().trigger("soldierHighlightedIdleAnimate");
            }else{
                owner.getEntity().getEvents().trigger("soldierIdleAnimate");
            }
            idling = true;
        } else if(idling){
            if(isHovered || isSelected){
                owner.getEntity().getEvents().trigger("soldierHighlightedIdleAnimate");
            }else{
                owner.getEntity().getEvents().trigger("soldierIdleAnimate");
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
            owner.getEntity().getEvents().trigger("soldierHighlightedBackwardRightMoveAnimate");
        } else if (target.x < owner.getEntity().getPosition().x && target.y > owner.getEntity().getPosition().y) {
            owner.getEntity().getEvents().trigger("soldierHighlightedBackwardLeftMoveAnimate");
        } else if (target.x < owner.getEntity().getPosition().x && target.y < owner.getEntity().getPosition().y) {
            owner.getEntity().getEvents().trigger("soldierHighlightedLeftMoveAnimate");
        } else if (target.x > owner.getEntity().getPosition().x && target.y < owner.getEntity().getPosition().y) {
            owner.getEntity().getEvents().trigger("soldierHighlightedRightMoveAnimate");
        } else {
            owner.getEntity().getEvents().trigger("soldierHighlightedIdleAnimate");
        }
        idling = false;
    }

    /**
     * Return the current movement task
     *
     * @return the movement task
     */
    public SoldierMovementTask getMovementTask() {
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

    private void attackLeft(){
        owner.getEntity().getEvents().trigger("soldierLeftAttackAnimate");
    }

    private void attackRight(){
        owner.getEntity().getEvents().trigger("soldierRightAttackAnimate");
    }
    
}