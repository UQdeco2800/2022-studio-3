package com.deco2800.game.worker.components;

import com.deco2800.game.components.Component;

public class MinerComponent extends Component{
    private int isMiner;

    public MinerComponent(){
        this.isMiner = 1;
    }

    public int getIsMiner(){
        return this.isMiner;
    }
}
