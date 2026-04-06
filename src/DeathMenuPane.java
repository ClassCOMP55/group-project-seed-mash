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
}
