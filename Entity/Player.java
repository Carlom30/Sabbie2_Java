package Entity;
import Main.*;
import Main.Utils.Directions;
import Math.RectInt;
import Math.Vector2;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;

import Engine.*;

import java.awt.image.BufferedImage;

public class Player extends Entity 
{
    public GamePanel gp; //rendering part
    KeyHandler kh; //input manager (sarà comunque una classe a parte)
    public Vector2 screenPosition;

    public boolean DEV_MODE;

    public Player(GamePanel gp, KeyHandler kh, Vector2 worldPos)
    {
        DEV_MODE = false;
        this.gp = gp;
        this.kh = kh;
        setDefaultValues();
        if(worldPos != null)
        {
            worldPosition = worldPos;
        }
        readPlayerSprites();
        screenPosition = new Vector2(gp.screenWidth / 2 - gp.tileSize / 2, gp.screenHeight / 2 - gp.tileSize / 2);
        collisionArea = new RectInt(new Vector2(8, 16), 32, 32);  
        //up: print character at the exact centre of the screen
    }

    public void setDefaultValues()
    {
        worldPosition = new Vector2((gp.map.width / 2) * gp.tileSize, (gp.map.height / 2) * gp.tileSize); //new Vector2(gp.tileSize * 23, gp.tileSize * 21);
        velocity = 4; //velocity = 4 ma metto di più per testing
        direction = Directions.down;
        
        if(DEV_MODE)
        {
            velocity = velocity * 10;
            collisionArea = new RectInt(new Vector2(0, 0), 0, 0);
        }
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
                gp.collision.checkForCollision_Tile(this);
                if(collisionOn == false)
                {
                    worldPosition.y -= velocity;
                }
            }
    
            else if(kh.downPressed)
            {
                direction = Directions.down;
                gp.collision.checkForCollision_Tile(this);
                if(collisionOn == false)
                {
                    worldPosition.y += velocity;
                }
            }
            
            else if(kh.leftPressed)
            {
                direction = Directions.left;
                gp.collision.checkForCollision_Tile(this);
                if(collisionOn == false)
                {
                    worldPosition.x -= velocity;
                }
            }
    
            else if(kh.rightPressed)
            {
                direction = Directions.right;
                gp.collision.checkForCollision_Tile(this);
                if(collisionOn == false)
                {
                    worldPosition.x += velocity;
                }
            }
    
            collisionOn = false;


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
