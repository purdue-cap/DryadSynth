public class Boom {
	public int x;
	public int y = 830;
	private int dy = 2;
	public boolean isVisiable = false;
	
	public void reset(int x){
		this.x = x+50;
		y = 830;
		isVisiable = true;
	}
	
	public void update(){
		if(y > 10){
			y -= dy;
		}else{
			isVisiable = false;
		}
	}
}
