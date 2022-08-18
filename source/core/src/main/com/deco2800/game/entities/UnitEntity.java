package com.deco2800.game.entities;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.math.Vector2;

public class UnitEntity extends Entity {

    /**
     * each worker unit will have a set health upon spawning which will deplete 
     * when they are damaged by an enemy unit
     */
    private int health;
    /**
     * the worker unit's sprite will be surrounded by a region in which they can 
     * be selected, and take damage from enemies
     */
    private int hitboxRadius;
    /**
     * the time taken for a worker unit to travel a given distance should be fixed 
     * and specific to each unit type. The movement speed may vary based on where 
     * the unit is moving (i.e., faster over land than through water).
     */
    private int movementSpeed;
    /**
     * a worker unit's set of resources that they have gathered.
     */
    private UnitInventory unitInventory;
    /**
     * a list of objects on the map which the worker unit can interact with
     */
    private List<Entity> interactableObjects;
    /**
     * a queue for tasks which the worker unit performs in order. The player 
     * should be able to assign new tasks to a worker unit, or else tell a unit to 
     * halt their current task and start on a new one.
     */
    private Entity taskQueue;

    /**
     * Constructor with no params
     */
    public UnitEntity() {
        this.unitInventory = new UnitInventory();
        this.interactableObjects = new ArrayList<>();
        this.taskQueue = new Entity();
    }

    /**
     * Constructor with params
     * @param health health
     * @param hitboxRadius hitbox radius
     * @param movementSpeed movement speed
     * @param unitInventory unit inventory
     * @param interactableObjects interactable objects 
     * @param taskQueue task queue
     */
    public UnitEntity(int health, int hitboxRadius, int movementSpeed,
     UnitInventory unitInventory, List<Entity> interactableObjects,
     Entity taskQueue) {
        this.health = health;
        this.hitboxRadius = hitboxRadius;
        this.unitInventory = unitInventory;
        this.interactableObjects = interactableObjects;
        this.taskQueue = taskQueue;
    }

    /**
     * Return's the unit's hp
     * 
     * @return health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the unit's hp
     * @param health health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Gets the distance from centre position for setting the region where unit can take damage
     * @return hitbox radius
     */
    public int getHitboxRadius() {
        return hitboxRadius;
    }

    /**
     * Sets the distance from centre position for setting the region where unit can take damage
     * @param hitboxRadius hitbox radius
     */
    public void setHitboxRadius(int hitboxRadius) {
        this.hitboxRadius = hitboxRadius;
    }

    /**
     * Gets the unit's movement speed
     * @return movement speed
     */
    public int getMovementSpeed() {
        return movementSpeed;
    }

    /**
     * Sets the unit's movement speed
     * @param movementSpeed movement speed
     */
    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    /**
     * Gets the unit's inventory
     * @return unit inventory
     */
    public UnitInventory getUnitInventory() {
        return unitInventory;
    }

    /**
     * Sets the unit's inventory
     * @param unitInventory unit inventory 
     */
    public void setUnitInventory(UnitInventory unitInventory) {
        this.unitInventory = unitInventory;
    }

    /**
     * Gets the object entities on the map that the unit can interact with 
     * @return interactable objects
     */
    public List<Entity> getInteractableObjects() {
        return interactableObjects;
    }

    /**
     * Sets the object entities on the map that the unit can interact with 
     * @param interactableObjects interactable objects
     */
    public void setInteractableObjects(List<Entity> interactableObjects) {
        this.interactableObjects = interactableObjects;
    }

    /**
     * Gets the tasks in order of priority 
     * @return task queue
     */
    public Entity getTaskQueue() {
        return taskQueue;
    }

    /**
     * Sets the tasks in order of priority
     * @param taskQueue task queue
     */
    public void setTaskQueue(Entity taskQueue) {
        this.taskQueue = taskQueue;
    }

    /**
     * Checks if unit is dead
     * @return true if dead, false otherwise 
     */
    public boolean isDead() {
        if (getHealth() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the unit is hittable/selected 
     * @param position position
     * @return true if hittable, false otherwise 
     */
    public boolean isHittable(Vector2 position) {
        float min_x = getPosition().x - getHitboxRadius();
        float max_x = getPosition().x + getHitboxRadius();
        float min_y = getPosition().y - getHitboxRadius();
        float max_y = getPosition().x - getHitboxRadius();
        if (position.x >= min_x && position.x <= max_x && position.y >= min_y && position.y <= max_y) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the unit can interact with the given object
     * @param object object entity on the map
     * @return true if interactable, false otherwise 
     */
    public boolean isInteractable(Entity object) {
        if (this.interactableObjects.contains(object)) {
            return true;
        }
        return false;
    }

    /**
     * Decreases user's health and damage should only be positive
     * @param damage damage 
     */
    public void takeDamage(int damage) {
        if (damage > 0) {
            this.health -= damage;
        }
    }

    /**
     * Increases user's health and gain should only be positive
     * @param gain gain 
     */
    public void heal(int gain) {
        if (gain > 0) {
            this.health -= gain;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + health;
        result = prime * result + hitboxRadius;
        result = prime * result + ((interactableObjects == null) ? 0 : interactableObjects.hashCode());
        result = prime * result + movementSpeed;
        result = prime * result + ((taskQueue == null) ? 0 : taskQueue.hashCode());
        result = prime * result + ((unitInventory == null) ? 0 : unitInventory.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        UnitEntity other = (UnitEntity) obj;
        if (health != other.health)
            return false;
        if (hitboxRadius != other.hitboxRadius)
            return false;
        if (interactableObjects == null) {
            if (other.interactableObjects != null)
                return false;
        } else if (!interactableObjects.equals(other.interactableObjects))
            return false;
        if (movementSpeed != other.movementSpeed)
            return false;
        if (taskQueue == null) {
            if (other.taskQueue != null)
                return false;
        } else if (!taskQueue.equals(other.taskQueue))
            return false;
        if (unitInventory == null) {
            if (other.unitInventory != null)
                return false;
        } else if (!unitInventory.equals(other.unitInventory))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UnitEntity [health=" + health + ", hitboxRadius=" + hitboxRadius + ", interactableObjects="
                + interactableObjects + ", movementSpeed=" + movementSpeed + ", taskQueue=" + taskQueue
                + ", unitInventory=" + unitInventory + "]";
    }
}
