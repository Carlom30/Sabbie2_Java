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
    BufferedImage sprite;
    boolean collision = false;

    public Tile(BufferedImage sprite)
    {
        this.sprite = sprite;
    }
}
