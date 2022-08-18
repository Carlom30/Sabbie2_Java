package Entity;
import java.util.List;
import DataStructures.*;
import Engine.CollisionLogic;
import Engine.GamePanel;
import Engine.Tile;
import Engine.CollisionLogic.CollisionType;
import Main.Main;
import Math.Vector2;
import Object.Projectile;
import Object.SuperObject;
import World.Room;
import Main.Utils;
import Main.Utils.Directions;
import Math.RectInt;
import java.awt.Graphics2D;

public class Monster extends Entity implements Runnable
{
    GamePanel gp;
    public Thread monsterThread = null;
    
    public static int lifePoints_Range = 5;
    public Room room;
    public Vector2 spawnPoint;

    //ai
    Player player;
    int goalSteps; 
    int numberOfStep;
    Vector2 onMoveDirection = new Vector2(0, 0);

    int a = 0;

    final int lifePoint_max = Main.rand.nextInt(lifePoints_Range) + 1;
    public Monster(GamePanel gp, Vector2 roomPosition, Room room)
    {
        this.room = room;

        velocity = 3; //as fast as character
        lifePoints = lifePoint_max;
        numberOfStep = 0;
        collisionArea = new RectInt(new Vector2(8, 16), 32, 32); 
        collisionAreaMin_Default = collisionArea.min;
        worldPosition = new Vector2(roomPosition.x + room.bounds.min.x, roomPosition.y + room.bounds.min.y);
        direction = Directions.up;
        this.worldPosition.x *= GamePanel.tileSize;
        this.worldPosition.y *= GamePanel.tileSize;

        spawnPoint = worldPosition;
        
        this.idle = Utils.loadSprite("/Sprites/monster/monster_idle.png");
        this.gp = gp;

    }

    public static Vector2 roomToGlobalPosition(Vector2 localPosition, Room room)
    {
        Vector2 worldPoition = new Vector2(localPosition.x + room.bounds.min.x, localPosition.y + room.bounds.min.y);
        worldPoition.x *= GamePanel.tileSize;
        worldPoition.y *= GamePanel.tileSize;

        return worldPoition;
    }
    
    public void ComandareUnSeguace(Player player)
    {
        Graph graph = new Graph(room);

        Node source = null; //this guy nel grafo
        Node playerNode = null; //the player nel grafo

        //per prima cosa devo trovare questi due ragazzoni nel grafo
        if(gp == null)
        {
            Utils.printf("gp is null");
            gp = Main.gp;
        }

        List<Tile> onStepTiles_player = gp.collision.checkForCollision_Tile(player, CollisionType.onStepTile);

        Tile randomTile = (Tile)onStepTiles_player.toArray()[Main.rand.nextInt(onStepTiles_player.size())];
        Vector2 onRoomPlayerPosition = randomTile.onRoomPosition;

        if(randomTile.linkedRoom == null || randomTile.linkedRoom != this.room)
        {
            Utils.printf("the player is away...");
            return;
        }

        List<Tile> onStepTiles_monster = gp.collision.checkForCollision_Tile(this, CollisionType.onStepTile);
        Tile randomTile_monster = (Tile)onStepTiles_monster.toArray()[Main.rand.nextInt(onStepTiles_monster.size())];
        Vector2 onRoomMonsterPosition = randomTile_monster.onRoomPosition;
        
        
        for(int i = 0; i < graph.V.length; i++)
        {
            //java fa tutto per riferimento quindi questi due vettori devono letteralemnte avere lo stesso puntatore
            //quindi richiedo formalmente lo smantellamento di oracle e la nuclearizzazione di java, grazie.
            if(graph.V[i].coordinates.x == onRoomPlayerPosition.x && graph.V[i].coordinates.y == onRoomPlayerPosition.y)
            {
                playerNode = graph.V[i];
                break;
            }
        }

        if(playerNode == null)
        {
            Utils.printf("player not found");
            return;
        }

        for(int i = 0; i < graph.V.length; i++)
        {
            if(graph.V[i] == null)
            {
                Utils.printf("node is null");
                return;
            }

            if(onRoomMonsterPosition == null)
            {
                Utils.printf("position not found");
                return;
            }

            if(graph.V[i].coordinates.x == onRoomMonsterPosition.x && graph.V[i].coordinates.y == onRoomMonsterPosition.y)
            {
                source = graph.V[i];
                break;
            }
        }

        if(source == null)
        {
            Utils.printf("monster not found");
            return;
        }
        
        /*adesso la parte un po tricky, devo risalire la foglia del MST fino ad arrivare 
        al nodo che ha p = alla source, ovvero la source (enemy) è il padre del nodo.
        allora, le coordinate della source diventeranno quella del nodo in questione.*/
        
        Node searchNode = playerNode;
        //if(Utils.currentTime == -1)
        //{
            //Utils.currentTime = System.currentTimeMillis();
            Graph.Dijkstra(graph, source);
    
            //cerco il figlio della source (monster)
            while(searchNode.pi != source)
            {
                if(searchNode.pi == null)
                {
                    //Utils.printf("pi is null");
                    return;
                }
    
                searchNode = searchNode.pi;
            }

            for(int i = 0; i < Utils.allDirections.length; i++)
            {
                //onMoveDirection = new Vector2(0, 0);
                Vector2 direction = Vector2.directionsVector[i];
                Directions dir = Utils.allDirections[i];
                int offset = (source.coordinates.y + direction.y) * room.bounds.width + (source.coordinates.x + direction.x);
                if(graph.unconnectedNodeMatrix[offset] != null && graph.unconnectedNodeMatrix[offset] == searchNode)
                {
                    Utils.printf("searchNode found: " + graph.unconnectedNodeMatrix[offset].coordinates.x + ", " + graph.unconnectedNodeMatrix[offset].coordinates.y);
                    onMoveDirection = Vector2.scalarPerVector(direction, 1);
                    this.direction = dir;
                }

            }
    
            /*if(!Vector2.areEqual(searchNode.coordinates, playerNode.coordinates))
            {
                this.worldPosition = roomToGlobalPosition(searchNode.coordinates, room);
                
            }*/
        //}
        //Utils.timeIsPassed(Utils.currentTime, 500);
        return;
    }

