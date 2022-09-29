package com.deco2800.game.worker.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.enemy.EnemySignal;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.components.type.BaseComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnemyDetectionComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(EnemyDetectionComponent.class);
    private HitboxComponent hitboxComponent;

    public EnemyDetectionComponent() {}

    @Override
    public void create(){
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    private void onCollisionStart(Fixture me, Fixture other) {
        if (me == null || (BodyUserData) other.getBody().getUserData() == null) {
            return;
        }
        /**
         * If 'other' doesn't have components related to 'attack', 'other' will be assumed as enemy
         */
        Entity isEnemy = ((BodyUserData) other.getBody().getUserData()).entity;
        EnemySignal hasEnemySignalComponent = isEnemy.getComponent(EnemySignal.class);
        if(hasEnemySignalComponent != null && hasEnemySignalComponent.getIsEnemy() == 1){
            returnToBase();
        }
    }

    /**
     * Gets the base entity.
     * @return The base entity.
     */
    public Entity getBase() {
        Entity base = null;
        for (Entity entity : ServiceLocator.getEntityService().getEntities()) {
            if (entity.getComponent(BaseComponent.class) != null) {
                base = entity;
            }
        }
        return base;
    }

    /**
     * Directs the worker to the base after enemy detection
     */
    public void returnToBase() {
        Entity base = this.getBase();
        if (base != null) {
            entity.getEvents().trigger("workerWalk", this.getBase().getCenterPosition());
        }   
    }
}
