package panes;

import acm.program.GraphicsProgram;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


public abstract class GraphicApplication extends GraphicsProgram {
    
    protected GraphicsPane curscreen;
    

    public GraphicApplication() {
        super();
    }
    

    public void setupInteraction() {
        requestFocus();
        addKeyListeners();
        addMouseListeners();
    }
    

    public void switchScreen(GraphicsPane newScreen) {
        if (curscreen != null) {
            curscreen.hideContent();
        }
        curscreen = newScreen;
        curscreen.showContent();
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (curscreen != null) {
            curscreen.mouseClicked(e);
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (curscreen != null) {
            curscreen.mousePressed(e);
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if (curscreen != null) {
            curscreen.mouseReleased(e);
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if (curscreen != null) {
            curscreen.mouseDragged(e);
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        if (curscreen != null) {
            curscreen.mouseMoved(e);
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (curscreen != null) {
            curscreen.keyPressed(e);
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        if (curscreen != null) {
            curscreen.keyReleased(e);
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        if (curscreen != null) {
            curscreen.keyTyped(e);
        }
    }
}