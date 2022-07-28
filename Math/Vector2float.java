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

    public static float dotProduct(Vector2float a, Vector2float b)
    {
        return (a.x * b.x) + (a.y * b.y);
    }
}
