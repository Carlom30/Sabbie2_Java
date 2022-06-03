package Main;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Utils 
{
    public static enum Directions
    {
        up,
        down,
        right,
        left,
        idle,

        ALL_DIRECTIONS
    };

    public BufferedImage loadSprite(String path)
    {
        BufferedImage sprite = null;
        try 
        {
            sprite = ImageIO.read(getClass().getResourceAsStream(path));
        }

        catch (Exception e) 
        {
            e.printStackTrace();
        }

        return sprite;
    }

}
