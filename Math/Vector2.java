package Math;

public class Vector2 
{
    public int x;
    public int y; 
    
    public Vector2(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public static int dotProduct(Vector2 a, Vector2 b)
    {
        return (a.x * b.x) + (a.y * b.y);
    }

}
