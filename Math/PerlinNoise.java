package Math;
import java.lang.Math;
import java.awt.image.BufferedImage;

import Main.Main;
import Main.Utils;
import javax.swing.JFrame;

import Engine.GamePanel;
import Engine.Tile;
import World.Map;

public class PerlinNoise 
{
    private static BufferedImage positive = null; 
    private static BufferedImage zero = null;
    private static BufferedImage negative = null;
    /*source: http://eastfarthing.com/blog/2015-04-21-noise/ */
    //implementazione PerlinNoise per generazione procedurale della mappa
    
    //partendo con la funzione di interpolazione, si userà una funzione derivante dall'originale
    //f(t) = 1 − (3 − 2|t|)t^2

    private static void init(Map map)
    {
        return;
    }

    private static float interpolate(float t)
    {
        float a = Math.abs(t);
        float value = (float)(1.0 - (3.0 - 2.0 * a) * (t * t));
        return value;
    }

    private static void assigneSprite(float value, Map map, int offset)
    {
        Tile tile = new Tile(null);

        /*if(roundedValue == -1)
        {
            tile.sprite = negative;
        }*/

        if(value <= 0.01f)
        {
            tile.sprite = zero;
        }

        else if(value > 0.01f && value < 1.0f)
        {
            tile.sprite = positive;
        }

        else if(value == 1)
        {
            tile.sprite = positive;
        }

        else
        {
            System.out.println("something went wrong\n");
            return;
        }
        
        map.tiles[offset] = tile;
    }

    public static void noise(Map map)
    {
        if(positive == null) //solo se non le ho ancora caricate
        {
            positive = Utils.loadSprite("/Sprites/gradient/positive.png");
            zero = Utils.loadSprite("/Sprites/gradient/zero.png");
            negative = Utils.loadSprite("/Sprites/gradient/negative.png");
        }
        //----------------TESTING-----------------------

        //----------------------------------------------

        //questa funzione modifica i parametri height delle tile della mappa passata come parametro
        //in questo gioco utilizzo un semplice bool per definire 
        //per prima cosa scorro la matrice Tile della mappa, e per ogni puntatore ad una tile, ricalcolo la sua posizione 
        //*in modo che si trovi sempre compresa tra 0 e 1, e che abbia come origine degli assi il centro della mappa (o del chunk), invece che in alto a sinistra
        
        //per testing faccio solo mezza mappa
        int yValue = 9;
        int xValue = 9;
        for(int i = 0; i < yValue; i++)
        {
            float percentageX = (((float)(i) / (float)(yValue - 1)) - 0.5f) * 2.0f; //*guarda riga asteriscata commenti sopra
            float valueX = interpolate(percentageX);
            for(int j = 0; j < xValue; j++)
            {
                float percentageY = (((float)(j) / (float)(xValue - 1)) - 0.5f) * 2.0f;
                float valueY = interpolate(percentageY);
                
                Vector2float unitTileVector = new Vector2float(percentageX, percentageY);
                Vector2float arbitraryVector = new Vector2float(0.0f, 1.0f);

                float gradient = Vector2float.dotProduct(arbitraryVector, unitTileVector);
                //int roundedValue = Math.round(valueX * valueY * gradient);
                float finalValue = valueX * valueY * gradient;
                
                assigneSprite(Math.abs(finalValue), map, i * map.width + j);

            }
        }
    }
}
