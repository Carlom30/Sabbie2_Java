package Entity;
import Main.*;
import Main.Utils.Directions;
import Math.RectInt;
import Math.Vector2;
import Object.SuperObject;
import Object.remoteTnt;
import Object.SuperObject.objecType;
import World.Dungeon;
import World.Map;
import World.Room;
import World.Room.RoomType;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Utilities;

import Engine.*;

import java.awt.image.BufferedImage;

public class Player extends Entity 
{
    public GamePanel gp; //rendering part
    KeyHandler kh; //input manager (sarà comunque una classe a parte)
    public Vector2 screenPosition;
    public Map linkedMap;
    public Dungeon linkedDungeon;

    public boolean DEV_MODE;

    public Player(GamePanel gp, KeyHandler kh, Vector2 worldPos)
    {
        DEV_MODE = false;
        this.gp = gp;
        this.kh = kh;
        setDefaultValues();
        if(worldPos != null)
        {
            worldPosition = worldPos;
        }
        readPlayerSprites();
        screenPosition = new Vector2(gp.screenWidth / 2 - gp.tileSize / 2, gp.screenHeight / 2 - gp.tileSize / 2);
        collisionArea = new RectInt(new Vector2(8, 16), 32, 32); // 32 x 32  
        //up: print character at the exact centre of the screen
    }

    public void setDefaultValues()
    {
        worldPosition = new Vector2((gp.map.width / 2) * gp.tileSize, (gp.map.height / 2) * gp.tileSize); //new Vector2(gp.tileSize * 23, gp.tileSize * 21);
        velocity = 4; //velocity = 4 ma metto di più per testing
        direction = Directions.down;

        if(DEV_MODE)
        {
            velocity = velocity * 10;
            collisionArea = new RectInt(new Vector2(0, 0), 0, 0);
        }
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
    }

    public void update()
    {
        if(kh.downPressed || kh.leftPressed || kh.rightPressed || kh.upPressed)
        {
            if(kh.upPressed)
            {
                direction = Directions.up;
                gp.collision.checkForCollision_Tile(this);
                if(collisionOn == false)
                {
                    worldPosition.y -= velocity;
                }
            }
    
            else if(kh.downPressed)
            {
                direction = Directions.down;
                gp.collision.checkForCollision_Tile(this);
                if(collisionOn == false)
                {
                    worldPosition.y += velocity;
                }
            }
            
            else if(kh.leftPressed)
            {
                direction = Directions.left;
                gp.collision.checkForCollision_Tile(this);
                if(collisionOn == false)
                {
                    worldPosition.x -= velocity;
                }
            }
    
            else if(kh.rightPressed)
            {
                direction = Directions.right;
                gp.collision.checkForCollision_Tile(this);
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

        if(kh.E_Pressed)
        {
            //it just fucking works!
            if(Utils.currentTime == -1)
            {
                Utils.currentTime = System.currentTimeMillis();
                Utils.printf("e pressed");
                
                List<Tile> nextTiles = gp.collision.checkForCollision_Tile(this);
                Tile[] tileArray = new Tile[2]; //max element is 2
                nextTiles.toArray(tileArray);
                
                if(nextTiles.isEmpty())
                {
                    Utils.printf("no wall detected");
                    return;
                }
                if(tileArray[0].hasTnt)
                {
                    return;
                }
                
                //se il giocatore ha tnt, allora ne posiziono una nella tile designata

                remoteTnt newTnt = new remoteTnt(tileArray[0]);
                kh.E_Pressed = false;
            }
            
            Utils.timeIsPassed(Utils.currentTime, 1000);
        }

        else if(kh.R_Pressed)
        {

            if(Utils.currentTime == -1)
            {
                Utils.currentTime = System.currentTimeMillis();
                Utils.printf("r pressed");
                abbatti();
            }
            
            Utils.timeIsPassed(Utils.currentTime, 1000);
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

        int length = 0;

        if(Main.gp.printableObj.isEmpty())
        {
            Utils.printf("list is empty, no Tnt found");
            return;
        }

        for(SuperObject obj : Main.gp.printableObj)
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
            Map map = Main.gp.map;
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
            
            Utils.printf("BOOM!! at: " + boomDirection.toString());
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
                Room newRoom = new Room(bounds, null, (Main.rand.nextInt(100) + 1) <= 35 ? RoomType.normal : RoomType.chest);
                dungeon.addRoomToMemArea(mainRoom, newRoom, boomDirection);
                newRoom.drawRoomOnMap(map, gp);
            }

            Tile floor = new Tile(Utils.loadSprite("/Sprites/world/sand/sand0.png"));

            map.tiles[tileVector.y * map.width + tileVector.x] = floor;
            map.tiles[tileVector.y * map.width + tileVector.x].collision = false; //jsut declaration of intent

            //do l'idea di esplosione distruggendo le tile perpendicolari
            for(int i = 0; i < ALL_DIRECTIONS; i++) 
            {
                map.tiles[(tileVector.y + dungeon.directionsVector[i].y) * map.width + (tileVector.x + dungeon.directionsVector[i].x)] = floor;
            }

            Main.gp.printableObj.remove(obj);

            //then destroy near tiles

            //aggiungere poi i crolli, forse...
        }
    }

}
