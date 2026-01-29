import java.awt.Color;
import java.awt.Graphics;

/**
 * @class GiocatoreGrafico
 * @brief Gestisce la parte grafica della racchetta (il giocatore) utilizzando la giocatoreLogico
 */
public class GiocatoreGrafico {
    private giocatoreLogico giocatore;
    
    public giocatoreLogico getGiocatoreLogico() {
        return giocatore;
    }

    //costruttore con paramtetri
    public GiocatoreGrafico(giocatoreLogico logica) {
        this.giocatore = logica;
        this.colore = Color.BLUE;
    }

    public void setGiocatore(giocatoreLogico giocatore) {
        this.giocatore = giocatore;
    }

    private Color colore;

    /**
     * @brief costruttore
     * @param giocatore giocatoreLogico associato
     * @param colore colore della racchetta
     */
    public GiocatoreGrafico(giocatoreLogico giocatore, Color colore) {
        this.giocatore = giocatore;
        this.colore = colore;
    }

    /**
     * @brief disegna il giocatore
     * @param g oggetto su cui disegnare
     */
    public void disegna(Graphics g){
        int x=(int)giocatore.getPosizione().getX();
        int y=(int)giocatore.getPosizione().getY();
        int larghezza=(int)giocatore.getLarghezza();
        int altezza=(int)giocatore.getAltezza();

        g.setColor(colore);
        g.fillRect(x,y,larghezza,altezza);

        g.setColor(Color.BLACK);
        g.drawRect(x,y,larghezza,altezza);
    }

    /**
     * @brief setter per il colore
     * @param colore nuovo colore
     */
    public void setColore(Color colore){
        this.colore=colore;
    }
}