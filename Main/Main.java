package Main;
import javax.swing.JFrame;

import Engine.GamePanel;

public class Main {
    
    public static GamePanel gp;
    public static void main(String[] args) 
    {
        //fix stuttering and lagging problems with linux gpu scheduling
        System.setProperty("sun.java2d.opengl", "true");
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // il programma si conclude quando l'utente chiude la finestra con la x
        window.setResizable(false);
        window.setTitle("Sabbie 2");

        gp = new GamePanel();
        window.add(gp); //aggiungo gamePanel come componente di window, mi servirà dopo

        window.pack();
        
        window.setLocationRelativeTo(null); //la finestra viene creata la centro dello schermo
        window.setVisible(true);

        gp.startGameThread();
    }
}
