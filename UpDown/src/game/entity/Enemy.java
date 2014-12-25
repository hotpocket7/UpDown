package game.entity;

import game.Game;

import java.awt.Color;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Enemy extends Entity {
	public enum MovementType {
		STRAIGHT, SINE;
		public static MovementType getRandomMovementType(){
			return MovementType.values()[Game.random.nextInt(MovementType.values().length)];
		}
	}
	
	public static Queue<Enemy> enemies = new ConcurrentLinkedQueue<Enemy>();
	
	MovementType movementType;
	int amplitude, period;
	
	public Color color;
	
	public Enemy(int x, int y, int width, int height, int xVel, MovementType movementType){
		super(x, y, width, height, xVel, 0);
		this.xVel = xVel;
		this.movementType = movementType;
		switch(movementType){
			case SINE: //Enemy moves in sine curve
				amplitude = Game.random.nextInt(Game.HEIGHT/2 - height - (Game.HEIGHT/8 - height)) + Game.HEIGHT/8 - height;
				yInitial = Game.random.nextInt( Game.HEIGHT - 2 * amplitude - height ) + amplitude; // Make sure the enemy is positioned such that it doesn't move off the screen vertically
				period = Game.random.nextInt(500) + 1000;
			default:
				color = new Color(0xAA0000);
		}
	}
	
	public void update(){
		super.update();
		switch(movementType){
			case SINE: //Enemy moves in sine curve
				y = (int) (yInitial + amplitude * Math.sin(System.currentTimeMillis() * 2 * Math.PI / period));
			default:
		}
		if((x + width < 0 && xVel < 0) || (x > Game.WIDTH && xVel > 0)){
			enemies.remove(this);
		}
	}
	
}
