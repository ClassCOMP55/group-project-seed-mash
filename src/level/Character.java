package level;
import acm.graphics.*;
import java.util.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;
import java.awt.event.*;


public class Character extends GraphicsProgram{
	
	GImage sprite = new GImage("Character Sprite (1).png", 0, 0);
	GRect hitbox;
	GRect bottomHitbox;
	ObstacleType[][] geometry;
	private final GameLevel[] levels = {GameLevel.TEST_LEVEL, GameLevel.TEST_LEVEL_2};
	GameLevel level; // the one I'm using
	
	public Character(int levelNum) {
		level = levels[levelNum];
	}
	
	public void run() {
		addMouseListeners();
		sprite.scale(0.2);
		add(sprite);
		
		GImage theBlock = new GImage("obstacles/block.png", 50, 50);
		//add(theBlock);
		
		updateHitbox();
		checkHitboxes();
	}
	
	public void updateHitbox() {
		hitbox = new GRect(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight()-10); // The -10 is so the character can walk on ground
		bottomHitbox = new GRect(hitbox.getX(), hitbox.getY() + hitbox.getHeight(), hitbox.getWidth(), 10); // The 10 is the blank space
	}
	
	public void checkHitboxes() {
		GImage block = new GImage("obstacles/block.png");
		GImage platform = new GImage("obstacles/platform.png");
		GImage spikeDown = new GImage("obstacles/spike_down.png");
		GImage spikeUp = new GImage("obstacles/spike_up.png");
		
		//Checks the right side
		for (int i = 0; i < hitbox.getHeight(); i++) {
			GObject element = getElementAt(hitbox.getX() + hitbox.getWidth() + 1, hitbox.getY() + i);
			if (element != null) {
				die();
			}
		}
		//Checks the top side
		for (int i = 0; i < hitbox.getWidth(); i++) {
			GObject element = getElementAt(hitbox.getX() + i, hitbox.getY() - 1);
			if (element != null) {
				die();
			}
		}
	}
	
	public void die() {
		System.out.println("Dead.");
	}
	
	public boolean checkImages(GImage obstacle, GImage element) {
		if (element == null) {
			return false;
		}
		int[][] elementList = element.getPixelArray();
		int[][] obstacleList = obstacle.getPixelArray();
		
		for (int j = 0; j < elementList[0].length; j++) {
			for (int i = 0; i < elementList.length; i++) {
				if (elementList[i][j] != obstacleList[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	public static void main(String[] args) {
		new Character(0).start();
	}
	
	
}
