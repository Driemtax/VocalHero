package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.swing.SwingUtilities;

import audio.PitchDetector;
import audio.Recorder;
import controller.TrainingController;
import controller.WindowController;
import model.AudioSettings;
import model.RecordingFinishedCallback;

public class TestBaseVoiceRecognition {
    public static void main(String[] args) {
        List<Double> pitches = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        Mixer.Info output = null;
        Mixer.Info[] devices = AudioSystem.getMixerInfo();
        int i = 0;
        for (Mixer.Info info : devices) {
            System.out.println(i + info.getName());
            i++;
        }
        int device = scanner.nextInt();
        output = devices[device];

        Recorder recorder = new Recorder();
        PitchDetector pitchDetector = new PitchDetector(2048, AudioSettings.getSampleRate());

        recorder.setAudioChunkListener(chunk -> {
            double pitch = pitchDetector.getDominantFrequency(chunk);
            pitches.add(pitch);
        });

        RecordingFinishedCallback updateUiAfterRecordingCallback = (boolean success) -> {
            if (success) {
                int pitchCount = 0;
                double pitchTotal = 0.0;
                for (double pitch : pitches) {
                    pitchCount++;
                    pitchTotal += pitch;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println(pitchTotal / pitchCount);
            }
        };

        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("start in 3");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("start in 2");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("start in 1");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            recorder.startRecording(6, output, updateUiAfterRecordingCallback);
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
