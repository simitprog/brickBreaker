import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

import org.w3c.dom.events.MouseEvent;

public class MyKeyboardAdapter implements KeyListener{

    MyPanel pannelloSuCuiLavorare;
    public MyKeyboardAdapter(MyPanel p){
        this.pannelloSuCuiLavorare = p;
    }
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }
    @Override
        public void keyPressed(KeyEvent e) {
    
        
        int step = 20; 

        if(e.getKeyChar()=='w'){
            
        }
         if(e.getKeyChar()=='a'){
        }
         if(e.getKeyChar()=='s'){
        }
         if(e.getKeyChar()=='d'){
        }


        
    }
    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }

}