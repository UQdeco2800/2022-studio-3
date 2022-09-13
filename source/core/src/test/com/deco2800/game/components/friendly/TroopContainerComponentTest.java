package com.deco2800.game.components.friendly;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TroopContainerComponentTest {

    @Test
    void shouldCreateTroops() {
        Entity troop1 = spy(Entity.class);
        Entity troop2 = spy(Entity.class);
        ArrayList<Entity> troops = new ArrayList<>();
        troops.add(troop2);
        troops.add(troop1);
        TroopContainerComponent container =
                new TroopContainerComponent(troops, 1f);

        // need parent centre for formation
        Entity parent = new Entity();
        parent.addComponent(container);

        // check that we create the first time
        container.create();
        verify(troop1).create();
        verify(troop2).create();
        // check that we don't create twice
        container.create();
        verify(troop1, times(1)).create();
        verify(troop2, times(1)).create();
    }

    @Test
    void shouldCreateInFormation() {
        Entity troop1 = new Entity();
        Entity troop2 = new Entity();
        Entity troop3 = new Entity();
        troop3.setScale(2, 2);
        ArrayList<Entity> troops = new ArrayList<>();
        troops.add(troop1);
        troops.add(troop2);
        troops.add(troop3);

        TroopContainerComponent container =
                new TroopContainerComponent(troops, 12f/7f);

        // need parent centre for formation
        Entity parent = new Entity();
        parent.addComponent(container);
        parent.setScale(2, 2);
        parent.setPosition(3, 3);

        // since we start at 225 degrees, troop3 should be directly above parent
        // so troop3 centre is parent.position + (1, 1) + (0, 1) + (1, 1)
        parent.create();
        assertEquals(4f, troop3.getCenterPosition().x, 0.1f);
        assertEquals(4.75f, troop3.getCenterPosition().y, 0.1f);
    }

    @Test
    void update() {
        Entity troop1 = spy(Entity.class);
        Entity troop2 = spy(Entity.class);
        ArrayList<Entity> troops = new ArrayList<>();
        troops.add(troop1);
        troops.add(troop2);
        TroopContainerComponent container =
                new TroopContainerComponent(troops, 12f/7f);

        // need parent centre for formation
        Entity parent = new Entity();
        parent.addComponent(container);
        parent.create();
        parent.update();
        parent.update();
        verify(troop1, times(2)).update();
        verify(troop2, times(2)).update();

    }

    @Test
    void getFormationOfOne() {
        Vector2 centre = new Vector2(3, 3);
        ArrayList<Vector2> actual =
                TroopContainerComponent.getFormation(centre, 15f, 1);
        ArrayList<Vector2> expected = new ArrayList<>();
        expected.add(centre);
        assertEquals(expected, actual);
    }

    @Test
    void getFormationOfNegative() {
        ArrayList<Vector2> actual =
                TroopContainerComponent.getFormation(new Vector2(0, 0), 15f,
                        -3);
        assertEquals(new ArrayList<Vector2>(), actual);
    }
}