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
    CombatStatsComponent combat = new CombatStatsComponent(10, 100, 20, 5, 20);
    assertEquals(100, combat.getHealth());

    combat.setHealth(150);
    assertEquals(100, combat.getHealth());

    combat.setMaxHealth(150);
    combat.setHealth(150);
    assertEquals(150, combat.getHealth());

    combat.setHealth(-50);
    assertEquals(0, combat.getHealth());
  }

  @Test
  void shouldSetGetMaxHealth() {
    CombatStatsComponent combat = new CombatStatsComponent(10, 100, 20, 5, 20);
    assertEquals(100, combat.getMaxHealth());

    combat.setHealth(200);
    assertEquals(100, combat.getHealth());

    combat.setMaxHealth(500);
    combat.setHealth(200);
    assertEquals(200, combat.getHealth());

    combat.setMaxHealth(-50);
    assertEquals(1, combat.getMaxHealth());
  }

  @Test
  void shouldCheckIsDead() {
    CombatStatsComponent combat = new CombatStatsComponent(10, 100, 20, 5, 20);
    assertFalse(combat.isDead());

    combat.setHealth(0);
    assertTrue(combat.isDead());
  }

  @Test
  void shouldAddHealth() {
    CombatStatsComponent combat = new CombatStatsComponent(10, 100, 20, 5, 20);
    combat.addHealth(-500);
    assertEquals(0, combat.getHealth());

    combat.addHealth(100);
    combat.addHealth(-20);
    assertEquals(80, combat.getHealth());
  }

  @Test
  void shouldSetGetBaseAttack() {
    CombatStatsComponent combat = new CombatStatsComponent(10, 100, 20, 5, 20);
    assertEquals(20, combat.getBaseAttack());

    combat.setBaseAttack(150);
    assertEquals(150, combat.getBaseAttack());

    combat.setBaseAttack(-50);
    assertEquals(150, combat.getBaseAttack());
  }

  @Test
  void shouldSetGetBaseDefence() {
    CombatStatsComponent combat = new CombatStatsComponent(10, 100, 20, 5, 20);
    assertEquals(5, combat.getBaseDefence());

    combat.setBaseDefence(20);
    assertEquals(20, combat.getBaseDefence());

    combat.setBaseDefence(-10);
    assertEquals(20, combat.getBaseDefence());
  }

  @Test
  void shouldGetNumberOfTroops() {
    CombatStatsComponent combat = new CombatStatsComponent(10, 100, 20, 5, 20);
    assertEquals(10, combat.getTroops());

    combat.setTroops(150);
    assertEquals(150, combat.getTroops());

    combat.setTroops(-50);
    assertEquals(150, combat.getTroops());
  }

  @Test
  void shouldGetAttackRange() {
    CombatStatsComponent combat = new CombatStatsComponent(10, 100, 20, 5, 20, 30);
    assertEquals(30, combat.getRange());

    combat.setRange(150);
    assertEquals(150, combat.getRange());

    combat.setRange(-50);
    assertEquals(150, combat.getRange());
  }

  @Test
  void shouldGetLandSpeed() {
    CombatStatsComponent combat = new CombatStatsComponent(10, 100, 20, 5, 20f, 30);
    assertEquals(20f, combat.getLandSpeed());

    combat.setLandSpeed(150f);
    assertEquals(150f, combat.getLandSpeed());

    combat.setLandSpeed(-50f);
    assertEquals(150f, combat.getLandSpeed());
  }

  //TODO: add test for one combatant hitting another
}
