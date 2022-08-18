package Object;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import Engine.GamePanel;
import Entity.Inventory;
import Entity.Player;
import Main.Main;
import Main.Utils;
import Math.Vector2;
import World.Map;

public class Chest extends SuperObject 
{
    int tntNumb = 0;
    int healthNumb = 0;
    int electronicNumb = 0;
    int explosiveNumb = 0;
    int gold = 0;

    int maxObjInChest = 5;
    int maxQuantityPerObj = 3; //the classic rule of three in videogames always works 

    int totalObjects = SuperObject.totalObjects - 1; //nella chest non è possibile trovare ladders

    int[] loot = new int[]
    {
        tntNumb,
        healthNumb,
        electronicNumb,
        explosiveNumb,
        gold
    };


    public Chest(Vector2 worldPos, Map map)
    {
        sprite = Utils.loadSprite("/Sprites/objects/chest.png");
        collision = true;
        generateLoot();
        this.worldPos = new Vector2(worldPos.x * GamePanel.tileSize, worldPos.y * GamePanel.tileSize);
        map.onMapObjects.add(this);
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
        int[] randObjs = new int[Main.rand.nextInt(maxObjInChest) + 1];
        for(int j = 0; j < randObjs.length; j++)
        {
            randObjs[j] = Main.rand.nextInt(totalObjects);
        }

        for(int i = 0; i < randObjs.length; i++)
        {
            loot[randObjs[i]] = Main.rand.nextInt(maxQuantityPerObj);
        }
    }

    @Override
    public boolean interact(Player player) 
    {
        generateLoot();
        player.inventory.modifyValue_tnt(loot[0]);
        player.inventory.modifyValue_healthPotion(loot[1]);
        player.inventory.addObj_component(loot[2], loot[3]);
        player.inventory.modifyValue_gold(loot[4]);

        return true;
    }
}
