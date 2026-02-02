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
    private boolean vittoria = false; // AGGIUNTO: Stato vittoria
    
    // Giocatore (Piattaforma)
    public giocatoreLogico gl = new giocatoreLogico((larghezzaPannello - larghezzaPiattaforma) / 2, 630, larghezzaPiattaforma, altezzaPiattaforma, larghezzaPannello);
    public GiocatoreGrafico piattaforma = new GiocatoreGrafico(gl, Color.BLACK);
    
    // Pallina
    public PallinaLogica pl = new PallinaLogica(500, 600, 10, larghezzaPannello, altezzaPannello);
    public PallinaGrafica palla = new PallinaGrafica(pl, Color.RED);

    private List<BloccoGrafico> listaBlocchi = new ArrayList<>();
    private List<BonusGrafico> listaBonus = new ArrayList<>();
    private Image sfondo;
    private Image immagineBonus = new ImageIcon("resources/IconBonus.jpg").getImage();

    public MyPanel() {
        int nRighe = 6;
        int nColonne = 7;
    
        setBorder(BorderFactory.createLineBorder(Color.black));

        double larghezzaBlocco = 142.85;
        double altezzaBlocco = 25;

        //inizializzazione blocchi
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
        
        Thread threadPalla = new Thread(pl);
        threadPalla.start();

        Timer gameLoop = new Timer(16, e -> {
            updateGame();
            repaint();
        });
        gameLoop.start();

        MyMouseAdapter mouse = new MyMouseAdapter(this);
        addMouseListener(mouse);
        addMouseMotionListener(mouse); 

        MyKeyboardAdapter keyboard = new MyKeyboardAdapter(this);
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(keyboard);
    }

    private void updateGame() {
        if (gameOver || vittoria || !giocoIniziato) return;

        gl.update();
        pl.controllaRimbalzoGiocatore(gl);

        //CONTROLLO SCONFITTA
        if (pl.getPosizione().getY() + (pl.getRaggio() * 2) > altezzaPannello) {
            gameOver = true;
            pl.setAttiva(false); 
        }

        //Collisioni blocchi con RIMOZIONE
        listaBlocchi.removeIf(b -> {
            if (b.getLogico().collisione(pl)) {
                pl.invertiY();

                // Spawn Bonus 20%
                if (Math.random() < 0.20) {
                    int x = (int) b.getLogico().posizione.getX();
                    int y = (int) b.getLogico().posizione.getY();
                    BonusLogico boL = new BonusLogico(x, y, 40, 10000, this);
                    BonusGrafico boG = new BonusGrafico(boL, immagineBonus, this);
                    listaBonus.add(boG);
                    new Thread(boL).start();
                }
                return true; //rimuovo il blocco dalla lista
            }
            return false;
        });

        //Controllo se la lista blocchi è vuota
        if (listaBlocchi.isEmpty()) {
            vittoria = true;
            pl.setAttiva(false);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (sfondo != null) {
            g.drawImage(sfondo, 0, 0, larghezzaPannello, altezzaPannello, this);
        }

        piattaforma.disegna(g);
        palla.disegna(g);
        for (BloccoGrafico b : listaBlocchi) {
            b.disegna(g);
        }

        for (BonusGrafico bo : listaBonus) {
            bo.disegna(g);
        }

        // Overlay INIZIO GIOCO
        if (!giocoIniziato && !gameOver && !vittoria) {
            disegnaMessaggioCentrale(g, "PREMI INVIO PER GIOCARE", Color.WHITE);
        }

        //overlay di vittoria
        if (vittoria) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, larghezzaPannello, altezzaPannello);
            disegnaMessaggioCentrale(g, "VITTORIA!", Color.GREEN);
            
            g.setFont(new Font("Verdana", Font.PLAIN, 20));
            g.setColor(Color.WHITE);
            g.drawString("Premi 'R' per ricominciare", (larghezzaPannello / 2) - 130, (altezzaPannello / 2) + 50);
        }

        //overlay di game over
        if (gameOver) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, larghezzaPannello, altezzaPannello);
            disegnaMessaggioCentrale(g, "GAME OVER", Color.RED);

            g.setFont(new Font("Verdana", Font.PLAIN, 20));
            g.setColor(Color.WHITE);
            g.drawString("Premi 'R' per ricominciare", (larghezzaPannello / 2) - 130, (altezzaPannello / 2) + 50);
        }
    }

    private void disegnaMessaggioCentrale(Graphics g, String testo, Color colore) {
        g.setFont(new Font("Verdana", Font.BOLD, 40));
        FontMetrics fm = g.getFontMetrics();
        int x = (larghezzaPannello - fm.stringWidth(testo)) / 2;
        int y = altezzaPannello / 2;

        g.setColor(Color.BLACK);
        g.drawString(testo, x + 3, y + 3);
        g.setColor(colore);
        g.drawString(testo, x, y);
    }

    public void iniziaPartita() {
        if (!gameOver && !vittoria) {
            this.giocoIniziato = true;
            this.pl.setAttiva(true);
        }
    }

    public void rimuoviBonus(BonusLogico logico) {
        listaBonus.removeIf(bg -> bg.getBonus() == logico);
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

    public boolean isVittoria() {
        return vittoria;
    }

    public boolean isGiocoIniziato() {
        return giocoIniziato;
    }

    public void resetGioco() {
        this.gameOver = false;
        this.vittoria = false;
        this.giocoIniziato = false;

        gl.getPosizione().setX((larghezzaPannello - larghezzaPiattaforma) / 2);
        gl.getPosizione().setY(630);

        pl.getPosizione().setX(500);
        pl.getPosizione().setY(600);
        pl.setAttiva(false);

        listaBlocchi.clear();
        listaBonus.clear();
        
        double larghezzaBlocco = 142.85;
        double altezzaBlocco = 25;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                double x = j * larghezzaBlocco;
                double y = i * altezzaBlocco;
                listaBlocchi.add(new BloccoGrafico(new BloccoLogico(new Punto(x, y), altezzaBlocco, larghezzaBlocco)));
            }
        }

        repaint();
        requestFocusInWindow();
    }
}