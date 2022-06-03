package World;
import Main.GamePanel;
import Math.RectInt;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class Map 
{
    Tile[] tiles;
    GamePanel gp;
    
    int width;
    int height;
    int area;


    public Map(GamePanel gp, int width, int height)
    {
        //NOTA FUCKING BENE, le dimenzioni effetive della mappa non saranno mai queste, ma ben più grandi
        this.gp = gp;
        area = width * height;
        tiles = new Tile[area];
        this.width = width;
        this.height = height;
    }

    //qualche funzione di utility immagino

    public void printMap(Graphics2D g2D)
    {
        for(int y = 0; y < gp.maxScreenRow; y++)
        {
            for(int x = 0; x < gp.maxScreenColumn; x++)
            {
                int offset = y * gp.maxScreenColumn + x;
                Tile tile = tiles[offset];
                BufferedImage sprite = tile.sprite;
                g2D.drawImage(sprite, x * gp.tileSize, y * gp.tileSize , gp.tileSize, gp.tileSize, null);
            }
        }
    }

    public void fillMapWithOneTile(Tile tile)
    {
        for(int i = 0; i < area; i++)
        {
            tiles[i] = tile;
        }
    }

}
