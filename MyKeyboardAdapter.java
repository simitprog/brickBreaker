import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @class MyKeyboardAdapter
 * @brief Gestisce l'input da tastiera per il gioco
 */
public class MyKeyboardAdapter extends KeyAdapter {

    private MyPanel pannelloSuCuiLavorare;

    public MyKeyboardAdapter(MyPanel p) {
        this.pannelloSuCuiLavorare = p;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // 1. Avvio partita
        if (key == KeyEvent.VK_ENTER) {
            pannelloSuCuiLavorare.iniziaPartita();
        }

        // 2. Reset del gioco
        if (key == KeyEvent.VK_R && pannelloSuCuiLavorare.getCanReload()) {
            
            pannelloSuCuiLavorare.resetGioco();
        }

        // 3. Controlli Musica
        if (key == KeyEvent.VK_H) {
            pannelloSuCuiLavorare.prossimaCanzone();
        }
        if (key == KeyEvent.VK_M) {
            pannelloSuCuiLavorare.toggleMute();
        }

        // 4. Gestione PAUSA (Corretto l'errore della variabile 'p' in 'pannelloSuCuiLavorare')
        if (key == KeyEvent.VK_ESCAPE || key == KeyEvent.VK_P) {
            if (pannelloSuCuiLavorare.isGiocoIniziato() && 
                !pannelloSuCuiLavorare.isGameOver() && 
                !pannelloSuCuiLavorare.isVittoria()) {
                
                pannelloSuCuiLavorare.setPausa(!pannelloSuCuiLavorare.isPausa());
            }
        }

        // 5. Movimento racchetta (Aggiunto controllo !isPausa)
        giocatoreLogico g = pannelloSuCuiLavorare.getGiocatoreLogico();
        if (g != null && !pannelloSuCuiLavorare.isGameOver() && !pannelloSuCuiLavorare.isPausa()) {
            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) g.muoviSinistra();
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) g.muoviDestra();
        }
    }
}