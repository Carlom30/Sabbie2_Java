package Object;

import java.util.Vector;

import Math.Vector2;
import World.Dungeon;
import World.Map;

public class Ladder extends SuperObject 
{
    public enum LadderType
    {
        goesUp,
        goesDown
    }
    //union, uno dei due è sempre null
    LadderType ladderType;
    
    Dungeon linkeDungeon;
    Map linkedMap;

    Ladder linkedLadder;

    //da finire
    public Ladder(Vector2 worldPosition, LadderType lType)
    {
        this.worldPos = worldPosition;
        ladderType = lType;
        type = objecType.Ladder;

        //NB la connessioni con eventuali mappe o dungeon va fatta da remoto
    }
}
