public class BonusLogico extends Thread{
    private String tipoBonus;
    private Punto coordinate;
    private int velocitaCaduta,durataEffetto;
    private boolean preso;
    private MyPanel pannello;






    public BonusLogico(int x,int y, int velocitaCaduta, int durataEffetto,MyPanel p) {
        int numerorandom=(int)(Math.random()*3);
        if(numerorandom==0){tipoBonus="Barralunga";}
        else if(numerorandom==1){tipoBonus="DuplicaPallina";}
        else{tipoBonus="VelocizzaBarra";}
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
        while(preso==false && coordinate.getY()<700&&pannello.isGameOver()==false){
            controllaCollisione(pannello.gl);
            muovi();
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
                PallinaLogica esistente= pannello.listaPallineLogiche.get(0);
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




    public void controllaCollisione(giocatoreLogico g){

       if(coordinate.getY()==g.getPosizione().getY()){
         if(coordinate.getX()>=g.getPosizione().getX()||coordinate.getX()<=g.getLarghezza()){
            preso=true;
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
