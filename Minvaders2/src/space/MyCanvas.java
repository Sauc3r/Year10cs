package space;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Timer;
import java.util.TimerTask;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;



public class MyCanvas extends JPanel implements Runnable {

    /**
	 * 
	 */

	private Dimension d;
    private ArrayList<Badguy> Badguys;
    private Goodguy Goodguy;
    private Shot shot;

    private final int Badguy_INIT_X = 150;
    private final int Badguy_INIT_Y = 5;
    private int direction = -1; 
    private int deaths = 0;
 
    private boolean ingame = true;
    private String message1 = "You Lost";
    private Thread animator;

    public MyCanvas() {
    	playIt("Files/008.wav");
        initBoard();
    }
    public interface Screen
    {
        void update();
        void draw();
        void handleInput();
    }
    private void initBoard() { //ground

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(1000, 750);
        setBackground(Color.GREEN);  

        gameInit();
        setDoubleBuffered(true);
    	
    }


    

    public void gameInit() {

        Badguys = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) { //amount of aliens

                Badguy Badguy = new Badguy(Badguy_INIT_X + 18 * j, Badguy_INIT_Y + 18 * i);
                Badguys.add(Badguy);
            }
        }

        Goodguy = new Goodguy();
        shot = new Shot();

        if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
        }
    }
    
    

    public void drawBadguys(Graphics g) {

        for (Badguy Badguy: Badguys) {

            if (Badguy.isVisible()) {

                g.drawImage(Badguy.getImage(), Badguy.getX(), Badguy.getY(), this);
            }

            if (Badguy.isDying()) {

                Badguy.die();
            }
        }
    }

    public void drawGoodguy(Graphics g) {

        if (Goodguy.isVisible()) {
            
            g.drawImage(Goodguy.getImage(), Goodguy.getX(), Goodguy.getY(), this);
        }

        if (Goodguy.isDying()) {

            Goodguy.die();
            ingame = false;
        }
    }

    public void drawShot(Graphics g) {

        if (shot.isVisible()) {
            
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    public void drawBombing(Graphics g) {

        for (Badguy a : Badguys) {
            
            Badguy.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {
                
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }
    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.blue); 
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (ingame) {

            g.drawLine(0, 750, 1000, 750);
            drawBadguys(g);
            drawGoodguy(g);
            drawShot(g);
            drawBombing(g);
            drawDeathslabel(g);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    private void drawDeathslabel(Graphics g) {
        Font small = new Font("Helvetica", Font.BOLD, 36); //importing the fonts and font sizes
        g.setFont(small);
        g.drawString("TotalKills: " + deaths, (100),100);
        g.drawRect(90, 65, 250, 50);
        
    }
		
	
 
	

	
    
    public void gameOver() {
 //game-over screen
        Graphics g = this.getGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, 1000, 1000);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, 1000 / 2 - 30, 1000 - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, 1000 / 2 - 30, 1000 - 100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 36); //importing the fonts and font sizes
        
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message1, (400),500);
        
    }
   
        
    
    public void animationCycle() {

        if (deaths == 24) { //# of badguys to destroy

            ingame = false;
            message1 = "Good Job Jimmy";
        }

        // Goodguy
        Goodguy.act();

        // shot
        if (shot.isVisible()) {

            int shotX = shot.getX();
            int shotY = shot.getY();

            for (Badguy Badguy: Badguys) {

                int BadguyX = Badguy.getX();
                int BadguyY = Badguy.getY();

                if (Badguy.isVisible() && shot.isVisible()) {
                    if (shotX >= (BadguyX)
                            && shotX <= (BadguyX + 30)
                            && shotY >= (BadguyY)
                            && shotY <= (BadguyY + 30)) { //size of target when hit
                        Badguy.setDying(true);
                        deaths++; //adds the counter for the amount of kills
                        shot.die();
                    }
                }
            }

            int y = shot.getY();
            y -= 12;   //Speed of the shot

            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        // Badguys

        for (Badguy Badguy: Badguys) {

            int x = Badguy.getX();

            if (x >= 1000 - 25 && direction != -1) {

                direction = -3;  //speed of the aliens
                Iterator<Badguy> i1 = Badguys.iterator();

                while (i1.hasNext()) {

                    Badguy a2 = (Badguy) i1.next();
                    a2.setY(a2.getY() + 50); //distance down
                }
            }

            if (x <= 5 && direction != 1) {

                direction = 3;

                Iterator<Badguy> i2 = Badguys.iterator();

                while (i2.hasNext()) {

                    Badguy a = (Badguy) i2.next();
                    a.setY(a.getY() + 5);   //distance the alien goes down
                }
            }
        }

        Iterator<Badguy> it = Badguys.iterator();

        while (it.hasNext()) {
            
            Badguy Badguy = (Badguy) it.next();
            
            if (Badguy.isVisible()) {

                int y = Badguy.getY();

                if (y > 750 - 30) {
                    ingame = false;
                    message1 = "You Lose!";
                }

                Badguy.act(direction);
            }
        }

        // bombs
        Random generator = new Random();

        for (Badguy Badguy: Badguys) {

            int shot = generator.nextInt(15);
            Badguy.Bomb b = Badguy.getBomb();

            if (shot == 5 && Badguy.isVisible() && b.isDestroyed()) { //how often the shot is taken

                b.setDestroyed(false);
                b.setX(Badguy.getX());
                b.setY(Badguy.getY());
            }

            int bombX = b.getX();
            int bombY = b.getY();
            int GoodguyX = Goodguy.getX(); //position of the ship
            int GoodguyY = Goodguy.getY();

            if (Goodguy.isVisible() && !b.isDestroyed()) {

                if (bombX >= (GoodguyX)
                        && bombX <= (GoodguyX + 50)
                        && bombY >= (GoodguyY)           //bomb detection
                        && bombY <= (GoodguyY + 15)) {
                    Goodguy.setDying(true);
                    b.setDestroyed(true);
                }
            }

            if (!b.isDestroyed()) {
                
                b.setY(b.getY() + 1);
                
                if (b.getY() >= 750 - 0) {
                    b.setDestroyed(true); //gets rid of bomb when it hits the ground
                }
            }
        }
    }
	public void playIt(String filename) {

		try {

			InputStream in = new FileInputStream(filename);

			AudioStream as = new AudioStream(in);

			AudioPlayer.player.start(as);

			

		} catch (IOException e) {

			System.out.println(e);

		}

	}

    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (ingame) {

            repaint();
            animationCycle();

            timeDiff = System.currentTimeMillis() - beforeTime; //aliens moving down
            sleep = 10 - timeDiff;

            if (sleep < 0) { //timing is everything
                sleep = 500;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");  
            }
            
            beforeTime = System.currentTimeMillis();
        }

        gameOver();
    }

    
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            Goodguy.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            Goodguy.keyPressed(e);

            int x = Goodguy.getX();
            int y = Goodguy.getY();

            int key = e.getKeyCode(); //creates new shot, with space bar

            if (key == 32) {
                
                if (ingame) {
                	
                        shot = new Shot(x, y);
                        
                    }
                }
            }
    }
        }






