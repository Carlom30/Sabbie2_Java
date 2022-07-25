package Engine;

import java.util.Vector;

import Entity.Entity;
import Main.Utils.Directions;
import Math.Vector2;

public class CollisionLogic 
{
    GamePanel gp;
    
    public CollisionLogic(GamePanel gp)
    {
        this.gp = gp;
    }

    public void checkForCollision_Tile(Entity entity)
    {
        int leftCollisionX =  entity.worldPosition.x + entity.collisionArea.min.x; 
        int rightCollisionX = entity.worldPosition.x + entity.collisionArea.min.x + entity.collisionArea.width;
                                                       
        int topCollisionY = entity.worldPosition.y + entity.collisionArea.min.y; 
        int bottomCollisionY = entity.worldPosition.y + entity.collisionArea.min.y + entity.collisionArea.height;
        
        int entityLeftCol = leftCollisionX / gp.tileSize;
        int entityRightCol = rightCollisionX / gp.tileSize;

        int entityTopRow = topCollisionY / gp.tileSize;
        int entityBottomRow = bottomCollisionY / gp.tileSize;

        //ora la parte bruttina con gli if

        switch(entity.direction)
        {
            case up:
            {

                break;
            }
        }


        //if(entity.direction == Directions.up)

    }
}
