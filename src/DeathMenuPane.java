import acm.graphics.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class DeathMenuPane {
	private MainApplication mainScreen;
	private ArrayList<GObject> elements = new ArrayList<>();
	
	private GRect replayButton;
    private GLabel replayLabel;
    private GRect levelSelectButton;
    private GLabel levelSelectLabel;
 
    private boolean visible = false;
 
    public DeathMenuPane(MainApplication mainScreen) {
        this.mainScreen = mainScreen;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void show() {
        visible = true;
 
        double screenW = mainScreen.getWidth();
        double screenH = mainScreen.getHeight();
 
        // Semi-transparent dark overlay
        GRect overlay = new GRect(0, 0, screenW, screenH);
        overlay.setFilled(true);
        overlay.setFillColor(new Color(0, 0, 0, 150));
        addElement(overlay);
 
        // Menu box
        double boxW = 500;
        double boxH = 350;
        double boxX = (screenW - boxW) / 2;
        double boxY = (screenH - boxH) / 2;
 
        GRect menuBox = new GRect(boxX, boxY, boxW, boxH);
        menuBox.setFilled(true);
        menuBox.setFillColor(new Color(0, 50, 120));
        menuBox.setColor(Color.WHITE);
        addElement(menuBox);
 
        // "GAME OVER" title
        GLabel title = new GLabel("GAME OVER");
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 48));
        title.setColor(new Color(255, 80, 80));
        title.setLocation((screenW - title.getWidth()) / 2, boxY + 70);
        addElement(title);
 
        // Replay button
        double btnW = 300;
        double btnH = 60;
        double btnX = (screenW - btnW) / 2;
 
        replayButton = new GRect(btnX, boxY + 120, btnW, btnH);
        replayButton.setFilled(true);
        replayButton.setFillColor(new Color(46, 204, 113));
        replayButton.setColor(new Color(30, 160, 90));
        addElement(replayButton);
 
        replayLabel = new GLabel("REPLAY");
        replayLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
        replayLabel.setColor(Color.WHITE);
        replayLabel.setLocation(
                btnX + (btnW - replayLabel.getWidth()) / 2,
                boxY + 120 + (btnH + replayLabel.getAscent()) / 2 - 5
        );
        addElement(replayLabel);
 
        // Level Select button
        levelSelectButton = new GRect(btnX, boxY + 210, btnW, btnH);
        levelSelectButton.setFilled(true);
        levelSelectButton.setFillColor(new Color(0, 102, 204));
        levelSelectButton.setColor(new Color(0, 70, 160));
        addElement(levelSelectButton);
 
        levelSelectLabel = new GLabel("LEVEL SELECT");
        levelSelectLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
        levelSelectLabel.setColor(Color.WHITE);
        levelSelectLabel.setLocation(
                btnX + (btnW - levelSelectLabel.getWidth()) / 2,
                boxY + 210 + (btnH + levelSelectLabel.getAscent()) / 2 - 5
        );
        addElement(levelSelectLabel);
    }
    
    private void addElement(GObject obj) {
        elements.add(obj);
        mainScreen.add(obj);
    }
}
