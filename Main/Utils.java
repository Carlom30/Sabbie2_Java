package Main;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.lang.model.util.ElementScanner6;
import javax.swing.text.Utilities;

import java.io.IOException;
import java.util.List;
import java.util.Vector;
import Math.Vector2;

public class Utils 
{
    public static long currentTime = -1; //the cursed "-1 second has passed"
    /*public static void main(String[] args) 
    {
        long timeToPass = 1000;
        Utils.currentTime = System.currentTimeMillis();
        int counter = 0;
        while(true)
        {
            if(Utils.timeIsPassed(Utils.currentTime, timeToPass))
            {
                counter++;
                printf("" + counter);
                //Utilities = System.currentTimeMillis();
            }
        }    
    }*/
    public static enum Directions
    {
        up,
        down,
        right,
        left,
        idle,

        ALL_DIRECTIONS;
        
        /* Java
         * 
         * 
         *  Java
         */
     
    };

    public static Directions[] allDirections = new Directions[]
    {
        Directions.up,
        Directions.down,
        Directions.right,
        Directions.left
    };

    public static void printf(String string)
    {
        System.out.println(string);
    }

    public static BufferedImage loadSprite(String path)
    {
        BufferedImage sprite = null;
        try 
        {
            sprite = ImageIO.read(Utils.class.getResourceAsStream(path));
        }

        catch (Exception e) 
        {
            e.printStackTrace();
        }

        return sprite;
    }

    public static Directions getOppositeDirection(Directions direction)
    {
        if(direction == Directions.up)
        {
            return Directions.down;
        }

        else if(direction == Directions.down)
        {
            return Directions.up;
        }

        else if(direction == Directions.right)
        {
            return Directions.left;
        }

        else if(direction == Directions.left)
        {
            return Directions.right;
        }

        else
        {
            return Directions.ALL_DIRECTIONS;
        }
    }

    public static boolean timeIsPassed(long lastTime, long timeToWait) //time to wait must be in millisec
    {
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - lastTime;
        if(delta >= timeToWait)
        {
            Utils.currentTime = -1;
            return true;
        }

        return false;
    }

}
