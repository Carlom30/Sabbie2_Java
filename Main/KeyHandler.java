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
    
    public boolean Q_Pressed;
    public boolean R_Pressed;
    public boolean C_Pressed;
    public boolean E_Pressed;
    public boolean H_Pressed;

    public boolean shootUp;
    public boolean shootDown;
    public boolean shootRight;
    public boolean shootLeft;
    

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

        else if(code == KeyEvent.VK_Q)
        {
            Q_Pressed = true;
        }

        else if(code == KeyEvent.VK_R)
        {
            R_Pressed = true;
        }
        
        else if(code == KeyEvent.VK_C)
        {
            C_Pressed = true;
        }

        else if(code == KeyEvent.VK_E)
        {
            E_Pressed = true;
        }

        else if(code == KeyEvent.VK_H)
        {
            H_Pressed = true;
        }

        else if(code == KeyEvent.VK_UP)
        {
            shootUp = true;
        }

        else if(code == KeyEvent.VK_DOWN)
        {
            shootDown = true;
        }

        else if(code == KeyEvent.VK_RIGHT)
        {
            shootRight = true;
        }

        else if(code == KeyEvent.VK_LEFT)
        {
            shootLeft = true;
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) 
    {
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
        
        else if(code == KeyEvent.VK_Q)
        {
            Q_Pressed = false;
        }

        else if(code == KeyEvent.VK_R)
        {
            R_Pressed = false;
        }

        else if(code == KeyEvent.VK_C)
        {
            C_Pressed = false;
        }
        
        else if(code == KeyEvent.VK_E)
        {
            E_Pressed = false;
        }

        else if(code == KeyEvent.VK_UP)
        {
            shootUp = false;
        }

        else if(code == KeyEvent.VK_DOWN)
        {
            shootDown = false;
        }

        else if(code == KeyEvent.VK_RIGHT)
        {
            shootRight = false;
        }

        else if(code == KeyEvent.VK_LEFT)
        {
            shootLeft = false;
        }

        else if(code == KeyEvent.VK_H)
        {
            H_Pressed = false;
        }
        
    }

    @Override
    public void keyTyped(KeyEvent e) 
    {
        //useless for me
        
    }

    public static void interact()
    {
        return;
    }
    
}
