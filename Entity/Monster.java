package Entity;
import java.util.List;
import DataStructures.*;
import Engine.GamePanel;
import Engine.Tile;
import Engine.CollisionLogic.CollisionType;
import Main.Main;
import Math.Vector2;
import World.Room;
import Main.Utils;
import Math.RectInt;
import java.awt.Graphics2D;

public class Monster extends Entity implements Runnable
{
    GamePanel gp;
    Thread monsterThread;
    
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

        velocity = 2; //as fast as character
        lifePoints = lifePoint_max;
        numberOfStep = 0;
        collisionArea = new RectInt(new Vector2(8, 16), 32, 32); 
        collisionAreaMin_Default = collisionArea.min;
        worldPosition = new Vector2(roomPosition.x + room.bounds.min.x, roomPosition.y + room.bounds.min.y);
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
        
        //per ora faccio che il mostro si teletrasporta da tile a tile una volta al secondo
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
                int offset = (source.coordinates.y + direction.y) * room.bounds.width + (source.coordinates.x + direction.x);
                if(graph.unconnectedNodeMatrix[offset] != null && graph.unconnectedNodeMatrix[offset] == searchNode)
                {
                    Utils.printf("searchNode found: " + graph.unconnectedNodeMatrix[offset].coordinates.x + ", " + graph.unconnectedNodeMatrix[offset].coordinates.y);
                    onMoveDirection = Vector2.scalarPerVector(direction, 1);
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

        this.worldPosition.x += onMoveDirection.x;
        this.worldPosition.y += onMoveDirection.y;
        numberOfStep++;
        a++;

    }

    public void draw(Graphics2D g2D)
    {
        if(gp == null)
        {
            gp = Main.gp;
        }

        int screenX = worldPosition.x - gp.player.worldPosition.x + gp.player.screenPosition.x;
        int screenY = worldPosition.y - gp.player.worldPosition.y + gp.player.screenPosition.y;


        if( worldPosition.x + GamePanel.tileSize < gp.player.worldPosition.x - gp.player.screenPosition.x ||
            worldPosition.x - GamePanel.tileSize > gp.player.worldPosition.x + gp.player.screenPosition.x ||
            worldPosition.y + GamePanel.tileSize < gp.player.worldPosition.y - gp.player.screenPosition.y ||
            worldPosition.y - GamePanel.tileSize > gp.player.worldPosition.y + gp.player.screenPosition.y)
        {
            return;
        }

        g2D.drawImage(this.idle, screenX, screenY, GamePanel.tileSize, GamePanel.tileSize, null);
    }

    public void startMonsterThread(Player player)
    {
        Thread monstThread = new Thread(this);
        monstThread.run();
    }

    @Override
    public void run() 
    {   
        if(player.linkedRoom != this.room)
            return;

        moveMonster(player);    
    }
}
