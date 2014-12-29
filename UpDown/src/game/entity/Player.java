package game.entity;

import game.Game;

import java.awt.Color;
import java.awt.Rectangle;

public class Player extends Entity {

	public enum State{
		ALIVE, DYING
	}
	
	public int xVelocity         = 6;
	public int yVelocity		 = 10;
	public int health            = 2;
	public int invincibilityTime = 0;
	public Color color;
	
	public boolean isInvincible = false;
	public boolean isVisible = true;
	State state;
	
	public Player(int x, int y, int width, int height){
		super(x, y, width, height);
		xVel = 0;
		yVel = yVelocity;
		state = State.ALIVE;
		bounds = new Rectangle(x, y, width, height);
		color = Color.BLACK;
	}
	
	public void setInvincibleTime(int ms){
		
	}
	
	public void die(){
		Game.setGameState(Game.GameState.DEAD);
		invincibilityTime = 0;
		isInvincible = false;
		health = 0;
		Game.restart();
	}
	
	public void takeDamage(int damage){
		if(isInvincible)
			return;
		if((health -= damage) <= 0){
			die();
			return;
		}
		isInvincible = true;
		invincibilityTime = 60;
		isVisible = false;
	}
	
	public void update(){
		if(y + height + yVel > Game.HEIGHT || y + yVel < 0) yVel *= -1;
		if(x + width + xVel > Game.WIDTH || x + xVel < 0) xVel = 0;
		if(invincibilityTime > 0){ 
			invincibilityTime -= 1;
			if(invincibilityTime % 5 == 0) isVisible = !isVisible;
		}
		else if(isInvincible){
			color = Color.BLACK;
			isInvincible = false;
			isVisible = true;
		}
		super.update();
	}
	
}
