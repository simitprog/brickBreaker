import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class MyPanel extends JPanel {

    public int squareX = 50;
    public int squareY = 50;
    public int squareW = 20;
    public int squareH = 20;
    public Color square_color = Color.RED;
    private List<BloccoGrafico> listaBlocchi = new ArrayList<>();
    private  int larghezzaPannello=1000;
    private int altezzaPannello=700;

    private Image sfondo;

    public MyPanel() {

        int nRighe=6;
        int nColonne=7;
        int spazioTraIBlocchi=11;
        setBorder(BorderFactory.createLineBorder(Color.black));

        double larghezzaBlocco= 130;
        double altezzaBlocco=25;
        int margineVerticaleInizialeDallaCimaDelPannello = 10;


        for (int i = 0; i < nRighe; i++) {
            for (int j = 0; j < nColonne; j++) {
                double x= spazioTraIBlocchi + j *(larghezzaBlocco +spazioTraIBlocchi);
                double y = margineVerticaleInizialeDallaCimaDelPannello + i *(altezzaBlocco +spazioTraIBlocchi);
                Punto punto = new Punto(x, y);
                BloccoLogico logico = new BloccoLogico(punto, altezzaBlocco, larghezzaBlocco);
                listaBlocchi.add(new BloccoGrafico(logico));
            }
        }
      
        sfondo= new ImageIcon("resources/secondosfondoprovvisorio.jpg").getImage();
        




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
        return new Dimension(larghezzaPannello, altezzaPannello);
    }







    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);


        if(sfondo!=null){   
            g.drawImage(sfondo,0,0,larghezzaPannello,altezzaPannello,this);
        }else{
            super.paintComponent(g);
        }




        for(BloccoGrafico b : listaBlocchi){    
            b.disegna(g);
        }
        
    }








   


    

    
    
}
