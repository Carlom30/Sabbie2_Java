package World;

import Math.*;
import java.awt.image.BufferedImage;

import Engine.GamePanel;
import Engine.Tile;
import Main.Utils;


public class Room 
{

    RectInt bounds;
    Tile[] tiles;
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

    public Room(RectInt bounds)
    {
        this.bounds = bounds;
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
                
                if(i == 0 || i == (bounds.height - 1) || j == 0 || j == (bounds.width - 1))
                {
                    tiles[offset].sprite = wall;
                    tiles[offset].collision = true;    
                }
            }
        }
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
