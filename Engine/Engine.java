package Engine;
import java.awt.image.BufferedImage;
import Entity.Player;
import Main.*;
import World.Map;

import java.awt.Graphics2D;


public class Engine 
{
    //could be great to make a function that load all sprites. So the game doesn't need to do it every time 
    //a struct is allocated
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
                if(mapX + gp.tileSize < gp.player.worldPosition.x - gp.player.screenPosition.x ||
                   mapX - gp.tileSize > gp.player.worldPosition.x + gp.player.screenPosition.x ||
                   mapY + gp.tileSize < gp.player.worldPosition.y - gp.player.screenPosition.y ||
                   mapY - gp.tileSize > gp.player.worldPosition.y + gp.player.screenPosition.y)
                {
                    continue;
                }

                g2D.drawImage(sprite, screenX, screenY , gp.tileSize, gp.tileSize, null);
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
