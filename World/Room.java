package World;

import Math.*;
import Obj.Chest;
import Obj.yendorAmulet;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import Engine.GamePanel;
import Engine.Tile;
import Engine.Tile.TileType;
import Entity.Monster;
import Entity.Player;
import Main.Main;
import Main.Utils;
import Main.Utils.Directions;


public class Room 
{
    public enum RoomType
    {
        normal,
        chest,
        merchant,
        treasure
    };

    public RectInt bounds;
    public Tile[] tiles;
    List<Directions> doors;
    List<Integer> doorsVector;
    public List<Monster> onRoomMonsters;
    public Monster[] onRoomMonsterArray;

    public RoomType type;

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
        onRoomMonsters = new ArrayList<Monster>();
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

                tiles[offset].onRoomPosition = new Vector2(j, i);
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

        if(type == RoomType.treasure)
        {
            //add treasure
            yendorAmulet yendor = new yendorAmulet(this, GamePanel.player.linkedDungeon);
            GamePanel.player.linkedMap.onMapObjects.add(yendor);
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

    public void addMonsters(int numb, Vector2[] positions)
    {
        if(positions != null && positions.length != numb)
        {
            Utils.printf("numb and positions length on addMonsters are different");
            return;
        }

        Vector2[] pos = positions;

        if(pos == null)
        {
            pos = new Vector2[numb];
            for(int i = 0; i < numb; i++)
            {
                pos[i] = new Vector2(Main.rand.nextInt(bounds.width) + 1, Main.rand.nextInt(bounds.height) + 1);
                pos[i].x = pos[i].x >= bounds.width - 1 ? bounds.width - 2 : pos[i].x;
                pos[i].y = pos[i].y >= bounds.height - 1 ? bounds.height - 2 : pos[i].x;

                //bugged
                for(int k = 0; k < i - 1; k++)
                {
                    if(pos[i].x == pos[k].x && pos[i].y == pos[k].y)
                    {
                        i--;
                        break;
                    }
                }

            }
        }
        
        for(int j = 0; j < pos.length; j++)
        {
            Monster monster = new Monster(null, pos[j], this);
            onRoomMonsters.add(monster);
            //monster.startMonsterThread(GamePanel.player);
            //Utils.printf("monster: " + monster + " positions: (" + pos[j].x + ", " + pos[j].y + ")");
        }
        onRoomMonsterArray = new Monster[onRoomMonsters.size()];
        onRoomMonsters.toArray(onRoomMonsterArray);
    }

    public void drawRoomOnMap(Map map) //print data only, no rendering
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
