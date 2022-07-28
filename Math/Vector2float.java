package Math;

public class Vector2float 
{
    public float x;
    public float y;
    
    public Vector2float(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public static float dotProduct(Vector2 a, Vector2 b)
    {
        return (a.x * b.x) + (a.y * b.y);
    }
}
