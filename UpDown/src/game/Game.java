package game;

import game.controller.Controller;
import game.entity.Enemy;
import game.entity.Player;
import game.graphics.Screen;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = -622927646585048709L;

	public static enum GameState {
		MENU, ALIVE, DEAD
	}

	private enum GenerationType{
		RANDOM, STEP
	}
	
	private GenerationType generationType = GenerationType.RANDOM;
	private int enemySpawnDelayFrames = 60;
	
	// Random pattern
	
	// Step pattern
	int yP = -1;
	int enemyNumber = 6;
	int direction;
	
	private float timeSurvived;
	long startTime;
	long currentTime;
	
	public static String title    = "";
	public static final int WIDTH = 768, HEIGHT = WIDTH / 16 * 9;
	JFrame frame                  = new JFrame();
	
	private boolean isRunning;
	private Thread thread;
	private int updates;
	private int startTimeFrames;
	private int frames;
	
	private BufferedImage img;
	private int[] pixels;
	private Screen screen;
	
	private Controller controller;
	public static Player player;
	
	public static Random random;
	
	private static GameState gameState;
	
	public Game(){
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		gameState = GameState.DEAD;
		
		isRunning = false;
		updates   = 0;
		frames    = 0;
		timeSurvived     = 0;
		
		screen     = new Screen(WIDTH, HEIGHT);
		player     = new Player(WIDTH/2-16, HEIGHT/2-16, 16, 16);
		controller = new Controller();
		
		img    = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		
		random = new Random();
		
		addKeyListener(controller);
	}
	
	public static void setGameState(GameState gs){
		gameState = gs;
	}
	
	public void update(){
		title = String.format("Game | FPS: %d | Time survived: %.3f seconds", frames, timeSurvived);
		System.out.println(timeSurvived);
		frame.setTitle(title);
		controller.update();
		if(controller.restartDown) restart();
		if(gameState == GameState.DEAD){
			if(controller.startDown){
				timeSurvived = 0;
				startTime = System.currentTimeMillis();
				gameState = GameState.ALIVE;
				startTimeFrames = updates;
			}
		}
		
		if(gameState == GameState.ALIVE){
			currentTime = System.currentTimeMillis();
			timeSurvived = (currentTime - startTime) / 1000f;
			
			if(controller.leftDown) 
				player.xVel = -player.xVelocity;
			else if(controller.rightDown) 
				player.xVel = player.xVelocity;
			else 
				player.xVel = 0;
			
			if((updates - startTimeFrames) % 600 == 0 && updates - startTimeFrames != 0){
				// (Maybe) Change generation type every 10 seconds
				if(random.nextInt(10) < 4){
					generationType = GenerationType.RANDOM;
				} else {
					generationType = GenerationType.STEP;
				}
			}
			
			if((updates - startTimeFrames) % 1200 == 0 && updates - startTimeFrames != 0){
				// Difficulty up + heal every 20 seconds
				player.yVel += player.yVel < 0 ? -2 : 2;
				player.health += 2;
				enemySpawnDelayFrames -= 5;
			}
			generateEnemies();
			
			player.update();
			for(Enemy e : Enemy.enemies) {
				e.update();
				if(player.bounds.intersects(e.bounds) && !player.isInvincible){
					if(player.health == 0)
						player.die();
					else
						player.takeDamage(1);
				}
			}
		}
	}
	
	public void generateEnemies(){
		switch(generationType){
			case RANDOM:
				if(updates % enemySpawnDelayFrames != 0) break;
				int side = random.nextInt(2); // 0 is left side; 1 is right side
				int size = random.nextInt(17) + 16; // 16-32
				int speed = random.nextInt(4) + 3; // 3 - 6
				// TODO: Generalize probability system for future movement types, items, and events
				Enemy.MovementType movementType = random.nextInt(10) > 2 ? Enemy.MovementType.STRAIGHT : Enemy.MovementType.SINE;
				int yPos;
				switch(movementType){
					case SINE:
						yPos = 0;
						break;
					default:
						yPos = random.nextInt(Game.HEIGHT-size);
				}
				Enemy.enemies.add(new Enemy(side == 0 ? -size : Game.WIDTH,yPos, size, size, (speed - 2 * speed * side), movementType));
			break;
			case STEP:
				
				if(enemyNumber > random.nextInt(4) + 2){
					enemyNumber = 1;
					
					//Prevent "steps" from going offscreen
					if(yP< 5 * 24)
						direction = 1;
					else if(yP > HEIGHT - 24*5 - 16)
						direction = -1;
					else
						direction = 1 - 2 * random.nextInt(2);
				} else if(updates % (enemySpawnDelayFrames*3/4) == 0) {
					Enemy.enemies.add(new Enemy(-16, yP, 16, 16, 5, Enemy.MovementType.STRAIGHT));
					enemyNumber++;
					yP += 24 * direction;
				}
				
			default:
		}
	}
	
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		screen.render();
		
		for(int i = 0; i < pixels.length; i++){
			pixels[i] = screen.pixels[i];
		}
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
		
		g.dispose();
		bs.show();
	}
	
	public static synchronized void restart(){
		gameState = GameState.DEAD;
		for(Enemy e : Enemy.enemies){
			Enemy.enemies.remove(e);
		}
		player = new Player(WIDTH/2-16, HEIGHT/2-16, 16, 16);
	}

	public synchronized void start(){
		isRunning = true;
		thread = new Thread(this, "Game");
		thread.start();
	}
	
	public synchronized void stop(){
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double ns = 1000000000d / 60;
		double delta = 0;
		int frames = 0;
		long now = System.nanoTime();
		while (isRunning) {
			now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				updates++;
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer  > 1000){
				timer += 1000;
				this.frames = frames;
				frames = 0;
			}
		}
	}
	
	public static void main(String[] args){
		Game game = new Game();		
		game.frame.add(game);
		game.frame.setTitle(title);
		game.frame.setSize(game.getSize());
		game.frame.setResizable(false);
		game.frame.pack();
		game.frame.setVisible(true);
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		
		game.start();
	}
}
