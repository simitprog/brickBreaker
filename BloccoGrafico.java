import java.awt.Color;
import java.awt.Graphics;

public class BloccoGrafico {
    private BloccoLogico blocco;
    private Color colore;


    private Color generaColorCasuale(){
        int r= (int)(Math.random()*256);
        int g= (int)(Math.random()*256);
        int b= (int)(Math.random()*256);
        return new Color(r,g,b);
    }


    public BloccoGrafico(BloccoLogico blocco) {
        this.blocco = blocco;
        this.colore = generaColorCasuale();
    }

    public void disegna(Graphics g)
    {
        if(this.blocco.distrutto==false){
            // Coordinate prese dalla logica
            int x = (int)blocco.posizione.getX();
            int y = (int)blocco.posizione.getY();
            int larghezza = (int)blocco.larghezza;
            int altezza = (int)blocco.altezza;

            // Disegno il rettangolo colorato
            g.setColor(colore);
            g.fillRect(x, y, larghezza, altezza);

            // Disegno un bordino nero per staccare i blocchi tra loro
            g.setColor(Color.BLACK);
            g.drawRect(x, y, larghezza, altezza);
        }
    }




}
