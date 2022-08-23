package Math;
import java.lang.Math;

import Main.Main;

public class PerlinNoise 
{

    public static final int sizeofInt = 4;

    public static float interpolate(float a0, float a1, float w)
    {
        //return (a1 - a0) * w + a0;
        return (a1 - a0) * ((w * (w * 6.0f - 15.0f) + 10.0f) * w * w * w) + a0;
    }

    public static Vector2float randomGradient(int ix, int iy) 
    {
        // No precomputed gradients mean this works for any number of grid coordinates
        Vector2float v = new Vector2float(0, 0);
        
        float val = (Main.rand.nextFloat() * 2.0f) - 1.0f;

        v.x = (float)(Math.cos((double)val)); 
        v.y = (float)(Math.sin((double)val));
        return v;
    } 
    
    public static float dotGridGradient(int ix, int iy, float x, float y) {
        // Get gradient from integer coordinates
        Vector2float gradient = randomGradient(ix, iy);
    
        // Compute the distance vector
        float dx = x - (float)ix;
        float dy = y - (float)iy;
    
        // Compute the dot-product
        return (dx * gradient.x + dy * gradient.y);
    }
    
    public static float perlin(float x, float y) 
    {
        // Determine grid cell coordinates
        int x0 = (int)x; //floor
        int x1 = x0 + 1;
        int y0 = (int)y; //floor
        int y1 = y0 + 1;
    
        // Determine interpolation weights
        // Could also use higher order polynomial/s-curve here
        float sx = x - (float)x0;
        float sy = y - (float)y0;
    
        // Interpolate between grid point gradients
        float n0, n1, ix0, ix1, value;
    
        n0 = dotGridGradient(x0, y0, x, y);
        n1 = dotGridGradient(x1, y0, x, y);
        ix0 = interpolate(n0, n1, sx);
    
        n0 = dotGridGradient(x0, y1, x, y);
        n1 = dotGridGradient(x1, y1, x, y);
        ix1 = interpolate(n0, n1, sx);
    
        value = interpolate(ix0, ix1, sy);
        return value; // Will return in range -1 to 1. To make it in range 0 to 1, multiply by 0.5 and add 0.5
    }
}
