public class BloccoLogico {
    public Punto posizione;
    public double altezza,larghezza;
    public boolean distrutto;


    public BloccoLogico(Punto posizione, double altezza, double larghezza, boolean distrutto) {
        this.posizione = posizione;
        this.altezza = altezza;
        this.larghezza = larghezza;
        this.distrutto = distrutto;
    }

    public boolean collisione(PallinaLogica palla)
    {
        if(distrutto){return false;}
        double pallaX=palla.getPosizione().getX();
        double pallaY=palla.getPosizione().getY();
        double diametro= palla.getRaggio()*2;
        /*   Controllo se la Y della palla è dentro a quella del blocco*/
        if (pallaX + diametro >= posizione.getX()) {
        if (pallaX <= posizione.getX() + larghezza) {
            
            /*  Controllo se la Y della palla è dentro a quella del blocco*/
            if (pallaY + diametro >= posizione.getY()) {
                if (pallaY <= posizione.getY() + altezza) {
                    
                    this.distrutto = true;
                    return true;
                }
            }
        }
    }

    return false;

    }

}
