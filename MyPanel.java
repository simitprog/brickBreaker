import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javax.sound.sampled.*;
import java.io.File;

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

    private String[] playlist = {"inTheEnd_LP.wav", "decode_Paramore.wav", "spiders_SOAD.wav", "MyWay_LB.wav", "byTheWay_RHCP.wav"}; // Aggiungi i tuoi nomi reali
    private int indiceMusica = 0;
    private Clip backgroundMusic;
    private boolean isMuted = false;

    private Image imgVolumeOn = new ImageIcon("resources/volumeOn.png").getImage();
    private Image imgVolumeOff = new ImageIcon("resources/volumeOff.png").getImage();

    private boolean isPausa = false;
    private Image imgPausa = new ImageIcon("resources/pause.png").getImage();
    private Image imgPlay = new ImageIcon("resources/play.png").getImage();
    private Rectangle areaBottonePausa = new Rectangle(950, 650, 40, 40);
    private Rectangle areaBottonePlay = new Rectangle(450, 345, 100,100);

    // Giocatore
    public giocatoreLogico gl = new giocatoreLogico((larghezzaPannello - larghezzaPiattaforma) / 2, 630,
    larghezzaPiattaforma, altezzaPiattaforma, larghezzaPannello);
    public GiocatoreGrafico piattaforma = new GiocatoreGrafico(gl, Color.BLACK);

    // Pallina
    public PallinaLogica pl = new PallinaLogica(500, 600, 10, larghezzaPannello, this);
    public PallinaGrafica palla = new PallinaGrafica(pl, Color.RED);

    public List<PallinaGrafica> listaPallineGrafiche= new ArrayList<>();
    public List<PallinaLogica>listaPallineLogiche=new ArrayList<>();
    public int numPalline=1;


    private List<BloccoGrafico> listaBlocchi = new ArrayList<>();
    private List<BonusGrafico> listaBonus = new ArrayList<>();
    private Image sfondo;
    private Image immagineGameOver = new ImageIcon("resources/game_over_panel.png").getImage();

    private Rectangle areaBottoneHome = new Rectangle(0, 0, 200, 60);
    private Image imgGioca = new ImageIcon("resources/button_gioca.png").getImage(); // Opzionale

    public boolean getCanReload(){
        return gameOver || vittoria;
    }

    public MyPanel() {
        int nRighe = 6;
        int nColonne = 7;

        setBorder(BorderFactory.createLineBorder(Color.black));

        double larghezzaBlocco = 142.857;
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
        if (gameOver || vittoria || !giocoIniziato || isPausa)
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
        PallinaLogica nuova= new PallinaLogica(x, y, 10,larghezzaPannello, this);
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

        // 1. SFONDO
        if (sfondo != null) {
            g.drawImage(sfondo, 0, 0, larghezzaPannello, altezzaPannello, this);
        }

        // 2. ELEMENTI DI GIOCO (Piattaforma, Palline, Blocchi, Bonus)
        // Li disegniamo sempre, ma saranno coperti dall'overlay se il gioco non è iniziato
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

        // 3. INTERFACCIA UI (Bottoni visibili solo DURANTE il gioco)
        if (giocoIniziato && !gameOver && !vittoria) {
            
            // --- BOTTONE PAUSA (Scompare se è già in pausa)
            if (!isPausa()) {
                g.drawImage(imgPausa, areaBottonePausa.x, areaBottonePausa.y, 
                            areaBottonePausa.width, areaBottonePausa.height, this);
            }

            // --- ICONA VOLUME
            Image iconaVolume = isMuted ? imgVolumeOff : imgVolumeOn;
            if (iconaVolume != null) {
                int dimensioneIcona = 35;
                int margine = 10;
                int xIcona = margine;
                int yIcona = altezzaPannello - dimensioneIcona - margine;
                g.drawImage(iconaVolume, xIcona, yIcona, dimensioneIcona, dimensioneIcona, this);
            }
        }

        // 4. OVERLAY DI STATO

        // --- SCHERMATA HOME (Nuova!) ---
        if (!giocoIniziato && !gameOver && !vittoria) {
            // Oscuriamo lo sfondo per far risaltare la Home
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, larghezzaPannello, altezzaPannello);

            // Titolo del Gioco
            disegnaMessaggioCentrale(g, "BRICK BREAKER", Color.CYAN, 100);

            // Disegno Bottone "GIOCA"
            int larghezzaB = 200;
            int altezzaB = 60;
            areaBottoneHome.x = (larghezzaPannello - larghezzaB) / 2;
            areaBottoneHome.y = (altezzaPannello / 2) + 50;
            areaBottoneHome.width = larghezzaB;
            areaBottoneHome.height = altezzaB;

            if (imgGioca != null) {
                g.drawImage(imgGioca, areaBottoneHome.x, areaBottoneHome.y, areaBottoneHome.width, areaBottoneHome.height, this);
            } else {
                // Se non hai l'immagine, disegna un bottone stilizzato
                g.setColor(Color.WHITE);
                g.fillRoundRect(areaBottoneHome.x, areaBottoneHome.y, areaBottoneHome.width, areaBottoneHome.height, 20, 20);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 25));
                g.drawString("GIOCA", areaBottoneHome.x + 55, areaBottoneHome.y + 40);
            }
        }

        // --- SCHERMATA DI PAUSA ---
        if (isPausa && !gameOver && !vittoria) {
            g.setColor(new Color(0, 0, 0, 215)); 
            g.fillRect(0, 0, larghezzaPannello, altezzaPannello);

            disegnaMessaggioPausa(g, "PAUSA", Color.YELLOW, 100);

            int dimensionePlay = 80;
            areaBottonePlay.x = (larghezzaPannello - dimensionePlay) / 2;
            areaBottonePlay.y = (altezzaPannello / 2) + 60;
            areaBottonePlay.width = dimensionePlay;
            areaBottonePlay.height = dimensionePlay;

            if (imgPlay != null) {
                g.drawImage(imgPlay, areaBottonePlay.x, areaBottonePlay.y, 
                            areaBottonePlay.width, areaBottonePlay.height, this);
            } else {
                g.setColor(Color.GREEN);
                g.fillOval(areaBottonePlay.x, areaBottonePlay.y, dimensionePlay, dimensionePlay);
            }
        }

        // --- SCHERMATA VITTORIA ---
        if (vittoria) {
            g.setColor(new Color(0, 0, 0, 215));
            g.fillRect(0, 0, larghezzaPannello, altezzaPannello);
            disegnaMessaggioCentrale(g, "VITTORIA!", Color.GREEN, 150);
            
            g.setFont(new Font("Verdana", Font.PLAIN, 20));
            g.setColor(Color.WHITE);
            g.drawString("Premi 'R' per ricominciare", (larghezzaPannello / 2) - 130, (altezzaPannello / 2) + 120);
        }

        // --- SCHERMATA GAME OVER ---
        if (gameOver) {
            g.setColor(new Color(0, 0, 0, 215));
            g.fillRect(0, 0, larghezzaPannello, altezzaPannello);

            if (immagineGameOver != null) {
                g.drawImage(immagineGameOver, 0, 0, larghezzaPannello, altezzaPannello, this);
            }

            disegnaMessaggioCentrale(g, "GAME OVER", Color.RED, 150);

            Image imgCorrenteReset = bottonePremuto ? imgBottonePressed : imgBottoneNormal;
            areaBottone.x = (larghezzaPannello - areaBottone.width) / 2;
            areaBottone.y = (altezzaPannello / 2) + 100;
            
            if (imgCorrenteReset != null) {
                g.drawImage(imgCorrenteReset, areaBottone.x, areaBottone.y, areaBottone.width, areaBottone.height, this);
            }
        }
    }

    private void disegnaMessaggioCentrale(Graphics g, String testo, Color colore, int dimensione) {
        // .deriveFont(float size) cambia la dimensione del font caricato
        g.setFont(font.deriveFont((float)dimensione)); 
        
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

    private void disegnaMessaggioPausa(Graphics g, String testo, Color colore, int dimensione) {
        // .deriveFont(float size) cambia la dimensione del font caricato
        g.setFont(font.deriveFont((float)dimensione)); 
        
        FontMetrics fm = g.getFontMetrics();
        int x = (larghezzaPannello - fm.stringWidth(testo)) / 2;
        int y = altezzaPannello / 2 - 50;

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

        if (backgroundMusic == null) {
            playSoundtrack("inTheEnd_LP.wav");
        }
        
        this.giocoIniziato = true;
        this.pl.setAttiva(true);
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

    public void playSoundtrack(String filename) {
        try {
            // 1. FERMA E CHIUDI la musica precedente se esiste
            if (backgroundMusic != null) {
                backgroundMusic.stop();  // Ferma la riproduzione
                backgroundMusic.flush(); // Svuota il buffer dei dati
                backgroundMusic.close(); // Rilascia le risorse di sistema
            }

            // 2. Carica il nuovo file
            File soundFile = new File("resources/soundtracks/" + filename);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            
            // 3. Crea il nuovo clip
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            
            // 4. Avvia il loop
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
            
        } catch (Exception e) {
            System.out.println("Errore audio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopSoundtrack() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    public void prossimaCanzone() {
        indiceMusica = (indiceMusica + 1) % playlist.length;
        playSoundtrack(playlist[indiceMusica]);
    }

    public void toggleMute() {
        if (backgroundMusic == null) return;

        // Otteniamo il controllo del volume del clip
        FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
        
        if (!isMuted) {
            // Mettiamo il volume al minimo possibile (silenzio assoluto)
            gainControl.setValue(gainControl.getMinimum());
            isMuted = true;
            System.out.println("Audio OFF");
        } else {
            // Riportiamo il volume a 0.0 decibel (volume standard del file)
            gainControl.setValue(0.0f);
            isMuted = false;
            System.out.println("Audio ON");
        }

        repaint();
    }

    public void setPausa(boolean pausa) {
        this.isPausa = pausa;
        
        if (backgroundMusic != null) {
            if (isPausa) {
                backgroundMusic.stop();
            } else if (giocoIniziato) {
                backgroundMusic.start();
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
        repaint();
    }

    public boolean isPausa() {
        return isPausa;
    }

    public Rectangle getAreaBottonePausa() {
        return areaBottonePausa;
    }

    public Rectangle getAreaBottonePlay() {
        return areaBottonePlay;
    }

    public Rectangle getAreaBottoneHome() {
        return areaBottoneHome;
    }
}