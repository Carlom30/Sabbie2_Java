package Engine;
import java.awt.image.BufferedImage;

import Entity.Player;
import Main.*;
import World.Map;
import World.Tile;
import java.awt.Graphics2D;


public class Engine 
{

    //could be great to make a function that load all sprites. So the game doesn't need to do it every time 
    //a struct is allocated
    public static void printMap(Map map,  Graphics2D g2D)
    {
        GamePanel gp = Main.gp;
        for(int y = 0; y < gp.maxScreenRow; y++)
        {
            for(int x = 0; x < gp.maxScreenColumn; x++)
            {
                int offset = y * gp.maxScreenColumn + x;
                Tile tile = map.tiles[offset];
                BufferedImage sprite = tile.sprite;
                g2D.drawImage(sprite, x * gp.tileSize, y * gp.tileSize , gp.tileSize, gp.tileSize, null);
            }
        }
    } 
    
    public static void printPlayer(Graphics2D g2D, Player player)
    {
        /*g2D.setColor(Color.white);
        g2D.fillRect(position.x, position.y, gp.tileSize, gp.tileSize);*/

        BufferedImage image = null;

        switch(player.direction)
        { 
            case up:
                if(player.spriteNum == 1)
                {
                    image = player.up_1;
                }

                if(player.spriteNum == 2)
                {
                    image = player.up_2;
                }
                break;
                
            case down:
                if(player.spriteNum == 1)
                {
                    image = player.down_1;
                }

                if(player.spriteNum == 2)
                {
                    image = player.down_2;
                }
                break;

            case right:
                if(player.spriteNum == 1)
                {
                    image = player.right_idle;
                }
                
                if(player.spriteNum == 2)
                {
                    image = player.right_1;
                }
                break;
                
            case left:
                if(player.spriteNum == 1)
                {
                    image = player.left_idle;
                }

                if(player.spriteNum == 2)
                {
                    image = player.left_1;
                }
                break;
        }
        g2D.drawImage(image, player.screenPosition.x, player.screenPosition.y, player.gp.tileSize, player.gp.tileSize, null);
    }
}
