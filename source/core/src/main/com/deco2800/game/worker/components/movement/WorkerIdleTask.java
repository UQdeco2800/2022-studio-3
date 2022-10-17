package com.deco2800.game.worker.components.movement;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.components.friendlyunits.SelectableComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WorkerIdleTask extends DefaultTask implements PriorityTask{
    private static final Logger logger = LoggerFactory.getLogger(WorkerIdleTask.class);
    private boolean idling = false;
    private WorkerMovementTask movementTask;
    private Vector2 startPos;
    private List<GridPoint2> path;

    // Selection
    private boolean isSelected = false;
    private boolean isHovered = false;
    private SelectableComponent selectableComponent;

    public WorkerIdleTask() {}

    @Override
    public int getPriority() {
        return 1; // Low priority task
    }

    @Override
    public void start() {
        super.start();
        startPos = owner.getEntity().getPosition();
        movementTask = new WorkerMovementTask(startPos);
        movementTask.create(owner);
        movementTask.start();
        owner.getEntity().getEvents().addListener("workerWalk", this::startMoving);

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
                owner.getEntity().getEvents().trigger("workerHighlightedIdleAnimate");
            }else{
                owner.getEntity().getEvents().trigger("workerIdleAnimate");
            }
            idling = true;
        } else if(idling){
            if(isHovered || isSelected){
                owner.getEntity().getEvents().trigger("workerHighlightedIdleAnimate");
            }else{
                owner.getEntity().getEvents().trigger("workerIdleAnimate");
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
            owner.getEntity().getEvents().trigger("workerHighlightedBackwardRightMoveAnimate");
        } else if (target.x < owner.getEntity().getPosition().x && target.y > owner.getEntity().getPosition().y) {
            owner.getEntity().getEvents().trigger("workerHighlightedBackwardLeftMoveAnimate");
        } else if (target.x < owner.getEntity().getPosition().x && target.y < owner.getEntity().getPosition().y) {
            owner.getEntity().getEvents().trigger("workerHighlightedLeftMoveAnimate");
        } else if (target.x > owner.getEntity().getPosition().x && target.y < owner.getEntity().getPosition().y) {
            owner.getEntity().getEvents().trigger("workerHighlightedRightMoveAnimate");
        } else {
            owner.getEntity().getEvents().trigger("workerHighlightedIdleAnimate");
        }
        idling = false;
    }

    /**
     * Return the current movement task
     *
     * @return the movement task
     */
    public WorkerMovementTask getMovementTask() {
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
