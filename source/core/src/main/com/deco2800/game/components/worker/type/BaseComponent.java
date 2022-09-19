package com.deco2800.game.components.worker.type;

import com.deco2800.game.components.Component;

public class BaseComponent extends Component {
    private int isBase;

    public BaseComponent(){
        this.isBase = 1;
    }

    public int getIsBase(){
        return this.isBase;
    } 
}
