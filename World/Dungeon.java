package World;

import World.Map;
import World.Map.MapType;
import World.Room.RoomType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Engine.GamePanel;
import Entity.Entity;
import Entity.Monster;
import Entity.Player;
import Main.Main;
import Main.Utils;
import Main.Utils.Directions;
import Math.RectInt;
import Math.Vector2;
import Obj.Ladder;
import Obj.SuperObject;
import Obj.Ladder.LadderType;
import Engine.Tile;
import Engine.CollisionLogic.CollisionType;

public class Dungeon 
{
    public int roomNumb;
    public int chestRoomNumb;
    public Room[] memArea;
    public Map area;


    //aggiungo una lista di rooms per tenere traccia di tutte le stanze, anche quelle allocate in game
    public List<Room> rooms= new ArrayList<Room>();
    public List<SuperObject> onDungeonObjects = new ArrayList<>();

    //testing 
    public Room firstRoom = null;

    final int memWidth = 10;
    final int memHeight = 10;
    
    final int maxRoom = 15;
    final int minRoom = 3;

    final int maxChestRoom = 3;

    final public int roomWidth = 9;
    final public int roomHeight = 9;

    final int maxAreaWidth = 1000;
    final int maxAreaHeight = 1000;

    int ALL_DIRECTIONS;

    public Dungeon()
    {

        ALL_DIRECTIONS = Utils.allDirections.length;
        roomNumb = Main.rand.nextInt((maxRoom - minRoom)) + minRoom;
        
        //se ci sono più di metà del massimo delle stanza, aumento autmaticamente le chestRoom di 1
        chestRoomNumb = (Main.rand.nextInt(maxChestRoom) + 1) + Math.round((roomNumb / maxRoom) * 10);
        
        if(chestRoomNumb > maxChestRoom)
        {
            chestRoomNumb = maxChestRoom;
        }
        
        Utils.printf("roomNumb: " + roomNumb);
        //rooms = new Room[roomNumb]; 
        memArea = new Room[memWidth * memHeight]; // area logica delle stanze
        area = new Map(Main.gp, maxAreaWidth, maxAreaHeight); // area effettiva di rendering
        
        Tile nullGrey = new Tile(Utils.loadSprite("/Sprites/nullGrey.png"));
        nullGrey.collision = true;
        area.fillMapWithOneTile(nullGrey);
        //generateDungeonRooms();
        
    }

    public Vector2 calculateRoomMin(Room mainRoom, Directions randDir)
    {
        RectInt roomBounds;
        Vector2 min = new Vector2(0, 0);

        if(randDir == Directions.up)
        {
            min.x = mainRoom.bounds.min.x;
            min.y = mainRoom.bounds.min.y - roomHeight;
        }

        else if(randDir == Directions.down)
        {
            min.x = mainRoom.bounds.min.x;
            min.y = mainRoom.bounds.min.y + roomHeight;
        }

        else if(randDir == Directions.right)
        {
            min.y = mainRoom.bounds.min.y;
            min.x = mainRoom.bounds.min.x + roomWidth;
        }

        else if(randDir == Directions.left)
        {
            min.y = mainRoom.bounds.min.y;
            min.x = mainRoom.bounds.min.x - roomWidth;
        }

        else 
        {
            return null;
        }

        return min;
    }

    public List<Room> checkForNeighbors(Room room, List<Directions> busyDirections) //busyDirection è vuoto e va riempito
    {
        //non faccio for o roba simile perché sono 4 casi molto specifici e non servirebbe a nulla modularizzare
        List<Room> nearRooms = new ArrayList<Room>();
        
        int height = room.onDungeonMemPosition.y;
        int width = room.onDungeonMemPosition.x;

        int upOffset = (height - 1) * memWidth + width;
        int downOffset = (height + 1) * memWidth + width;
        int rightOffset = height * memWidth + (width + 1);
        int leftOffset = height * memWidth + (width - 1);

        if(memArea[upOffset] != null)
        {
            nearRooms.add(memArea[upOffset]);
            busyDirections.add(Directions.up);
        }

        if(memArea[downOffset] != null)
        {
            nearRooms.add(memArea[downOffset]);
            busyDirections.add(Directions.down);
        }

        if(memArea[rightOffset] != null)
        {
            nearRooms.add(memArea[rightOffset]);
            busyDirections.add(Directions.right);
        }

        if(memArea[leftOffset] != null)
        {
            nearRooms.add(memArea[leftOffset]);
            busyDirections.add(Directions.left);
        }

        return nearRooms;

        //ora vado a controllare se per caso intorno 
    }

