package Obj;

import Engine.GamePanel;
import Engine.Tile;
import Main.Utils;
import Math.Vector2;

public class remoteTnt extends SuperObject 
{
    public Tile attachedTile;
    public remoteTnt(Tile directedTile)
    {
        attachedTile = directedTile;
        Vector2 vector = GamePanel.player.linkedMap.getGlobalTileVector(attachedTile);

        this.type = objecType.remoteTnt;
        this.sprite = Utils.loadSprite("/Sprites/objects/remoteTnt.png");
        this.worldPos = new Vector2(vector.x * GamePanel.tileSize, vector.y * GamePanel.tileSize);
        this.name = "remoteTnt";
        addObjToList();
        directedTile.hasTnt = true;
    }
    
}
