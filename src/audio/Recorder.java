// Authors: Lars Beer
package audio;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Recorder {
    private TargetDataLine targetLine;
    private AudioFormat format;
    private Mixer.Info selectedMixer;
    private ByteArrayOutputStream audioBuffer;
    private Consumer<byte[]> audioChunkListener;

    
    public Recorder() {
        this(new AudioFormat(22050.0f, 16, 1, true, false));
    }
    
    public Recorder(AudioFormat format) {
        this.format = format;
    }

    public AudioFormat getFormat() { return format; }
    public void setFormat(AudioFormat format) { this.format = format; }
    public Mixer.Info getSelectedMixer() { return selectedMixer; }
    public void setAudioChunkListener(Consumer<byte[]> listener) {this.audioChunkListener = listener;}

    /**
     * Returns all available microphones
     * @return List<Mixer.Info>
     */
    public List<Mixer.Info> getAvailableMicrophones() {
        List<Mixer.Info> microphones = new ArrayList<>();
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(info);
            Line.Info[] targetLines = mixer.getTargetLineInfo();
            for (Line.Info lineInfo : targetLines) {
                if (lineInfo instanceof DataLine.Info) {
                    try {
                        TargetDataLine testLine = (TargetDataLine) mixer.getLine(lineInfo);
                        testLine.open(format);
                        testLine.close();
                        microphones.add(info);
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return microphones;
    }

    /**
     * Starts a recording of a specified duration
     * @param durationSeconds specifies how long the recording should be
     * @param mixerInfo specifies which microphone to use for the recording
     * @throws LineUnavailableException
     */
    public void startRecording(int durationSeconds, Mixer.Info mixerInfo) throws LineUnavailableException {
        this.selectedMixer = mixerInfo;
        Mixer mixer = AudioSystem.getMixer(mixerInfo);
        
        targetLine = (TargetDataLine) mixer.getLine(new DataLine.Info(TargetDataLine.class, format));
        targetLine.open(format);
        targetLine.start();
        
        audioBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        
        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < durationSeconds * 1000L) {
                int bytesRead = targetLine.read(buffer, 0, buffer.length);
                audioBuffer.write(buffer, 0, bytesRead);

                // Send a COPY of the buffer to the listener
                // This is important to avoid modifying the original buffer
                if (audioChunkListener != null) {
                    byte[] chunkCopy = new byte[bytesRead];
                    System.arraycopy(buffer, 0, chunkCopy, 0, bytesRead);
                    audioChunkListener.accept(chunkCopy);
                }
            }
            stopRecording();
        }).start();
    }

    /**
     * Stops the recording if not automaticlly stopped
     */
    public void stopRecording() {
        if (targetLine != null) {
            targetLine.stop();
            targetLine.close();
        }
    }

   /**
    * returns the recorded audio data
    * @return byte[] of the audio
    */
    public byte[] getAudioData() {
        return audioBuffer != null ? audioBuffer.toByteArray() : new byte[0];
    }

    /**
     * saves the recorded audio to a .wav file
     * @param filename specifies the name of the file
     * @throws IOException
     */
    public void saveToFile(String filename) throws IOException {
        if (audioBuffer == null) return;
        
        ByteArrayInputStream bais = new ByteArrayInputStream(audioBuffer.toByteArray());
        AudioInputStream ais = new AudioInputStream(bais, format, 
            audioBuffer.size() / format.getFrameSize());
            
        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(filename));
    }
}
