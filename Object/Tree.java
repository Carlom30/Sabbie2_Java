package Object;
import java.awt.image.BufferedImage;

import Engine.GamePanel;
import Math.Vector2;

public class Tree extends SuperObject 
{
    public Tree(BufferedImage sprite, Vector2 worldPosition)
    {
        this.sprite = sprite;
        this.worldPos = worldPosition;
        worldPos = Vector2.scalarPerVector(worldPos, GamePanel.tileSize);
    }
}
