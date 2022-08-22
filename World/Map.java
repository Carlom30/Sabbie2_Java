package World;
import Math.PerlinNoise;
import Math.RectInt;
import Math.Vector2;
import Obj.SuperObject;
import World.Room.RoomType;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import Engine.GamePanel;
import Engine.Tile;
import Engine.Tile.TileType;
import Entity.Merchant;
import Entity.Player;
import Main.Main;
import Main.Utils;
import Main.Utils.Directions;

import java.awt.Graphics2D; 
import java.util.List;

public class Map 
{
    public Tile[] tiles;
    public GamePanel gp;
    
    public final int width;
    public final int height;
    public final int area;

    public List<SuperObject> onMapObjects;
    public List<Room> onOutsideRooms; //only for outside

    public Merchant merchant;

    public enum MapType
    {
        outside,
        dungeon
    };

    public Map(GamePanel gp, int width, int height)
    {
        this.gp = gp;
        area = width * height;
        tiles = new Tile[area];
        this.width = width;
        this.height = height;
        onMapObjects = new ArrayList<SuperObject>();
    }

    //qualche funzione di utility immagino

    public void fillMapWithOneTile(Tile tile)
    {
        for(int i = 0; i < area; i++)
        {
            tiles[i] = tile;
        }

        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                int offset = i * width + j;
                tiles[offset].type = TileType.terrain;
                if(i == 0 || i == height - 1 || j == 0 || j == width - 1)
                {
                    tiles[offset] = new Tile(Utils.loadSprite("/Sprites/world/outside/maplimit.png"));
                    tiles[offset].collision = true;
                    tiles[offset].type = TileType.background;
                }
            }
        }

    }

    public Vector2 getGlobalTileVector(Tile tile)
    {
        Vector2 tileVector = new Vector2(0, 0);

        for(int y = 0; y < this.height; y++)
        {
            for(int x = 0; x < this.width; x++)
            {
                int offset = y * this.width + x;
                if(tiles[offset] == tile)
                {
                    tileVector.x = x;
                    tileVector.y = y;

                    return tileVector;
                }
            }
        }

        Utils.printf("tile not found in map (assert)");

        return null;
    }

    public static Map generateOutsideWorld(GamePanel gp)
    {
        
        Map outside = new Map(gp, gp.maxWorldColumn, gp.maxWorldRow);
        outside.fillMapWithOneTile(new Tile(Utils.loadSprite("/Sprites/world/sand/sand3.png")));
        outside.onOutsideRooms = new ArrayList<Room>();


        BufferedImage treeSprite = Utils.loadSprite("/Sprites/world/sand/sandAndTree.png");
        
        for(int i = 0; i < outside.height; i++)
        {
            for(int j = 0; j < outside.width; j++)
            {
                int offset = i * outside.width + j;
                float value = PerlinNoise.perlin((j + 0.7f) * 0.15f, (i + 0.7f) * 0.15f);
                value = value * 0.5f + 0.5f;
                float threshold = 0.38f;
                Utils.printf("perlin noise value: " + value);
                if(value <= threshold) 
                {
                    outside.tiles[offset] = new Tile(treeSprite);
                    outside.tiles[offset].collision = true;
                    outside.tiles[offset].type = TileType.tree;
                }
            }
        }

        //good values
        /*float value = PerlinNoise.perlin((j + 0.7f) * 0.15f, (i + 0.7f) * 0.15f);
                value = value * 0.5f + 0.5f;
                float threshold = 0.38f; */

        return outside;
    }

    public void addOutsideRooms(Player player, Map outside)
    {
        int maxRandRooms = 5;
        int randomOutsideRooms = Main.rand.nextInt(maxRandRooms);

        for(int i = 0; i < randomOutsideRooms; i++)
        {
            int randDoors = Main.rand.nextInt(Utils.allDirections.length); 
            List<Directions> dir = new ArrayList<Directions>();
        
            dir.add(Utils.allDirections[randDoors]);

            int width = Main.rand.nextInt(5) + 5;
            int height = Main.rand.nextInt(5) + 5;

            RectInt bounds = null;
            Boolean boundsOK = true;
            Room room = null;
            do
            {
                boundsOK = true;
                Vector2 randMin = new Vector2(Main.rand.nextInt(outside.width - (width + 3)), Main.rand.nextInt(outside.height - (height + 3)));
                bounds = new RectInt(randMin, width, height);
                if(outside.onOutsideRooms.isEmpty())
                {
                    continue;
                }

                for(Room r : outside.onOutsideRooms)
                {
                    if(RectInt.intersect(r.bounds, bounds) || RectInt.intersect(player.spawnArea, bounds))
                    {
                        boundsOK = false;
                    }
                }
            }
            while(!boundsOK);

            room = new Room(bounds, dir ,RoomType.chest, outside);
            outside.onOutsideRooms.add(room);
            room.drawRoomOnMap(outside);
        }

        outside.merchant = new Merchant(outside, GamePanel.player);
    }


}