    public void addRoomToMemArea(Room mainRoom, Room newRoom, Directions dir)
    {
        for(int i = 0; i < Utils.allDirections.length; i++)
        {
            if(Utils.allDirections[i] == dir)
            {
                int x = mainRoom.onDungeonMemPosition.x + Vector2.directionsVector[i].x;
                int y = mainRoom.onDungeonMemPosition.y + Vector2.directionsVector[i].y;

                int offset = 
                    (mainRoom.onDungeonMemPosition.y + Vector2.directionsVector[i].y) * memWidth + (mainRoom.onDungeonMemPosition.x + Vector2.directionsVector[i].x); 
                
                newRoom.onDungeonMemPosition = 
                    new Vector2(mainRoom.onDungeonMemPosition.x + Vector2.directionsVector[i].x, mainRoom.onDungeonMemPosition.y + Vector2.directionsVector[i].y);
                
                    memArea[offset] = newRoom;

                    newRoom.onDungeonMemPosition = new Vector2(x, y);
                break;
            }
            //riguardo l'offset qui sotto: semplicemente vado ad aggiungere alle componenti dell'espressione i valori
            //del vector corrispondenti alla direzione trovata
        }
        return;
    }

    Room pickRandomRoom()
    {
        Room[] arrayRoom = new Room[rooms.size()];
        rooms.toArray(arrayRoom);

        return arrayRoom[Main.rand.nextInt(arrayRoom.length)];
    }
    
