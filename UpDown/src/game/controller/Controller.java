package game.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener {
	
	private boolean[] keys = new boolean[65535];
	public boolean leftDown, rightDown, startDown, restartDown;
	
	public void update(){
		leftDown = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		rightDown = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		restartDown = keys[KeyEvent.VK_R];
		startDown = keys[KeyEvent.VK_SPACE];
	}

	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) {
	}

	public boolean isKeyDown(int index){
		return keys[index];
	}
	
}
