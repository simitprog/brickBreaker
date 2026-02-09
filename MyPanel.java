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
    private int larghezzaPannello = 1050;
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

    private String[] playlist = {"inTheEnd_LP.wav", "decode_Paramore.wav", "spiders_SOAD.wav", "MyWay_LB.wav", "byTheWay_RHCP.wav"};
    private String[] nomeCanzoni={"In the End - Linkin Park", "Decode - Paramore", "Spiders - System of a Down", "My Way - Limp Bizkit", "By the Way - Red Hot Chilly Peppers"};
    private String[] nomePhotoCanzone={"hybridTheory.png","brandNewEyes.png","soad.png","chocolateStafish.png", "btw.png"};
    private List<Image> coverAlbums = new ArrayList<>();
    private int indiceMusica = 0;
    private Clip backgroundMusic;
    private boolean isMuted = false;

    private Image imgVolumeOn = new ImageIcon("resources/volumeOn.png").getImage();
    private Image imgVolumeOff = new ImageIcon("resources/volumeOff.png").getImage();

    private boolean isPausa = false;
    private Image imgPausa = new ImageIcon("resources/pause.png").getImage();

    private Image imgPlay = new ImageIcon("resources/play.png").getImage();
    private Image imgPlay_pressed = new ImageIcon("resources/play_Pressed.png").getImage();
    private boolean playPremuto = false;

    private Rectangle areaBottonePausa = new Rectangle(1000, 650, 40, 40);
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
    private Image imgGioca = new ImageIcon("resources/button_gioca.png").getImage();
    private Image imgGiocaPressed = new ImageIcon("resources/button_gioca_pressed.png").getImage();
    private boolean giocaPremuto = false;

    private Image imgComandi = new ImageIcon("resources/button_comandi.png").getImage();
    private Image imgComandiPressed = new ImageIcon("resources/button_comandi_pressed.png").getImage();
    private boolean comandiPremuto = false;

    private Rectangle areaBottoneComandi = new Rectangle(0, 0, 200, 60);
    private boolean mostraComandi = false;

    private Image imgMainHome = new ImageIcon("resources/ButtonsMainMenu.png").getImage();
    private Image imgMainHomePressed = new ImageIcon("resources/ButtonsMainMenu_pressed.png").getImage();
    private Rectangle areaBottoneTornaHome = new Rectangle(0, 0, 200, 60);
    private boolean tornaHomePremuto = false;

    private int punteggio=0;
    private int moltiplicatore = 1;
    private int record;
    private final String FILE_RECORD = "highscore.txt";

    public boolean getCanReload(){
        return gameOver || vittoria;
    }

    public MyPanel() {
        int nRighe = 6;
        int nColonne = 7;

        setBorder(BorderFactory.createLineBorder(Color.black));

        double larghezzaBlocco = 150;
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

        for (String nomeFile : nomePhotoCanzone) {
            coverAlbums.add(new ImageIcon("resources/" + nomeFile).getImage());
        }

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

        // 1. RIMBALZO GIOCATORE E RESET MOLTIPLICATORE
        for (int i = listaPallineLogiche.size() - 1; i >= 0; i--) {
            PallinaLogica p = listaPallineLogiche.get(i);
            
            // Creiamo un rettangolo temporaneo per la pallina
            java.awt.Rectangle rectPallina = new java.awt.Rectangle(
                (int)p.getPosizione().getX(), 
                (int)p.getPosizione().getY(), 
                (int)p.getRaggio() * 2, 
                (int)p.getRaggio() * 2
            );

            // Creiamo un rettangolo temporaneo per il giocatore (gl)
            java.awt.Rectangle rectGiocatore = new java.awt.Rectangle(
                (int)gl.getPosizione().getX(), 
                (int)gl.getPosizione().getY(), 
                (int)gl.getLarghezza(), 
                (int)gl.getAltezza()
            );

            // Ora il controllo .intersects() funzionerà perfettamente
            if (rectPallina.intersects(rectGiocatore)) {
                moltiplicatore = 1; 
            }
            
            p.controllaRimbalzoGiocatore(gl);
        }

        // 2. CONTROLLO SCONFITTA (Caduta pallina)
        for (int i = listaPallineLogiche.size() - 1; i >= 0; i--) {
            if (listaPallineLogiche.get(i).getPosizione().getY() + (listaPallineLogiche.get(i).getRaggio() * 2) > altezzaPannello) {
                listaPallineLogiche.get(i).setAttiva(false);
                listaPallineGrafiche.remove(i);
                listaPallineLogiche.remove(i);
                numPalline--;
                
                // Reset moltiplicatore anche quando si perde una pallina
                moltiplicatore = 1;

                
            }
        }

        // 3. COLLISIONE BLOCCHI CON RIMOZIONE E COMBO
        for (int i = 0; i < listaPallineLogiche.size(); i++) {
            PallinaLogica pCorrente = listaPallineLogiche.get(i);

            listaBlocchi.removeIf(b -> {
                if (b.getLogico().collisione(pCorrente)) {
                    pCorrente.invertiY(); 

                    // Calcolo punteggio con moltiplicatore
                    punteggio += 100;
                    punteggio += (10 * moltiplicatore);
                    moltiplicatore++; // Aumenta la combo per il prossimo blocco

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
                    return true; 
                }
                return false;
            });
        }

        // 4. CONTROLLO STATO FINALE
        if (numPalline <= 0) {
            gameOver = true;
        }

        if (listaBlocchi.isEmpty()) {
            vittoria = true;
            for (PallinaLogica ball : listaPallineLogiche) {
                ball.setAttiva(false);
            }
        }

        //controllo punteggio massimo
        if (listaBlocchi.isEmpty()) {
            vittoria = true;
            // Anche in caso di vittoria controlliamo il record
            if (punteggio > record) {
                record = punteggio;
                salvaRecord();
            }
            for (PallinaLogica ball : listaPallineLogiche) {
                ball.setAttiva(false);
            }
        }
    }
    

    //metodo creato per aggiungere creare una nuova pallina
    //i parametri sono la x e la y di una pallina già esistente
    public void aggiungiPallina(double x, double y){

        PallinaLogica nuova= new PallinaLogica(x, y, 10,larghezzaPannello, this);
        nuova.setAttiva(true);
        //creo la pallina e la aggiungo alla lista delle palline
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

        // 1. Sfondo
        if (sfondo != null) {
            g.drawImage(sfondo, 0, 0, larghezzaPannello, altezzaPannello, this);
        }

        // 2. Elementi di gioco (sempre disegnati come base)
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

        // 3. Interfaccia UI durante il gioco attivo
        if (giocoIniziato && !gameOver && !vittoria && !isPausa) {
            // Bottone pausa in alto/lato
            if (imgPausa != null) {
                g.drawImage(imgPausa, areaBottonePausa.x, areaBottonePausa.y, 
                            areaBottonePausa.width, areaBottonePausa.height, this);
            }

            // Icona volume
            Image iconaVolume = isMuted ? imgVolumeOff : imgVolumeOn;
            if (iconaVolume != null) {
                int dimensioneIcona = 35;
                int margine = 10;
                g.drawImage(iconaVolume, margine, altezzaPannello - dimensioneIcona - margine, 
                            dimensioneIcona, dimensioneIcona, this);
            }

            // --- INFO CANZONE (Copertina e Titolo) ---
            int dimAlbum = 35;
            // Posizionamento in basso a destra
            int xMusica = larghezzaPannello - 500; 
            int yMusica = altezzaPannello - dimAlbum - 10;

            // Disegno copertina (se presente nella lista)
            if (indiceMusica < coverAlbums.size() && coverAlbums.get(indiceMusica) != null) {
                g.drawImage(coverAlbums.get(indiceMusica), xMusica, yMusica, dimAlbum, dimAlbum, this);
            }

            // Disegno Titolo Canzone
            g.setFont(font.deriveFont(16f));
            String titolo = nomeCanzoni[indiceMusica];
            
            // Testo principale
            g.setColor(new Color(220, 220, 220));
            g.drawString(titolo, xMusica + dimAlbum + 10, yMusica + 25);


            // --- DISEGNO PUNTEGGIO IN BASSO ---
            g.setFont(font.deriveFont(25f)); 
            g.setColor(Color.WHITE);

            String testoPunteggio = "PUNTEGGIO: " + punteggio; 
            FontMetrics fm = g.getFontMetrics();

            // Calcolo posizione originale
            int xPunteggio = (larghezzaPannello - fm.stringWidth(testoPunteggio)) / 2 - 200;
            int yPunteggio = altezzaPannello - 20; 

            // Ombra per leggibilità
            g.setColor(Color.BLACK);
            g.drawString(testoPunteggio, xPunteggio + 2, yPunteggio + 2);

            // Testo principale
            g.setColor(Color.WHITE); 
            g.drawString(testoPunteggio, xPunteggio, yPunteggio);
        }

        // 4. Overlay SCHERMATA HOME
        if (!giocoIniziato && !gameOver && !vittoria && !mostraComandi) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, larghezzaPannello, altezzaPannello);

            disegnaMessaggio(g, "BRICK BREAKER", Color.CYAN, 100, 280);

            int larghezzaB = 220;
            int altezzaB = 70;
            int xCentrale = (larghezzaPannello - larghezzaB) / 2;

            // Bottone GIOCA
            areaBottoneHome.setBounds(xCentrale - 130, (altezzaPannello / 2) + 30, larghezzaB, altezzaB);
            Image imgCorrenteGioca = giocaPremuto ? imgGiocaPressed : imgGioca;
            if (imgCorrenteGioca != null && imgCorrenteGioca.getWidth(null) != -1) {
                g.drawImage(imgCorrenteGioca, areaBottoneHome.x, areaBottoneHome.y, areaBottoneHome.width, areaBottoneHome.height, this);
            } else {
                g.setColor(Color.WHITE);
                g.fillRoundRect(areaBottoneHome.x, areaBottoneHome.y, areaBottoneHome.width, areaBottoneHome.height, 20, 20);
                g.setColor(Color.BLACK);
                g.drawString("GIOCA", areaBottoneHome.x + 60, areaBottoneHome.y + 25);
            }

            // Bottone COMANDI
            areaBottoneComandi.setBounds(xCentrale + 130, (altezzaPannello / 2) + 30, larghezzaB, altezzaB);
            Image imgCorrenteComandi = comandiPremuto ? imgComandiPressed : imgComandi;
            if (imgCorrenteComandi != null && imgCorrenteComandi.getWidth(null) != -1) {
                g.drawImage(imgCorrenteComandi, areaBottoneComandi.x, areaBottoneComandi.y, areaBottoneComandi.width, areaBottoneComandi.height, this);
            } else {
                g.setColor(new Color(200, 200, 200));
                g.fillRoundRect(areaBottoneComandi.x, areaBottoneComandi.y, areaBottoneComandi.width, areaBottoneComandi.height, 20, 20);
                g.setColor(Color.BLACK);
                g.drawString("COMANDI", areaBottoneComandi.x + 40, areaBottoneComandi.y + 25);
            }

            disegnaMessaggio(g, "RECORD: " + record, Color.WHITE, 50, 560);
        }

        // 5. Overlay SCHERMATA COMANDI
        if (mostraComandi) {
            g.setColor(new Color(0, 0, 0, 240));
            g.fillRect(0, 0, larghezzaPannello, altezzaPannello);
            disegnaMessaggio(g, "COMANDI", Color.YELLOW, 80, 250);

            int xT = (larghezzaPannello / 2) - 330;
            int yT = (altezzaPannello / 2) - 20;

            g.setColor(Color.WHITE);
            g.setFont(font.deriveFont(30f));  
            g.drawString("• FRECCE - WASD : Muovi Piattaforma", xT, yT);
            g.drawString("• ESC: Pausa / Riprendi", xT, yT + 50);
            g.drawString("• M: Mute Audio", xT, yT + 100);
            g.drawString("• Premendo l'icona del volume si muta l'audio", xT, yT + 150);
            g.drawString("• Premendo il tasto pausa il gioco va in pausa", xT, yT + 200);

            g.setColor(Color.GRAY);
            g.drawString("Clicca ovunque per tornare", (larghezzaPannello / 2) - 200, altezzaPannello - 80);
        }

        // 6. Overlay SCHERMATA PAUSA
        if (isPausa && !gameOver && !vittoria) {
            g.setColor(new Color(0, 0, 0, 215)); 
            g.fillRect(0, 0, larghezzaPannello, altezzaPannello);
            disegnaMessaggio(g, "PAUSA", Color.YELLOW, 100, 280);

            // --- Bottone PLAY (Centrale) ---
            int dimensionePlay = 80;
            areaBottonePlay.x = (larghezzaPannello - dimensionePlay) / 2 - 180;
            areaBottonePlay.y = (altezzaPannello / 2) + 10;
            areaBottonePlay.width = dimensionePlay;
            areaBottonePlay.height = dimensionePlay;

            // Sceglie l'immagine in base allo stato
            Image imgDaDisegnarePlay = playPremuto ? imgPlay_pressed : imgPlay;

            if (imgDaDisegnarePlay != null) {
                g.drawImage(imgDaDisegnarePlay, areaBottonePlay.x, areaBottonePlay.y, 
                            areaBottonePlay.width, areaBottonePlay.height, this);
            }

            // --- Bottone TORNA HOME (Posizione originale) ---
            int larghezzaH = 300;
            int altezzaH = 80;
            
            // Ripristino del tuo calcolo originale per la posizione X
            int xHome = (larghezzaPannello - larghezzaH) - 335; 
            int yHome = areaBottonePlay.y; // Stessa Y del bottone Play come prima
            
            areaBottoneTornaHome.setBounds(xHome, yHome, larghezzaH, altezzaH);
            
            Image imgCorrenteMainHome = tornaHomePremuto ? imgMainHomePressed : imgMainHome;
            
            if (imgCorrenteMainHome != null) {
                g.drawImage(imgCorrenteMainHome, areaBottoneTornaHome.x, areaBottoneTornaHome.y, 
                            areaBottoneTornaHome.width, areaBottoneTornaHome.height, this);
            }
        }

        // 7. Overlay SCHERMATA VITTORIA
        if (vittoria) {
            g.setColor(new Color(0, 0, 0, 215));
            g.fillRect(0, 0, larghezzaPannello, altezzaPannello);
            disegnaMessaggio(g, "VITTORIA!", Color.GREEN, 150,250);

            // Configuriamo il bottone (usiamo le stesse dimensioni della pausa se vuoi coerenza)
            int larghezzaReset = 300; 
            int altezzaReset = 80;
            areaBottone.setBounds((larghezzaPannello - larghezzaReset) / 2, (altezzaPannello / 2) -30 , larghezzaReset, altezzaReset);
            
            // Scegliamo l'immagine (usiamo quelle della home/pausa come hai chiesto)
            Image imgCorrenteReset = bottonePremuto ? imgMainHomePressed : imgMainHome;
            
            if (imgCorrenteReset != null) {
                g.drawImage(imgCorrenteReset, areaBottone.x, areaBottone.y, areaBottone.width, areaBottone.height, this);
            }

            disegnaMessaggio(g, "PUNTEGGIO ATTUALE: " + record, Color.WHITE, 40, 480);
            disegnaMessaggio(g, "RECORD: " + record, Color.WHITE, 40, 530);
        }

        // 8. Overlay SCHERMATA GAME OVER
        if (gameOver) {
            g.setColor(new Color(0, 0, 0, 215));
            g.fillRect(0, 0, larghezzaPannello, altezzaPannello);

            if (immagineGameOver != null) {
                //g.drawImage(immagineGameOver, 0, 0, larghezzaPannello, altezzaPannello, this);
            }

            disegnaMessaggio(g, "GAME OVER", Color.RED, 150, 250);

            // Configuriamo il bottone
            int larghezzaReset = 300;
            int altezzaReset = 80;
            areaBottone.setBounds((larghezzaPannello - larghezzaReset) / 2, (altezzaPannello / 2) -30 , larghezzaReset, altezzaReset);
            
            // Scegliamo l'immagine
            Image imgCorrenteReset = bottonePremuto ? imgMainHomePressed : imgMainHome;
            
            if (imgCorrenteReset != null) {
                g.drawImage(imgCorrenteReset, areaBottone.x, areaBottone.y, areaBottone.width, areaBottone.height, this);
            }

            disegnaMessaggio(g, "PUNTEGGIO ATTUALE: " + record, Color.WHITE, 40, 480);
            disegnaMessaggio(g, "RECORD: " + record, Color.WHITE, 40, 530);
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

    private void disegnaMessaggio(Graphics g, String testo, Color colore, int dimensione, int spostamentoY) {
        // .deriveFont(float size) cambia la dimensione del font caricato
        g.setFont(font.deriveFont((float)dimensione)); 
        
        FontMetrics fm = g.getFontMetrics();
        int x = (larghezzaPannello - fm.stringWidth(testo)) / 2;
        int y = spostamentoY;

        // Ombra
        g.setColor(Color.BLACK);
        g.drawString(testo, x + 3, y + 3);
        
        // Testo principale
        g.setColor(colore);
        g.drawString(testo, x, y);
    }

    public void iniziaPartita() {
        if (!gameOver && !vittoria) {
            //a inizio partita comincio con il svuotare le liste e creo la prima pallina
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

        //svuoto le varie liste
        listaBlocchi.clear();
        listaBonus.clear();
        listaPallineGrafiche.clear();
        listaPallineLogiche.clear();

        double larghezzaBlocco = 150;
        double altezzaBlocco = 25;
        //ricostruisco il campo di blocchi
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                double x = j * larghezzaBlocco;
                double y = i * altezzaBlocco;
                listaBlocchi.add(new BloccoGrafico(new BloccoLogico(new Punto(x, y), altezzaBlocco, larghezzaBlocco)));
            }
        }

        punteggio=0;
        
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
   

    public Rectangle getAreaBottoneComandi() { return areaBottoneComandi; }
    public boolean isMostraComandi() { return mostraComandi; }
    public void setMostraComandi(boolean b) { this.mostraComandi = b; }
    public void setGiocaPremuto(boolean b) { this.giocaPremuto = b; }
    public boolean isGiocaPremuto() { return giocaPremuto; }
    public void setComandiPremuto(boolean b) { this.comandiPremuto = b; }
    public boolean isComandiPremuto() { return comandiPremuto; }
    public Rectangle getAreaBottoneTornaHome() { return areaBottoneTornaHome; }
    public void setTornaHomePremuto(boolean b) { this.tornaHomePremuto = b; }
    public boolean isTornaHomePremuto() { return tornaHomePremuto; }
    public void setPlayPremuto(boolean b) { this.playPremuto = b; }
    public boolean isPlayPremuto() { return playPremuto; }

    private void salvaRecord() {
        try (java.io.PrintWriter out = new java.io.PrintWriter(new java.io.FileWriter(FILE_RECORD))) {
            out.println(record);
        } catch (java.io.IOException e) {
            System.out.println("Errore nel salvataggio del record: " + e.getMessage());
        }
    }

    private void caricaRecord() {
        java.io.File file = new java.io.File(FILE_RECORD);
        if (!file.exists()) {
            record = 0; // Se il file non esiste, il record è 0
            return;
        }
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String linea = br.readLine();
            if (linea != null) {
                record = Integer.parseInt(linea.trim());
            }
        } catch (Exception e) {
            record = 0;
        }
    }
}