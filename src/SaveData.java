import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles saving and loading level progress data to/from a file.
 * Each line in the save file stores: levelName,completionPercent
 */
public class SaveData {

    private static final String SAVE_FILE = "savedata.txt";

    /**
     * Saves completion data for all levels to the save file.
     * @param levels Array of GameLevel objects to save
     */
    public static void save(level.GameLevel[] levels) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE))) {
            for (level.GameLevel level : levels) {
                writer.println(level.getLevelName() + "," + level.getCompletionPercent());
            }
            System.out.println("Progress saved.");
        } catch (IOException e) {
            System.out.println("Error saving progress: " + e.getMessage());
        }
    }

    /**
     * Loads completion data from the save file and applies it to the levels.
     * @param levels Array of GameLevel objects to load data into
     */
    public static void load(level.GameLevel[] levels) {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("No save file found. Starting fresh.");
            return;
        }

        // Read save data into a map
        HashMap<String, Float> savedData = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    float percent = Float.parseFloat(parts[1].trim());
                    savedData.put(name, percent);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading progress: " + e.getMessage());
            return;
        }

        // Apply saved data to levels
        for (level.GameLevel level : levels) {
            Float saved = savedData.get(level.getLevelName());
            if (saved != null) {
                level.setCompletionPercent(saved);
                System.out.println("Loaded " + level.getLevelName() + ": " + saved + "%");
            }
        }
    }
}