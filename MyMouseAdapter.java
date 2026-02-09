import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Rectangle;
import java.awt.Point;

public class MyMouseAdapter extends MouseAdapter {

    private MyPanel pannelloSuCuiLavorare;

    public MyMouseAdapter(MyPanel p) {
        this.pannelloSuCuiLavorare = p;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pannelloSuCuiLavorare.requestFocusInWindow();
        Point click = e.getPoint();

        // --- 1. SCHERMATA COMANDI ---
        if (pannelloSuCuiLavorare.isMostraComandi()) {
            pannelloSuCuiLavorare.setMostraComandi(false);
            pannelloSuCuiLavorare.repaint();
            return;
        }

        // --- 2. SCHERMATA PAUSA ---
        if (pannelloSuCuiLavorare.isPausa() && !pannelloSuCuiLavorare.isGameOver() && !pannelloSuCuiLavorare.isVittoria()) {
            // Bottone Torna Home
            if (pannelloSuCuiLavorare.getAreaBottoneTornaHome().contains(click)) {
                pannelloSuCuiLavorare.setTornaHomePremuto(true);
                pannelloSuCuiLavorare.repaint();
                return;
            }
            // NUOVO: Bottone Play (pressione)
            if (pannelloSuCuiLavorare.getAreaBottonePlay().contains(click)) {
                pannelloSuCuiLavorare.setPlayPremuto(true); // Cambia stato
                pannelloSuCuiLavorare.repaint();
                return;
            }
        }

        // --- 3. VOLUME (Sempre attivo se non in comandi) ---
        int dimVol = 40;
        int margVol = 10;
        Rectangle areaVolume = new Rectangle(margVol, pannelloSuCuiLavorare.getHeight() - dimVol - margVol, dimVol, dimVol);
        if (areaVolume.contains(click)) {
            pannelloSuCuiLavorare.toggleMute();
            return;
        }

        // --- 4. SCHERMATA HOME (Gioco non iniziato) ---
        if (!pannelloSuCuiLavorare.isGiocoIniziato() && !pannelloSuCuiLavorare.isGameOver() && !pannelloSuCuiLavorare.isVittoria()) {
            if (pannelloSuCuiLavorare.getAreaBottoneHome().contains(click)) {
                pannelloSuCuiLavorare.setGiocaPremuto(true);
                pannelloSuCuiLavorare.repaint();
                return;
            }
            if (pannelloSuCuiLavorare.getAreaBottoneComandi().contains(click)) {
                pannelloSuCuiLavorare.setComandiPremuto(true);
                pannelloSuCuiLavorare.repaint();
                return;
            }
        }

        // --- 5. GIOCO IN CORSO (Click su icona Pausa) ---
        if (pannelloSuCuiLavorare.isGiocoIniziato() && !pannelloSuCuiLavorare.isPausa() && 
            !pannelloSuCuiLavorare.isGameOver() && !pannelloSuCuiLavorare.isVittoria()) {
            
            if (pannelloSuCuiLavorare.getAreaBottonePausa().contains(click)) {
                pannelloSuCuiLavorare.setPausa(true);
                return; 
            }
        }

        // --- 6. FINE PARTITA (Reset) ---
        if (pannelloSuCuiLavorare.isGameOver() || pannelloSuCuiLavorare.isVittoria()) {
            if (pannelloSuCuiLavorare.getAreaBottone().contains(click)) {
                pannelloSuCuiLavorare.setBottonePremuto(true); 
                pannelloSuCuiLavorare.repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point rilascio = e.getPoint();

        // Rilascio Tasto GIOCA
        if (pannelloSuCuiLavorare.isGiocaPremuto()) {
            pannelloSuCuiLavorare.setGiocaPremuto(false);
            if (pannelloSuCuiLavorare.getAreaBottoneHome().contains(rilascio)) {
                pannelloSuCuiLavorare.iniziaPartita();
            }
            pannelloSuCuiLavorare.repaint();
        }

        // Rilascio Tasto COMANDI
        if (pannelloSuCuiLavorare.isComandiPremuto()) {
            pannelloSuCuiLavorare.setComandiPremuto(false);
            if (pannelloSuCuiLavorare.getAreaBottoneComandi().contains(rilascio)) {
                pannelloSuCuiLavorare.setMostraComandi(true);
            }
            pannelloSuCuiLavorare.repaint();
        }

        // Rilascio Tasto TORNA HOME (In Pausa)
        if (pannelloSuCuiLavorare.isTornaHomePremuto()) {
            pannelloSuCuiLavorare.setTornaHomePremuto(false);
            if (pannelloSuCuiLavorare.getAreaBottoneTornaHome().contains(rilascio)) {
                pannelloSuCuiLavorare.setPausa(false); 
                pannelloSuCuiLavorare.resetGioco();    
                pannelloSuCuiLavorare.stopSoundtrack(); 
            }
            pannelloSuCuiLavorare.repaint();
        }

        // Rilascio Tasto RESET (Game Over / Vittoria)
        if (pannelloSuCuiLavorare.isBottonePremuto()) {
            pannelloSuCuiLavorare.setBottonePremuto(false);
            if (pannelloSuCuiLavorare.getAreaBottone().contains(rilascio)) {
                pannelloSuCuiLavorare.resetGioco();
            }
            pannelloSuCuiLavorare.repaint();
        }

        if (pannelloSuCuiLavorare.isPlayPremuto()) {
            pannelloSuCuiLavorare.setPlayPremuto(false); // Reset stato
            if (pannelloSuCuiLavorare.getAreaBottonePlay().contains(rilascio)) {
                pannelloSuCuiLavorare.setPausa(false); // Riprende il gioco
            }
            pannelloSuCuiLavorare.repaint();
        }
    }
}