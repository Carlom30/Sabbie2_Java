package Obj;

import java.util.Vector;

import Main.Main;
import Main.Utils;
import Math.Vector2;
import World.Dungeon;
import World.Map;
import World.Map.MapType;

import java.awt.image.BufferedImage;
import Engine.GamePanel;
import Entity.Player;

public class Ladder extends SuperObject 
{
    public enum LadderType
    {
        goesUp,
        goesDown
    }

    //union, uno dei due è sempre null
    LadderType ladderType;
    Dungeon linkedDungeon;
    Map linkedMap;
    Ladder linkedLadder;


    //da finire
    public Ladder(Vector2 worldPosition, LadderType lType, Map linkedMap, Dungeon linkedDungeon)
    {   
        BufferedImage spriteGoesDown = Utils.loadSprite("/Sprites/world/ladder/ladder_goup.png");
        BufferedImage spriteGoesUp = Utils.loadSprite("/Sprites/world/ladder/ladder_goup.png");
        collision = false;
        this.worldPos = new Vector2(worldPosition.x, worldPosition.y);
        ladderType = lType;
        //type è solo il "tipo" del super oggetto.
        type = objecType.Ladder;
        this.linkedDungeon = linkedDungeon;

        //se linkedDungeon arriva null, allora la ladder è interna è serve solo a riportare il giocatore fuori
        this.linkedMap = linkedDungeon == null ? Main.gp.map : linkedDungeon.area;
        sprite = ladderType == ladderType.goesUp ? spriteGoesUp : spriteGoesDown;
        
        if(lType == LadderType.goesDown) 
            GamePanel.printableObj.add(this);

        else
            linkedDungeon.area.onMapObjects.add(this);
        //NB la connessioni con eventuali mappe o dungeon va fatta da remoto
    }

    public void climbLadder(Player player)
    {
        //questa funzione cambia la mappa principale del gamepanel e sposta il player
        Map toRenderMap = null;
        Vector2 newPlayerPosition = new Vector2(0, 0);

        if(ladderType == LadderType.goesDown)
        {
            toRenderMap = this.linkedDungeon.area;
            Main.gp.currentMapType = MapType.dungeon;
            player.linkedDungeon = this.linkedDungeon;
            newPlayerPosition.x = (toRenderMap.width / 2) * Main.gp.tileSize;
            newPlayerPosition.y = (toRenderMap.height / 2) * Main.gp.tileSize;
        }
        
        else if(ladderType == LadderType.goesUp)
        {
            toRenderMap = Main.gp.map;
            Main.gp.currentMapType = MapType.outside;
            newPlayerPosition = player.lastKnownOutsidePosition;
            player.linkedDungeon = null;
            player.linkedRoom = null;
        }
        
        Main.gp.changeRenderedMap(toRenderMap, newPlayerPosition);
    
    }

    @Override
    public boolean interact(Player player) 
    {
        return super.interact(player);
    }
}
