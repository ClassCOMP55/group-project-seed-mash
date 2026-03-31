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
	
	
	
	public void run() {
		addMouseListeners();
		sprite.scale(0.2);
		add(sprite);
		
		updateHitbox();
	}
	
	public void updateHitbox() {
		hitbox = new GRect(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight()-10); // The -10 is so the character can walk on ground
		bottomHitbox = new GRect(hitbox.getX(), hitbox.getY() + hitbox.getHeight(), hitbox.getWidth(), 10); // The 10 is the blank space
		
		add(hitbox);
		add(bottomHitbox);
	}
	
	public static void main(String[] args) {
		new Character().start();
	}
	
	
}
