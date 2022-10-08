package com.deco2800.game.components.friendly;

import com.badlogic.gdx.Input;
import com.deco2800.game.components.maingame.MainGameExitDisplay;
import com.deco2800.game.components.soldiermenu.SoldierMenuDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.services.ServiceLocator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FriendlyUnitInputComponent extends InputComponent {

    private static final Logger logger = LoggerFactory.getLogger(FriendlyUnitInputComponent.class);
    private Entity soldierDisplay;
    private int displayFlag;

    public FriendlyUnitInputComponent() {
        super(5);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button){
        if(button == Input.Buttons.LEFT){
            if(displayFlag == 1){
                soldierDisplay.dispose();
                ServiceLocator.getEntityService().unregister(soldierDisplay);
                displayFlag = 0;
            }else {
                soldierDisplay = new Entity();
                soldierDisplay.addComponent(new SoldierMenuDisplay());
                ServiceLocator.getEntityService().register(soldierDisplay);
                displayFlag = 1;
            }
        }
        return true;
    }
}
