package panes;

import java.awt.*;
import java.awt.event.MouseEvent;

import acm.graphics.GImage;
import acm.graphics.GObject;
import acm.graphics.*;
import acm.util.MediaTools;



public class StartPane extends GraphicsPane{
	
	private MainApplication mainScreen;
	private GImage settingButton;
	private GImage descriptionButton;
	
    private GRect playButtonBg;
    private GRect playButtonBorder;
    private GLabel playButtonLabel;
    private GRect playButtonHighlight;
    private GRect settingsBtnBg;
    private GRect settingsBtnBorder;
    private GLabel settingsLabel;
    private GRect settingsHighlight;
    
    // ─── Original Theme Colors ───
    // From StartPane / LevelSelectPane background
    private static final Color THEME_BLUE = new Color(0, 102, 204);
    // Darker shades for depth
    private static final Color THEME_BLUE_DARK = new Color(0, 70, 150);
    private static final Color THEME_BLUE_DARKER = new Color(0, 50, 110);
    private static final Color THEME_BLUE_DARKEST = new Color(0, 30, 80);
    // Lighter shades for highlights
    private static final Color THEME_BLUE_LIGHT = new Color(40, 140, 230);
    private static final Color THEME_BLUE_LIGHTEST = new Color(80, 170, 255);
 
    // From LevelSelectPane play button
    private static final Color THEME_GREEN = new Color(46, 204, 113);
    private static final Color THEME_GREEN_LIGHT = new Color(80, 230, 140);
    private static final Color THEME_GREEN_DARK = new Color(30, 150, 80);
    private static final Color THEME_GREEN_DARKEST = new Color(20, 110, 60);
 
    // From Settings menu body
    private static final Color THEME_TEAL = new Color(54, 212, 201);
    private static final Color THEME_TEAL_LIGHT = new Color(90, 235, 225);
    private static final Color THEME_TEAL_DARK = new Color(35, 160, 150);
 
    // From Settings slider
    private static final Color THEME_SLIDER_BLUE = new Color(19, 117, 203);
    // From Settings knob
    private static final Color THEME_KNOB_BLUE = new Color(50, 159, 255);
 
    // Neutral colors
    private static final Color TEXT_WHITE = new Color(240, 240, 240);
    private static final Color STAR_BRIGHT = new Color(200, 230, 255);
    private static final Color STAR_DIM = new Color(100, 150, 200);
    private static final Color SPIKE_RED = new Color(200, 50, 50);
    private static final Color SPIKE_RED_LIGHT = new Color(230, 80, 80);
 
    // Ground colors derived from theme blue
    private static final Color GROUND_TOP = new Color(0, 85, 170);
    private static final Color GROUND_MID = new Color(0, 65, 135);
    private static final Color GROUND_BOTTOM = new Color(0, 45, 100);
    private static final Color GROUND_DEEP = new Color(0, 30, 70);
 
    // Pixel block size
    private static final int PX = 8;

	
	public StartPane(MainApplication mainScreen) {
		this.mainScreen = mainScreen;
	}
	


	@Override
	public void showContent() {
		drawBackground();
		drawGround();
		drawTitle();
		drawLine();
		playButton();
		settingButton();
        System.out.println("show content");

	}

	@Override
	public void hideContent() {
		for(GObject item : contents) {
			mainScreen.remove(item);
		}
		contents.clear();
	}
	
	private void drawBackground() {
        int bands = 8;
        double bandHeight = mainScreen.getHeight() / bands;
        for (int i = 0; i < bands; i++) {
        	int r = lerp(0, THEME_BLUE.getRed(), i, bands);
        	int g = lerp(30, THEME_BLUE.getGreen(), i, bands);
        	int b = lerp(80, THEME_BLUE.getBlue(), i, bands);
            GRect band = new GRect(0, i * bandHeight, mainScreen.getWidth(), bandHeight + 1);
            band.setFilled(true);
            band.setFillColor(new Color(r, g, b));
            band.setColor(new Color(r, g, b));
            contents.add(band);
            mainScreen.add(band);
        }
    }
	
