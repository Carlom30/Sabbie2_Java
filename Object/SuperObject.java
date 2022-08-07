package Object;
import java.awt.image.BufferedImage;

import Math.RectInt;
import Math.Vector2;
import java.awt.Graphics2D;
import Engine.GamePanel;
import Main.Main;

public abstract class SuperObject 
{

    public enum objecType
    {
        gold,
        explosive,
        electronicComponent,
        remoteTnt,
        potion,
        Ladder;
    }

    public String name;
    public boolean collision = false;
    public RectInt collisionArea = new RectInt(new Vector2(0, 0), GamePanel.tileSize, GamePanel.tileSize);
    public BufferedImage sprite;
    public Vector2 worldPos;
    public objecType type;

    public Vector2 collsionAreaMin_Default = new Vector2(0, 0);

    //in genere questo è lo script per printare qualcosa a schermo che non sia già in mappa
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

        g2D.drawImage(sprite, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

    public void addObjToList()
    {
        if(Main.gp.printableObj.size() < Main.gp.maxPrintableObject)
        {
            Main.gp.printableObj.add(this);
        }
    }


}
