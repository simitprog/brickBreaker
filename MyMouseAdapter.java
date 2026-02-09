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
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pannelloSuCuiLavorare.requestFocusInWindow();
        Point click = e.getPoint();

        // --- 1. PRIORITÀ: SCHERMATA COMANDI ---
        // Se i comandi sono aperti, qualsiasi click li chiude e interrompe il metodo
        if (pannelloSuCuiLavorare.isMostraComandi()) {
            pannelloSuCuiLavorare.setMostraComandi(false);
            pannelloSuCuiLavorare.repaint();
            return; // Esci subito, non controllare altri bottoni
        }

        // --- 2. PAUSA ATTIVA ---
        if (pannelloSuCuiLavorare.isPausa() && !pannelloSuCuiLavorare.isGameOver() && !pannelloSuCuiLavorare.isVittoria()) {
            Rectangle areaPlayCentrale = pannelloSuCuiLavorare.getAreaBottonePlay();
            if (areaPlayCentrale != null && areaPlayCentrale.contains(click)) {
                pannelloSuCuiLavorare.setPausa(false);
                return; 
            }
        }

        // --- 3. VOLUME (Sempre cliccabile se non siamo nei comandi) ---
        int dimVol = 40;
        int margVol = 10;
        Rectangle areaVolume = new Rectangle(margVol, pannelloSuCuiLavorare.getHeight() - dimVol - margVol, dimVol, dimVol);
        if (areaVolume.contains(click)) {
            pannelloSuCuiLavorare.toggleMute();
            return;
        }

        // --- 4. GESTIONE HOME (Solo se il gioco NON è iniziato) ---
        if (!pannelloSuCuiLavorare.isGiocoIniziato() && !pannelloSuCuiLavorare.isGameOver() && !pannelloSuCuiLavorare.isVittoria()) {
            // Click su GIOCA
            if (pannelloSuCuiLavorare.getAreaBottoneHome().contains(click)) {
                pannelloSuCuiLavorare.iniziaPartita();
                pannelloSuCuiLavorare.repaint();
                return;
            }
            
            // Click su COMANDI
            if (pannelloSuCuiLavorare.getAreaBottoneComandi().contains(click)) {
                pannelloSuCuiLavorare.setMostraComandi(true);
                pannelloSuCuiLavorare.repaint();
                return;
            }
        }

        // --- 5. GIOCO IN CORSO (Pausa) ---
        if (pannelloSuCuiLavorare.isGiocoIniziato() && !pannelloSuCuiLavorare.isPausa() && 
            !pannelloSuCuiLavorare.isGameOver() && !pannelloSuCuiLavorare.isVittoria()) {
            
            Rectangle areaPausa = pannelloSuCuiLavorare.getAreaBottonePausa();
            if (areaPausa != null && areaPausa.contains(click)) {
                pannelloSuCuiLavorare.setPausa(true);
                return; 
            }
        }

        // --- 6. FINE PARTITA (Reset) ---
        if (pannelloSuCuiLavorare.isGameOver() || pannelloSuCuiLavorare.isVittoria()) {
            Rectangle areaReset = pannelloSuCuiLavorare.getAreaBottone();
            if (areaReset != null && areaReset.contains(click)) {
                pannelloSuCuiLavorare.setBottonePremuto(true); 
                pannelloSuCuiLavorare.repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if ((pannelloSuCuiLavorare.isGameOver() || pannelloSuCuiLavorare.isVittoria()) 
            && pannelloSuCuiLavorare.isBottonePremuto()) {
            
            Rectangle area = pannelloSuCuiLavorare.getAreaBottone();
            if (area != null && area.contains(e.getPoint())) {
                pannelloSuCuiLavorare.resetGioco();
            }
            
            pannelloSuCuiLavorare.setBottonePremuto(false);
            pannelloSuCuiLavorare.repaint();
        }
    }
}