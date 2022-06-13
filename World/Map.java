package World;
import Math.RectInt;

import java.awt.image.BufferedImage;
import Engine.GamePanel;
import java.awt.Graphics2D; 

public class Map 
{
    public Tile[] tiles;
    public GamePanel gp;
    
    public final int width;
    public final int height;
    public final int area;


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

    public void fillMapWithOneTile(Tile tile)
    {
        for(int i = 0; i < area; i++)
        {
            tiles[i] = tile;
        }
    }

}
