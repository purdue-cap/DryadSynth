import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;


public class GameManager extends JFrame implements KeyListener{

	public int rowsOfInvaders = 6;
	public int columnsOfInvaders = 8;
	public int rateInvadersMove = 3;
	public int rateInvadersDropDown = 10;
	public int rateInvadersFire = 1;
	public int defenderLives = 3;

	private Image bg;
	private Image boomIm;
	private boolean isReleaseds = true;
	public Defender defender;
	private Image[][] invaders;
	private Invader invader;

	public GameManager(){
		super("Space Invader");
		bg = loadImage("src/images/bg.jpg");
		new Thread(){  
			public void run(){  
				while(true){  
					repaint();  
					try {  
						Thread.sleep(rateInvadersMove);  
					} catch (InterruptedException e) { }  
				}  
			}  
		}.start();
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_SPACE && isReleaseds){
			defender.shoot();
			isReleaseds = false;
		}
		if (keyCode == KeyEvent.VK_LEFT ) {
			defender.move(false);
		}
		if(keyCode == KeyEvent.VK_RIGHT ){
			defender.move(true);
		}
		e.consume();
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_LEFT) {
			defender.isMoving = false;
		}
		if(keyCode == KeyEvent.VK_RIGHT){
			defender.isMoving = false;
		}
		if(keyCode == KeyEvent.VK_SPACE){
			isReleaseds = true;
		}
		e.consume();
	}

	public void keyTyped(KeyEvent e) {e.consume();}

	public void loadDefender(){
		Image defenderIm = loadImage("src/png/ship.png");
		boomIm = loadImage("src/images/boom.png");
		defender = new Defender(defenderIm, boomIm, defenderLives);
	}
	
	public void loadInvader(){
		invaders = new Image[2][];
		invaders[0]= new Image[] {
				loadImage("src/png/Series 1/1.png"),
				loadImage("src/png/Series 1/2.png"),
				loadImage("src/png/Series 1/3.png"),
				loadImage("src/png/Series 1/4.png"),
				loadImage("src/png/Series 1/5.png"),
				loadImage("src/png/Series 1/6.png"),
		};
		invaders[1]= new Image[] {
				loadImage("src/png/Series 1/1f.png"),
				loadImage("src/png/Series 1/2f.png"),
				loadImage("src/png/Series 1/3f.png"),
				loadImage("src/png/Series 1/4f.png"),
				loadImage("src/png/Series 1/5f.png"),
				loadImage("src/png/Series 1/6f.png"),
		};
		invader = new Invader(invaders, rowsOfInvaders, columnsOfInvaders, rateInvadersMove, rateInvadersDropDown, rateInvadersFire);
	}

	public Image loadImage(String name) {
		return new ImageIcon(name).getImage();
	}

	public void run(){
		this.setSize(1500,1000);  
		this.setTitle("Space Invader");  
		this.setResizable(false);  
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(3); 
		loadInvader();
		loadDefender();
		this.setVisible(true); 
		this.addKeyListener(this);
	}

	public void paint(Graphics g) {
		if (g instanceof Graphics2D) {
			 Graphics2D g2 = (Graphics2D) g;
			 g2.setRenderingHint(
			 RenderingHints.KEY_TEXT_ANTIALIASING,
			 RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}
		
		checkCollision(invader, defender.booms);
		
		BufferedImage bi =(BufferedImage)this.createImage(this.getSize().width,this.getSize().height);  
		Graphics big =bi.getGraphics();  
		big.drawImage(bg, 0, 0, null);
		defender.update();
		big.drawImage(defender.defender, defender.x, defender.y, null);
		big = defender.draw(big);
		big = invader.draw(big);
		g.drawImage(bi,0,0,null);
	}
	
	public void checkCollision(Invader invaders, Boom[] booms){
		for(int i = 0; i < rowsOfInvaders; i++){
			for(int j = 0; j < columnsOfInvaders; j++){
				for(int k = 0; k < booms.length; k++){
					if(invaders.isAlive[i][j] && booms[k].isVisiable && isCollision(invaders.x+(int)1200/columnsOfInvaders*j+20, invaders.y+(int)500/rowsOfInvaders*i+20,
							invaders.scaledSize, invaders.scaledSize, booms[k].x, booms[k].y, 50, 50)){
						booms[k].isVisiable = false;
						invaders.isAlive[i][j] = false;
					}
				}
			}
		}
	}
	
	public boolean isCollision(int x1, int y1,int width1, int height1, int x2, int y2,int width2, int height2){
		return (x1 < x2 + width2 && x2 < x1 + width1 && y1 < y2 + height2 && y2 < y1 + height1);
	}
}