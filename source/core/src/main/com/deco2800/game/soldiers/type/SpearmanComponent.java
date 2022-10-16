package com.deco2800.game.soldiers.type;

import com.deco2800.game.components.Component;

public class SpearmanComponent extends Component {
        
    private int isSpearman;
    
    public SpearmanComponent(){
        this.isSpearman = 1;
    }
    
    public int getIsSpearman(){
        return this.isSpearman;
    }
}
