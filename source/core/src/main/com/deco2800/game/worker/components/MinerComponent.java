package com.deco2800.game.worker.components;

import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinerComponent extends Component{
    private static final Logger logger = LoggerFactory.getLogger(WorkerInventoryComponent.class);
    private int isMiner;

    public MinerComponent(){
        this.isMiner = 1;
    }

    public int getIsMiner(){
        return this.isMiner;
    }
    
}
