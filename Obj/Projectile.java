package Obj;

import Entity.Player;
import Main.Utils;
import Main.Utils.Directions;
import Math.RectInt;
import Math.Vector2;
import java.awt.image.BufferedImage;
import Engine.GamePanel;

public class Projectile extends SuperObject
{
    //player cant interact with this ofc
    public int velocity;
    public long lifeTimeInMilliseconds = 2000;
    long currentTime = -1;

    public BufferedImage p_up;
    public BufferedImage p_down;
    public BufferedImage p_right;
    public BufferedImage p_left;

    public BufferedImage[] p_sprites;

    public Vector2 direction;

    Player player;
    
    public Projectile(Player player, Vector2 startPosition)
    {
        velocity = 6;
        worldPos = startPosition;
        sprite = null;
        p_up = Utils.loadSprite("/Sprites/character/projectile/p_up.png");
        p_down = Utils.loadSprite("/Sprites/character/projectile/p_down.png");
        p_right = Utils.loadSprite("/Sprites/character/projectile/p_right.png");
        p_left = Utils.loadSprite("/Sprites/character/projectile/p_left.png");

        name = "p";
        collisionArea = new RectInt(new Vector2(8, 8), 32, 32);

        p_sprites = new BufferedImage[]
        {
            p_up,
            p_down,
            p_right,
            p_left
        }; //rispetta l'ordine in convenzione nel progetto "up, down, right, left"
        
    }

    public static void shoot(Player player, Directions shootingDirection)
    {
        Vector2 projPosition = new Vector2(0, 0);
        for(int i = 0; i < Utils.allDirections.length; i++)
        {
            if(Utils.allDirections[i] == shootingDirection)
            {
                //player.direction = shootingDirection;
                
                projPosition = (Vector2.vectorSumm(player.worldPosition, Vector2.scalarPerVector((Vector2.directionsVector[i]), (GamePanel.tileSize / 2))));  
                Projectile p = new Projectile(player, projPosition);
                p.sprite = p.p_sprites[i];
                p.direction = Vector2.directionsVector[i];
                p.direction = Vector2.scalarPerVector(p.direction, p.velocity);
                
                player.shootedProjectile.add(p);
                GamePanel.printableObj.add(p);
                //List<Tile> tiles = Main.gp.collision.checkForCollision_Tile(player, CollisionType.nextTiles);

                break;
            }
        }
    }

    public boolean timeIsPassed(long lastTime, long timeToWait) //time to wait must be in millisec
    {
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - lastTime;
        if(delta >= timeToWait)
        {
            currentTime = -1;
            return true;
        }

        return false;
    }

    public void update()
    {
        this.worldPos = Vector2.vectorSumm(this.worldPos, direction);
        //Main.gp.collision.shoot_CollisionEnter(this);
        if(currentTime == -1)
        {
            currentTime = System.currentTimeMillis();
        }

        if(timeIsPassed(currentTime, lifeTimeInMilliseconds))
        {
            GamePanel.player.shootedProjectile.remove(this);
            GamePanel.printableObj.remove(this);
        }
    }
    
}
