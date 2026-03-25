public class BackgroundMusic {
    public static void main(String[] args) {
        AudioPlayer.getInstance().playSound("Media", "sunflower-seed-wav"); //background music

        javax.swing.JOptionPane.showMessageDialog(null, "Music is playing! Press OK to stop.");
    }
}