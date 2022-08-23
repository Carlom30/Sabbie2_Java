package Main;
import javax.swing.JFrame;
import java.util.Random;

import Engine.GamePanel;

public class Main 
{
    public enum GameState
    {
        inGame,
        title
    }
    
    public static GamePanel gp;
    public static Random rand = new Random();
    public static JFrame window;
    public static void main(String[] args) 
    {   
        startNewGame();
    }

    public static void startNewGame()
    {
        if(window != null)
        {
            window = null;
            gp = null;
        }
        rand = new Random();
        long seed = rand.nextLong();
        Utils.printf("seed: " + seed);
        rand.setSeed(seed);

        //fix stuttering and lagging problems with linux gpu scheduling
        System.setProperty("sun.java2d.opengl", "true");
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // il programma si conclude quando l'utente chiude la finestra con la x
        window.setResizable(false);
        window.setTitle("Sands");

        //testing perlin noise

        gp = new GamePanel();
        window.add(gp); //aggiungo gamePanel come componente di window, mi servirà dopo

        window.pack();
        
        window.setLocationRelativeTo(null); //la finestra viene creata la centro dello schermo
        window.setVisible(true);
        //gp.init();
        gp.startGameThread();
    }
}
