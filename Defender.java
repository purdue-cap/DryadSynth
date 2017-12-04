import java.awt.*;

public class Defender{
	public Image defender;
	public int x = 650;
	public int y = 850;
	public int dx = 2;
	private int lives;
	private Image boom;
	public Boom[] booms;
	private boolean moveRight;
	public boolean isMoving = false;
	public boolean isAlive = true;

	public Defender(Image defenderIm, Image boomIm, int defenderLives){
		boom = boomIm;
		defender = defenderIm;
		lives = defenderLives;
		booms = new Boom[]{new Boom(),new Boom(),new Boom(),new Boom(),new Boom(),new Boom(),new Boom(),new Boom(),new Boom(),new Boom()};
		
	}
	public void reset(){
		lives--;
		x = 650;
		dx = 0;
	}

	public void move(boolean moveRight) {
		dx = 2;
		this.moveRight = moveRight;
		isMoving = true;

	}

	public void shoot(){
		for (int i=0; i<10; i++) {
			if(!(booms[i].isVisiable)){
				booms[i].reset(x);
				booms[i].update();
				return;
			}
        }
	}
	
	public Graphics draw(Graphics g){
		for (Boom b:booms) {
			if(b.isVisiable){
				g.drawImage(boom, b.x, b.y, null);
				b.update();
			}
		}
		return g;
	}

	public void update(){
		if((x+defender.getWidth(null) > 1480 && moveRight) || (x < 20 && !moveRight)){
			return;
		}
		if(isMoving){
			if(moveRight)
				x += dx;
			else
				x -= dx;
		}
	}
}
