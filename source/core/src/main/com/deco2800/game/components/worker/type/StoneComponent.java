package com.deco2800.game.components.worker.type;

import com.deco2800.game.components.Component;

public class StoneComponent extends Component {
    private int isStone;

    public StoneComponent(){
        this.isStone = 1;
    }

    public int getIsStone(){
        return this.isStone;
    }
    
}
