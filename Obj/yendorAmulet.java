package Obj;

import Engine.GamePanel;
import Entity.Player;
import Main.Utils;
import Math.RectInt;
import Math.Vector2;
import World.Dungeon;
import World.Room;

public class yendorAmulet extends SuperObject
{
    public yendorAmulet(Room room, Dungeon dungeon)
    {
        sprite = Utils.loadSprite("/Sprites/objects/Yendor.png");
        worldPos = Vector2.roomToGlobalPosition(new Vector2(room.bounds.width / 2, room.bounds.height / 2), room);
        dungeon.area.onMapObjects.add(this);
        collisionArea = new RectInt(new Vector2(0, 0), GamePanel.tileSize, GamePanel.tileSize);
        collision = true;
    }

    @Override
    public boolean interact(Player player) 
    {
        //player win function
        GamePanel.playerHasWon = true;
        Utils.printf("you win!!");
        return true;
         
    }
}
