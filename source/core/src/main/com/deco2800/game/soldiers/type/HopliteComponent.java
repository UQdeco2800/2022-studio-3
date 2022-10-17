package com.deco2800.game.soldiers.type;

import com.deco2800.game.components.Component;

public class HopliteComponent extends Component {
    
    private int isHoplite;

    public HopliteComponent(){
        this.isHoplite = 1;
    }

    public int getIsHoplite(){
        return this.isHoplite;
    }
}
