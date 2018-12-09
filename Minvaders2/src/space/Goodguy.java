package space;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Goodguy extends Setters_Getters {


    private final String GoodguyImg = "src/images/Player2.png";
    private int width;

    public Goodguy() {

        initGoodguy();
    }

    private void initGoodguy() {
        
        ImageIcon ii = new ImageIcon(GoodguyImg);

        width = ii.getImage().getWidth(null);

        setImage(ii.getImage());
        setX(250);
        setY(700); //start position
    }

    public void act() {
        
        x += dx;
        
        if (x <= 2) {
            x = 2;
        }
        
        if (x >= 1000 - 50 ) { // borders
            x = 1000 - 50 ;
        }
    }

    public void keyPressed(KeyEvent e) {
        
        int key = e.getKeyCode();

        if (key == 37) { //keypressed 37 = left
        
            dx = -5; //speed of bullet
        }

        if (key == 39) {
        
            dx = 5; 
        }
    }

    public void keyReleased(KeyEvent e) {
        
        int key = e.getKeyCode();

        if (key == 37) {
        
            dx = 0; //speed when stopped
        }

        if (key == 39) {
        
            dx = 0;
        }
    }
}