    public void generateDungeonRooms()
    {
        //siccome gli enum in questo linguaggio sono delle abominazioni ancestrali, una random direction la prenderò da questo array

        int i = 0;
        Room mainRoom = null;
        while(i < roomNumb + chestRoomNumb) //for intralcerebbe, ci sono dei casi in cui mi serve continuare senza incrementare i
        {
            //firstRoom, qui ci va la ladder per tornare nella outside map
            if(i == 0 && mainRoom == null)
            {
                //la prima stanza la posiziono estattamente al centro della map
                List<Directions> doorDir = new ArrayList<Directions>();

                //testing so scripted
                //doorDir.add(Directions.up);
                //doorDir.add(Directions.right);

                Room room = new Room(new RectInt(new Vector2
                    (area.width / 2 - roomWidth / 2, area.height / 2 - roomHeight / 2), roomWidth, roomHeight), doorDir, RoomType.normal, area);
                
                room.drawRoomOnMap(area); //questo dovrebbe avvenire fuori dal costruttore (testing)                
                memArea[memHeight / 2 * memWidth + memWidth / 2] = room;

                room.onDungeonMemPosition = new Vector2(memHeight / 2, memWidth / 2);
                rooms.add(room);

                mainRoom = room;

                Ladder upLadder = new Ladder(new Vector2((room.bounds.min.x + room.bounds.width / 2) * GamePanel.tileSize,
                    (room.bounds.min.y + room.bounds.height / 2) * GamePanel.tileSize), LadderType.goesUp, this.area, this);

                firstRoom = room;
                //firstRoom.addMonsters(Main.rand.nextInt(3), null);

                i++;
                continue;
            }
            
            //quello che ora va fatto è una specie di serpente, ogni volta che devo aggiungere una stanza:
            // - scelgo una direzione a caso tra le 4 cardinali
            // - in base alla direzioni scelta, aggiungo una porta alla stanza principale e alla secondaria nella direzione opposta
            // - la secondaria diventa la primaria
            // - se non ci sono direzioni libere ne prendo una a caso dalla lista delle stanze
            Directions randDir = Utils.allDirections[Main.rand.nextInt(Utils.allDirections.length)];
            System.out.println("random direction: " + randDir.toString());
            
            //sorry for this im a C programmer :D
            List<Directions> busyDirs = new ArrayList<Directions>();
            checkForNeighbors(mainRoom, busyDirs);

            if(busyDirs.contains(randDir))
            {
                if(busyDirs.contains(Directions.up) && busyDirs.contains(Directions.down) && busyDirs.contains(Directions.right) && busyDirs.contains(Directions.left))
                {
                    //se arrivo qui, allora la stanza ha tutte e 4 le direzioni occupate, quindi ne prendo un'altra
                    Room randRoom = null;
                    
                    do
                    {
                        randRoom = pickRandomRoom();
                    } 
                    while(mainRoom == randRoom); //ne prendo una a caso fino a che non ne prendo una che non sia la mainRoom
    
                    mainRoom = randRoom;
                }
                continue; //direzione già in uso, ritento
            }
            
            List<Directions> newRoomDoors = new ArrayList<Directions>();
            List<Directions> mainRoomNewDoors = new ArrayList<Directions>();

            if(i < roomNumb)
            {
    
                newRoomDoors.add(Utils.getOppositeDirection(randDir));
                mainRoomNewDoors.add(randDir);
                
                mainRoom.addDoors(mainRoomNewDoors);
            }

            mainRoom.drawRoomOnMap(area);

            //RectInt newRoomBounds = new RectInt(, width, height)
            
            //ora creo una stanza nuova
            Room newRoom = new Room(new RectInt(calculateRoomMin(mainRoom, randDir), roomWidth, roomHeight), newRoomDoors, (i < roomNumb) ? RoomType.normal : RoomType.chest, area);
            addRoomToMemArea(mainRoom, newRoom, randDir);
            newRoom.drawRoomOnMap(area);
            newRoom.addMonsters(Main.rand.nextInt(3), null);
            //e adesso la aggiungo alle varie struture dati
            rooms.add(newRoom);
            
            Room[] arrayRoom = new Room[rooms.size()];
            rooms.toArray(arrayRoom);

            mainRoom = arrayRoom[Main.rand.nextInt(rooms.size())];

            i++;
        }

        //NB FARE DROW DI TUTTE LE STANZE
        Utils.printf("numero stanze effettive: " + rooms.size());

        //ora aggiungo un random di 1/3 "stanze del tesoro", avranno una chest ma non saranno mai accessibili se non facendo "abbatti"
        //su di un muro adiacente
        return;
    }

    public static void digDungeon(Player player)
    {
        if(Main.gp.currentMapType == MapType.dungeon)
        {
            Utils.printf("you cant dig inside a fucking dungeon");
            return;
        }
        player.lastKnownOutsidePosition = player.worldPosition;
        Vector2 ladderPosition = new Vector2(0, 0);
        for(int i = 0; i < Utils.allDirections.length; i++)
        {
            if(Utils.allDirections[i] == player.direction)
            {
                ladderPosition = (Vector2.vectorSumm(player.worldPosition, Vector2.scalarPerVector((Vector2.directionsVector[i]), GamePanel.tileSize)));
                List<Tile> tiles = Main.gp.collision.checkForCollision_Tile(player, CollisionType.nextTiles, null);
                
                for(Tile t : tiles)
                {
                    if(t.collision == true)
                    {
                        //Utils.printf("collision detected, no ladder, sorry");
                        return;
                    }
                }
                
                break;
            }
        }

        Dungeon dungeon = new Dungeon();
        dungeon.generateDungeonRooms();

        Ladder ladder = new Ladder(ladderPosition, LadderType.goesDown, player.linkedMap, dungeon);
    } 
}
