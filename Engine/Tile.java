package Engine;

import World.*;
import Math.Vector2;
import Math.Vector2float;

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
    public BufferedImage sprite;
    public boolean collision = false;
    public boolean isElevated; //terza dimenzione per il perlin noise

    public Map connectedMap;
    public Room linkedRoom;

    public TileType type;
    public boolean hasTnt;

    public Vector2 onRoomPosition;


    public Tile(BufferedImage sprite)
    {
        this.sprite = sprite;
    }

    public void setConnectedMap(Map map)
    {
        connectedMap = map;
    }

    public Map getConnectedMap()
    {
        return connectedMap;
    }
}
