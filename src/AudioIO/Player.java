package AudioIO;

// Player.java
import javax.sound.sampled.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Player {
    private final byte[] audioData;
    private final AudioFormat format;
    private Thread playbackThread;
    private final AtomicBoolean isPaused = new AtomicBoolean(false);
    private final AtomicBoolean isStopped = new AtomicBoolean(false);
    
    public Player(byte[] audioData, AudioFormat format) {
        this.audioData = audioData;
        this.format = format;
    }

    public List<Mixer.Info> getAvailableSpeakers(AudioFormat format) {
        List<Mixer.Info> speakers = new ArrayList<>();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            if (mixer.isLineSupported(info)) {
                speakers.add(mixerInfo);
            }
        }
        return speakers;
    }

    public void play(Mixer.Info selectedDevice) throws LineUnavailableException {
        isStopped.set(false);
        isPaused.set(false);

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        Mixer mixer = AudioSystem.getMixer(selectedDevice);
        SourceDataLine sourceLine = (SourceDataLine) mixer.getLine(info);

        sourceLine.open(format);
        sourceLine.start();
        
        // System.out.println("Entered play function");
        // sourceLine = AudioSystem.getSourceDataLine(format);
        // System.out.println("Line: " + sourceLine.toString());
        // System.out.println("Mixer: " + sourceLine);
        // sourceLine.open(format);
        // System.out.println("Line opened...");
        // sourceLine.start();
        // System.out.println("Line started...");

        playbackThread = new Thread(() -> {
            int bufferSize = 4096;
            int bytesWritten = 0;

            while (bytesWritten < audioData.length && !isStopped.get()) {
                if (!isPaused.get()) {
                    int bytesToWrite = Math.min(bufferSize, audioData.length - bytesWritten);
                    int bytesActuallyWritten = sourceLine.write(audioData, bytesWritten, bytesToWrite);
                    bytesWritten += bytesActuallyWritten;
                } else {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            
            if (!isStopped.get()) {
                sourceLine.drain();
            }
            sourceLine.close();
        });
        
        playbackThread.start();
    }

    public void pause() {
        isPaused.set(true);
    }

    public void resume() {
        isPaused.set(false);
    }

    public void stop() {
        isStopped.set(true);
        if (playbackThread != null) {
            playbackThread.interrupt();
        }
    }

    // Optional: Fortschritts-Callback
    public interface PlaybackProgressListener {
        void onProgress(double percentage);
        void onComplete();
    }

    public void setProgressListener(PlaybackProgressListener listener) {
        // Implementierung ähnlich wie play()-Methode mit zusätzlichen Callbacks
    }
}

