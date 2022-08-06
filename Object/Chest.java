package Object;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import Engine.GamePanel;
import Main.Main;
import Main.Utils;
import Math.Vector2;

public class Chest extends SuperObject 
{
    List<SuperObject> loot;

    public Chest(Vector2 worldPos)
    {
        loot = new ArrayList<SuperObject>();
        sprite = Utils.loadSprite("/Sprites/objects/chest.png");
        collision = true;
        generateLoot();
        this.worldPos = new Vector2(worldPos.x * GamePanel.tileSize, worldPos.y * GamePanel.tileSize);
        Main.gp.printableObj.add(this);
    }

    //per ora farò della matematica semplice, ma nel caso decidessi di continuare questo gioco come side project dovrò fare 
    //più attenzione al loot
    void generateLoot()
    {
        /*logica di looting:
         * nel gioco il fulcro è trovare remote/componenti per la remote
         * quindi diciamo che in generale ogni chestroom dovrebbe ridare con una possibilità del 75% 
         * le componenti spese per arrivare alla chest, quindi diciamo almeno 
         */
    }
}
