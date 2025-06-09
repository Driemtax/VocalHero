// Authors: Lars Beer
package audio;

import javax.sound.sampled.*;
import javax.swing.SwingUtilities;

import model.RecordingFinishedCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class Recorder {
    private TargetDataLine targetLine;
    private AudioFormat format;
    private Mixer.Info selectedMixer;
    private ByteArrayOutputStream audioBuffer;
    private Consumer<byte[]> audioChunkListener;

    
    public Recorder() {
        this(new AudioFormat(44100.0f, 16, 1, true, false));
    }
    
    public Recorder(AudioFormat format) {
        this.format = format;
    }

    public AudioFormat getFormat() { return format; }
    public void setFormat(AudioFormat format) { this.format = format; }
    public Mixer.Info getSelectedMixer() { return selectedMixer; }
    public void setAudioChunkListener(Consumer<byte[]> listener) {this.audioChunkListener = listener;}

    /**
     * Starts a recording of a specified duration
     * @param durationSeconds specifies how long the recording should be
     * @param mixerInfo specifies which microphone to use for the recording
     * @throws LineUnavailableException
     */
    public boolean startRecording(int durationSeconds, Mixer.Info mixerInfo, RecordingFinishedCallback getRecordedDataCallback) throws LineUnavailableException {
        this.selectedMixer = mixerInfo;
        Mixer mixer = AudioSystem.getMixer(mixerInfo);
        boolean success = true;

        // close old line if still open
        if (targetLine != null && targetLine.isOpen()) {
            targetLine.stop();
            targetLine.close();
        }
        
        try {
            targetLine = (TargetDataLine) mixer.getLine(new DataLine.Info(TargetDataLine.class, format));
            targetLine.open(format);
            targetLine.start();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());

            // Recording could not be started
            success = false;
        }
        

        audioBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];

        // Need a final to pass it to the thread
        final boolean finalSuccess = success;
        
        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            try {
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
            } catch (Exception e) {
                // TODO: handle exception
            } finally {
                stopRecording();

                // Important: run callback on the Event Dispatch Thread (EDT), cause UI updates must be done on the EDT
                if (getRecordedDataCallback != null) {
                    SwingUtilities.invokeLater(() -> getRecordedDataCallback.onRecordingFinished(finalSuccess));
                }
            }
        }).start();

        // If this is reached thread was startet, thus recording was started successfully
        return success;
    }

    /**
     * Stops the recording if not automaticlly stopped
     */
    public void stopRecording() {
        if (targetLine != null) {
            targetLine.flush();
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
        
        audioBuffer.flush();
        ByteArrayInputStream bais = new ByteArrayInputStream(audioBuffer.toByteArray());
        AudioInputStream ais = new AudioInputStream(bais, format, 
            audioBuffer.size() / format.getFrameSize());
            
        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(filename));
    }
}
