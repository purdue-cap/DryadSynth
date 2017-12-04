import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Invader {
	private Image[][] invadersIm;
	private int rowsOfInvaders;
	private int columnsOfInvaders;
	private int dx;
	private int dy;
	private int rateInvadersFire;
	public boolean[][] isAlive;
	public int x = 0;
	public int y = 0;
	private boolean moveRight = true;
	private int control = 0;
	public int scaledSize;
	public int[] aliveNum;
	
	public Invader(Image[][] invaders, int rowsOfInvaders, int columnsOfInvaders, int rateInvadersMove, int rateInvadersDropDown, int rateInvadersFire){
		invadersIm = invaders;
		this.rowsOfInvaders = rowsOfInvaders;
		this.columnsOfInvaders = columnsOfInvaders;
		this.dx = rateInvadersMove;
		this.dy = rateInvadersDropDown;
		this.rateInvadersFire = rateInvadersFire;
		isAlive = new boolean[rowsOfInvaders][columnsOfInvaders];
		for(boolean[] a: isAlive){
			for(int i = 0; i < a.length; i++){
				a[i] = true;
			}
		}
		for(Image[] a: invaders){
			for(int i = 0; i < a.length; i++){
				a[i] = resizeImage(a[i]);
			}
		}
		aliveNum = new int[rowsOfInvaders];
		for(int i = 0; i < rowsOfInvaders; i++){
			aliveNum[i] = columnsOfInvaders;
		}
	}
	
	public Graphics draw(Graphics g){
		for(int i = 0; i < rowsOfInvaders; i++){
			for(int j = 0; j < columnsOfInvaders; j++){
				if(isAlive[i][j]){
					g.drawImage(invadersIm[0][i%6], x+(int)1200/columnsOfInvaders*j+20 , y+(int)500/rowsOfInvaders*i+20, null);
				}
			}
		}
		if(control == 0){
			move();
		}
		control = ++control % dx;
		return g;
	}
	
	public void move(){
		int max = 0;
		for(int i = 0; i < rowsOfInvaders; i++){
			if(max < aliveNum[i]){
				max = aliveNum[i];
			}
		}
		if (x+1200/columnsOfInvaders*(columnsOfInvaders-1)+ scaledSize < 1450 && moveRight){
			x += dx;
		}else if (x > 0 && !moveRight){
			x -= dx;
		}else{
			y += dy;
			if(moveRight){
				x = 1450-1200/columnsOfInvaders*(columnsOfInvaders-1) - scaledSize;
				moveRight = false;
			}else{
				x = 0;
				moveRight = true;
			}
		}
	}
	
	public Image resizeImage(Image image){
		scaledSize = Math.min(1200/columnsOfInvaders-10, 500/rowsOfInvaders -10);
		return image.getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
    }
}
