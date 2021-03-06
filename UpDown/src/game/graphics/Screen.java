package game.graphics;

import game.Game;
import game.entity.Enemy;

import java.awt.Color;

public class Screen {
	private int width, height;
	public int[] pixels;
	
	public Screen(int width, int height){
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}
	
	public void render(){
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				pixels[x + y * width] = 0xFFFFFF;
			}
		}
		
		if(Game.player.isVisible)
			fillRect(Game.player.x, Game.player.y, Game.player.width, Game.player.height, Game.player.color);
		
		for(Enemy e : Enemy.enemies){
			fillRect(e.x, e.y, e.width, e.height, e.color);
		}
		drawHealth();
	}
	
	private void fillRect(int x, int y, int width, int height, Color color) {
		for (int yCount = y; yCount <= y + height; yCount++) {
			for (int xCount = x; xCount <= x + width; xCount++) {
				if(xCount >= Game.WIDTH || xCount < 0 || yCount >= Game.HEIGHT || yCount < 0) continue;
				pixels[xCount+yCount*this.width] = color.getRGB();
			}
		}
	}
	
	private void drawHealth(){
		int y = -10;
		int x;
		for(int i = 0; i < Game.player.health; i++){
			x = 20 + 30 * (i % 5);
			if(i % 5 == 0){
				y += 30;
				x = 20;
			}
			fillRect(x, y, 20, 20, Color.RED);
		}
	}
}
