import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class MyPanel extends JPanel {
    private  int larghezzaPannello=1000;
    private int altezzaPannello=700;

    private int larghezzaPiattaforma=140;
    private int altezzaPiattaforma=20;
    
    public giocatoreLogico gl= new giocatoreLogico((larghezzaPannello-larghezzaPiattaforma)/2, 50, larghezzaPiattaforma, altezzaPiattaforma,larghezzaPannello );
    public GiocatoreGrafico piattaforma = new GiocatoreGrafico(gl, Color.BLACK);
    private List<BloccoGrafico> listaBlocchi = new ArrayList<>();
   

    private Image sfondo;

    public MyPanel() {

        int nRighe=6;
        int nColonne=7;
    
        setBorder(BorderFactory.createLineBorder(Color.black));

        double larghezzaBlocco= 142.85;
        double altezzaBlocco=25;
        


        for (int i = 0; i < nRighe; i++) {
            for (int j = 0; j < nColonne; j++) {
                double x=  j *larghezzaBlocco ;
                double y =  i *altezzaBlocco ;
                Punto punto = new Punto(x, y);
                BloccoLogico logico = new BloccoLogico(punto, altezzaBlocco, larghezzaBlocco);
                listaBlocchi.add(new BloccoGrafico(logico));
            }
        }
      
        sfondo= new ImageIcon("resources/Possibilesfondo.jpg").getImage();
        
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

        piattaforma.disegna(g);


        for(BloccoGrafico b : listaBlocchi){    
            b.disegna(g);
        }
        
    }








   


    

    
    
}
