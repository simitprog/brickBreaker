import java.awt.Color;
import java.awt.Graphics;

/**
 * @class PallinaGrafica
 * @brief Gestisce la parte grafica della pallina utilizzando la PallinaLogica
 */
public class PallinaGrafica {
    private PallinaLogica pallina;
    private Color colore;

    /**
     * @brief costruttore
     * @param palla pallinaLogica associata
     * @param colore colore della pallina
     */
    public PallinaGrafica(PallinaLogica palla, Color colore){
        this.pallina=palla;
        this.colore=colore;
    }

    /**
     * @brief disegna la pallina
     * @param g oggetto su cui disegnare
     */
    public void disegna(Graphics g){
        int x=(int)pallina.getPosizione().getX();
        int y=(int)pallina.getPosizione().getY();
        int diametro=(int)pallina.getRaggio()*2;

        g.setColor(colore);
        g.fillOval(x, y, diametro, diametro);

        g.setColor(Color.BLACK);
        g.drawOval(x,y,diametro,diametro);
    }

    public void setColore(Color colore){
        this.colore=colore;
    }

}
