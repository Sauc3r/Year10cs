package space;

import javax.swing.ImageIcon;

public class Badguy extends Setters_Getters {

    private Bomb bomb;
    private final String BadguyImg = "src/images/invader2.png";

    public Badguy(int x, int y) {

        initBadguy(x, y);
    }

    private void initBadguy(int x, int y) {

        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);
        ImageIcon ii = new ImageIcon(BadguyImg);
        setImage(ii.getImage());
    }

    public void act(int direction) {
        
        this.x += direction;
    }

    public Bomb getBomb() {
        
        return bomb;
    }

    public class Bomb extends Setters_Getters {

        private final String bombImg = "src/images/bomb.png";
        private boolean destroyed;

        public Bomb(int x, int y) {

            initBomb(x, y);
        }

        private void initBomb(int x, int y) {

            setDestroyed(true);
            this.x = x;
            this.y = y;
            ImageIcon ii = new ImageIcon(bombImg);
            setImage(ii.getImage());

        }

        public void setDestroyed(boolean destroyed) {
        
            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {
        
            return destroyed;
        }
    }
}