package Math;

public class RectInt 
{
    public Vector2 min;
    public int width;
    public int height;   
    
    public RectInt(Vector2 min, int width, int height)
    {
        this.min = min;
        this.width = width;
        this.height = height;
    }

    //https://stackoverflow.com/questions/2752349/fast-rectangle-to-rectangle-intersection
    
    public static boolean intersect(RectInt r1, RectInt r2)
    {
        int r1Right = r1.min.x + r1.width;
        int r2Right = r2.min.x + r2.width;

        int r1Bottom = r1.min.y + r1.height;
        int r2Bottom = r2.min.y + r2.height;

        return !(r2.min.y > r1Right ||
                 r2Right < r1.min.y ||
                 r2.min.x > r1Bottom ||
                 r2Bottom < r1.min.y);
    }
}
