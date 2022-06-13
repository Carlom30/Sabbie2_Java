package Entity;
import Main.*;
import Main.Utils.Directions;
import Math.Vector2;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;

import Engine.GamePanel;

import java.awt.image.BufferedImage;

public class Player extends Entity 
{
    public GamePanel gp; //rendering part
    KeyHandler kh; //input manager (sarà comunque una classe a parte)
    public Vector2 screenPosition;

    public Player(GamePanel gp, KeyHandler kh)
    {
        this.gp = gp;
        this.kh = kh;
        setDefaultValues();
        readPlayerSprites();
        screenPosition = new Vector2(gp.screenWidth / 2 - gp.tileSize / 2, gp.screenHeight / 2 - gp.tileSize / 2);
        //up: print character at the exact centre of the screen
    }

    public void setDefaultValues()
    {
        worldPosition = new Vector2(gp.tileSize * 23, gp.tileSize * 21);
        velocity = 4;
        direction = Directions.idle;
    }

    public void readPlayerSprites()
    {
        try
        {
            idle = ImageIO.read(getClass().getResourceAsStream("/Sprites/character/char_idle.png"));
            up_1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/character/char_up1.png"));
            up_2 = ImageIO.read(getClass().getResourceAsStream("/Sprites/character/char_up2.png"));
            down_1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/character/char_down1.png"));
            down_2 = ImageIO.read(getClass().getResourceAsStream("/Sprites/character/char_down2.png"));
            right_idle = ImageIO.read(getClass().getResourceAsStream("/Sprites/character/char_right_idle.png"));
            right_1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/character/char_right1.png"));
            left_idle = ImageIO.read(getClass().getResourceAsStream("/Sprites/character/char_left_idle.png"));
            left_1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/character/char_left1.png"));
        }

        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void update()
    {
        if(kh.downPressed || kh.leftPressed || kh.rightPressed || kh.upPressed)
        {
            if(kh.upPressed)
            {
                direction = Directions.up;
                worldPosition.y -= velocity;
            }
    
            if(kh.downPressed)
            {
                direction = Directions.down;
                worldPosition.y += velocity;
            }
            
            if(kh.leftPressed)
            {
                direction = Directions.left;
                worldPosition.x -= velocity;
            }
    
            if(kh.rightPressed)
            {
                direction = Directions.right;
                worldPosition.x += velocity;
            }
    
            spriteCounter++;
            if(spriteCounter > 15) // sostanzialmente 10 è la "velocità" di change dello sprite, più è alto, e più gli sprite ci mettono a cambiare
            {                      // quindi in questo caso cambia ogni 10 frame, poiché questa funzione è chiamata ad ogni frame
                if(spriteNum == 1)
                {
                    spriteNum = 2;
                }
    
                else if(spriteNum == 2)
                {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
    }

}
