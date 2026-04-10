package sfx;

public class BackgroundMusic {
    public static void main(String[] args) {
        AudioPlayer.getInstance().playSound("Media/", "tian_mi_mi");

        javax.swing.JOptionPane.showMessageDialog(null, "Music is playing! Press OK to stop.");
    }
}