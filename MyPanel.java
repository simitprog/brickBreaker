import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MyPanel extends JPanel {

    public int squareX = 50;
    public int squareY = 50;
    public int squareW = 20;
    public int squareH = 20;
    public Color square_color = Color.RED;


    public MyPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));

        // Aggiungo mouse listener
        MyMouseAdapter mouse = new MyMouseAdapter(this);
        addMouseListener(mouse);

        // Aggiungo key listener
        MyKeyboardAdapter keyboard = new MyKeyboardAdapter(this);
        setFocusable(true); // permette al pannello di ricevere eventi da tastiera
        requestFocusInWindow();
        addKeyListener(keyboard);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(250, 200);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawString("This is my custom Panel!", 10, 20);

        g.setColor(square_color);
        g.fillRect(squareX, squareY, squareW, squareH);
        
        g.setColor(Color.BLACK);
        g.drawRect(squareX, squareY, squareW, squareH);

        
    }

    public void moveSquare(int x, int y) {
        squareX += x;
        squareY += y;

        if (squareX < 0) squareX = 0;
        if (squareY < 0) squareY = 0;
        if (squareX + squareW > getWidth()) squareX = getWidth() - squareW;
        if (squareY + squareH > getHeight()) squareY = getHeight() - squareH;

        repaint();
    }


   


    

    
    
}
