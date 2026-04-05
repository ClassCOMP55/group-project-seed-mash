package level;


import acm.graphics.*;
import java.util.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;
import java.awt.event.*;

public class Character extends GraphicsProgram{
	
	GImage sprite = new GImage("Character Sprite (1).png", 0, 0);
	ObstacleType[][] geometry;
	private final GameLevel[] levels = {GameLevel.TEST_LEVEL, GameLevel.TEST_LEVEL_2};
	GameLevel level; // the one I'm using
	GridPos pos;
	int characterXPos;
	int characterYPos;
	
	public Character(int levelNum) {
		level = levels[levelNum];
		geometry = level.getGeometry();
	}
	
	public void run() {
		addMouseListeners();
		sprite.scale(0.2);
		add(sprite);
		startChar();
		jump();
		drop();
		level.printGeom();
	}
	
	public void createCharacter() {
		sprite.scale(0.2);
		add(sprite);
	}
	
	
	public void die() {
		System.out.println("Dead.");
	}
	
	public void startChar() {
		//Starts character on top of whatever the first block is
		int startY = 0;
		for (int i = 0; i < geometry.length; i++) {
			if (geometry[geometry.length-1-i][0] != null) {
				startY++;
			}
		}
		
		characterXPos = 0;
		characterYPos = startY;
		pos = new GridPos(characterXPos, characterYPos);
		geometry[characterYPos][characterXPos] = ObstacleType.CHARACTER;
	}
	
	public boolean somethingInFront() { //returns true if something is there
		if (geometry[characterYPos][characterXPos+1] != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean somethingAbove() { //returns true if something is there
		if (geometry[characterYPos+1][characterXPos] != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean somethingBelow() { //returns true if something is there
		if (geometry[characterYPos-1][characterXPos] != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public void moveForward() {
		if (!somethingInFront()) {
			geometry[characterYPos][characterXPos] = null;
			characterXPos++;
			geometry[characterYPos][characterXPos] = ObstacleType.CHARACTER;
		} else {
			die();
		}
	}
	
	public void jump() { //jumps three blocks up
		for (int i = 0; i < 3; i++) {
			if (!somethingAbove()) {
				geometry[characterYPos][characterXPos] = null;
				characterYPos++;
				geometry[characterYPos][characterXPos] = ObstacleType.CHARACTER;
			} else {
				die();
			}
		}
	}
	
	public void drop() { //drop character
		while (!somethingBelow()) {
			geometry[characterYPos][characterXPos] = null;
			characterYPos--;
			geometry[characterYPos][characterXPos] = ObstacleType.CHARACTER;
		}
		if (geometry[characterYPos-1][characterXPos].getCharRepresentation() == '^') {
			die();
		}
	}
	
	public static void main(String[] args) {
		new Character(0).start();
	}
	
	
}
