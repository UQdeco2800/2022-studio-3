package com.deco2800.game.components.enemy;

import com.deco2800.game.components.Component;

public class EnemySignal extends Component{
    private int isEnemy;

    public EnemySignal(){
        this.isEnemy = 1;
    }

    public int getIsEnemy(){
        return this.isEnemy;
    }
}
