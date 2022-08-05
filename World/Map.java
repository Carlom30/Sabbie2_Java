package World;
import Math.RectInt;
import Math.Vector2;

import java.awt.image.BufferedImage;

import javax.swing.text.Utilities;

import Engine.GamePanel;
import Engine.Tile;
import Main.Main;
import Main.Utils;

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

}
