package utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;
import java.io.File;

import model.AudioSettings;

public class FileUtils {

private final static String RECORDING_PATH = "recordings";
    
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

    /**
     * Saves the audio data to a WAV file in a specified path.
     * @param fileName the name of the  WAV file 
     * @param audioData the audio data as a byte array
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    public static void saveRecordingToWAV(String fileName, byte[] audioData)
        throws IOException, UnsupportedAudioFileException {
        AudioFormat format = AudioSettings.getFormat();
        String projectRoot = System.getProperty("user.dir");
        String filePath = projectRoot + File.separator + RECORDING_PATH + File.separator + fileName;

        // make sure the directory exists or create it
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        // Put byte array into AudioInputStream
        try (ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
             AudioInputStream ais = new AudioInputStream(bais, format, audioData.length / (format.getSampleSizeInBits() / 8))) {
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
             }
    }

    public static byte[] loadRecordingFromWAV(String fileName) throws IOException, UnsupportedAudioFileException {
        String projectRoot = System.getProperty("user.dir");
        String filePath = projectRoot + File.separator + RECORDING_PATH + File.separator + fileName;
        File file = new File(filePath);
        
        if (!file.exists()) {
            throw new IOException("Recording file not found: " + filePath);
        }

        byte[] wavBytes = Files.readAllBytes(Paths.get(filePath));

        return wavBytes;
        
    }
}
