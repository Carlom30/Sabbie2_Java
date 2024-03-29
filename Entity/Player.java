package Entity;
import Main.*;
import Main.Main.GameState;
import Main.Utils.Directions;
import Math.RectInt;
import Math.Vector2;
import Obj.Ladder;
import Obj.Projectile;
import Obj.SuperObject;
import Obj.remoteTnt;
import Obj.SuperObject.objecType;
import World.Dungeon;
import World.Map;
import World.Room;
import World.Map.MapType;
import World.Room.RoomType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import Engine.*;
import Engine.CollisionLogic.CollisionType;
import Engine.Tile.TileType;

import java.awt.image.BufferedImage;

public class Player extends Entity 
{
    public GamePanel gp; //rendering part
    public KeyHandler kh; //input manager (sarà comunque una classe a parte)
    public Vector2 screenPosition;
    public Map linkedMap;
    public Dungeon linkedDungeon;
    public Room linkedRoom;
    public Vector2 lastKnownOutsidePosition;
    public Inventory inventory;
    public List<Projectile>shootedProjectile = new ArrayList<Projectile>();
    public RectInt spawnArea;

    final public int lifePoints_max = 5;

    public boolean DEV_MODE;
    public boolean isDead = false;
    public boolean hasWon = false;

    public BufferedImage heart;
    public BufferedImage healtBar_base;
    public BufferedImage healtBar_end;
    public BufferedImage healtBar_base_empty;
    public BufferedImage healtBar_end_empty;

    public Player()
    {
        
        readPlayerSprites();
        screenPosition = new Vector2(GamePanel.screenWidth / 2 - GamePanel.tileSize / 2, GamePanel.screenHeight / 2 - GamePanel.tileSize / 2);
        //up: print character at the exact centre of the screen
    }
    
    public void setDefaultValues(GamePanel gp, KeyHandler kh, Map startingMap)
    {
        DEV_MODE = false;
        this.gp = gp;
        this.kh = kh;
        
        linkedMap = startingMap;
        lifePoints = lifePoints_max;
        velocity = 4; //velocity = 4 ma metto di più per testing
        worldPosition = new Vector2((linkedMap.width / 2) * GamePanel.tileSize, (linkedMap.height / 2) * GamePanel.tileSize); //new Vector2(gp.tileSize * 23, gp.tileSize * 21);
        spawnArea = new RectInt(new Vector2((linkedMap.width / 2) - 5, (linkedMap.height / 2) - 5), (linkedMap.width / 2) + 5, (linkedMap.height / 2) + 5);
        collisionArea = new RectInt(new Vector2(8, 16), 32, 32); 
        collisionAreaMin_Default = collisionArea.min;
        lastKnownOutsidePosition = new Vector2(Integer.MIN_VALUE, Integer.MIN_VALUE);
        direction = Directions.down;
        inventory = new Inventory();

        if(DEV_MODE)
        {
            velocity = velocity * 10;
            collisionArea = new RectInt(new Vector2(0, 0), 0, 0);
        }
        
        
    }

    void readStatusSprites()
    {
        heart = Utils.loadSprite("/Sprites/character/health/heart.png");
        healtBar_base = Utils.loadSprite("/Sprites/character/health/healtbar_base.png");
        healtBar_end = Utils.loadSprite("/Sprites/character/health/healtbar_end.png");
        healtBar_base_empty = Utils.loadSprite("/Sprites/character/health/healtbar_base_empty.png");
        healtBar_end_empty = Utils.loadSprite("/Sprites/character/health/healtbar_end_empty.png");
    }

    public void readPlayerSprites()
    {
        try
        {
            idle = ImageIO.read(getClass().getResourceAsStream("/Sprites/character/char_idle.png"));
            up_1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/character/char_up1.png"));
            up_2 = ImageIO.read(getClass().getResourceAsStream("/Sprites/character/char_up2.png"));
            down_1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/character/char_down1.png"));
            down_2 = ImageIO.read(getClass().getResourceAsStream("/Sprites/character/char_down2.png"));
            right_idle = ImageIO.read(getClass().getResourceAsStream("/Sprites/character/char_right_idle.png"));
            right_1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/character/char_right1.png"));
            left_idle = ImageIO.read(getClass().getResourceAsStream("/Sprites/character/char_left_idle.png"));
            left_1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/character/char_left1.png"));

        }

        catch(IOException e)
        {
            e.printStackTrace();
        }

        readStatusSprites();
    }
    
