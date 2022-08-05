package Main;
import javax.swing.JFrame;
import java.util.Random;

import Engine.GamePanel;
import Object.remoteTnt;
import World.Dungeon;

public class Main {
    
    public static GamePanel gp;
    public static Random rand = new Random();
    public static void main(String[] args) 
    {
        rand = new Random();
        long seed = rand.nextLong();
        Utils.printf("seed: " + seed);
        rand.setSeed(seed);

        Dungeon.allocateDirections();

        //fix stuttering and lagging problems with linux gpu scheduling
        System.setProperty("sun.java2d.opengl", "true");
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // il programma si conclude quando l'utente chiude la finestra con la x
        window.setResizable(false);
        window.setTitle("Sands");

        gp = new GamePanel();
        window.add(gp); //aggiungo gamePanel come componente di window, mi servirà dopo

        window.pack();
        
        window.setLocationRelativeTo(null); //la finestra viene creata la centro dello schermo
        window.setVisible(true);
        //gp.init();
        gp.startGameThread();
    }
}
