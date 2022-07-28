package Math;
import java.lang.Math;

import World.Map;

public class PerlinNoise 
{
    /*source: http://eastfarthing.com/blog/2015-04-21-noise/ */
    //implementazione PerlinNoise per generazione procedurale della mappa
    
    //partendo con la funzione di interpolazione, si userà una funzione derivante dall'originale
    //f(t) = 1 − (3 − 2|t|)t^2

    public static void main(String[] args) 
    {
        int a = (int)Math.round(-0.9f);
        int b = (int)Math.round(0.4f);
        int c = (int)Math.round(0.9);
        System.out.println("a:" + a + " b:" + b + " c:" + c + "\n");    
    }

    private static float interpolate(float v)
    {
        float value = (float)(1.0 - (3.0 - (2.0 * Math.abs((float)v))) * ((float)v * (float)v));
        return value;
    }

    public static void generateNoise(Map map)
    {
        //questa funzione modifica i parametri height delle tile della mappa passata come parametro
        //in questo gioco utilizzo un semplice bool per definire 
        //per prima cosa scorro la matrice Tile della mappa, e per ogni puntatore ad una tile, ricalcolo la sua posizione 
        //*in modo che si trovi sempre compresa tra 0 e 1, e che abbia come origine degli assi il centro della mappa, invece che in alto a sinistra
        
        //per testing faccio solo mezza mappa
        int yValue = map.width / 2;
        int xValue = map.height / 2;
        for(int i = 0; i < yValue; i++)
        {
            float percentageX = (((float)(i) / (float)(yValue - 1)) - 0.5f) * 2.0f; //*guarda riga asteriscata commenti sopra
        }
    }
}