	private void drawGround() {
        double groundY = mainScreen.getHeight() - 160;
        double w = mainScreen.getWidth();
        int blockSize = PX * 5;
 
        // Top row - theme blue surface blocks
        for (int x = 0; x < w; x += blockSize) {
            GRect surface = new GRect(x, groundY, blockSize, blockSize);
            surface.setFilled(true);
            surface.setFillColor((x / blockSize) % 2 == 0 ? THEME_BLUE : THEME_BLUE_DARK);
            surface.setColor(THEME_BLUE_DARK);
            contents.add(surface);
            mainScreen.add(surface);
 
            // Teal highlight strip on top edge
            GRect highlight = new GRect(x, groundY, blockSize, PX);
            highlight.setFilled(true);
            highlight.setFillColor(THEME_TEAL);
            highlight.setColor(THEME_TEAL);
            contents.add(highlight);
            mainScreen.add(highlight);
        }
 
        // Deeper rows
        Color[] depthColors = {GROUND_TOP, GROUND_MID, GROUND_BOTTOM};
        Color[] depthAltColors = {THEME_BLUE_DARKER, GROUND_TOP, GROUND_MID};
        for (int row = 1; row <= 3; row++) {
            for (int x = 0; x < w; x += blockSize) {
                GRect block = new GRect(x, groundY + row * blockSize, blockSize, blockSize);
                block.setFilled(true);
                Color c = ((x / blockSize) + row) % 2 == 0 ? depthColors[row - 1] : depthAltColors[row - 1];
                block.setFillColor(c);
                block.setColor(GROUND_DEEP);
                contents.add(block);
                mainScreen.add(block);
            }
        }
    }
	
	private void drawTitle() {
        // Shadow in darkest blue
        GLabel shadow = new GLabel("TRIGONOMETRY JUMP");
        shadow.setFont(new Font("Courier New", Font.BOLD, 90));
        shadow.setColor(THEME_BLUE_DARKEST);
        shadow.setLocation((mainScreen.getWidth() - shadow.getWidth()) / 2 + 4, 254);
        contents.add(shadow);
        mainScreen.add(shadow);
 
        // Main title in white
        GLabel title = new GLabel("TRIGONOMETRY JUMP");
        title.setFont(new Font("Courier New", Font.BOLD, 90));
        title.setColor(TEXT_WHITE);
        title.setLocation((mainScreen.getWidth() - title.getWidth()) / 2, 250);
        contents.add(title);
        mainScreen.add(title);
    }
	
	private void drawLine() {
		double centerX = mainScreen.getWidth() / 2;
		double underlineY = 275;
		double width = 1000;
		double barX = centerX - width / 2;
		
		GRect bar = new GRect(barX,underlineY,width,1);
		bar.setFilled(true);
        bar.setFillColor(THEME_TEAL);
        bar.setColor(THEME_TEAL);
		contents.add(bar);
		mainScreen.add(bar);
		
		
	}
	
	private void playButton() {
		double centerX = mainScreen.getWidth() / 2;
        double btnW = 400;
        double btnH = 80;
        double btnX = centerX - btnW / 2;
        double btnY = 420;
        
        GRect outerShadow = new GRect(btnX + 4, btnY + 4, btnW, btnH);
        outerShadow.setFilled(true);
        outerShadow.setFillColor(new Color(0, 0, 0, 80));
        outerShadow.setColor(new Color(0, 0, 0, 80));
        contents.add(outerShadow);
        mainScreen.add(outerShadow);
        
        playButtonBorder = new GRect(btnX - PX, btnY - PX, btnW + PX * 2, btnH + PX * 2);
        playButtonBorder.setFilled(true);
        playButtonBorder.setFillColor(THEME_GREEN_DARKEST);
        playButtonBorder.setColor(THEME_GREEN_DARKEST);
        contents.add(playButtonBorder);
        mainScreen.add(playButtonBorder);
        
        playButtonBg = new GRect(btnX, btnY, btnW, btnH);
        playButtonBg.setFilled(true);
        playButtonBg.setFillColor(THEME_GREEN);
        playButtonBg.setColor(THEME_GREEN);
        contents.add(playButtonBg);
        mainScreen.add(playButtonBg);
        
        playButtonHighlight = new GRect(btnX, btnY, btnW, PX * 2);
        playButtonHighlight.setFilled(true);
        playButtonHighlight.setFillColor(THEME_GREEN_LIGHT);
        playButtonHighlight.setColor(THEME_GREEN_LIGHT);
        contents.add(playButtonHighlight);
        mainScreen.add(playButtonHighlight);
        
        playButtonLabel = new GLabel("P L A Y");
        playButtonLabel.setFont(new Font("Courier New", Font.BOLD, 42));
        playButtonLabel.setColor(TEXT_WHITE);
        playButtonLabel.setLocation(
            btnX + (btnW - playButtonLabel.getWidth()) / 2,
            btnY + btnH / 2 + 15
        );
        contents.add(playButtonLabel);
        mainScreen.add(playButtonLabel);
        
	}
	
	
	
