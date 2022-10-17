package com.deco2800.game.worker.components.movement;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import java.util.List;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.map.MapService;
import com.deco2800.game.services.ServiceLocator;

import java.security.Provider.Service;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default state of the worker when they have nothing to do. Waits until a movement task is triggered.
 */
public class WorkerIdleTask extends DefaultTask implements PriorityTask {
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
        selectableComponent = owner.getEntity().getComponent(SelectableComponent.class);
        startPos = owner.getEntity().getPosition();
        movementTask = new WorkerMovementTask(startPos);
        movementTask.create(owner);
        movementTask.start();
        owner.getEntity().getEvents().addListener("workerWalk", this::startMoving);
    }

    @Override
    public void update() {

        if (selectableComponent != null) {
            isSelected = selectableComponent.isSelected();
            isHovered = selectableComponent.isHovered();
        }

        if(isHovered || isSelected){
            if (movementTask.isMoving()) {
                movementTask.update();
            } else if (!idling) {
                // Start idling again
                owner.getEntity().getEvents().trigger("workerHighlightedIdleAnimate");
                idling = true;
            }
        }else{
            if (movementTask.isMoving()) {
                movementTask.update();
            } else if (!idling) {
                // Start idling again
                owner.getEntity().getEvents().trigger("workerIdleAnimate");
                idling = true;
            }
        }
    }

    /**
     * Moves the worker to the target
     *
     * @param target target of the worker
     */
    public void startMoving(Vector2 target) {

        if (selectableComponent != null) {
            isSelected = selectableComponent.isSelected();
            isHovered = selectableComponent.isHovered();
        }

        logger.debug("Starting moving to {}", target);
        movementTask.stop();
        movementTask.setTarget(target);
        movementTask.start();
        
        // Get the vector of the target and the worker
        // If target's X & Y point is bigger than the worker's X point, call the back right animation
        // If target's X point is smaller than the worker's X point but target's Y > worker's Y point, call the left animation
        if(isHovered || isSelected){
            if (target.x > owner.getEntity().getPosition().x && target.y > owner.getEntity().getPosition().y){
                owner.getEntity().getEvents().trigger("workerHighlightedBackRightMove");
            } else if (target.x < owner.getEntity().getPosition().x && target.y > owner.getEntity().getPosition().y) {
                owner.getEntity().getEvents().trigger("workerHighlightedBackLeftMove");
            } else if (target.x < owner.getEntity().getPosition().x && target.y < owner.getEntity().getPosition().y) {
                owner.getEntity().getEvents().trigger("workerHighlightedForwardLeftMove");
            } else if (target.x > owner.getEntity().getPosition().x && target.y < owner.getEntity().getPosition().y) {
                owner.getEntity().getEvents().trigger("workerHighlightedForwardRightMove");
            } else {
                owner.getEntity().getEvents().trigger("workerHighlightedIdleAnimate");
            }
            idling = false;
        }else{
            if (target.x > owner.getEntity().getPosition().x && target.y > owner.getEntity().getPosition().y){
                owner.getEntity().getEvents().trigger("workerBackRightMove");
            } else if (target.x < owner.getEntity().getPosition().x && target.y > owner.getEntity().getPosition().y) {
                owner.getEntity().getEvents().trigger("workerBackLeftMove");
            } else if (target.x < owner.getEntity().getPosition().x && target.y < owner.getEntity().getPosition().y) {
                owner.getEntity().getEvents().trigger("workerForwardLeftMove");
            } else if (target.x > owner.getEntity().getPosition().x && target.y < owner.getEntity().getPosition().y) {
                owner.getEntity().getEvents().trigger("workerForwardRightMove");
            } else {
                owner.getEntity().getEvents().trigger("workerIdleAnimate");
            }
            idling = false;
        }
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
