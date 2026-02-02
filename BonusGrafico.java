import java.awt.*;


public class BonusGrafico {
    private BonusLogico bonus;
    private Image immagine;
    private MyPanel pannello;

    public BonusGrafico(BonusLogico bonus, Image immagine, MyPanel pannello) {
        this.bonus = bonus;
        this.immagine = immagine;
        this.pannello = pannello;
    }


    public BonusLogico getBonus() {
        return bonus;
    }


    public void setBonus(BonusLogico bonus) {
        this.bonus = bonus;
    }


    public Image getImmagine() {
        return immagine;
    }


    public void setImmagine(Image immagine) {
        this.immagine = immagine;
    }




    public void disegna (Graphics g){
        int x = (int)bonus.getCoordinate().getX();
        int y=(int)bonus.getCoordinate().getY();
        
        g.drawImage(immagine,x,y,30,30,pannello);
    
    }


    










}
