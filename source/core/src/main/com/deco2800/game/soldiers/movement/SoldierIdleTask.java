package com.deco2800.game.soldiers.movement;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import java.util.List;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.worker.components.movement.WorkerMovementTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default state of the worker when they have nothing to do. Waits until a movement task is triggered.
 */
public class SoldierIdleTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(SoldierIdleTask.class);
    private boolean idling = false;
    private FightingMovementTask movementTask;
    private Vector2 startPos;

    // Selection
    private boolean isSelected = false;
    private boolean isHovered = false;
    private SelectableComponent selectableComponent;

    public SoldierIdleTask() { /* already has start */ }

    @Override
    public int getPriority() {
        return 2; // Low priority task
    }

    @Override
    public void start() {
        super.start();
        startPos = owner.getEntity().getPosition();
        movementTask = new FightingMovementTask(startPos, this.owner.getEntity());
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
                owner.getEntity().getEvents().trigger("soldierHighlightedIdleAnimate");
            }else{
                owner.getEntity().getEvents().trigger("soldierIdleAnimate");
            }
            idling = true;
        }else if(idling){
            if(isHovered || isSelected){
                owner.getEntity().getEvents().trigger("soldierHighlightedIdleAnimate");
            }else{
                owner.getEntity().getEvents().trigger("soldierIdleAnimate");
            }
        }
    }

    /**
     * Moves the soldier to the target
     *
     * @param target target of the soldier
     */
    public void startMoving(Vector2 target) {
        movementTask.stop();
        movementTask.setTarget(target);
        movementTask.start();

        if (target.x > owner.getEntity().getPosition().x && target.y > owner.getEntity().getPosition().y){
            owner.getEntity().getEvents().trigger("soldierBackwardRightMoveAnimate");
        } else if (target.x < owner.getEntity().getPosition().x && target.y > owner.getEntity().getPosition().y) {
            owner.getEntity().getEvents().trigger("soldierBackwardLeftMoveAnimate");
        } else if (target.x < owner.getEntity().getPosition().x && target.y < owner.getEntity().getPosition().y) {
            owner.getEntity().getEvents().trigger("soldierLeftMoveAnimate");
        } else if (target.x > owner.getEntity().getPosition().x && target.y < owner.getEntity().getPosition().y) {
            owner.getEntity().getEvents().trigger("soldierRightMoveAnimate");
        } else {
            owner.getEntity().getEvents().trigger("soldierIdleAnimate");
        }
        idling = false;
    }

    /**
     * Return the current movement task
     *
     * @return the movement task
     */
    public FightingMovementTask getMovementTask() {
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