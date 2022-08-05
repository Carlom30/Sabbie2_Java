package Main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener
{

    public boolean upPressed;
    public boolean downPressed;
    public boolean rightPressed;
    public boolean leftPressed;
    public boolean nothingIsPressed;
    
    public boolean E_Pressed;
    

    public boolean keyIsPressed = false;
    
    @Override
    public void keyPressed(KeyEvent e) 
    {
        int code = e.getKeyCode(); //e is the keyevent

        if(code == KeyEvent.VK_W)
        {
            upPressed = true;
        }

        else if(code == KeyEvent.VK_S)
        {
            downPressed = true;
        }

        else if(code == KeyEvent.VK_A)
        {
            leftPressed = true;
        }

        else if(code == KeyEvent.VK_D)
        {
            rightPressed = true;
        }

        else if(code == KeyEvent.VK_E)
        {
            E_Pressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        int code = e.getKeyCode(); //e is the keyevent

        if(code == KeyEvent.VK_W)
        {
            upPressed = false;
        }

        if(code == KeyEvent.VK_S)
        {
            downPressed = false;
        }

        if(code == KeyEvent.VK_A)
        {
            leftPressed = false;
        }

        if(code == KeyEvent.VK_D)
        {
            rightPressed = false;
        }
        
        else if(code == KeyEvent.VK_E)
        {
            E_Pressed = false;
        }
        
    }

    @Override
    public void keyTyped(KeyEvent e) 
    {
        //useless for me
        
    }
    
}
