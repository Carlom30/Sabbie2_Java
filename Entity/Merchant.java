package Entity;

import Engine.GamePanel;
import Main.Main;
import Main.Utils;
import Math.RectInt;
import Math.Vector2;
import Obj.Chest;
import World.Map;
import World.Room;
import World.Room.RoomType;
import java.awt.image.BufferedImage;

import java.awt.Graphics2D;


public class Merchant extends Entity
{
    
    final int randomGoldValueWanted = Main.rand.nextInt(9) + 1;
    public int goldObtained = 0;
    public int randomChestCost = Main.rand.nextInt(5) + 1;
    int roomWidth = 9;
    int roomHeight = 9;
    BufferedImage sprite;

    public Chest merchantChest;



    public Merchant(Map outside, Player player)
    {
        Room merchRoom = generateMerchantRoom(outside, player);
        sprite = Utils.loadSprite("/Sprites/world/outside/merchant.png");
        worldPosition = new Vector2(merchRoom.bounds.width / 2, merchRoom.bounds.height / 2 - 1);
        worldPosition = Vector2.roomToGlobalPosition(worldPosition, merchRoom);
        
    }

    Room generateMerchantRoom(Map outside, Player player)
    {
        int randMinX = 0;
        int randMinY = 0;
        RectInt bounds = null;
        boolean boundsOK = true;
        do
        {
            boundsOK = true;
            randMinX = Main.rand.nextInt(outside.width - (roomWidth + 3)) + 3;
            randMinY = Main.rand.nextInt(outside.height  - (roomHeight + 3)) + 3;
            bounds = new RectInt(new Vector2(randMinX, randMinY), this.roomWidth, this.roomHeight);

            if(RectInt.intersect(player.spawnArea, bounds))
            {
                boundsOK = false;
            }

            for(Room room : outside.onOutsideRooms)
            {
                if(RectInt.intersect(room.bounds, bounds))
                {
                    boundsOK = false;
                    break;
                }
            }
        }
        //questo while assicura che la stanza non intersechi mai il player
        while(!boundsOK);

        Room merchantRoom = new Room(bounds, null, RoomType.merchant, outside);
        merchantRoom.drawRoomOnMap(outside);
        outside.onOutsideRooms.add(merchantRoom);

        merchantChest = new Chest(new Vector2(merchantRoom.bounds.min.x + merchantRoom.bounds.width / 2, merchantRoom.bounds.min.y + merchantRoom.bounds.height / 2 + 1), outside);
        GamePanel.printableObj.add(merchantChest);

        return merchantRoom;
    }

    public void draw(Graphics2D g2D, GamePanel gp)
    {
        int screenX = worldPosition.x - GamePanel.player.worldPosition.x + GamePanel.player.screenPosition.x;
        int screenY = worldPosition.y - GamePanel.player.worldPosition.y + GamePanel.player.screenPosition.y;

        if( worldPosition.x + GamePanel.tileSize < GamePanel.player.worldPosition.x - GamePanel.player.screenPosition.x ||
            worldPosition.x - GamePanel.tileSize > GamePanel.player.worldPosition.x + GamePanel.player.screenPosition.x ||
            worldPosition.y + GamePanel.tileSize < GamePanel.player.worldPosition.y - GamePanel.player.screenPosition.y ||
            worldPosition.y - GamePanel.tileSize > GamePanel.player.worldPosition.y + GamePanel.player.screenPosition.y)
        {
            return;
        }

        g2D.drawImage(sprite, screenX, screenY, GamePanel.tileSize, GamePanel.tileSize, null);
    }
    

}
