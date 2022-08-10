package Entity;

import java.util.List;

import Main.Utils;
import Object.ElectronicComponent;
import Object.HealtPotion;
import Object.remoteTnt;
import java.awt.image.BufferedImage;

public class Inventory 
{
    //l'inventario in questo gioco è molto semplice perché non esiste nessun tipo di interazione complicata tra oggetti
    int onInventory_tnt;
    int onInventory_tnt_max = 9;

    int onInventory_healthPotion;
    int onInventory_healthPotion_max = 9;
    
    int onInventory_electronic;
    int onInventory_electronic_max = 9;

    int onInventory_explosive;
    int onInventory_explosive_max = 9;

    int onInventory_gold;
    int onInventory_gold_max = 9;

    public static BufferedImage[] allItemsSprite = new BufferedImage[]
    {
        Utils.loadSprite("/Sprites/objects/gold.png"),
        Utils.loadSprite("/Sprites/objects/healthpotion.png"),
        Utils.loadSprite("/Sprites/objects/remoteTnt.png"),
        Utils.loadSprite("/Sprites/objects/electronic.png"),
        Utils.loadSprite("/Sprites/objects/explosive.png"),
    };

    public static BufferedImage[] allNumbers = new BufferedImage[]
    {
        Utils.loadSprite("/Sprites/numbers/zero.png"),
        Utils.loadSprite("/Sprites/numbers/one.png"),
        Utils.loadSprite("/Sprites/numbers/two.png"),
        Utils.loadSprite("/Sprites/numbers/three.png"),
        Utils.loadSprite("/Sprites/numbers/four.png"),
        Utils.loadSprite("/Sprites/numbers/five.png"),
        Utils.loadSprite("/Sprites/numbers/six.png"),
        Utils.loadSprite("/Sprites/numbers/seven.png"),
        Utils.loadSprite("/Sprites/numbers/eight.png"),
        Utils.loadSprite("/Sprites/numbers/nine.png"),
    };

    public Inventory()
    {
        onInventory_tnt = 0;
        onInventory_healthPotion = 1;
        onInventory_electronic = 4;
        onInventory_explosive = 2;
        onInventory_gold = 1;
    }

    public int[] getValuesInOrder()
    {
        int[] valuesInOrder = 
        {
            onInventory_gold,
            onInventory_healthPotion,
            onInventory_tnt,
            onInventory_electronic,
            onInventory_explosive
        };

        return valuesInOrder;
    }

    //quindi le funzioni sono banalmente hardcoded
    public boolean modifyValue_tnt(int tnt)
    {
        //questo if mi permette di utilizzare la stessa funzione sia in positivi che in negativo
        if(onInventory_tnt == onInventory_tnt_max || onInventory_tnt + tnt < 0)
        {
            return false;
        }

        onInventory_tnt = onInventory_tnt + tnt > onInventory_tnt_max ? onInventory_tnt_max : onInventory_tnt + tnt;
        return true;
    }

    public boolean modifyValue_healthPotion(int hltP)
    {
        if(onInventory_healthPotion == onInventory_healthPotion_max || onInventory_healthPotion + hltP > onInventory_healthPotion_max || onInventory_healthPotion + hltP < 0)
        {
            return false;
        }

        onInventory_healthPotion += hltP;
        return true;
    }

    public boolean modifyValue_gold(int gold)
    {
        //questo if mi permette di utilizzare la stessa funzione sia in positivi che in negativo
        if(onInventory_gold == onInventory_gold_max || onInventory_gold + gold > onInventory_gold_max || onInventory_gold + gold < 0)
        {
            return false;
        }

        onInventory_gold += gold;
        return true;
    }

    public void addObj_component(int electronics, int explosive)
    {
        if(onInventory_electronic == onInventory_electronic_max)
        {
            return;
        }

        if(onInventory_explosive == onInventory_explosive_max)
        {
            return;
        }

        if(onInventory_electronic + electronics > onInventory_electronic_max)
        {
            onInventory_electronic = onInventory_electronic_max;
            return;
        }

        if(onInventory_explosive + explosive > onInventory_explosive_max)
        {
            onInventory_electronic = onInventory_electronic_max;
            return;
        }

        onInventory_explosive += explosive;
        onInventory_electronic += electronics;
    }

    public void craftRemoteTnt()
    {
        //questa funzione viene richiamata ad ogni frame on update, quindi:
        if(onInventory_electronic == 0 || onInventory_explosive == 0)
        {
            return;
        }

        while(onInventory_electronic > 0 && onInventory_explosive > 0 && onInventory_tnt <= onInventory_tnt_max)
        {
            onInventory_electronic--;
            onInventory_explosive--;

            onInventory_tnt++;
        }
    }

    public void updateInventory()
    {
        craftRemoteTnt();
    }
}
