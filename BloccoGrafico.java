import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class BloccoGrafico {
    private BloccoLogico blocco;
    private Image immagineBlocco;
    
    //percorsi delle immagini
    private static final String[] PATH_IMMAGINI = {
        "resources/block_blue.png",
        "resources/block_green.png",
        "resources/block_grey.png",
        "resources/block_lightblue.png",
        "resources/block_pink.png",
        "resources/block_purple.png",
        "resources/block_red.png"
    };

    public BloccoGrafico(BloccoLogico blocco) {
        this.blocco = blocco;
        this.immagineBlocco = caricaImmagineCasuale();
    }

    private Image caricaImmagineCasuale() {
        //numero casuale per i blocchi
        int indice = (int) (Math.random() * PATH_IMMAGINI.length);
        //carico l'immagine corrispondente
        return new ImageIcon(PATH_IMMAGINI[indice]).getImage();
    }

    public void disegna(Graphics g) {
        if (!this.blocco.distrutto) {
            int x = (int) blocco.posizione.getX();
            int y = (int) blocco.posizione.getY();
            int larghezza = (int) blocco.larghezza;
            int altezza = (int) blocco.altezza;

            if (immagineBlocco != null) {
                //disegno l'iimagine
                g.drawImage(immagineBlocco, x, y, larghezza, altezza, null);
            } else {
                //se l'immagine non c'e'
                g.setColor(java.awt.Color.GRAY);
                g.fillRect(x, y, larghezza, altezza);
            }
        }
    }

    public BloccoLogico getLogico() {
        return this.blocco;
    }
}