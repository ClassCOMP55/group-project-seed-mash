package sfx;

import acm.graphics.*;
import java.util.*;
import acm.program.*;
import panes.MainApplication;

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("unused")
public class Settings extends GraphicsProgram {
    private final panes.MainApplication mainApp;
    ArrayList<GObject> settingsMenu = new ArrayList<>();
    GImage closeButton;
    public static final int ScaleWidth = 500;

    private static final Color THEME_BLUE_DARK = new Color(0, 70, 150);
    private static final Color THEME_BLUE_DARKEST = new Color(0, 30, 80);
    private static final Color BG_DARK = new Color(0, 30, 80);
    private static final Color BG_MID = new Color(0, 70, 150);
    private static final Color TEAL = new Color(54, 212, 201);
    private static final Color SLIDER_BG = new Color(0, 45, 100);
    private static final Color SLIDER_FILL = new Color(46, 204, 113);
    private static final Color KNOB_COLOR = new Color(80, 230, 140);

    GOval musicKnob;
    GOval sfxKnob;
    GRect musicTrack;
    GRect sfxTrack;
    GRect musicFill;
    GRect sfxFill;
    GLabel musicPct;
    GLabel sfxPct;

    private GObject toDrag;

    public Settings() { mainApp = new MainApplication(); }
    public Settings(MainApplication mainApp) { this.mainApp = mainApp; }

    public double getMusicVol() { return mainApp.getMusicVol(); }
    public void setMusicVol(double musicVol) { mainApp.setMusicVol(musicVol); }
    public double getSfxVol() { return mainApp.getSfxVol(); }
    public void setSfxVol(double sfxVol) { mainApp.setSfxVol(sfxVol); }

    public void init() {}
    public void run() {
        addMouseListeners();
        openSettingsMenu();
    }

    public void openSettingsMenu() {
        int centerX = (int) (mainApp.getWidth() / 2);
        int centerY = (int) (mainApp.getHeight() / 2);
        int panelW = 750;
        int panelH = 550;
        int left = centerX - panelW / 2;
        int top = centerY - panelH / 2;

        // Dark overlay
        GRect overlay = new GRect(0, 0, mainApp.getWidth(), mainApp.getHeight());
        overlay.setFilled(true);
        overlay.setFillColor(new Color(0, 0, 0, 170));
        settingsMenu.add(overlay);

        // Main panel
        GRect panel = new GRect(left, top, panelW, panelH);
        panel.setFilled(true);
        panel.setFillColor(BG_MID);
        panel.setColor(TEAL);
        settingsMenu.add(panel);

        // Top bar
        GRect topBar = new GRect(left, top, panelW, 80);
        topBar.setFilled(true);
        topBar.setFillColor(BG_DARK);
        topBar.setColor(TEAL);
        settingsMenu.add(topBar);

        // Teal accent line
        GRect accentLine = new GRect(left, top + 80, panelW, 5);
        accentLine.setFilled(true);
        accentLine.setFillColor(TEAL);
        accentLine.setColor(TEAL);
        settingsMenu.add(accentLine);

        //Shadow
        GLabel shadow = new GLabel("S E T T I N G S");
        shadow.setFont(new Font("Courier New", Font.BOLD, 36));
        shadow.setColor(THEME_BLUE_DARK);
        shadow.setLocation(centerX - shadow.getWidth() / 2 + 4, top + 55);
        settingsMenu.add(shadow);
        
        // Title
        GLabel title = new GLabel("S E T T I N G S");
        title.setFont(new Font("Courier New", Font.BOLD, 36));
        title.setColor(Color.WHITE);
        title.setLocation(centerX - title.getWidth() / 2, top + 55);
        settingsMenu.add(title);

        // Close button
        closeButton = new GImage("close.png");
        closeButton.scale(0.35);
        closeButton.setLocation(left + 15, top + 10);
        settingsMenu.add(closeButton);

        // Music slider
        addSliderSection("MUSIC VOLUME", left + 125, top + 180, getMusicVol(), true); 	

        // Divider between sliders
        GLine divider = new GLine(left + 50, top + 320, left + panelW - 50, top + 320);
        divider.setColor(new Color(0, 120, 180));
        settingsMenu.add(divider);

        // SFX slider
        addSliderSection("SFX VOLUME", left + 125, top + 360, getSfxVol(), false);

        for (GObject x : settingsMenu) {
            mainApp.add(x);
        }
    }

