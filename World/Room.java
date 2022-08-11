package World;

import Math.*;
import Object.Chest;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import Engine.GamePanel;
import Engine.Tile;
import Engine.Tile.TileType;
import Main.Main;
import Main.Utils;
import Main.Utils.Directions;


public class Room 
{
    public enum RoomType
    {
        normal,
        chest
    };

    public RectInt bounds;
    public Tile[] tiles;
    List<Directions> doors;
    List<Integer> doorsVector;

    RoomType type;

    Vector2 onDungeonMemPosition;

    public BufferedImage wall;
    public BufferedImage floor;
    public BufferedImage doorUp;
    public BufferedImage doorDown;
    public BufferedImage doorRight;
    public BufferedImage doorLeft;

    void loadRoomSprite()
    {
        wall = Utils.loadSprite("/Sprites/wallme.png");
        floor = Utils.loadSprite("/Sprites/world/sand/sand0.png");
        //garbagecollector or shit like that i dunno why there's no free() function?
    }

    public Room(RectInt bounds, List<Directions> doorDirections, RoomType type, Map map)
    {
        this.type = type;
        this.bounds = bounds;
        doors = new ArrayList<Utils.Directions>();
        doorsVector = new ArrayList<Integer>();
        //ora serve di creare la stanza e (altra funzione) "disegnarla" sulla mappa
        /*
            NB: il rendering funzionerà in questo modo:
                1) rendering della mappa, ovvero tutto ciò che è "statico"
                2) tutto ciò che non è mappa, e che quindi può cambiare
        */
        loadRoomSprite();
        tiles = new Tile[bounds.width * bounds.height];
        for(int i = 0; i < bounds.height; i++)
        {
            for(int j = 0; j < bounds.width; j++)
            {
                int offset = i * bounds.width + j; 
                tiles[offset] = new Tile(floor);
                tiles[offset].linkedRoom = this;
                tiles[offset].type = TileType.floor;
                
                if(i == 0 || i == (bounds.height - 1) || j == 0 || j == (bounds.width - 1))
                {
                    tiles[offset].sprite = wall;
                    tiles[offset].collision = true;  
                    tiles[offset].type = TileType.wall;  
                }
            }
        }

        if(doorDirections != null)
        {
            addDoors(doorDirections);
        }

        if(type == RoomType.chest)
        {
            Vector2 globalChestPosition = new Vector2(0, 0);
            globalChestPosition.x = bounds.min.x + bounds.width / 2;
            globalChestPosition.y = bounds.min.y + bounds.height / 2;

            Chest roomChest = new Chest(globalChestPosition, map);
        }
    }

    void addDoors(List<Directions> directions) //list perché potrebbe arrivarne anche solo una
    {
        int offset = 0;
        //non utilizzo loop poichè le direzioni sono sempre 4 e le porte compaiono sempre nelle stesse posizioni
        if(directions.contains(Directions.up) && !this.doors.contains(Directions.up))
        {
            doors.add(Directions.up);
            offset = (0 * bounds.width + (bounds.width / 2)); //che equivale a bounds.width / 2 ma lo lascio per completismo
            tiles[offset] = new Tile(floor);
            //la linearizzazione dell'array è lasciata come esercizio al lettore XD
        }

        if(directions.contains(Directions.down) && !this.doors.contains(Directions.down))
        {
            doors.add(Directions.down);
            offset = (bounds.height - 1) * bounds.width + (bounds.width / 2);
            tiles[offset] = new Tile(floor);
        }

        if(directions.contains(Directions.right) && !this.doors.contains(Directions.right))
        {
            doors.add(Directions.right);
            offset = (bounds.height / 2) * bounds.width + (bounds.width - 1);
            tiles[offset] = new Tile(floor);
        }

        if(directions.contains(Directions.left) && !this.doors.contains(Directions.left))
        {
            doors.add(Directions.left);
            offset = (bounds.height / 2) * bounds.width + 0; // sisisisisisi il + 0 si lascia fare.
            tiles[offset] = new Tile(floor);
        }

        return;
    }

    public void drawRoomOnMap(Map map, GamePanel gp) //print data only, no rendering
    {
        int k = 0;
        for(int i = bounds.min.y; i < bounds.min.y + bounds.height; i++)
        {
            for(int j = bounds.min.x; j < bounds.min.x + bounds.width; j++)
            {
                int offset = i * map.width + j; 
                map.tiles[offset] = this.tiles[k];
                k++;
            }
        }
    }
}
