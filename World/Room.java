package World;

import Math.*;
import java.awt.image.BufferedImage;

import Main.GamePanel;
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
        Utils u = new Utils();
        wall = u.loadSprite("/Sprites/wallme.png");
        floor = u.loadSprite("/Sprites/world/sand0.png");
        u = null; //garbagecollector or shit like that i dunno why there's no free() function?
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
                tiles[offset] = new Tile(new Vector2(0, 0), floor);
                
                if(i == 0 || i == (bounds.height - 1) || j == 0 || j == (bounds.width - 1))
                {
                    tiles[offset].sprite = wall;
                }
            }
        }
    }

    public void printRoomOnMap(Map map, GamePanel gp)
    {
        int k = 0;
        for(int i = bounds.min.y; i < bounds.min.y + bounds.height; i++)
        {
            for(int j = bounds.min.x; j < bounds.min.x + bounds.width; j++)
            {
                int offset = i * gp.maxScreenColumn + j; 
                map.tiles[offset] = this.tiles[k];
                k++;
            }
        }
    }
}
