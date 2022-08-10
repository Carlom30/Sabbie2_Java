package Engine;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

import Entity.Inventory;
import Entity.Player;
import Main.*;
import Math.Vector2;
import Object.SuperObject;
import Object.SuperObject.objecType;
import World.Map;

import java.awt.Graphics2D;


public class Engine 
{
    //could be great to make a function that load all sprites. So the game doesn't need to do it every time 
    //a struct is allocated
    public static int HUDoffset = 2;

    public static void printMap(Map map,  Graphics2D g2D)
    {
        GamePanel gp = Main.gp;

        for(int y = 0; y < map.height; y++)
        {
            for(int x = 0; x < map.width; x++)
            {
                int offset = y * map.width + x;
                // SE C'È SEGM FAULT TI PREGO SONO LE VARIABILI DEL PORCODIO SU gamepanel
                Tile tile = map.tiles[offset];
                
                /* supponiamo che mapcolm e maprow siano 0, allora mapxy sarà 0, 0 e così via.
                 * avremo quindi ogni tile nella sua posizione nel mondo.
                 * ora però è necessario convertire queste coordinate in coordinate per lo screen,
                 * quindi si sottrae da mapXY la posizione del player rispetto alla mappa totale
                 * sommata però alla screen position (che però, nota bene, è prefixed al centro dello schermo):
                 * 
                 * ############
                 * #..........#
                 * #..........#
                 * #.......@..#
                 * #..........#
                 * ############
                 * 
                 * supponiamo che il player (@) sia a [8, 3] (world pos) e che lo screen sia un 3 x 3,
                 * quindi player è a [1, 1] nello screen:
                 * 
                 * allora si avrà
                 * mapX = 0 * 1 (nell'esempio tilesize è 1) = 0
                 * e quindi:
                 * screenX = 0 - 8 + 1 = -7
                 * 
                 * contando dallo [0, 0] dello screen "all'indietro" si ottiene -7 per le X
                 * che è esattamente il risultato voluto, e come atteso, non comparirà a schermo
                 */

                 //quindi, banalmente, queste 4 righe implementano la camera
                int mapX = x * gp.tileSize;
                int mapY = y * gp.tileSize;
                int screenX = mapX - gp.player.worldPosition.x + gp.player.screenPosition.x;
                int screenY = mapY - gp.player.worldPosition.y + gp.player.screenPosition.y;


                BufferedImage sprite = tile.sprite;

                //easlly, se è furoi dallo schermo non faccio print
                if(mapX + gp.tileSize < gp.player.worldPosition.x - gp.player.screenPosition.x ||
                   mapX - gp.tileSize > gp.player.worldPosition.x + gp.player.screenPosition.x ||
                   mapY + gp.tileSize < gp.player.worldPosition.y - gp.player.screenPosition.y ||
                   mapY - gp.tileSize > gp.player.worldPosition.y + gp.player.screenPosition.y)
                {
                    continue;
                }

                g2D.drawImage(sprite, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
        }
    } 

    public static void printObjects(Graphics2D g2D)
    {
        for(SuperObject obj : Main.gp.printableObj)
        {

            obj.draw(g2D, Main.gp);
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
    
    public static void printInventory(Graphics2D g2D, Player player)
    {

        for(int i = 0; i < Inventory.allItemsSprite.length; i++)
        {
            
            Vector2 spritePosition = new Vector2(GamePanel.screenWidth - HUDoffset * (GamePanel.tileSize), (i * GamePanel.tileSize) + GamePanel.tileSize /*+1*/);

            BufferedImage image = Inventory.allItemsSprite[i];
            BufferedImage value = Inventory.allNumbers[player.inventory.getValuesInOrder()[i]];

            g2D.drawImage(image, spritePosition.x, spritePosition.y, GamePanel.tileSize, GamePanel.tileSize, null);
            g2D.drawImage(value, spritePosition.x + GamePanel.tileSize, spritePosition.y, GamePanel.tileSize, GamePanel.tileSize, null);
        }
    }

    public static void printStatus(Graphics2D g2D, Player player)
    {
        Vector2 heartPosition = new Vector2(0, 0);
        g2D.drawImage(player.heart, heartPosition.x, heartPosition.y, GamePanel.tileSize, GamePanel.tileSize, null);
        
        Vector2 barPosition = new Vector2(0, 0);
        BufferedImage sprite;

        int i = 0;
        
        for(int j = 0; j < player.getLifePoints(); j++)
        {
            //qui ci vano i pieni
            barPosition = new Vector2(GamePanel.tileSize * (j + 1), 0);
            sprite = player.healtBar_base;
            if(j == (player.lifePoints_max - 1))
            {
                sprite = player.healtBar_end;
                i = j;
                g2D.drawImage(sprite, barPosition.x, barPosition.y, GamePanel.tileSize, GamePanel.tileSize, null);
                break;
            }
            g2D.drawImage(sprite, barPosition.x, barPosition.y, GamePanel.tileSize, GamePanel.tileSize, null);
            i = j;
        }

        if(i == player.lifePoints_max - 1)
        {
            //vuol dire full hp, ritorno
            return;    
        }

        for(; i < player.lifePoints_max; i++)
        {
            barPosition = new Vector2(GamePanel.tileSize * (i + 1), 0);
            sprite = player.healtBar_base_empty;
            if(i == (player.lifePoints_max - 1))
            {
                sprite = player.healtBar_end_empty;
                g2D.drawImage(sprite, barPosition.x, barPosition.y, GamePanel.tileSize, GamePanel.tileSize, null);
                break;
            }
            g2D.drawImage(sprite, barPosition.x, barPosition.y, GamePanel.tileSize, GamePanel.tileSize, null);
        }

        return;
    }

    public static void printHUD(Graphics2D g2D, Player player)
    {
        printInventory(g2D, player);
        printStatus(g2D, player);
    }
}
