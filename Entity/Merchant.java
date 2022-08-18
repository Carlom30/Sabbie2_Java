package Entity;

import Engine.GamePanel;
import Main.Main;
import Main.Utils;
import Math.RectInt;
import Math.Vector2;
import World.Map;
import World.Room;
import World.Room.RoomType;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;


public class Merchant extends Entity
{
    
    final int randomGoldValueWanted = Main.rand.nextInt(9) + 1;
    int goldObtained = 0;
    int randomChestCost = Main.rand.nextInt(5) + 1;
    int roomWidth = 9;
    int roomHeight = 9;
    BufferedImage sprite;


    public Merchant(Map outside, Player player)
    {
        Room merchRoom = generateMerchantRoom(outside, player);
        sprite = Utils.loadSprite("/Sprites/world/outside/merchant.png");
        worldPosition = new Vector2(merchRoom.bounds.width / 2, merchRoom.bounds.height / 2);
        worldPosition = Vector2.roomToGlobalPosition(worldPosition, merchRoom);
        
    }

    Room generateMerchantRoom(Map outside, Player player)
    {
        int randMinX = 0;
        int randMinY = 0;
        do
        {
            randMinX = Main.rand.nextInt(outside.width) - (roomWidth + 1);
            randMinY = Main.rand.nextInt(outside.height) - (roomHeight + 1);
        }
        //questo while assicura che la stanza non intersechi mai il player
        while(randMinX > outside.width - (player.worldPosition.x / GamePanel.tileSize) - roomWidth / 2 &&
              randMinX < outside.width + (player.worldPosition.x / GamePanel.tileSize) + roomWidth / 2 &&
              randMinY > outside.height - (player.worldPosition.y / GamePanel.tileSize) - roomHeight / 2 &&
              randMinY < outside.height + (player.worldPosition.y / GamePanel.tileSize) + roomHeight / 2);
        
        Vector2 roomMin = new Vector2(randMinX, randMinY);
        RectInt bounds = new RectInt(roomMin, roomWidth, roomHeight);

        Room merchantRoom = new Room(bounds, null, RoomType.merchant, outside);
        merchantRoom.drawRoomOnMap(outside);
        outside.onOutsideRooms.add(merchantRoom);

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
