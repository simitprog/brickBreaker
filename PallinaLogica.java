/**
 * @class PallinaLogica
 * @brief Gestisce la logica della pallina utilizzando la classe Punto
 */
public class PallinaLogica implements Runnable{
    private Punto posizione;
    private double raggio;
    private double velX, velY;
    private double limiteX, limiteY;

    /**
     * @brief costruttore
     * @param x iniziale
     * @param y iniziale
     * @param raggio raggio della pallina
     * @param limiteX larghezza dell'area di gioco
     * @param limiteY altezza dell'area di gioco
     */
    public PallinaLogica(double x, double y, double raggio, double limiteX, double limiteY) {
        this.posizione = new Punto(x, y);
        this.raggio = raggio;
        this.limiteX = limiteX;
        this.limiteY = limiteY;
        this.velX = 3.0;
        this.velY = -3.0;
    }

    /**
     * @brief la pallina si muove con i thread
     */
    @Override
    public void run() {
        while (true) {
            muovi();
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {  }
        }
    }

    /**
     * @brief muove la pallina
     */
    public void muovi() {
        posizione.setX(posizione.getX()+velX);
        posizione.setY(posizione.getY()+velY);
    }

    /**
     * @brief controlla collisioni con i muri
     */
    private void controllaCollisioniMuri(){
        //controllo muri per le x
        if(posizione.getX()<=0 || posizione.getX()+raggio*2>=limiteX){
            velX=-velX;
        }

        //controllo muri per le y
        if(posizione.getY()<=0 || posizione.getY()+raggio*2>=limiteY){
            velY=-velY;
        }
    }

    /**
     * @brief controlla collisione con il giocatore
     */
    public void controllaRimbalzoGiocatore(giocatoreLogico giocatore){
        //controllo per vedere se la pallina tocca la racchetta
        if(posizione.getY()+(raggio*2)>=giocatore.getPosizione().getY() && 
           posizione.getX() + (raggio * 2) >= giocatore.getPosizione().getX() && 
           posizione.getX() <= giocatore.getPosizione().getX() + giocatore.getLarghezza()){
            
            //se la pallina sta scendendo
            if(velY>0){
                    //calcolo il centro della racchetta
                    double centroRacchetta=giocatore.getPosizione().getX() + (giocatore.getLarghezza()/2);
                    
                    //calcolo la distanza dal centro della pallina relativa alla racchetta
                    double distanzaDalCentro=(posizione.getX()+raggio)-centroRacchetta;

                    //modifica la velocita' sulla x in modo tale da avere l'effetto di rimbalzo
                    velX=distanzaDalCentro/10;

                    //la pallina rimbalza e va verso l'alto
                    velY=-velY;
                }
        }
    }

    public Punto getPosizione(){
        return this.posizione;
    }

    public double getRaggio(){
        return this.raggio;
    }
}