import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

class MyPanel extends JPanel {
    private int larghezzaPannello = 1000;
    private int altezzaPannello = 700;

    private int larghezzaPiattaforma = 140;
    private int altezzaPiattaforma = 20;

    private boolean giocoIniziato = false;
    private boolean gameOver = false;
    private boolean vittoria = false;

    private Font font;

    private Image imgBottoneNormal = new ImageIcon("resources/button_play_again.png").getImage();
    private Image imgBottonePressed = new ImageIcon("resources/button_pressed_play_again.png").getImage();
    private boolean bottonePremuto = false;
    private Rectangle areaBottone = new Rectangle(400, 450, 200, 60);

    private Image imgBarraLunga = new ImageIcon("resources/bonus_allunga.png").getImage();
    private Image imgDuplica = new ImageIcon("resources/bonus_duplica.png").getImage();
    private Image imgVelocita = new ImageIcon("resources/bonus_velocita.png").getImage();

    // Giocatore
    public giocatoreLogico gl = new giocatoreLogico((larghezzaPannello - larghezzaPiattaforma) / 2, 630,
    larghezzaPiattaforma, altezzaPiattaforma, larghezzaPannello);
    public GiocatoreGrafico piattaforma = new GiocatoreGrafico(gl, Color.BLACK);

    // Pallina
    public PallinaLogica pl = new PallinaLogica(500, 600, 10, larghezzaPannello, altezzaPannello);
    public PallinaGrafica palla = new PallinaGrafica(pl, Color.RED);

    public List<PallinaGrafica> listaPallineGrafiche= new ArrayList<>();
    public List<PallinaLogica>listaPallineLogiche=new ArrayList<>();
    public int numPalline=1;


    private List<BloccoGrafico> listaBlocchi = new ArrayList<>();
    private List<BonusGrafico> listaBonus = new ArrayList<>();
    private Image sfondo;
    private Image immagineBonus = new ImageIcon("resources/IconBonus.jpg").getImage();
    private Image immagineGameOver = new ImageIcon("resources/game_over_panel.png").getImage();

    public MyPanel() {
        int nRighe = 6;
        int nColonne = 7;

        setBorder(BorderFactory.createLineBorder(Color.black));

        double larghezzaBlocco = 142.85;
        double altezzaBlocco = 25;

        // inizializzazione blocchi
        for (int i = 0; i < nRighe; i++) {
            for (int j = 0; j < nColonne; j++) {
                double x = j * larghezzaBlocco;
                double y = i * altezzaBlocco;
                Punto punto = new Punto(x, y);
                BloccoLogico logico = new BloccoLogico(punto, altezzaBlocco, larghezzaBlocco);
                listaBlocchi.add(new BloccoGrafico(logico));
            }
        }

        sfondo = new ImageIcon("resources/bg1.png").getImage(); //c'e' n'e' un altro nelle resources, pero' questo fa vedere meglio la pallina

        try {
            // Sostituisci "nome_tuo_font.ttf" con il nome esatto del file
            InputStream is = new BufferedInputStream(new FileInputStream("resources/font.ttf"));
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception e) {
            System.out.println("Errore caricamento font, uso Verdana");
            font = new Font("Verdana", Font.BOLD, 40);
        }

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
        if (gameOver || vittoria || !giocoIniziato)
            return;

        gl.update();

        for (int i = listaPallineLogiche.size()-1; i>=0; i--) {
            listaPallineLogiche.get(i).controllaRimbalzoGiocatore(gl);
        }
        

        // CONTROLLO SCONFITTA
         for (int i = listaPallineLogiche.size()-1; i>=0; i--) {
            if(listaPallineLogiche.get(i).getPosizione().getY()+(listaPallineLogiche.get(i).getRaggio()*2)>altezzaPannello){
                listaPallineLogiche.get(i).setAttiva(false);
                listaPallineGrafiche.remove(i);
                listaPallineLogiche.remove(i);
                numPalline--;
            }
        }
        



        // Collisioni blocchi con RIMOZIONE
       for (int i = 0; i < listaPallineLogiche.size(); i++) {
    PallinaLogica pCorrente = listaPallineLogiche.get(i);
    
    listaBlocchi.removeIf(b -> {
        if (b.getLogico().collisione(pCorrente)) {
            pCorrente.invertiY(); // La pallina che ha colpito rimbalza

            // Spawn Bonus 20%
            if (Math.random() < 0.20) {
                int bx = (int) b.getLogico().posizione.getX();
                int by = (int) b.getLogico().posizione.getY();
                
                BonusLogico boL = new BonusLogico(bx, by, 40, 10000, this);
                
                Image immagineDaUsare;
                if (boL.getTipoBonus().equals("Barralunga")) {
                    immagineDaUsare = imgBarraLunga;
                } else if (boL.getTipoBonus().equals("DuplicaPallina")) {
                    immagineDaUsare = imgDuplica;
                } else {
                    immagineDaUsare = imgVelocita;
                }

                BonusGrafico boG = new BonusGrafico(boL, immagineDaUsare, this);
                
                listaBonus.add(boG);
                new Thread(boL).start();
            }
            return true; // Rimuove il blocco colpito
        }
        return false;
    });
}   

        //se non ci sono più palline ho perso
        if(numPalline<=0){gameOver=true;}

        // Controllo se la lista blocchi è vuota
        if (listaBlocchi.isEmpty()) {
            vittoria = true;
            for (PallinaLogica ball : listaPallineLogiche) {
                ball.setAttiva(false);
            }
        }
    }
    

