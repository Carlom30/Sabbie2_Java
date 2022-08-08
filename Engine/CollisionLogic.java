package Engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.text.Utilities;

import Engine.Tile.TileType;
import Entity.Entity;
import Main.Utils;
import Main.Utils.Directions;
import Math.RectInt;
import Math.Vector2;
import Object.SuperObject;
import World.Map;

import java.awt.Rectangle;

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
        entity.collisionArea.min = entity.collisionAreaMin_Default;
        List<Tile> collisionTiles = new ArrayList<Tile>();
        Map map = gp.player.linkedMap;

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
    public static SuperObject checkForCollision_Obj(Entity entity, boolean isPlayer)
    {   
        //nonostante i due for, il costo di questa funzione è sempre O(4n) in quanto il secondo for annidato ha lunghezza sempre di 4,
        //4 è una costante trascurabile, quindi il costo rimane lineare: O(n)
        SuperObject checkObj = null;
        
        for(SuperObject obj : GamePanel.printableObj)
        {
            //get the entity and obj solid area positions
            entity.collisionArea.min = 
                new Vector2((entity.collisionArea.min.x + entity.worldPosition.x), (entity.collisionArea.min.y + entity.worldPosition.y));

            obj.collisionArea.min =  
                new Vector2((obj.collisionArea.min.x + obj.worldPos.x), (obj.collisionArea.min.y + obj.worldPos.y));
            
            /* questo for, (che ha un costo trascurabile, in quanto length è sempre 4, quindi O(1)) mi permette di evitare 
             * dei check direction by direction con if, switch o roba simile. Quello che faccio è:
             * 
             * - trovare la direzione dell'entità, 
             * - prendo la sua direzione vettoriale,
             * - moltiplico il vettore per lo scalare velocità dell'entità.
             * 
             * in questo modo, ho il vettore direzione non unitario che posso usare per fare check di intersezione
             *  
            */
            for(int i = 0; i < Utils.allDirections.length; i++)
            {
                if(Utils.allDirections[i] == entity.direction)
                {
                    Vector2 entityDirection = Vector2.scalarPerVector(Vector2.directionsVector[i], entity.velocity);
                    entity.collisionArea.min = Vector2.vectorSumm(entity.collisionArea.min, entityDirection);

                    if(RectInt.intersect(entity.collisionArea, obj.collisionArea))
                    {
                        Utils.printf("Obj: " + obj + " collision on: " + Utils.allDirections[i]);
                        
                        checkObj = obj;

                        if(obj.collision)
                            entity.collisionOn = true;
                    }
                    
                    break;
                }
                
                //Utils.printf("direction not found in obj collision");
                
                //return null;
            }
            obj.collisionArea.min = obj.collsionAreaMin_Default;
            entity.collisionArea.min = entity.collisionAreaMin_Default;
        }
        
        return checkObj;
    }
}
