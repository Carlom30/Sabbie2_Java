package Entity;
import Math.*;
import Object.SuperObject;

import java.awt.image.BufferedImage;
import java.text.CollationElementIterator;

import Main.Utils.*;

public abstract class Entity 
{
    public Vector2 worldPosition;
    public int velocity;

    public BufferedImage idle;
    public BufferedImage up_1;
    public BufferedImage up_2;
    public BufferedImage down_1;
    public BufferedImage down_2;
    public BufferedImage right_idle;
    public BufferedImage right_1;
    public BufferedImage left_idle;
    public BufferedImage left_1;

    public Directions direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public RectInt collisionArea;
    public Boolean collisionOn = false;

    public SuperObject[] inventory;
    
}