    private void addSliderSection(String labelText, int x, int y, double vol, boolean isMusic) {        
        GLabel shadow = new GLabel(labelText);
        shadow.setFont(new Font("Courier New", Font.BOLD, 22));
        shadow.setColor(THEME_BLUE_DARKEST);
        shadow.setLocation(x + 3, y);
        settingsMenu.add(shadow);
        
        GLabel label = new GLabel(labelText);
        label.setFont(new Font("Courier New", Font.BOLD, 22));
        label.setColor(Color.WHITE);
        label.setLocation(x, y);
        settingsMenu.add(label);

        GRect track = new GRect(x, y + 25, ScaleWidth, 16);
        track.setFilled(true);
        track.setFillColor(SLIDER_BG);
        track.setColor(BG_DARK);
        settingsMenu.add(track);

        double fillWidth = (ScaleWidth / 100.0) * vol;
        GRect fill = new GRect(x, y + 25, fillWidth, 16);
        fill.setFilled(true);
        fill.setFillColor(SLIDER_FILL);
        fill.setColor(SLIDER_FILL);
        settingsMenu.add(fill);

        double knobX = x + fillWidth - 16;
        GOval knob = new GOval(knobX, y + 17, 32, 32);
        knob.setFilled(true);
        knob.setFillColor(KNOB_COLOR);
        knob.setColor(Color.WHITE);
        settingsMenu.add(knob);

        GLabel pct = new GLabel((int) vol + "%");
        pct.setFont(new Font("Courier New", Font.BOLD, 22));
        pct.setColor(Color.WHITE);
        pct.setLocation(x + ScaleWidth + 20, y + 43);
        settingsMenu.add(pct);

        if (isMusic) {
            musicTrack = track;
            musicFill = fill;
            musicKnob = knob;
            musicPct = pct;
        } else {
            sfxTrack = track;
            sfxFill = fill;
            sfxKnob = knob;
            sfxPct = pct;
        }
    }

    public void closeSettingsMenu() {
        for (GObject x : settingsMenu) {
            mainApp.remove(x);
        }
        settingsMenu.clear();
        mainApp.setSettingsOpen(false);
    }

    private void updateSlider(GOval knob, GRect track, GRect fill, GLabel pct, int mouseX) {
        double minX = track.getX() - 16;
        double maxX = track.getX() + ScaleWidth - 16;
        double newX = Math.max(minX, Math.min(maxX, mouseX - 16));
        knob.setLocation(newX, knob.getY());

        double fillWidth = Math.max(0, Math.min(ScaleWidth, newX - track.getX() + 16));
        fill.setBounds(track.getX(), track.getY(), fillWidth, track.getHeight());

        int percent = (int) ((fillWidth / ScaleWidth) * 100);
        pct.setLabel(percent + "%");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        GObject x = mainApp.getElementAtLocation(e.getX(), e.getY());
        if (x == musicKnob || x == sfxKnob) {
            toDrag = x;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (toDrag == musicKnob) {
            updateSlider(musicKnob, musicTrack, musicFill, musicPct, e.getX());
        } else if (toDrag == sfxKnob) {
            updateSlider(sfxKnob, sfxTrack, sfxFill, sfxPct, e.getX());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (toDrag != null) {
            double musicVal = ((musicKnob.getX() - musicTrack.getX() + 16) / ScaleWidth) * 100;
            mainApp.setMusicVol(Math.max(0, Math.min(100, musicVal)));

            double sfxVal = ((sfxKnob.getX() - sfxTrack.getX() + 16) / ScaleWidth) * 100;
            mainApp.setSfxVol(Math.max(0, Math.min(100, sfxVal)));
        }
        toDrag = null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        GObject x = mainApp.getElementAtLocation(e.getX(), e.getY());
        if (x == closeButton) {
            mainApp.playClickSound();
            closeSettingsMenu();
        }
    }
}