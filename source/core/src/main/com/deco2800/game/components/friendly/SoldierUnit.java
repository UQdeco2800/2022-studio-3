package com.deco2800.game.components.friendly;

import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;

public class SoldierUnit extends Component {
        private Entity defend;
        private GameArea gameArea;
        private int soldierUnit;

        public SoldierUnit(GameArea gameArea) {
            this.gameArea = gameArea;
        }

        public void create() {
            super.create();
            this.soldierUnit = 1;
        }
    }