    public void update(Player player)
    {
        if(player.lifePoints <= 0)
        {
            isDead = true;
            GamePanel.gameState = GameState.title;
        }
        SuperObject onCollisionObject = null;
        List<Tile> onCollisionTiles = null;
        List<Entity> onCollisionEntities = null;
        
        inventory.updateInventory();

        if(kh.returnPressed && GamePanel.gameState == GameState.title && lifePoints > 0)
        {
            GamePanel.gameState = GameState.inGame;
        }

        if(kh.returnPressed && GamePanel.gameState == GameState.title && (player.isDead || player.hasWon))
        {
            Main.window.dispose();
            Main.startNewGame();
            GamePanel.gameState = GameState.inGame;
        }

        if(GamePanel.gameState == GameState.title)
        {
            return;
        }

        if(kh.downPressed || kh.leftPressed || kh.rightPressed || kh.upPressed)
        {
            onCollisionObject = CollisionLogic.checkForCollision_Obj(this, true);
            onCollisionEntities = CollisionLogic.playerWithEntityCollision(this, CollisionType.nextTiles);

            
            if(kh.upPressed)
            {
                direction = Directions.up;
                gp.collision.checkForCollision_Tile(this, CollisionType.nextTiles, null);
                if(collisionOn == false)
                {
                    worldPosition.y -= velocity;
                }
            }
    
            else if(kh.downPressed)
            {
                direction = Directions.down;
                gp.collision.checkForCollision_Tile(this, CollisionType.nextTiles, null);
                if(collisionOn == false)
                {
                    worldPosition.y += velocity;
                }
            }
            
            else if(kh.leftPressed)
            {
                direction = Directions.left;
                gp.collision.checkForCollision_Tile(this, CollisionType.nextTiles, null);
                if(collisionOn == false)
                {
                    worldPosition.x -= velocity;
                }
            }
    
            else if(kh.rightPressed)
            {
                direction = Directions.right;
                gp.collision.checkForCollision_Tile(this, CollisionType.nextTiles, null);
                if(collisionOn == false)
                {
                    worldPosition.x += velocity;
                }
            }
    
            collisionOn = false;


            spriteCounter++;
            if(spriteCounter > 15) // sostanzialmente 10 è la "velocità" di change dello sprite, più è alto, e più gli sprite ci mettono a cambiare
            {                      // quindi in questo caso cambia ogni 10 frame, poiché questa funzione è chiamata ad ogni frame
                if(spriteNum == 1)
                {
                    spriteNum = 2;
                }
    
                else if(spriteNum == 2)
                {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }

        if(kh.Q_Pressed)
        {
            //it just fucking works!
            if(Utils.currentTime == -1)
            {
                Utils.currentTime = System.currentTimeMillis();
                Utils.printf("e pressed");
                
                List<Tile> nextTiles = gp.collision.checkForCollision_Tile(this, CollisionType.nextTiles, null);
                
                Tile[] tileArray = new Tile[2]; //max element is 2
                nextTiles.toArray(tileArray);
                
                if(nextTiles.isEmpty() || tileArray[0].type != TileType.wall)
                {
                    Utils.printf("no wall detected");
                    abbatti();
                    return;
                }

                if(tileArray[0].hasTnt || !inventory.modifyValue_tnt(-1))
                {
                    return;
                }
                
                //se il giocatore ha tnt, allora ne posiziono una nella tile designata

                remoteTnt newTnt = new remoteTnt(tileArray[0]);
                kh.Q_Pressed = false;
            }
            
            Utils.timeIsPassed(Utils.currentTime, 500);
        }

        else if(kh.H_Pressed)
        {

            if(Utils.currentTime == -1)
            {
                Utils.currentTime = System.currentTimeMillis();
                Utils.printf("h pressed");
                if(inventory.modifyValue_healthPotion(-1) && lifePoints < lifePoints_max)
                {
                    this.setLifePoints(lifePoints_max - lifePoints, lifePoints_max);
                }
            }
            
            Utils.timeIsPassed(Utils.currentTime, 500);
        }

        else if(kh.C_Pressed)
        {
            List<Tile> onStepTiles = gp.collision.checkForCollision_Tile(this, CollisionType.onStepTile, null);
            boolean hasTreasure = false;
            if(Utils.currentTime == -1)
            {
                Utils.currentTime = System.currentTimeMillis();
                onCollisionObject = CollisionLogic.checkForCollision_Obj(this, true);
                onCollisionTiles = gp.collision.checkForCollision_Tile(this, CollisionType.nextTiles, null);
                //onCollisionEntities = CollisionLogic.playerWithEntityCollision(this, CollisionType.nextTiles);

                if(!onCollisionTiles.isEmpty())
                {
                    for(Tile t : onCollisionTiles)
                    {
                        if(t.type == TileType.tree)
                        {
                            cutTree(t, linkedMap);
                            return;
                        }
                    }
                }
                
                for(Tile t : onStepTiles)
                {
                    if(t.treasureTile)
                    {
                        hasTreasure = true;
                    }
                }

                if(onCollisionObject == null && CollisionLogic.playerWithEntityCollision(this, CollisionType.nextTiles).isEmpty())
                {
                    Utils.printf("c pressed");
                    if(onCollisionTiles.isEmpty() && inventory.modifyValue_log(-3))
                    {
                        Dungeon d = Dungeon.digDungeon(this);
                        if(d != null && hasTreasure)
                        {
                            d.treasureDungeon = true;
                            Utils.printf("treasure!");
                        }
                    }    

                    
                }

                else if(onCollisionObject != null && onCollisionObject.type == objecType.Ladder)
                {
                    //funzione climb()
                    Ladder ladder = (Ladder)onCollisionObject;
                    ladder.climbLadder(this);
                }

            }
            Utils.timeIsPassed(Utils.currentTime, 500);
        }

        if(kh.E_Pressed)
        {
            if(Utils.currentTime == -1)
            {
                Utils.currentTime = System.currentTimeMillis();
                Utils.printf("E pressed");
                onCollisionObject = CollisionLogic.checkForCollision_Obj(this, true);
                onCollisionEntities = CollisionLogic.playerWithEntityCollision(this, CollisionType.nextTiles);
                if(onCollisionObject == null)
                {
                    Utils.printf("obj not found");
                }

                if(onCollisionObject != null && onCollisionObject.interact(this))
                {
                    GamePanel.printableObj.remove(onCollisionObject);
                    onCollisionObject = null;
                }

                if(!onCollisionEntities.isEmpty() && gp.currentMapType == MapType.outside)
                {
                    //l'unica interazione possibile outside è quella con il mercante,
                    //quindi se non è empty, è il mercante
                    Merchant m = (Merchant)onCollisionEntities.iterator().next();
                    m.interact(this);
                }
            }
            Utils.timeIsPassed(Utils.currentTime, 500);
        }

        if(kh.R_Pressed)
        {
            if(Utils.currentTime == -1)
            {
                Utils.currentTime = System.currentTimeMillis();
                inventory.craftRemoteTnt();
            }
            Utils.timeIsPassed(Utils.currentTime, 500);
        }

        if(kh.shootUp || kh.shootDown || kh.shootRight || kh.shootLeft)
        {
            if(Projectile.currentTime == -1)
            {
                Projectile.currentTime = System.currentTimeMillis();
                Directions d = Directions.ALL_DIRECTIONS;

                if(kh.shootUp)
                    d = Directions.up;

                else if(kh.shootDown)
                    d = Directions.down;

                else if(kh.shootRight)
                    d = Directions.right;

                else if(kh.shootLeft)
                    d = Directions.left;

                Utils.printf(d.toString());
                Projectile.shoot(this, d);
            }
            Utils.timeIsPassed(Projectile.currentTime, 2000);
        }

        if(linkedDungeon != null)
        {
            List<Tile> tilesList =  gp.collision.checkForCollision_Tile(this, CollisionType.onStepTile, null);
            Tile[] tiles = new Tile[tilesList.size()];
            tilesList.toArray(tiles);
            int a = Main.rand.nextInt(tiles.length);

            Room lastLinkedRoom = this.linkedRoom;
            this.linkedRoom = tiles[a].linkedRoom == null ? this.linkedRoom : tiles[a].linkedRoom;

            if(lastLinkedRoom != null && this.linkedRoom != lastLinkedRoom)
            {
                for(Monster m : lastLinkedRoom.onRoomMonsters)
                {
                    m.reset();
                }
            }
            //Utils.printf(linkedRoom.toString());
        }
        
    }

    public void abbatti() //nome legacy non sostituibile
    {
        //abbatti sostanzialmente attiva tutte le tnt che ci sono in mappa (quindi dovrà esserci un limite generale di 2/3)
        //quello che fa la tnt è semplice, la tile dove è presente la tnt e le adiacenti perpendiolari "crollano" e 
        //diventano camminabili
        //se in una direzione, alla parte opposta, non c'è già una stanza, allora viene creata e aggiunta.
        //quindi per prima cosa creo una lista di tile che hanno effettivamente la tnt
        List<remoteTnt> tntList = new ArrayList<remoteTnt>();


        if(GamePanel.printableObj.isEmpty())
        {
            Utils.printf("list is empty, no Tnt found");
            return;
        }

        for(SuperObject obj : GamePanel.printableObj)
        {
            if(obj.type == objecType.remoteTnt)
            {
                tntList.add((remoteTnt)obj);
                
            }

        }

        for(remoteTnt obj : tntList)
        {
            //per prima cosa tutti i dati utili
            Utils.printf("remote n: " + obj);
            Dungeon dungeon = this.linkedDungeon;
            Map map = GamePanel.player.linkedMap;
            Tile mainTile = obj.attachedTile;
            Room mainRoom = mainTile.linkedRoom;
            Vector2 tileVector = map.getGlobalTileVector(mainTile);

            int ALL_DIRECTIONS = 4;

            //enum che però funziona diversamente da quello di java che fa un po c***** :))

            //recupero adesso la direzione verso la quale la tnt dovrebbe abbattere
            Directions boomDirection = Directions.ALL_DIRECTIONS;
            if(tileVector.x == mainRoom.bounds.min.x)
            {
                boomDirection = Directions.left;
            }

            else if(tileVector.x == mainRoom.bounds.min.x + mainRoom.bounds.width - 1)
            {  
                boomDirection = Directions.right;
            }
            
            else if(tileVector.y == mainRoom.bounds.min.y)
            {
                boomDirection = Directions.up;
            }
            
            else if(tileVector.y == mainRoom.bounds.min.y + mainRoom.bounds.height - 1)
            {
                boomDirection = Directions.down;
            }
            
            Tile floor = new Tile(Utils.loadSprite("/Sprites/world/sand/sand0.png"));
            Utils.printf("BOOM!! at: " + boomDirection.toString());

            //caso outside.
            if(Main.gp.currentMapType == MapType.outside)
            {
                map.tiles[tileVector.y * map.width + tileVector.x] = floor;
                map.tiles[tileVector.y * map.width + tileVector.x].collision = false; //jsut declaration of intent

                //do l'idea di esplosione distruggendo le tile perpendicolari
                for(int i = 0; i < ALL_DIRECTIONS; i++) 
                {
                    map.tiles[(tileVector.y + Vector2.directionsVector[i].y) * map.width + (tileVector.x + Vector2.directionsVector[i].x)] = floor;
                }
                GamePanel.printableObj.remove(obj);
                return;
            }

            //case onDungeon
            List<Directions> busyDir = new ArrayList<Directions>();
            List<Room> nearRooms = dungeon.checkForNeighbors(mainRoom, busyDir);

            //DEBUG
            for(Room room : nearRooms)
            {
                Utils.printf("near room: " + room);
            }

            if(!(busyDir.contains(boomDirection)))
            {
                //code for add room
                RectInt bounds = new RectInt(dungeon.calculateRoomMin(mainRoom, boomDirection), dungeon.roomWidth, dungeon.roomHeight);
                /*List<Directions> doorDir = new ArrayList<Directions>();
                doorDir.add(Utils.getOppositeDirection(boomDirection));*/
                RoomType type = (Main.rand.nextInt(100) + 1) <= 35 ? RoomType.normal : RoomType.chest;

                if(dungeon.treasureDungeon)
                {
                    type = Main.rand.nextInt(100) <= 20 ? RoomType.treasure : type;
                    Utils.printf("treasure room");
                }

                Room newRoom = new Room(bounds, null, type, linkedDungeon.area);
                newRoom.addMonsters(Main.rand.nextInt(2) + 1, null);
                dungeon.addRoomToMemArea(mainRoom, newRoom, boomDirection);
                newRoom.drawRoomOnMap(map);
            }


            map.tiles[tileVector.y * map.width + tileVector.x] = floor;
            map.tiles[tileVector.y * map.width + tileVector.x].collision = false; //jsut declaration of intent

            //do l'idea di esplosione distruggendo le tile perpendicolari
            for(int i = 0; i < ALL_DIRECTIONS; i++) 
            {
                map.tiles[(tileVector.y + Vector2.directionsVector[i].y) * map.width + (tileVector.x + Vector2.directionsVector[i].x)] = floor;
            }

            GamePanel.printableObj.remove(obj);

            //then destroy near tiles

            //aggiungere poi i crolli, forse...
        }
    }

    public void cutTree(Tile tile, Map map)
    {
        for(int i = 0; i < map.tiles.length; i++)
        {
            if(map.tiles[i] == tile)
            {
                map.tiles[i] = new Tile(Utils.loadSprite("/Sprites/world/sand/sand3.png"));
                map.tiles[i].collision = false;
            }
        }

        inventory.modifyValue_log(+1);

    }
}
