package Engine;

import java.util.ArrayList;
import java.util.List;
import Entity.Entity;
import Entity.Player;
import Main.Utils;
import Math.RectInt;
import Math.Vector2;
import Obj.SuperObject;
import World.Map;

public class CollisionLogic 
{
    GamePanel gp;

    public enum CollisionType
    {
        nextTiles,
        onStepTile;
    }

    //IMPORTANTE TODO: FARE FUNZIONE CHE SETTA COLLISION TRUE SE UNA TILE TOCCA UN OGGETTO.

    public CollisionLogic(GamePanel gp)
    {
        this.gp = gp;
    }


    //questa funzione setta collisionOn al player, ma ritorna pure le due tile avanti alla direzione dove punta l'avatar
    //inoltre, la lista di entity passata come parametro serve per testare collisioni con entità senza creare funzoini simili.
    public List<Tile> checkForCollision_Tile(Entity entity, CollisionType type, List<Entity> collidedEntities)
    {
        entity.collisionArea.min = entity.collisionAreaMin_Default;
        List<Tile> collisionTiles = new ArrayList<Tile>();
        Map map = GamePanel.player.linkedMap;

        int leftCollisionX =  entity.worldPosition.x + entity.collisionArea.min.x; 
        int rightCollisionX = entity.worldPosition.x + entity.collisionArea.min.x + entity.collisionArea.width;
                                                       
        int topCollisionY = entity.worldPosition.y + entity.collisionArea.min.y; 
        int bottomCollisionY = entity.worldPosition.y + entity.collisionArea.min.y + entity.collisionArea.height;
        
        int entityLeftCol = leftCollisionX / GamePanel.tileSize;
        int entityRightCol = rightCollisionX / GamePanel.tileSize;

        int entityTopRow = topCollisionY / GamePanel.tileSize;
        int entityBottomRow = bottomCollisionY / GamePanel.tileSize;

        int offsetTileOne = 0;
        int offsetTileTwo = 0;


        if(type == CollisionType.onStepTile)
        {
            //nel caso peggiore, l'area di collisione del pg sta calpestando 4 tile, e quindi ogni lato ne toccherà 2,
            //per essere estremamente sicuri ciò che si sta toccando, necessitano 8 offset (4 * 2) ma alla fine se ne avranno solo 4
            int[] onStepOffset = 
            {
                entityTopRow * map.width + entityLeftCol,
                entityTopRow * map.width + entityRightCol,

                entityBottomRow * map.width + entityLeftCol,
                entityBottomRow * map.width + entityRightCol,

                entityTopRow * map.width + entityRightCol,
                entityBottomRow * map.width + entityRightCol,

                entityTopRow * map.width + entityLeftCol,
                entityBottomRow * map.width + entityLeftCol
            };

            for(int i = 0; i < Utils.allDirections.length; i++)
            {
                Tile tile = map.tiles[onStepOffset[i]];
                if(!collisionTiles.contains(tile))
                {
                    collisionTiles.add(tile);
                }
            }
            return collisionTiles;
        }
        
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
                entityTopRow = (topCollisionY - entity.velocity) / GamePanel.tileSize; //il rounding non fa differenza, mi troverò sempre nella stessa tile
                offsetTileOne = entityTopRow * map.width + entityLeftCol;
                offsetTileTwo = entityTopRow * map.width + entityRightCol;

                break;
            }

            //il resto ha la stessa logica di sopra
            case down:
            {
                entityBottomRow = (bottomCollisionY + entity.velocity) / GamePanel.tileSize;
                offsetTileOne = entityBottomRow * map.width + entityLeftCol;
                offsetTileTwo = entityBottomRow * map.width + entityRightCol;

                break;
            }

            case right:
            {
                entityRightCol = (rightCollisionX + entity.velocity) / GamePanel.tileSize;
                offsetTileOne = entityTopRow * map.width + entityRightCol;
                offsetTileTwo = entityBottomRow * map.width + entityRightCol;

                break;
            }

            case left:
            {
                entityLeftCol = (leftCollisionX - entity.velocity) / GamePanel.tileSize;
                offsetTileOne = entityTopRow * map.width + entityLeftCol;
                offsetTileTwo = entityBottomRow * map.width + entityLeftCol;

                break;
            }

            case ALL_DIRECTIONS:
                break;
        }

        if((map.tiles[offsetTileOne].collision == true || map.tiles[offsetTileTwo].collision == true) && GamePanel.player.DEV_MODE == false)
        {
            entity.collisionOn = true;
            
        }

        //aggiungo un if identico per semplicità
        if(map.tiles[offsetTileOne].collision)
        {
            collisionTiles.add(map.tiles[offsetTileOne]);
        }

        if(map.tiles[offsetTileTwo].collision)
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

    public static List<Entity> playerWithEntityCollision(Player player, CollisionType type)
    {
        List<Entity> onCollisionEntities = new ArrayList<Entity>();
        List<Entity> onMapEntities = Entity.getOnMapEntities(player);
        

        if(onMapEntities.isEmpty())
        {
            return onMapEntities;
            
        }
        
        for(Entity e : onMapEntities)
        {
            player.collisionArea.min = 
                new Vector2((player.collisionArea.min.x + player.worldPosition.x), (player.collisionArea.min.y + player.worldPosition.y));
            //Utils.printf(e.toString());
            e.collisionArea.min = 
                new Vector2((e.collisionArea.min.x + e.worldPosition.x), (e.collisionArea.min.y + e.worldPosition.y));
                
                for(int i = 0; i < Utils.allDirections.length; i++)
                {
                    if(Utils.allDirections[i] == player.direction)
                    {
                        Vector2 playerDirection = Vector2.scalarPerVector(Vector2.directionsVector[i], player.velocity);
                        
                        if(type == CollisionType.nextTiles)
                        {
                            player.collisionArea.min = Vector2.vectorSumm(player.collisionArea.min, playerDirection);
                        }
                        
                        if(RectInt.intersect(player.collisionArea, e.collisionArea) && !onCollisionEntities.contains(e))
                        {
                            //Utils.printf("entity: " + e + " collision on: " + Utils.allDirections[i]);
                            onCollisionEntities.add(e);
                            Utils.printf("entity: " + e + " " + e.collisionArea.min.x + ", " + e.collisionArea.min.y);
                            
                            if(e.collisionOn && type == CollisionType.nextTiles)
                            {
                                player.collisionOn = true;
                            }
    
                            if(e.collisionOn && type == CollisionType.onStepTile)
                            {
                                player.collisionOn = e.name == "merchant" ? false : true;
                            }
                        }
                    }
                }
                e.collisionArea.min = e.collisionAreaMin_Default;
                player.collisionArea.min = player.collisionAreaMin_Default;
        }

        return onCollisionEntities;
        
    }
 
}
