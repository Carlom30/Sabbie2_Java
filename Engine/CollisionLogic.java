package Engine;

import java.util.Vector;

import Entity.Entity;
import Main.Utils.Directions;
import Math.Vector2;
import World.Map;

public class CollisionLogic 
{
    GamePanel gp;
    
    public CollisionLogic(GamePanel gp)
    {
        this.gp = gp;
    }

    public void checkForCollision_Tile(Entity entity)
    {
        Map map = gp.map;

        int leftCollisionX =  entity.worldPosition.x + entity.collisionArea.min.x; 
        int rightCollisionX = entity.worldPosition.x + entity.collisionArea.min.x + entity.collisionArea.width;
                                                       
        int topCollisionY = entity.worldPosition.y + entity.collisionArea.min.y; 
        int bottomCollisionY = entity.worldPosition.y + entity.collisionArea.min.y + entity.collisionArea.height;
        
        int entityLeftCol = leftCollisionX / gp.tileSize;
        int entityRightCol = rightCollisionX / gp.tileSize;

        int entityTopRow = topCollisionY / gp.tileSize;
        int entityBottomRow = bottomCollisionY / gp.tileSize;

        int offsetTileOne;
        int offsetTileTwo;
        
        //per il paradigma generato dai pixel e la grandezza dell'area di collisione, il player può intersecare ALPIU' due tile nello stesso momento

        //ora la parte bruttina con gli if

        switch(entity.direction)
        {
            case up:
            {
                //quello che faccio è predictare dove finirà il giocatore quando si muove nelle 4 direzioni cardinali. 
            /*
                in questo caso, ad esempio, quello che faccio è 
                rimovere dalla topcollisiony la velocità del player
                questo perché, per convenzione, l'origine degli assi
                in java è in alto a sinistra, quindi se il player
                si muove in alto, la Y diminuisce
            */
                entityTopRow = (topCollisionY - entity.velocity) / gp.tileSize; 
                offsetTileOne = entityTopRow * map.width + entityLeftCol;
                offsetTileTwo = entityTopRow * map.width + entityRightCol;

                if(map.tiles[offsetTileOne].collision == true || map.tiles[offsetTileTwo].collision == true)
                {
                    entity.collisionOn = true;
                }

                break;
            }


            //il resto ha la stessa logica di sopra
            case down:
            {
                entityBottomRow = (bottomCollisionY + entity.velocity) / gp.tileSize;
                offsetTileOne = entityBottomRow * map.width + entityLeftCol;
                offsetTileTwo = entityBottomRow * map.width + entityRightCol;

                if(map.tiles[offsetTileOne].collision == true || map.tiles[offsetTileTwo].collision == true)
                {
                    entity.collisionOn = true;
                }

                break;
            }

            case right:
            {
                break;
            }

            case left:
            {
                break;
            }
        }


        //if(entity.direction == Directions.up)

    }
}
