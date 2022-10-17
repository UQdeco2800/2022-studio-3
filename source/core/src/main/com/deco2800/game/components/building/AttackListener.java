package com.deco2800.game.components.building;

import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.EnemyFactory;
import com.deco2800.game.soldiers.factories.ArcherFactory;

public class AttackListener extends Component {
    private Entity target;
    private GameArea gameArea;

    public AttackListener(Entity target, GameArea gameArea) {
        this.target = target;
        this.gameArea = gameArea;
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("attack", this::attack);
        entity.getEvents().addListener("shoot", this::shoot);
    }

    void attack() {
        gameArea.spawnEntity(EnemyFactory.createBullet(this.entity, target, gameArea));
    }

    void shoot() {
        gameArea.spawnEntity(ArcherFactory.createArrow(this.entity, target, gameArea));
    }
}
