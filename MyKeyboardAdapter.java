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

        //avvio partita
        if (key == KeyEvent.VK_ENTER) {
            pannelloSuCuiLavorare.iniziaPartita();
        }

        //reset del gioco
        if (key == KeyEvent.VK_R) {
            pannelloSuCuiLavorare.resetGioco();
        }

        //per la musica
        if (key == KeyEvent.VK_H) {
            pannelloSuCuiLavorare.prossimaCanzone();
        }

        if (key == KeyEvent.VK_M) {
            pannelloSuCuiLavorare.toggleMute();
        }

        //movimento racchetta
        giocatoreLogico g = pannelloSuCuiLavorare.getGiocatoreLogico();
        if (g != null && !pannelloSuCuiLavorare.isGameOver()) {
            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) g.muoviSinistra();
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) g.muoviDestra();
        }
    }
}