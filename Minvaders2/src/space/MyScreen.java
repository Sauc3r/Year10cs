package space;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class MyScreen extends JFrame {


	public MyScreen() {

        initUI();
    }

    private void initUI() {

        add(new MyCanvas());
        setTitle("Space Invaders");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 1000);
      
    }

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            MyScreen ex = new MyScreen();
            ex.setVisible(true);
        });
    }
}
