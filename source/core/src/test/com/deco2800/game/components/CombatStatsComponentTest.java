package com.deco2800.game.components;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(GameExtension.class)
class CombatStatsComponentTest {
  @Test
  void shouldSetGetHealth() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 10, 10);
    assertEquals(100, combat.getHealth());

    combat.setHealth(150);
    assertEquals(150, combat.getHealth());

    combat.setHealth(-50);
    assertEquals(0, combat.getHealth());
  }

  @Test
  void shouldCheckIsDead() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 10, 10);
    assertFalse(combat.isDead());

    combat.setHealth(0);
    assertTrue(combat.isDead());
  }

  @Test
  void shouldAddHealth() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 10, 10);
    combat.addHealth(-500);
    assertEquals(0, combat.getHealth());

    combat.addHealth(100);
    combat.addHealth(-20);
    assertEquals(80, combat.getHealth());
  }

  @Test
  void shouldSetGetBaseAttack() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 10, 10);
    assertEquals(20, combat.getAttack());

    combat.setAttack(150);
    assertEquals(150, combat.getAttack());

    combat.setAttack(-50);
    assertEquals(150, combat.getAttack());
  }

  @Test
  void shouldSetGetBaseDefence() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 10, 10);
    assertEquals(10, combat.getDefence());

    combat.setDefence(150);
    assertEquals(150, combat.getDefence());

    combat.setDefence(-50);
    assertEquals(150, combat.getDefence());
  }

  @Test
  void shouldSetGetBaseSpeed() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 10, 10);
    assertEquals(10, combat.getSpeed());

    combat.setSpeed(150);
    assertEquals(150, combat.getSpeed());

    combat.setSpeed(-50);
    assertEquals(150, combat.getSpeed());
  }
}
