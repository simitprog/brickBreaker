import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * @class GiocatoreGrafico
 * @brief Gestisce la parte grafica della racchetta utilizzando un'immagine
 */
public class GiocatoreGrafico {
    private giocatoreLogico giocatore;
    private Image immaginePaddle;

    /**
     * @brief Costruttore
     * @param giocatore giocatoreLogico associato
     */
    public GiocatoreGrafico(giocatoreLogico giocatore) {
        this.giocatore = giocatore;
        // Caricamento dell'immagine dal percorso specificato
        this.immaginePaddle = new ImageIcon("resources/paddle.png").getImage();
    }

    /**
     * @brief Secondo costruttore (mantenuto per compatibilità con MyPanel)
     * @param giocatore giocatoreLogico associato
     * @param coloreParametro (non più utilizzato, sostituito dall'immagine)
     */
    public GiocatoreGrafico(giocatoreLogico giocatore, java.awt.Color coloreParametro) {
        this.giocatore = giocatore;
        this.immaginePaddle = new ImageIcon("resources/paddle.png").getImage();
    }

    /**
     * @brief disegna il giocatore usando l'immagine caricata
     * @param g oggetto su cui disegnare
     */
    public void disegna(Graphics g) {
        int x = (int) giocatore.getPosizione().getX();
        int y = (int) giocatore.getPosizione().getY();
        int larghezza = (int) giocatore.getLarghezza();
        int altezza = (int) giocatore.getAltezza();

        if (immaginePaddle != null) {
            // Disegna l'immagine adattandola alle dimensioni logiche (importante per il bonus Barralunga)
            g.drawImage(immaginePaddle, x, y, larghezza, altezza, null);
        } else {
            // Backup grafico nel caso l'immagine non venga trovata
            g.setColor(java.awt.Color.GRAY);
            g.fillRect(x, y, larghezza, altezza);
        }
    }

    // Getters e Setters
    public giocatoreLogico getGiocatoreLogico() {
        return giocatore;
    }

    public void setGiocatore(giocatoreLogico giocatore) {
        this.giocatore = giocatore;
    }

    public Image getImmaginePaddle() {
        return immaginePaddle;
    }

    public void setImmaginePaddle(Image immaginePaddle) {
        this.immaginePaddle = immaginePaddle;
    }
}