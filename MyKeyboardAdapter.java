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
            pannelloSuCuiLavorare.moveSquare(0, -step);
        }
         if(e.getKeyChar()=='a'){
            pannelloSuCuiLavorare.moveSquare(-step, 0);
        }
         if(e.getKeyChar()=='s'){
            pannelloSuCuiLavorare.moveSquare(0, step);
        }
         if(e.getKeyChar()=='d'){
            pannelloSuCuiLavorare.moveSquare(step, 0);
        }


        
    }
    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }

}