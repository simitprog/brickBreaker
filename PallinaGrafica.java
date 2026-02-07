import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import javax.swing.ImageIcon;

/**
 * @class PallinaGrafica
 * @brief Gestisce la parte grafica della pallina utilizzando un'immagine
 */
public class PallinaGrafica {
    private PallinaLogica pallina;
    private Image immaginePalla;

    /**
     * @brief Costruttore
     * @param pallina PallinaLogica associata
     */
    public PallinaGrafica(PallinaLogica pallina) {
        this.pallina = pallina;
        this.immaginePalla = new ImageIcon("resources/palla.png").getImage();
    }

    /**
     * @brief costruttore per MyPanel
     * @param pallina pallinaLogica associata
     * @param coloreParametro (non più utilizzato per il disegno principale)
     */
    public PallinaGrafica(PallinaLogica pallina, Color coloreParametro) {
        this.pallina = pallina;
        this.immaginePalla = new ImageIcon("resources/palla.png").getImage();
    }

    /**
     * @brief disegna la pallina usando l'immagine
     * @param g oggetto Graphics su cui disegnare
     */
    public void disegna(Graphics g) {
        int x = (int) pallina.getPosizione().getX();
        int y = (int) pallina.getPosizione().getY();
        int diametro = (int)pallina.getRaggio() * 2;

        if (immaginePalla != null) {
            //disegno l'immagine della pallina
            g.drawImage(immaginePalla, x, y, diametro, diametro, null);
        } else {
            //nel caso l'immagine non ci sia la disegno cosi'
            g.setColor(Color.RED);
            g.fillOval(x, y, diametro, diametro);
        }
    }

    //getters e Setters
    public PallinaLogica getPallinaLogica() {
        return pallina;
    }

    public void setPallinaLogica(PallinaLogica pallina) {
        this.pallina = pallina;
    }

    public Image getImmaginePalla() {
        return immaginePalla;
    }

    public void setImmaginePalla(Image immaginePalla) {
        this.immaginePalla = immaginePalla;
    }
}