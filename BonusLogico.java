public class BonusLogico extends Thread{
    private String tipoBonus;
    private Punto coordinate;
    private int velocitaCaduta,durataEffetto;
    private boolean preso;
    private MyPanel pannello;






    public BonusLogico(int x,int y, int velocitaCaduta, int durataEffetto,MyPanel p) {
        //quando creo un bonus, questo ha l'effetto casuale
        int numerorandom=(int)(Math.random()*3);
        if(numerorandom==0){tipoBonus="Barralunga";}
        else if(numerorandom==1){tipoBonus="DuplicaPallina";}
        else{tipoBonus="VelocizzaBarra";}
        tipoBonus="DuplicaPallina";
        this.velocitaCaduta=velocitaCaduta;
        this.coordinate=new Punto(x, y);
        this.durataEffetto=durataEffetto;
        this.preso=false;
        pannello=p;
    }

    public void muovi(){
        coordinate.setY(coordinate.getY()+5);
    }


    @Override 
    public void run(){
       
        while(preso==false && coordinate.getY()<700&&pannello.isGameOver()==false&&pannello.isVittoria()==false){
            controllaCollisione(pannello.gl);
            //si muove solo se non è in pausa
            if(pannello.isPausa()==false){
            muovi();
            }   
            try{
                Thread.sleep(velocitaCaduta);
            }catch(InterruptedException e){}
        }
            pannello.rimuoviBonus(this); 

            if(preso==true){
                attivaEffetto();
                try{
                    Thread.sleep(durataEffetto);
                }catch(InterruptedException e){}
                rimuoviEffetto();
            }
           
        }
        
    




    public void attivaEffetto(){
        if(tipoBonus=="Barralunga"){
            pannello.gl.setLarghezza(240);
        }else if(tipoBonus=="VelocizzaBarra"){
            pannello.gl.setVelocita(11.0);
        }else if(tipoBonus=="DuplicaPallina"){
            if(pannello.numPalline!=0){
                //la pallina deve spawnare dove si trovava la più "vecchia".
                PallinaLogica esistente= pannello.listaPallineLogiche.get(0);
                //creo una nuova pallina con le coordinate di una già esistente
                pannello.aggiungiPallina(esistente.getPosizione().getX(),esistente.getPosizione().getY());
            }
        }
    }


    public void rimuoviEffetto(){
        if(tipoBonus=="Barralunga"){
            pannello.gl.setLarghezza(140);
        }else if(tipoBonus=="VelocizzaBarra"){
            pannello.gl.setVelocita(8.0);
        }
    }




    public void controllaCollisione(giocatoreLogico g) {
    double bonusX = coordinate.getX();
    double bonusY = coordinate.getY();
    int larghezzaBonus = 40;
    int altezzaBonus = 40;

    //margini della barra
    double barraXMin = g.getPosizione().getX();
    double barraXMax = g.getPosizione().getX() + g.getLarghezza();
    double barraY = g.getPosizione().getY();

    //controllo collisione:
    if (bonusY + altezzaBonus >= barraY && bonusY <= barraY + g.getAltezza()) {
        
        //la x del bonus deve essere compresa tra l'inizio e la fine della barra
        if (bonusX + larghezzaBonus >= barraXMin && bonusX <= barraXMax) {
            preso = true;
        }
    }
}

       public String getTipoBonus() {
        return tipoBonus;
    }

    public void setTipoBonus(String tipoBonus) {
        this.tipoBonus = tipoBonus;
    }

    public Punto getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Punto coordinate) {
        this.coordinate = coordinate;
    }

    public int getVelocitaCaduta() {
        return velocitaCaduta;
    }

    public void setVelocitaCaduta(int velocitaCaduta) {
        this.velocitaCaduta = velocitaCaduta;
    }

    public int getDurataEffetto() {
        return durataEffetto;
    }

    public void setDurataEffetto(int durataEffetto) {
        this.durataEffetto = durataEffetto;
    } 

    public boolean isPreso() {
        return preso;
    }

    public void setPreso(boolean preso) {
        this.preso = preso;
    }

    public MyPanel getPannello() {
        return pannello;
    }

    public void setPannello(MyPanel pannello) {
        this.pannello = pannello;
    }


}
