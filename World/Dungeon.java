package World;

import World.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import Main.Main;
import Main.Utils;
import Main.Utils.Directions;
import Math.RectInt;
import Math.Vector2;

public class Dungeon 
{
    public int roomNumb;
    public Room[] memArea;
    public Map area;

    //aggiungo una lista di rooms per tenere traccia di tutte le stanze, anche quelle allocate in game
    public List<Room> rooms= new ArrayList<Room>();

    final int memWidth = 10;
    final int memHeight = 10;
    
    final int maxRoom = 15;
    final int minRoom = 3;

    final int roomWidth = 9;
    final int roomHeight = 9;

    final int maxAreaWidth = 100;
    final int maxAreaHeight = 100;

    Directions[] allDirections;

    Vector2[] directionsVector;

    int ALL_DIRECTIONS;

    public Dungeon()
    {
        allDirections = new Directions[4];
            
        allDirections[0] = Directions.up;
        allDirections[1] = Directions.down;
        allDirections[2] = Directions.right;
        allDirections[3] = Directions.left;

        directionsVector = new Vector2[4];

        directionsVector[0] = new Vector2(0, -1);   //up
        directionsVector[1] = new Vector2(0, 1); //down
        directionsVector[2] = new Vector2(1, 0); //right
        directionsVector[3] = new Vector2(-1, 0);  //left

        ALL_DIRECTIONS = allDirections.length;

        roomNumb = Main.rand.nextInt((maxRoom - minRoom)) + minRoom;
        Utils.printf("roomNumb: " + roomNumb);
        //rooms = new Room[roomNumb]; 
        memArea = new Room[memWidth * memHeight]; // area logica delle stanze
        area = new Map(Main.gp, maxAreaWidth, maxAreaHeight); // area effettiva di rendering

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

    void addRoomToMemArea(Room mainRoom, Room newRoom, Directions dir)
    {
        for(int i = 0; i < allDirections.length; i++)
        {
            if(allDirections[i] == dir)
            {
                int x = mainRoom.onDungeonMemPosition.x + directionsVector[i].x;
                int y = mainRoom.onDungeonMemPosition.y + directionsVector[i].y;

                int offset = 
                    (mainRoom.onDungeonMemPosition.y + directionsVector[i].y) * memWidth + (mainRoom.onDungeonMemPosition.x + directionsVector[i].x); 
                
                newRoom.onDungeonMemPosition = 
                    new Vector2(mainRoom.onDungeonMemPosition.x + directionsVector[i].x, mainRoom.onDungeonMemPosition.y + directionsVector[i].y);
                
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
        while(i < roomNumb) //for intralcerebbe, ci sono dei casi in cui mi serve continuare senza incrementare i
        {
            if(i == 0 && mainRoom == null)
            {
                //la prima stanza la posiziono estattamente al centro della map
                List<Directions> doorDir = new ArrayList<Directions>();

                //testing so scripted
                //doorDir.add(Directions.up);
                //doorDir.add(Directions.right);

                Room room = new Room(new RectInt(new Vector2
                    (area.width / 2 - roomWidth / 2, area.height / 2 - roomHeight / 2), roomWidth, roomHeight), doorDir);
                
                room.drawRoomOnMap(area, Main.gp); //questo dovrebbe avvenire fuori dal costruttore (testing)                
                memArea[memHeight / 2 * memWidth + memWidth / 2] = room;

                room.onDungeonMemPosition = new Vector2(memHeight / 2, memWidth / 2);
                rooms.add(room);

                mainRoom = room;

                i++;

                continue;
            }
            
            //quello che ora va fatto è una specie di serpente, ogni volta che devo aggiungere una stanza:
            // - scelgo una direzione a caso tra le 4 cardinali
            // - in base alla direzioni scelta, aggiungo una porta alla stanza principale e alla secondaria nella direzione opposta
            // - la secondaria diventa la primaria
            // - se non ci sono direzioni libere ne prendo una a caso dalla lista delle stanze
            Directions randDir = allDirections[Main.rand.nextInt(allDirections.length)];
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
                    } while(mainRoom == randRoom); //ne prendo una a caso fino a che non ne prendo una che non sia la mainRoom
    
                    mainRoom = randRoom;
                }
                continue; //direzione già in uso, ritento
            }



            List<Directions> newRoomDoors = new ArrayList<Directions>();
            List<Directions> mainRoomNewDoors = new ArrayList<Directions>();

            newRoomDoors.add(Utils.getOppositeDirection(randDir));
            mainRoomNewDoors.add(randDir);
            
            mainRoom.addDoors(mainRoomNewDoors);
            mainRoom.drawRoomOnMap(area, Main.gp);

            //RectInt newRoomBounds = new RectInt(, width, height)
            
            //ora creo una stanza nuova
            Room newRoom = new Room(new RectInt(calculateRoomMin(mainRoom, randDir), roomWidth, roomHeight), newRoomDoors);
            addRoomToMemArea(mainRoom, newRoom, randDir);
            newRoom.drawRoomOnMap(area, Main.gp);

            //e adesso la aggiungo alle varie struture dati
            rooms.add(newRoom);
            
            Room[] arrayRoom = new Room[rooms.size()];
            rooms.toArray(arrayRoom);

            mainRoom = arrayRoom[Main.rand.nextInt(rooms.size())];

            i++;
        }

        //NB FARE DROW DI TUTTE LE STANZE
        Utils.printf("numero stanze effettive: " + rooms.size());
        return;
    }

}
