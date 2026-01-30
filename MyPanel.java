import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class MyPanel extends JPanel {
    private int larghezzaPannello = 1000;
    private int altezzaPannello = 700;

    private int larghezzaPiattaforma = 140;
    private int altezzaPiattaforma = 20;

    private boolean giocoIniziato = false;
    private boolean gameOver = false;
    
    // Giocatore (Piattaforma)
    public giocatoreLogico gl = new giocatoreLogico((larghezzaPannello - larghezzaPiattaforma) / 2, 630, larghezzaPiattaforma, altezzaPiattaforma, larghezzaPannello);
    public GiocatoreGrafico piattaforma = new GiocatoreGrafico(gl, Color.BLACK);
    
    // Pallina
    public PallinaLogica pl = new PallinaLogica(500, 600, 10, larghezzaPannello, altezzaPannello);
    public PallinaGrafica palla = new PallinaGrafica(pl, Color.RED);

    private List<BloccoGrafico> listaBlocchi = new ArrayList<>();
    private List<BonusGrafico>listaBonus = new ArrayList<>();
    private Image sfondo;
    private Image immagineBonus= new ImageIcon("resources/IconBonus.webp").getImage();

    public MyPanel() {
        int nRighe = 6;
        int nColonne = 7;
    
        setBorder(BorderFactory.createLineBorder(Color.black));

        double larghezzaBlocco = 142.85;
        double altezzaBlocco = 25;

        // Inizializzazione blocchi
        for (int i = 0; i < nRighe; i++) {
            for (int j = 0; j < nColonne; j++) {
                double x = j * larghezzaBlocco;
                double y = i * altezzaBlocco;
                Punto punto = new Punto(x, y);
                BloccoLogico logico = new BloccoLogico(punto, altezzaBlocco, larghezzaBlocco);
                listaBlocchi.add(new BloccoGrafico(logico));
            }
        }
      
        sfondo = new ImageIcon("resources/Possibilesfondo.jpg").getImage();
        
        // Avvio del Thread della pallina (partirà ferma perché pl.attiva è false)
        Thread threadPalla = new Thread(pl);
        threadPalla.start();

        // Game Loop
        Timer gameLoop = new Timer(16, e -> {
            updateGame();
            repaint();
        });
        gameLoop.start();

        // Listeners
        MyMouseAdapter mouse = new MyMouseAdapter(this);
        addMouseListener(mouse);
        addMouseMotionListener(mouse); 

        MyKeyboardAdapter keyboard = new MyKeyboardAdapter(this);
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(keyboard);
    }

    private void updateGame() {
        if (gameOver || !giocoIniziato) return; 

        gl.update();
        pl.controllaRimbalzoGiocatore(gl);

        // CONTROLLO SCONFITTA
        if (pl.getPosizione().getY() + (pl.getRaggio() * 2) > altezzaPannello) {
            gameOver = true;
            pl.setAttiva(false); 
        }

        // Collisioni blocchi
        for (BloccoGrafico b : listaBlocchi) {
            if (b.getLogico().collisione(pl)) {
                pl.invertiY();
                
                    int x=(int) b.getLogico().posizione.getX();
                    int y=(int) b.getLogico().posizione.getY();
                   BonusLogico boL= new BonusLogico(x,y,1350,40, this);
                   BonusGrafico boG= new BonusGrafico(boL, sfondo, this);
                    listaBonus.add(boG);
                   new Thread(boL).start();
                
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. Disegno Sfondo
        if (sfondo != null) {
            g.drawImage(sfondo, 0, 0, larghezzaPannello, altezzaPannello, this);
        }

        // 2. Disegno Entità
        piattaforma.disegna(g);
        palla.disegna(g);
        for (BloccoGrafico b : listaBlocchi) {
            b.disegna(g);
        }

        for(BonusGrafico bo: listaBonus){
            bo.disegna(g);
        }

        // 3. Overlay INIZIO GIOCO
        if (!giocoIniziato && !gameOver) {
            disegnaMessaggioCentrale(g, "PREMI INVIO PER GIOCARE", Color.WHITE);
        }

        // 4. Overlay GAME OVER
        if (gameOver) {
            // Sfondo scuro semitrasparente per dare enfasi al Game Over
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, larghezzaPannello, altezzaPannello);
            disegnaMessaggioCentrale(g, "GAME OVER", Color.RED);
        }
    }

    /**
     * Metodo di utility per disegnare testo centrato
     */
    private void disegnaMessaggioCentrale(Graphics g, String testo, Color colore) {
        g.setFont(new Font("Verdana", Font.BOLD, 40));
        FontMetrics fm = g.getFontMetrics();
        int x = (larghezzaPannello - fm.stringWidth(testo)) / 2;
        int y = altezzaPannello / 2;

        // Ombra
        g.setColor(Color.BLACK);
        g.drawString(testo, x + 3, y + 3);
        // Testo principale
        g.setColor(colore);
        g.drawString(testo, x, y);
    }

    public void iniziaPartita() {
        if (!gameOver) {
            this.giocoIniziato = true;
            this.pl.setAttiva(true);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(larghezzaPannello, altezzaPannello);
    }

    public giocatoreLogico getGiocatoreLogico() {
        return gl;
    }

    public boolean isGameOver() {
        return gameOver;
    }
    public boolean isGiocoIniziato() {
    return giocoIniziato;
}

}