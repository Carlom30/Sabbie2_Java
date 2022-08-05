package Engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import Engine.Tile.TileType;
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

    //questa funzione setta collisionOn al player, ma ritorna pure le due tile avanti alla direzione dove punta l'avatar
    public List<Tile> checkForCollision_Tile(Entity entity)
    {
        List<Tile> collisionTiles = new ArrayList<Tile>();
        Map map = gp.map;

        int leftCollisionX =  entity.worldPosition.x + entity.collisionArea.min.x; 
        int rightCollisionX = entity.worldPosition.x + entity.collisionArea.min.x + entity.collisionArea.width;
                                                       
        int topCollisionY = entity.worldPosition.y + entity.collisionArea.min.y; 
        int bottomCollisionY = entity.worldPosition.y + entity.collisionArea.min.y + entity.collisionArea.height;
        
        int entityLeftCol = leftCollisionX / gp.tileSize;
        int entityRightCol = rightCollisionX / gp.tileSize;

        int entityTopRow = topCollisionY / gp.tileSize;
        int entityBottomRow = bottomCollisionY / gp.tileSize;

        int offsetTileOne = 0;
        int offsetTileTwo = 0;
        
        //per il paradigma generato dai pixel e la grandezza dell'area di collisione, il player può intersecare ALPIU' due tile nello stesso momento

        //ora la parte bruttina con gli if

        switch(entity.direction)
        {
            case up:
            {
                //quello che faccio è predictare dove finirà il giocatore quando si muove nelle 4 direzioni cardinali. 
            /*
                in questo caso, ad esempio, quello che faccio è 
                rimuovere dalla topcollisiony la velocità del player
                questo perché, per convenzione, l'origine degli assi
                in java è in alto a sinistra, quindi se il player
                si muove in alto, la Y diminuisce
            */
                entityTopRow = (topCollisionY - entity.velocity) / gp.tileSize; //il rounding non fa differenza, mi troverò sempre nella stessa tile
                offsetTileOne = entityTopRow * map.width + entityLeftCol;
                offsetTileTwo = entityTopRow * map.width + entityRightCol;

                break;
            }

            //il resto ha la stessa logica di sopra
            case down:
            {
                entityBottomRow = (bottomCollisionY + entity.velocity) / gp.tileSize;
                offsetTileOne = entityBottomRow * map.width + entityLeftCol;
                offsetTileTwo = entityBottomRow * map.width + entityRightCol;

                break;
            }

            case right:
            {
                entityRightCol = (rightCollisionX + entity.velocity) / gp.tileSize;
                offsetTileOne = entityTopRow * map.width + entityRightCol;
                offsetTileTwo = entityBottomRow * map.width + entityRightCol;

                break;
            }

            case left:
            {
                entityLeftCol = (leftCollisionX - entity.velocity) / gp.tileSize;
                offsetTileOne = entityTopRow * map.width + entityLeftCol;
                offsetTileTwo = entityBottomRow * map.width + entityLeftCol;

                break;
            }
        }

        if((map.tiles[offsetTileOne].collision == true || map.tiles[offsetTileTwo].collision == true) && gp.player.DEV_MODE == false)
        {
            entity.collisionOn = true;
            
        }

        //aggiungo un if identico per semplicità
        if(map.tiles[offsetTileOne].type == TileType.wall)
        {
            collisionTiles.add(map.tiles[offsetTileOne]);
        }

        if(map.tiles[offsetTileTwo].type == TileType.wall)
        {
            collisionTiles.add(map.tiles[offsetTileTwo]);
        }

        return collisionTiles;

        //if(entity.direction == Directions.up)

    }

    //funzione ch ritorna 
    Tile[] onCollisionEnter(Entity entity)
    {
        Tile[] collidedTiles = new Tile[2];

        Map map = gp.map;

        int leftCollisionX =  entity.worldPosition.x + entity.collisionArea.min.x; 
        int rightCollisionX = entity.worldPosition.x + entity.collisionArea.min.x + entity.collisionArea.width;
                                                       
        int topCollisionY = entity.worldPosition.y + entity.collisionArea.min.y; 
        int bottomCollisionY = entity.worldPosition.y + entity.collisionArea.min.y + entity.collisionArea.height;
        
        int entityLeftCol = leftCollisionX / gp.tileSize;
        int entityRightCol = rightCollisionX / gp.tileSize;

        int entityTopRow = topCollisionY / gp.tileSize;
        int entityBottomRow = bottomCollisionY / gp.tileSize;

        int offsetTileOne = 0;
        int offsetTileTwo = 0;
        
        //ritorno solamente due tile, non questioni di tempo non posso fare collisioni troppo precise
        

        

        return collidedTiles;
    }
}
