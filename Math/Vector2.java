package Math;
import Engine.GamePanel;
import World.Room;

public class Vector2 
{
    public int x;
    public int y; 

    public static int UP = 0;
    public static int DOWN = 1;
    public static int RIGHT = 2;
    public static int LEFT = 3;
    
    public static Vector2[] directionsVector = new Vector2[]
    {
        new Vector2(0, -1),   //up
        new Vector2(0, 1), //down
        new Vector2(1, 0), //right
        new Vector2(-1, 0), //left
    };


    public Vector2(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public static Vector2 vectorSumm(Vector2 a, Vector2 b)
    {
        return new Vector2(a.x + b.x, a.y + b.y);
    }

    public static Vector2 scalarPerVector(Vector2 vector, int scalar)
    {
        return new Vector2(vector.x * scalar, vector.y * scalar);
    }

    public static int dotProduct(Vector2 a, Vector2 b)
    {
        return (a.x * b.x) + (a.y * b.y);
    }

    public static boolean areEqual(Vector2 a, Vector2 b)
    {
        return (a.x == b.x && a.y == b.y);
    }

    public static Vector2 roomToGlobalPosition(Vector2 localPosition, Room room)
    {
        Vector2 worldPoition = new Vector2(localPosition.x + room.bounds.min.x, localPosition.y + room.bounds.min.y);
        worldPoition.x *= GamePanel.tileSize;
        worldPoition.y *= GamePanel.tileSize;

        return worldPoition;
    }
}
