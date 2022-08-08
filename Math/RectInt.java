package Math;

import Engine.GamePanel;

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

    //https://math.stackexchange.com/questions/7356/how-to-find-rectangle-intersection-on-a-coordinate-plane
    
    public static boolean intersect(RectInt r1, RectInt r2)
    {
        int leftX = Math.max(r1.min.x, r2.min.x);
        int rightX = Math.min(r1.min.x + r1.width, r2.min.x + r2.width);
        int topY = Math.max(r1.min.y, r2.min.y);
        int bottomY = Math.min(r1.min.y + r1.height, r2.min.y + r2.height);

        if(leftX < rightX && topY < bottomY)
        {
            return true; //intersection
        }

        return false;
    }
}