    public void moveMonster(Player player)
    {
        if(player.linkedMap == null)
        {
            Utils.printf("plyer's linked map is null");
        }
        //potrei mettere questo conto nel costruttore, ma preferisco metterlo qui per chiarezza
        goalSteps = GamePanel.tileSize / velocity;
        
        if(numberOfStep >= goalSteps /*|| Vector2.areEqual(worldPosition, spawnPoint)*/)
        {
            //values reset
            numberOfStep = 0;
            onMoveDirection = new Vector2(0, 0);

            Utils.printf("monster ended one tile movement");
            ComandareUnSeguace(player);
            onMoveDirection = Vector2.scalarPerVector(onMoveDirection, this.velocity);
        }
        
        Main.gp.collision.checkForCollision_Tile(this, CollisionType.nextTiles);
        
        if(collisionOn)
        {
            collisionOn = false;
            //return;
        }

        worldPosition = Vector2.vectorSumm(worldPosition, onMoveDirection);
        numberOfStep++;
        a++;

    }

    public void draw(Graphics2D g2D)
    {
        if(gp == null)
        {
            gp = Main.gp;
        }

        int screenX = worldPosition.x - GamePanel.player.worldPosition.x + GamePanel.player.screenPosition.x;
        int screenY = worldPosition.y - GamePanel.player.worldPosition.y + GamePanel.player.screenPosition.y;


        if( worldPosition.x + GamePanel.tileSize < GamePanel.player.worldPosition.x - GamePanel.player.screenPosition.x ||
            worldPosition.x - GamePanel.tileSize > GamePanel.player.worldPosition.x + GamePanel.player.screenPosition.x ||
            worldPosition.y + GamePanel.tileSize < GamePanel.player.worldPosition.y - GamePanel.player.screenPosition.y ||
            worldPosition.y - GamePanel.tileSize > GamePanel.player.worldPosition.y + GamePanel.player.screenPosition.y)
        {
            return;
        }

        g2D.drawImage(this.idle, screenX, screenY, GamePanel.tileSize, GamePanel.tileSize, null);
    }

    public void reset()
    {
        this.worldPosition = spawnPoint;
        //distruggo il thread perché non in uso
        monsterThread = null;
    }

    public void startMonsterThread(Player player)
    {
        Thread monstThread = new Thread(this);
        monstThread.run();
    }

    @Override
    public void run() 
    {   
        Player player = GamePanel.player;
        if(player.linkedRoom != null && player.linkedRoom == room)
            moveMonster(player); 
        
        SuperObject onCollisionObj = CollisionLogic.checkForCollision_Obj(this, false);
        if(onCollisionObj != null && onCollisionObj.name == "p")
        {
            lifePoints = 0;
            GamePanel.player.linkedRoom.onRoomMonsters.remove(this);
            GamePanel.printableObj.remove(onCollisionObj);
            GamePanel.player.shootedProjectile.remove((Projectile)onCollisionObj);
            GamePanel.player.inventory.modifyValue_gold(Main.rand.nextInt(2) == 1 ? 1 : 0);
        }

        
    }
}
