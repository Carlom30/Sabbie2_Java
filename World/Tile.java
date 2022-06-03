package World;

import Math.Vector2;
import java.awt.image.BufferedImage;


public class Tile 
{
    //non so se mi servirà ma intanto lo faccio
    public static enum TileType 
    {
        terrain,
        floor,
        wall,
        door
    }
    //tile è riferito alle tile della mappa e non di ciò che non è mappa
    Vector2 globalPosition; //riferito al corner in alto a sinistra
    BufferedImage sprite;
    boolean collision = false;

    public Tile(Vector2 globalPosition, BufferedImage sprite)
    {
        this.globalPosition = globalPosition;
        this.sprite = sprite;
    }
}
