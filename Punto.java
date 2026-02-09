/**
* @author  timis, alexandromatteo.timis@gmail.com
* @version 1.0
* @file Punto.java 
* 
* @brief La classe Punto permette di creare un punto in un piano cartesiano a livello logico
*
*/

/** 
* @class Punto
* 
* @brief La classe Punto permette di creare un punto in un piano cartesiano a livello logico
* 
* La classe Punto permette di creare un punto in un piano cartesiano a livello logico, permettendo 
* di svolgere calcoli tra punti in modo preciso
*/ 
public class Punto{
    //PRIVATE

    /** x del punto */
    private double x;

    /** y del punto */
    private double y;

    //PUBLIC

    /**
     @brief costruttore della classe Punto

    Questo metodo permette di creare un punto sul piano cartesiano
    @param  par1 x del punto
    @param  par2 y del punto
    */
    public Punto(double x, double y){
        this.x=x;
        this.y=y;
    }

    /**
     @brief Setter per la X

     Questo metodo permette di settare la x del punto
     @param  par1 x del punto
    */
    public void setX(double x){
        this.x = x;
    }

    /**
     * @brief getter della x
     * @return valore della x
     */
    public double getX(){
        return this.x;
    }

    /**
     * @brief getter della y
     * @return valore della y
     */
    public double getY(){
        return this.y;
    }

    /**
     @brief Setter per la y

    Questo metodo permette di settare la x del punto
    @param  par1 y del punto
    */
    public void setY(double y){
        this.y = y;
    }

    /**
     @brief Calcolo distanza tra punti

    Questo metodo permette di calcolare la distanza tra due punti
    @param  par1 altro punto
    @return val distanza trea due punti
    */
    double calcolaDistanza(Punto altroPunto){
        return Math.sqrt(Math.pow(this.x - altroPunto.x,2)+Math.pow(this.y - altroPunto.y,2));
    }
}
