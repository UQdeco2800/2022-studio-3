package com.deco2800.game.worker.resources;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.services.GameTime;

public class CampSpawnStones extends DefaultTask implements PriorityTask{
    
    private GameTime gameTime;
    private long right_stone_time;
    private long left_stone_time;
    private long up_stone_time;
    private long down_stone_time;
    private static final long SPAWN_INTERVAL = 10000;

    public CampSpawnStones(){
        this.gameTime = ServiceLocator.getTimeSource();
        this.right_stone_time = this.gameTime.getTime();
        this.left_stone_time = this.gameTime.getTime();
        this.up_stone_time = this.gameTime.getTime();
        this.down_stone_time = this.gameTime.getTime();
    }

    public static boolean checkTile(float x, float y){
        // Since worldToTile() is a static function, implement the hardcoded version of it for the purpose of this class
        float tileSize = -1;

        float b = tileSize / 2;
        float a = tileSize / 3.724f;

        int tileX = (int) ((x / b) - (y / a)) / 2;
        int tileY = (int) (y / a) + tileX;

        GridPoint2 tile = new GridPoint2(tileX, tileY);
        return ServiceLocator.getMapService().isOccupied(tile);
    }

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

        // Check if the stones detected and the tile is not occupied
        if(right_stone_detected == 0 || checkTile(campX+1, campY)){
            if((this.gameTime.getTime() - this.right_stone_time) > SPAWN_INTERVAL){
                Entity stone = StoneFactory.createStone();
                stone.setPosition(campX+1, campY);
                ServiceLocator.getEntityService().register(stone);
                this.right_stone_time = this.gameTime.getTime();
            }
        }else if(left_stone_detected == 0 || checkTile(campX-1, campY)){
            if((this.gameTime.getTime() - this.left_stone_time) > SPAWN_INTERVAL){
                Entity stone = StoneFactory.createStone();
                stone.setPosition(campX-1, campY);
                ServiceLocator.getEntityService().register(stone);
                this.left_stone_time = this.gameTime.getTime();
            }
        }else if(up_stone_detected == 0 || checkTile(campX, campY+1)){
            if((this.gameTime.getTime() - this.up_stone_time) > SPAWN_INTERVAL){
                Entity stone = StoneFactory.createStone();
                stone.setPosition(campX, campY+1);
                ServiceLocator.getEntityService().register(stone);
                this.up_stone_time = this.gameTime.getTime();
            }
        }else if(down_stone_detected == 0 || checkTile(campX, campY-1)){
            if((this.gameTime.getTime() - this.down_stone_time) > SPAWN_INTERVAL){
                Entity stone = StoneFactory.createStone();
                stone.setPosition(campX, campY-1);
                ServiceLocator.getEntityService().register(stone);
                this.down_stone_time = this.gameTime.getTime();
            }
        }
    }

}
