package space;

import javax.swing.ImageIcon;

public class Shot extends Setters_Getters {

    private final String shotImg = "src/images/shot.png";

    public Shot() {
    }

    public Shot(int x, int y) {

        initShot(x, y);
    
    }

    private void initShot(int x, int y) {

        ImageIcon ii = new ImageIcon(shotImg);
        setImage(ii.getImage());
        
        setX(x);
        setY(y);
    }

	public Shot get(int i) {
		// TODO Auto-generated method stub //mmmhhhh
		return null;
	}
}