package Object;
import java.awt.image.BufferedImage;
import Math.Vector2;
import java.awt.Graphics2D;
import Engine.GamePanel;

public abstract class SuperObject 
{

    enum objecType
    {
        gold,
        explosive,
        electronicComponent,
        remoteTnt,
        potion;
    }

    public String name;
    public boolean collision = false;
    public BufferedImage sprite;
    public Vector2 worldPos;

    public void draw(Graphics2D g2D, GamePanel gp)
    {
        int screenX = worldPos.x - gp.player.worldPosition.x + gp.player.screenPosition.x;
        int screenY = worldPos.y - gp.player.worldPosition.y + gp.player.screenPosition.y;

        if( worldPos.x + gp.tileSize < gp.player.worldPosition.x - gp.player.screenPosition.x ||
            worldPos.x - gp.tileSize > gp.player.worldPosition.x + gp.player.screenPosition.x ||
            worldPos.y + gp.tileSize < gp.player.worldPosition.y - gp.player.screenPosition.y ||
            worldPos.y - gp.tileSize > gp.player.worldPosition.y + gp.player.screenPosition.y)
        {
            return;
        }

        g2D.drawImage(sprite, screenX, screenY , gp.tileSize, gp.tileSize, null);
    }
}
