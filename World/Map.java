package World;
import Math.RectInt;

import java.awt.image.BufferedImage;
import Engine.GamePanel;
import Engine.Tile;
import Main.Main;

import java.awt.Graphics2D; 

public class Map 
{
    public Tile[] tiles;
    public GamePanel gp;
    
    public final int width;
    public final int height;
    public final int area;

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
    }

    //qualche funzione di utility immagino


    public void fillMapWithOneTile(Tile tile)
    {
        for(int i = 0; i < area; i++)
        {
            tiles[i] = tile;
            if(i == (810000 - 1))
            {
                int a = 0;
            }
        }

    }

}
