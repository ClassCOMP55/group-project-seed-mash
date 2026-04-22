package panes;

import acm.graphics.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Death menu overlay that appears when the player dies.
 * Provides options to replay the current level or return to level select.
 */
public class DeathMenuPane {

    private final MainApplication mainScreen;
    private final ArrayList<GObject> elements = new ArrayList<>();

    // Buttons
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

    /**
     * Shows the death menu overlay on screen.
     */
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
        double boxH = 380;
        double boxX = (screenW - boxW) / 2;
        double boxY = (screenH - boxH) / 2;

        // Box shadow for depth
        GRect boxShadow = new GRect(boxX + 5, boxY + 5, boxW, boxH);
        boxShadow.setFilled(true);
        boxShadow.setFillColor(new Color(0, 0, 0, 80));
        addElement(boxShadow);

        // Main box
        GRect menuBox = new GRect(boxX, boxY, boxW, boxH);
        menuBox.setFilled(true);
        menuBox.setFillColor(new Color(0, 50, 120));
        menuBox.setColor(Color.WHITE);
        addElement(menuBox);

        // "GAME OVER" title
        GLabel title = new GLabel("GAME OVER");
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 52));
        title.setColor(new Color(255, 80, 80));
        title.setLocation((screenW - title.getWidth()) / 2, boxY + 75);
        addElement(title);

        // Subtitle
        GLabel subtitle = new GLabel("Better luck next time!");
        subtitle.setFont(new Font("Comic Sans MS", Font.PLAIN, 22));
        subtitle.setColor(new Color(200, 220, 255));
        subtitle.setLocation((screenW - subtitle.getWidth()) / 2, boxY + 115);
        addElement(subtitle);

        // --- Replay Button ---
        double btnW = 320;
        double btnH = 60;
        double btnX = (screenW - btnW) / 2;
        double replayY = boxY + 150;

        replayButton = new GRect(btnX, replayY, btnW, btnH);
        replayButton.setFilled(true);
        replayButton.setFillColor(new Color(46, 204, 113));
        replayButton.setColor(new Color(36, 170, 90));
        addElement(replayButton);

        replayLabel = new GLabel("REPLAY");
        replayLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        replayLabel.setColor(Color.WHITE);
        replayLabel.setLocation(
                btnX + (btnW - replayLabel.getWidth()) / 2,
                replayY + (btnH + replayLabel.getAscent()) / 2 - 5
        );
        addElement(replayLabel);

        // --- Level Select Button ---
        double levelSelectY = replayY + btnH + 25;

        levelSelectButton = new GRect(btnX, levelSelectY, btnW, btnH);
        levelSelectButton.setFilled(true);
        levelSelectButton.setFillColor(new Color(0, 102, 204));
        levelSelectButton.setColor(new Color(0, 75, 160));
        addElement(levelSelectButton);

        levelSelectLabel = new GLabel("LEVEL SELECT");
        levelSelectLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        levelSelectLabel.setColor(Color.WHITE);
        levelSelectLabel.setLocation(
                btnX + (btnW - levelSelectLabel.getWidth()) / 2,
                levelSelectY + (btnH + levelSelectLabel.getAscent()) / 2 - 5
        );
        addElement(levelSelectLabel);
    }

    /**
     * Hides the death menu and removes all elements from screen.
     */
    public void hide() {
        for (GObject item : elements) {
            mainScreen.remove(item);
        }
        elements.clear();
        visible = false;
    }

    /**
     * Handles click events on the death menu.
     * @return "replay" if replay was clicked, "levelselect" if level select was clicked, or null if neither
     */
    public String mouseClicked(MouseEvent e) {
        if (!visible) return null;

        GObject clicked = mainScreen.getElementAtLocation(e.getX(), e.getY());

        if (clicked == replayButton || clicked == replayLabel) {
            return "replay";
        } else if (clicked == levelSelectButton || clicked == levelSelectLabel) {
            return "levelselect";
        }

        return null; // Clicked on overlay but not a button
    }

    private void addElement(GObject obj) {
        elements.add(obj);
        mainScreen.add(obj);
    }
}