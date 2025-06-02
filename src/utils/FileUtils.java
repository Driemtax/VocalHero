package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {
    
    // CSV-Ladefunktion
    public static Map<String, int[]> loadProgressFromCSV(String filePath) {
        Map<String, int[]> progressData = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Header überspringen
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                int completed = Integer.parseInt(parts[1]);
                int total = Integer.parseInt(parts[2]);
                progressData.put(name, new int[]{completed, total});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressData;
    }
}
