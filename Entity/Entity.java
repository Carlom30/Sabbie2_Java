package Entity;
import Math.*;
import Object.SuperObject;

import java.awt.image.BufferedImage;
import java.text.CollationElementIterator;

import Engine.GamePanel;


import Main.Utils.*;

public abstract class Entity 
{
    public Vector2 worldPosition;
    public int velocity;

    int lifePoints;
    public String name;


    public BufferedImage idle;
    public BufferedImage up_1;
    public BufferedImage up_2;
    public BufferedImage down_1;
    public BufferedImage down_2;
    public BufferedImage right_idle;
    public BufferedImage right_1;
    public BufferedImage left_idle;
    public BufferedImage left_1;

    public BufferedImage sprite;

    public Directions direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public RectInt collisionArea;
    public Vector2 collisionAreaMin_Default;
    public Boolean collisionOn = false;


    //in questo caso è piuttosto importante lasciare lifePoint private, poiché è sempre meglio, nei videogiochi, 
    //avere una funzione che rimuova o aggiunga hp, invece di toccare il parametro direttamente al di fuori della classe 
    public int getLifePoints()
    {
        return this.lifePoints;
    }

    public void setLifePoints(int summValue)
    {
        //per adesso is just:
        lifePoints += summValue; //value può essere positivo o negativo
    }
}
