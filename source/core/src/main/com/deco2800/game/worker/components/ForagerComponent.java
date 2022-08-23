package com.deco2800.game.worker.components;

import com.deco2800.game.components.Component;

public class ForagerComponent extends Component{
    private int isForager;

    public ForagerComponent(){
        this.isForager = 1;
    }

    public int getIsForager(){
        return this.isForager;
    }    
}