    //metodo creato per aggiungere creare una nuova pallina
    public void aggiungiPallina(double x, double y){
        PallinaLogica nuova= new PallinaLogica(x, y, 10,larghezzaPannello , altezzaPannello);
        nuova.setAttiva(true);
        PallinaGrafica nuovaG=new PallinaGrafica(nuova, Color.RED);
        listaPallineLogiche.add(nuova);
        listaPallineGrafiche.add(nuovaG);
        Thread nuovoThread = new Thread(nuova);
        nuovoThread.start();
        numPalline++;
    }

  

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (sfondo != null) {
            g.drawImage(sfondo, 0, 0, larghezzaPannello, altezzaPannello, this);
        }

        piattaforma.disegna(g);
        for (PallinaGrafica pg : listaPallineGrafiche) {
             pg.disegna(g);
        }
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

        // overlay di vittoria
        if (vittoria) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, larghezzaPannello, altezzaPannello);
            disegnaMessaggioCentrale(g, "VITTORIA!", Color.GREEN);

            g.setFont(new Font("Verdana", Font.PLAIN, 20));
            g.setColor(Color.WHITE);
            g.drawString("Premi 'R' per ricominciare", (larghezzaPannello / 2) - 130, (altezzaPannello / 2) + 50);
        }

        // overlay di game over
        if (gameOver) {
            //oscuriamo lo schermo
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, larghezzaPannello, altezzaPannello);

            //disegniamo l'immagine di Game Over al centro
            if (immagineGameOver != null) {
                int imgLargh = larghezzaPannello;
                int imgAlt = altezzaPannello;
                
                g.drawImage(immagineGameOver, 0, 0, imgLargh, imgAlt, this);
            }

            //scrivo il testo
            disegnaMessaggioCentrale(g, "GAME OVER", Color.RED);

            Image imgCorrente = bottonePremuto ? imgBottonePressed : imgBottoneNormal;
                    
            //centro il bottone orizzontalmente
            areaBottone.x = (larghezzaPannello - areaBottone.width) / 2;
            areaBottone.y = (altezzaPannello / 2) + 100;
            g.drawImage(imgCorrente, areaBottone.x, areaBottone.y, areaBottone.width, areaBottone.height, this);
        }
    }

    private void disegnaMessaggioCentrale(Graphics g, String testo, Color colore) {
        // .deriveFont(float size) cambia la dimensione del font caricato
        g.setFont(font.deriveFont(70f)); 
        
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
        if (!gameOver && !vittoria) {
            listaPallineGrafiche.clear();
            listaPallineLogiche.clear();
            listaPallineLogiche.add(pl);
            listaPallineGrafiche.add(palla);
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

    public Rectangle getAreaBottone() { return areaBottone; }
    public boolean isBottonePremuto() { return bottonePremuto; }
    public void setBottonePremuto(boolean stato) { this.bottonePremuto = stato; }

    public void resetGioco() {
        this.gameOver = false;
        this.vittoria = false;
        this.giocoIniziato = false;
        this.numPalline=1;

        

        //reset posizione giocatore
        gl.getPosizione().setX((larghezzaPannello - larghezzaPiattaforma) / 2);
        gl.getPosizione().setY(630);

        pl.getPosizione().setX(500);
        pl.getPosizione().setY(600);
        pl.setAttiva(false);

        listaBlocchi.clear();
        listaBonus.clear();
        listaPallineGrafiche.clear();
        listaPallineLogiche.clear();

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
