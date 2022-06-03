package Main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import Entity.*;
import Math.*;
//import Math.*;
import World.*;
import javax.imageio.ImageIO;

/*  
    NB: quando nelle mie funzioni scrivo "draw" si intende inserimento di dati, quando scrivo 
        print, si intende rendering.
        java scrive "paint" quindi non ci sono troppi problemi
*/
public class GamePanel extends JPanel implements Runnable
{
    //screen settings

    //tilesize è la dimenzione finale di una tile (in pixel ovviamente)
    final public int originalTileSize = 16; // 16 x 16 tiles

    //16x16 è comunque un po piccolo in generale per i monitor moderni quindi
    //posso scalare 
    final public int scale = 3;
    final public int tileSize = originalTileSize * scale; // quindi 48x48 tile
    final public int maxScreenColumn = 16;
    final public int maxScreenRow = 12;  // 4:3 hehe
    final public int screenWidth = tileSize * maxScreenColumn;
    final public int screenHeight = tileSize * maxScreenRow;
    
    // and so, 48 pixels * 16 = 768 pixel for width and 48 * 12 = 576 pixels for height
    int fps = 60;
    KeyHandler kh = new KeyHandler();
    Thread gameThread;
    
    //-------- TESTING ---------------
    Map map;

    //--------------------------------    
    Player player;

    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(kh);
        this.setFocusable(true);

        //TESTING
        map = new Map(this, 10, 10);
        Utils u = new Utils();
        map.fillMapWithOneTile(new Tile(u.loadSprite("/Sprites/world/sand2.png")));
        player = new Player(this, kh);
        //END OF TESTING
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

        //so the game loop works like: 
        //update: processing stuff
        //rendering: draw shit
    }

    public void paintComponent(Graphics g) //this thing is from java
    {
        super.paintComponent(g); //questo va fatto ogni volta che si utilizza paintComponent
        Graphics2D g2 = (Graphics2D)g; //ofc graphics2d che fa override di graphics
                                       //grpahics2d è ottimo per il 2d ofc, ha più funzioni inerenti
        map.printMap(g2);
        player.print(g2);
        
        //g2.dispose();
    }

    public void update()
    {
        player.update();
    }
}
