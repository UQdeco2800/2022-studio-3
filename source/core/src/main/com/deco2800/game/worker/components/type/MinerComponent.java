package com.deco2800.game.worker.components.type;

import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;

public class MinerComponent extends Component{
    private int isMiner;
    private Entity durationBarEntity;

    public MinerComponent(){
        this.isMiner = 1;
    }

    public int getIsMiner(){
        return this.isMiner;
    }

    public void setDurationBarEntity(Entity durationBar){
        this.durationBarEntity = durationBar;
    }
    public Entity getDurationBarEntity(){
        return this.durationBarEntity;
    }
}
