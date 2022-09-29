package com.deco2800.game.worker.resources;

import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;

public class CampSpawnStones extends DefaultTask implements PriorityTask{
    
    public CampSpawnStones(){}

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update(){
        float campX = owner.getEntity().getPosition().x;
        float campY = owner.getEntity().getPosition().y;

        // Check stones in (x+1,y), (x-1,y), (x,y+1), (x,y-1)
        // If there is no stone in the location, spawn a new stone
        int right_stone_detected = 0;
        int left_stone_detected = 0;
        int up_stone_detected = 0;
        int down_stone_detected = 0;

        for(int i=0; i<ServiceLocator.getEntityService().getEntities().size; i++){
            if(ServiceLocator.getEntityService().getEntities().get(i).getPosition().x == campX+1 && ServiceLocator.getEntityService().getEntities().get(i).getPosition().y == campY){
                right_stone_detected = 1;
            }else if(ServiceLocator.getEntityService().getEntities().get(i).getPosition().x == campX-1 && ServiceLocator.getEntityService().getEntities().get(i).getPosition().y == campY){
                left_stone_detected = 1;
            }else if(ServiceLocator.getEntityService().getEntities().get(i).getPosition().x == campX && ServiceLocator.getEntityService().getEntities().get(i).getPosition().y == campY+1){
                up_stone_detected = 1;
            }else if(ServiceLocator.getEntityService().getEntities().get(i).getPosition().x == campX && ServiceLocator.getEntityService().getEntities().get(i).getPosition().y == campY-1){
                down_stone_detected = 1;
            }
        }

        if(right_stone_detected == 0){
            Entity stone = StoneFactory.createStone();
            stone.setPosition(campX+1, campY);
            ServiceLocator.getEntityService().register(stone);
        }else if(left_stone_detected == 0){
            Entity stone = StoneFactory.createStone();
            stone.setPosition(campX-1, campY);
            ServiceLocator.getEntityService().register(stone);
        }else if(up_stone_detected == 0){
            Entity stone = StoneFactory.createStone();
            stone.setPosition(campX, campY+1);
            ServiceLocator.getEntityService().register(stone);
        }else if(down_stone_detected == 0){
            Entity stone = StoneFactory.createStone();
            stone.setPosition(campX, campY-1);
            ServiceLocator.getEntityService().register(stone);
        }
    }

}
