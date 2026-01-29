public class BloccoLogico {
    public Punto posizione;
    public double altezza,larghezza;
    public boolean distrutto;


    public Punto getPosizione() {
        return posizione;
    }

    public void setPosizione(Punto posizione) {
        this.posizione = posizione;
    }

    public double getAltezza() {
        return altezza;
    }

    public void setAltezza(double altezza) {
        this.altezza = altezza;
    }

    public double getLarghezza() {
        return larghezza;
    }

    public void setLarghezza(double larghezza) {
        this.larghezza = larghezza;
    }

    public boolean isDistrutto() {
        return distrutto;
    }

    public void setDistrutto(boolean distrutto) {
        this.distrutto = distrutto;
    }

    public BloccoLogico(Punto posizione, double altezza, double larghezza) {
        this.posizione = posizione;
        this.altezza = altezza;
        this.larghezza = larghezza;
        this.distrutto=false;
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
