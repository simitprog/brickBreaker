import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

import org.w3c.dom.events.MouseEvent;

public class MyMouseAdapter implements MouseListener{

    MyPanel pannelloSuCuiLavorare;
    public MyMouseAdapter(MyPanel p){
        this.pannelloSuCuiLavorare = p;
    }

    @Override   
    public void mousePressed(java.awt.event.MouseEvent e) {
       if (e.getX() >= pannelloSuCuiLavorare.squareX &&
            e.getX() <= pannelloSuCuiLavorare.squareX + pannelloSuCuiLavorare.squareW &&
            e.getY() >= pannelloSuCuiLavorare.squareY &&
            e.getY() <= pannelloSuCuiLavorare.squareY + pannelloSuCuiLavorare.squareH)
      {
        int r,g,b;
        r=(int)(Math.random()*256);
        g=(int)(Math.random()*256);
        b=(int)(Math.random()*256);

        Color colore = new Color(r,g,b);
    
        pannelloSuCuiLavorare.square_color= colore ;
        pannelloSuCuiLavorare.repaint();
       }
    }
    


    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'mouseClicked'");
    }


    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
    }


    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'mouseEntered'");
    }


    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'mouseExited'");
    }


}
