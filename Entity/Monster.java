package Entity;

import Engine.GamePanel;
import Main.Main;
import Math.Vector2;
import World.Map;

public class Monster extends Entity
{
    public static int lifePoints_Range = 5;

    final int lifePoint_max = Main.rand.nextInt(lifePoints_Range) + 1;
    public Monster(GamePanel gp, Vector2 worldPos, Map map)
    {
        velocity = 4; //as fast as character
        lifePoints = lifePoint_max;
    }
}
