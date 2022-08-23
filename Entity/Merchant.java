package Entity;

import Engine.Engine;
import Engine.GamePanel;
import Engine.Tile;
import Engine.Tile.TileType;
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
    final int randomGoldValueWanted = Main.rand.nextInt(7) + 3;
    final int randomGoldWanted = randomGoldValueWanted * (Main.rand.nextInt(9) + 1);
    public int goldObtained = 0;
    public int randomChestCost = Main.rand.nextInt(4) + 2; //[2, 5]
    int roomWidth = 9;
    int roomHeight = 9;
    BufferedImage sprite;

    public Chest merchantChest;
    Vector2 goldSpriteLocalPosition;
    Vector2 chestCostSpriteLocalPosition;
    Vector2 goldWantedLocalPosition;

    Room merchantRoom;

    public Merchant(Map outside, Player player)
    {
        name = "merchant";
        Room merchRoom = generateMerchantRoom(outside, player);
        sprite = Utils.loadSprite("/Sprites/world/outside/merchant.png");
        worldPosition = new Vector2(merchRoom.bounds.width / 2, merchRoom.bounds.height / 2 - 1);
        worldPosition = Vector2.roomToGlobalPosition(worldPosition, merchRoom);
        merchantRoom = merchRoom;
        collisionOn = true;
        collisionArea = new RectInt(new Vector2(8, 8), 32, 32);
        collisionAreaMin_Default = collisionArea.min;
        
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

        goldSpriteLocalPosition = new Vector2(merchantRoom.bounds.width / 2 + 2, merchantRoom.bounds.height / 2 + 1);
        chestCostSpriteLocalPosition = new Vector2(merchantRoom.bounds.width / 2 + 1, merchantRoom.bounds.height / 2 + 1);
        goldWantedLocalPosition = new Vector2(merchantRoom.bounds.width / 2, merchantRoom.bounds.height / 2 - 2);

        return merchantRoom;
    }

    public void draw(Graphics2D g2D, GamePanel gp)
    {
        Engine.printSpriteOnWorld(g2D, GamePanel.player, worldPosition, sprite);
        Engine.printSpriteOnWorld(g2D, GamePanel.player, Vector2.roomToGlobalPosition(goldSpriteLocalPosition, merchantRoom),
            Inventory.allItemsSprite[Inventory.GOLD]);
        Engine.printSpriteOnWorld(g2D, GamePanel.player, Vector2.roomToGlobalPosition(chestCostSpriteLocalPosition, merchantRoom), 
            Inventory.allNumbers[randomChestCost]);
        Engine.printSpriteOnWorld(g2D, GamePanel.player, Vector2.roomToGlobalPosition(goldWantedLocalPosition, merchantRoom), 
            Inventory.allNumbers[(randomGoldWanted - goldObtained) / randomGoldValueWanted]);
    }
    
    public void interact(Player player)
    {
        if(player.inventory.modifyValue_gold(-(randomGoldValueWanted)))
        {
            goldObtained += randomGoldValueWanted;
        }

        if(goldObtained < randomGoldWanted)
        {
            return;
        }

        Map map = player.linkedMap;
        int i = 0;
        int j = 0;
        int k = 0;
        do
        {
            i = Main.rand.nextInt(map.height) + 1;
            j = Main.rand.nextInt(map.width) + 1;
            k = i * map.width + j;
        }
        while(map.tiles[k].type != TileType.terrain);

        map.tiles[k] = new Tile(Utils.loadSprite("/Sprites/world/sand/redx.png"));
        map.tiles[k].treasureTile = true;
        Utils.printf("red x at (" + j + ", " + i + ")");
    }

}
