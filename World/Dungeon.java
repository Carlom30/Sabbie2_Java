package World;

import World.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Main.Main;
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
    
    final int maxRoom = 10;
    final int minRoom = 3;

    final int roomWidth = 9;
    final int roomHeight = 9;

    final int maxAreaWidth = 50;
    final int maxAreaHeight = 50;

    public Dungeon()
    {
        Random rand = new Random();
        roomNumb = rand.nextInt((maxRoom - minRoom)) + minRoom;
        //rooms = new Room[roomNumb]; 
        memArea = new Room[memWidth * memHeight]; // area logica delle stanze
        area = new Map(Main.gp, maxAreaWidth, maxAreaHeight); // area effettiva di rendering
        
        //adesso creo la serie di rooms
        
    }
    
    public void generateDungeonRooms()
    {
        int i = 0;
        while(i < roomNumb)
        {
            if(i == 0)
            {
                //la prima stanza la posiziono estattamente al centro della stanza 
                List<Directions> doorDir = new ArrayList<Directions>();
                doorDir.add(Directions.up);
                doorDir.add(Directions.right);

                Room room = new Room(new RectInt(new Vector2
                    (area.width / 2 - roomWidth / 2, area.height / 2 - roomHeight / 2), roomWidth, roomHeight), doorDir);
                
                room.drawRoomOnMap(area, Main.gp); //questo dovrebbe avvenire fuori dal costruttore (testing)
                memArea[memHeight / 2 * memWidth + memWidth / 2] = room;
                rooms.add(room);
            }
            


            i++;
        }

        return;
    }

}
