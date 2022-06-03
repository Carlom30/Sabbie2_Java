package Entity;
import Main.*;
import Main.Utils.Directions;
import Math.Vector2;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Player extends Entity 
{
    GamePanel gp; //rendering part
    KeyHandler kh; //input manager (sarà comunque una classe a parte)

    public Player(GamePanel gp, KeyHandler kh)
    {
        this.gp = gp;
        this.kh = kh;
        setDefaultValues();
        readPlayerSprites();
    }

    public void setDefaultValues()
    {
        position = new Vector2(100, 100);
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
                position.y -= velocity;
            }
    
            if(kh.downPressed)
            {
                direction = Directions.down;
                position.y += velocity;
            }
            
            if(kh.leftPressed)
            {
                direction = Directions.left;
                position.x -= velocity;
            }
    
            if(kh.rightPressed)
            {
                direction = Directions.right;
                position.x += velocity;
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

    public void print(Graphics2D g2D)
    {
        /*g2D.setColor(Color.white);
        g2D.fillRect(position.x, position.y, gp.tileSize, gp.tileSize);*/

        BufferedImage image = null;

        switch(direction)
        { 
            case up:
                if(spriteNum == 1)
                {
                    image = up_1;
                }

                if(spriteNum == 2)
                {
                    image = up_2;
                }
                break;
                
            case down:
                if(spriteNum == 1)
                {
                    image = down_1;
                }

                if(spriteNum == 2)
                {
                    image = down_2;
                }
                break;

            case right:
                if(spriteNum == 1)
                {
                    image = right_idle;
                }
                
                if(spriteNum == 2)
                {
                    image = right_1;
                }
                break;
                
            case left:
                if(spriteNum == 1)
                {
                    image = left_idle;
                }

                if(spriteNum == 2)
                {
                    image = left_1;
                }
                break;
        }
        g2D.drawImage(image, position.x, position.y, gp.tileSize, gp.tileSize, null);
    }
}
