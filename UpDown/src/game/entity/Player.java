package game.entity;

import game.Game;

import java.awt.Rectangle;

public class Player extends Entity {

	public enum State{
		ALIVE, DYING
	}
	
	public int speed = 6;
	State state;
	
	public Player(int x, int y, int width, int height){
		super(x, y, width, height);
		xVel = 0;
		yVel = 10;
		state = State.ALIVE;
		bounds = new Rectangle(x, y, width, height);
	}
	
	public void update(){
		if(y + height + yVel > Game.HEIGHT || y + yVel < 0) yVel *= -1;
		if(x + width + xVel > Game.WIDTH || x + xVel < 0) xVel = 0;
		
		super.update();
	}
	
}
