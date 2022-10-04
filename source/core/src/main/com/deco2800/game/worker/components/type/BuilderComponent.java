package com.deco2800.game.worker.components.type;

import com.deco2800.game.components.Component;

public class BuilderComponent extends Component {

    private int isBuilder;

    public BuilderComponent(){
        this.isBuilder = 1;
    }

    public int getIsBuilder(){
        return this.isBuilder;
    }

}