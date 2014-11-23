package game.entity;

//import game.Game;

import java.awt.Rectangle;

public class Entity {
	public int x, y, xInitial, yInitial, width, height, xVel, yVel;
	public Rectangle bounds;
	
	public Entity(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		xInitial = x;
		yInitial = y;
		this.width = width;
		this.height = height;
		xVel = 0;
		yVel = 0;
		bounds = new Rectangle(x, y, width, height);
	}
	
	public Entity(int x, int y, int width, int height, int xVel, int yVel){
		this.x = x;
		this.y = y;
		xInitial = x;
		yInitial = y;
		this.width = width;
		this.height = height;
		this.xVel = xVel;
		this.yVel = yVel;
		bounds = new Rectangle(x, y, width, height);
	}
	
	public void update(){
		bounds.setLocation(x, y);
		x += xVel;
		y += yVel;
	}
}
