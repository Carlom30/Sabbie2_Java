package Object;

import Main.Main;
import Main.Utils;
import Math.Vector2;

public class remoteTnt extends SuperObject 
{
    public remoteTnt()
    {
        this.sprite = Utils.loadSprite("/Sprites/objects/remoteTnt.png");
        this.worldPos = new Vector2(Main.gp.player.worldPosition.x, Main.gp.player.worldPosition.y + Main.gp.tileSize);
        this.name = "remoteTnt";
    }
}
