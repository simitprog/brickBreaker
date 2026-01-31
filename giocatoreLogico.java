/**
 * @class GiocatoreLogico
 * @brief Gestisce la logica della racchetta (il giocatore) utilizzando la classe Punto
 */
public class giocatoreLogico {
    private Punto posizione;
    private double larghezza;
    private double altezza;
    private double velocita;
    


    private double limiteSchermoX;

    // Direzione: -1 (sinistra), 0 (fermo), 1 (destra)
    private int direzione = 0;

    /**
     * @brief costruttore
     * @param x iniziale
     * @param y iniziale
     * @param larghezza larghezza della racchetta
     * @param altezza altezza della racchetta
     * @param limiteSchermoX larghezza dell'area di gioco
     */
    public giocatoreLogico(double x, double y, double larghezza, double altezza, double limiteSchermoX) {
        this.posizione = new Punto(x, y);
        this.larghezza = larghezza;
        this.altezza = altezza;
        this.limiteSchermoX = limiteSchermoX;
        this.velocita = 8.0; 
    }

    /**
     * @brief aggiorna la posizione in base alla direzione e ai limiti
     */
    public void update() {
        //calcolo la nuova X
        double nuovaX = posizione.getX() + (direzione * velocita);

        //controllo collisioni con i bordi laterali
        if (nuovaX < 0) {
            nuovaX = 0;
        } else if (nuovaX + larghezza > limiteSchermoX) {
            nuovaX = limiteSchermoX - larghezza;
        }

        posizione.setX(nuovaX);
    }

    
    //metodi per movimento
    public void muoviSinistra() { direzione = -1; }
    public void muoviDestra() { direzione = 1; }
    public void ferma() { direzione = 0; }

    //getters
    public Punto getPosizione() { return posizione; }
    public double getLarghezza() { return larghezza; }
    public double getAltezza() { return altezza; }


    //setter
     public void setLarghezza(double larghezza) {
        this.larghezza = larghezza;
    }
    public void setVelocita(double velocita) {
        this.velocita = velocita;
    }
}
