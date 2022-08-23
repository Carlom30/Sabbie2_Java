package Engine;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JPanel;
import Entity.*;
import Main.KeyHandler;
import Main.Utils;
import Main.Main.GameState;
import Math.*;
import Obj.Projectile;
import Obj.SuperObject;
import World.*;
import World.Map.MapType;
import java.awt.image.BufferedImage;


/*  
    NB: quando nelle mie funzioni scrivo "draw" si intende inserimento di dati, quando scrivo 
        "print", si intende rendering.
        java scrive "paint" quindi non ci sono troppi problemi
*/
public class GamePanel extends JPanel implements Runnable
{
    //screen settings
    public static boolean playerHasWon = false;
    //tilesize è la dimenzione finale di una tile (in pixel ovviamente)
    final public static int originalTileSize = 16; // 16 x 16 tiles
    //16x16 è comunque un po piccolo in generale per i monitor moderni quindi
    //posso scalare 
    final public static int scale = 3;
    final public static int tileSize = originalTileSize * scale; // quindi 48x48 tile
    final public static int maxScreenColumn = 16;
    final public static int maxScreenRow = 12;  // 4:3 hehe
    final public static int screenWidth = tileSize * maxScreenColumn;
    final public static int screenHeight = tileSize * maxScreenRow;

    //settings world

    public final int maxWorldColumn = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldColumn;
    public final int worldHeight = tileSize * maxWorldRow;
    public final static int maxDungeons = 3;

    public static final int maxPrintableObject = 999;

    // and so, 48 pixels * 16 = 768 pixel for width and 48 * 12 = 576 pixels for height
    int fps = 60;
    public KeyHandler kh = new KeyHandler();
    Thread gameThread;
    
    public static GameState gameState = GameState.title;

    //testing
    Dungeon dungeon;

    public Map map;
    public MapType currentMapType;
    public CollisionLogic collision = new CollisionLogic(this);
    public static Player player = new Player();
    
    public static List<SuperObject> printableObj = new ArrayList<SuperObject>();
    public static Monster[] onMapMonsters;

    public List<Dungeon> onMapDungeons = new ArrayList<Dungeon>();

    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(kh);
        this.setFocusable(true);
    
        init();
    } 

    public void init()
    {
        gameState = GameState.title;
        player = new Player();
        player.worldPosition = new Vector2((maxWorldColumn / 2), (maxWorldRow / 2));
        map = Map.generateOutsideWorld(this);
        printableObj = map.onMapObjects;

        currentMapType = MapType.outside;
        player.setDefaultValues(this, kh, map);
        map.addOutsideRooms(player, map);
    }

    public void startGameThread()
    {
        gameThread = new Thread(this); //this perché gli sto passando lo stesso gamepanel (odio la programmazione ad oggetti :D)
        gameThread.start(); //start chiama run as usually

    }
    
    @Override
    public void run()
    {
        double drawInterval = 1000000000 / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime = 0;
        long timer = 0;
        int drawCount = 0;
        
        //here goes the game loop
        while(gameThread != null)
        {
            //System.out.println(currentTime);
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1)
            {
                update();
                repaint();
                //about the upcoming line, the linux gpu scheduling is different from windows and for some
                //reason it is fucking terrible, so i this line and another thing (see fist main line)
                //to fix this thing (i won't program in windows for god sick)
                Toolkit.getDefaultToolkit().sync(); 
                delta--;
                drawCount++;
            }

            if(timer >= 1000000000)
            {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }

        }

        //the game loop works like: 
        //update: processing stuff
        //rendering: draw shit
    }

    public void paintComponent(Graphics g) //this thing is from java
    {
        Graphics2D g2 = (Graphics2D)g; //ofc graphics2d che fa override di graphics
        super.paintComponent(g); //questo va fatto ogni volta che si utilizza paintComponent
        if(gameState == GameState.title)
        {
            if(GamePanel.player.isDead)
            {
                Engine.printEndGameScreen(g2);
            }

            else if(GamePanel.playerHasWon)
            {
                Engine.printWinGameScreen(g2);
            }

            else
            {
                Engine.printTitleScreen(g2);
            }
        }

        else
        {
            if(player.linkedMap == null)
                return;
                                           //grpahics2d è ottimo per il 2d ofc, ha più funzioni inerenti
            Engine.printMap(player.linkedMap, g2);
            Engine.printMonsters(g2, player.linkedRoom == null ? null : player.linkedRoom.onRoomMonsters);
            Engine.printObjects(g2);
            Engine.printPlayer(g2, player);
            Engine.printHUD(g2, player);
        }
        
        //g2.dispose();
    }

    public void changeRenderedMap(Map map, Vector2 playerPosition)
    {
        player.linkedMap.onMapObjects = printableObj;
        player.linkedMap = map;
        printableObj = map.onMapObjects;

        player.worldPosition = playerPosition;
    }

    public void updateWorld()
    {   
        if(!player.shootedProjectile.isEmpty())
        {
            for(Projectile p : player.shootedProjectile)
            {
                if(player.shootedProjectile.size() == 6)
                {
                    int a = 0;
                }
                p.update();
                
                if(player.shootedProjectile.isEmpty())
                {
                    break;
                }
                
            }
        }
        
        if(player.linkedRoom != null && !player.linkedRoom.onRoomMonsters.isEmpty())
        {
            for(Monster m : player.linkedRoom.onRoomMonsters)
            {
                //ListIterator<Monster> mIt = player.linkedRoom.onRoomMonsters.listIterator();
                
                if(m.monsterThread == null)
                {
                    m.startMonsterThread(player);
                    if(m.getLifePoints() == 0)
                       break;
                }
                
                if(player.linkedRoom.onRoomMonsters.isEmpty())
                    break;
            }
        }
        

    }

    public void update()
    {
        player.update(player);
        updateWorld();
    }
}

//TESTING
        /*Dungeon newDungeon = new Dungeon();
        dungeon = newDungeon;
        map = newDungeon.area;
        Tile nullGrey = new Tile(Utils.loadSprite("/Sprites/nullGrey.png"));
        nullGrey.collision = true;
        map.fillMapWithOneTile(nullGrey);
        newDungeon.generateDungeonRooms();*/
        
        //player.linkedMap = dungeon.area;
        //player.linkedDungeon = dungeon;
        //player.worldPosition.x = (dungeon.firstRoom.bounds.min.x + (dungeon.firstRoom.bounds.width / 2)) * GamePanel.tileSize;
        //player.worldPosition.y = (dungeon.firstRoom.bounds.min.y + (dungeon.firstRoom.bounds.height / 2)) * GamePanel.tileSize;


        //per testing creo una chest accanto al player
        //Chest chest = new Chest(new Vector2((player.worldPosition.x / GamePanel.tileSize) + 1, player.worldPosition.y / GamePanel.tileSize));