    private int lerp(int a, int b, int step, int total) {
        if (total == 0) return a;
        return a + (b - a) * step / total;
    }

	
	/*private void addDescriptionButton() {
		descriptionButton = new GImage("images-removebg-preview.png", 200, 400);
		descriptionButton.scale(0.8, 0.8);
		descriptionButton.setLocation((mainScreen.getWidth() - descriptionButton.getWidth())/ 2, 400);
		
		contents.add(descriptionButton);
		mainScreen.add(descriptionButton);

	}*/
    
    private void settingButton() {
        double centerX = mainScreen.getWidth() / 2;
        double btnW = 280;
        double btnH = 56;
        double btnX = centerX - btnW / 2;
        double btnY = 560;
        
        GRect shadow = new GRect(btnX + 3, btnY + 3, btnW, btnH);
        shadow.setFilled(true);
        shadow.setFillColor(new Color(0, 0, 0, 60));
        shadow.setColor(new Color(0, 0, 0, 60));
        contents.add(shadow);
        mainScreen.add(shadow);
        
        settingsBtnBorder = new GRect(btnX - PX / 2, btnY - PX / 2, btnW + PX, btnH + PX);
        settingsBtnBorder.setFilled(true);
        settingsBtnBorder.setFillColor(THEME_TEAL_DARK);
        settingsBtnBorder.setColor(THEME_TEAL_DARK);
        contents.add(settingsBtnBorder);
        mainScreen.add(settingsBtnBorder);
        
        settingsBtnBg = new GRect(btnX, btnY, btnW, btnH);
        settingsBtnBg.setFilled(true);
        settingsBtnBg.setFillColor(THEME_TEAL);
        settingsBtnBg.setColor(THEME_TEAL);
        contents.add(settingsBtnBg);
        mainScreen.add(settingsBtnBg);    
        
        settingsHighlight = new GRect(btnX, btnY, btnW, PX);
        settingsHighlight.setFilled(true);
        settingsHighlight.setFillColor(THEME_TEAL_LIGHT);
        settingsHighlight.setColor(THEME_TEAL_LIGHT);
        contents.add(settingsHighlight);
        mainScreen.add(settingsHighlight);
        
        settingsLabel = new GLabel("S E T T I N G S");
        settingsLabel = new GLabel("S E T T I N G S");
        settingsLabel.setFont(new Font("Courier New", Font.BOLD, 26));
        settingsLabel.setColor(TEXT_WHITE);
        settingsLabel.setLocation(
            btnX + (btnW - settingsLabel.getWidth()) / 2,
            btnY + btnH / 2 + 10
        );
        contents.add(settingsLabel);
        mainScreen.add(settingsLabel);
    }
	
	

	 @Override
	    public void mouseClicked(MouseEvent e) {
	        GObject clicked = mainScreen.getElementAtLocation(e.getX(), e.getY());

	        if (clicked == playButtonBg || clicked == playButtonBorder
	            || clicked == playButtonLabel || clicked == playButtonHighlight) {
	            mainScreen.switchToLevelSelectScreen();
	        } else if (clicked == settingsBtnBg || clicked == settingsBtnBorder
	            || clicked == settingsLabel || clicked == settingsHighlight) {
	            mainScreen.switchToSettings();
	        }
	    }
}
