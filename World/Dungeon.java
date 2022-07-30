package World;

import World.Map;
import java.util.Random;

import Main.Main;
import Math.RectInt;
import Math.Vector2;

public class Dungeon 
{

    public int roomNumb;
    //public Room[] rooms;
    public Room[] memArea;
    public Map area;

    final int memWidth = 10;
    final int memHeight = 10;
    
    final int maxRoom = 10;
    final int minRoom = 3;

    final int roomWidth = 7;
    final int roomHeight = 7;

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
        for(int i = 0; i < memHeight; i++)
        {
            for(int j = 0; j < memWidth; j++)
            {
                if(i == 0 && j == 0)
                {
                    //la prima stanza la posiziono estattamente al centro della stanza 
                    Room room = new Room(new RectInt(new Vector2
                        (area.width / 2 - roomWidth / 2, area.height / 2 - roomHeight / 2), roomWidth, roomHeight));
                    
                    room.drawRoomOnMap(area, Main.gp); //questo dovrebbe avvenire fuori dal costruttore (testing)
                    memArea[memHeight / 2 * memWidth + memWidth / 2] = room;
                }
            }
        }

        return;
    }

}
