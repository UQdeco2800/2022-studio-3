package com.deco2800.game.components.worker.type;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.components.Component;

public class ForagerComponent extends Component {
    private int isForager;
    private Entity durationBarEntity;

    public ForagerComponent(){
        this.isForager = 1;
    }

    public int getIsForager(){
        return this.isForager;
    }
    
    public void setDurationBarEntity(Entity durationBar){
        this.durationBarEntity = durationBar;
    }
    public Entity getDurationBarEntity(){
        return this.durationBarEntity;
    }    
}
