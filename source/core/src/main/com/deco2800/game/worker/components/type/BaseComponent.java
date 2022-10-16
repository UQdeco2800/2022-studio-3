package com.deco2800.game.worker.components.type;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.resources.ResourceCountDisplay;
import com.deco2800.game.services.ServiceLocator;

public class BaseComponent extends Component {
    private int isBase;
    private int wood;
    private int metal;
    private int stone;

    public BaseComponent(){
        this.isBase = 1;
        this.wood = 10;
        this.metal = 0;
        this.stone = 2;
        // in case this is made after resource count
        updateDisplay();
    }

    public int getIsBase(){
        return this.isBase;
    }

    public int getWood(){
        return this.wood;
    }

    public int getMetal(){
        return this.metal;
    }

    public int getStone(){
        return this.stone;
    }

    public void updateBaseStats(int wood, int metal, int stone){
        this.wood = this.wood + wood;
        this.metal = this.metal + metal;
        this.stone = this.stone + stone;
    }

    public void updateDisplay(){
        ResourceCountDisplay displayComponent = null;
        for(int i=0; i<ServiceLocator.getEntityService().getEntities().size; i++){
            if(ServiceLocator.getEntityService().getEntities().get(i).getComponent(ResourceCountDisplay.class) != null){
                displayComponent = ServiceLocator.getEntityService().getEntities().get(i).getComponent(ResourceCountDisplay.class);
            }
        }
        // avoid potential crash if resource display doesn't exist
        if (displayComponent == null) {
            return;
        }
        displayComponent.updatePlayerResourceUI("wood", this.wood);
        displayComponent.updatePlayerResourceUI("metal", this.metal);
        displayComponent.updatePlayerResourceUI("stone", this.stone);
    }
}
