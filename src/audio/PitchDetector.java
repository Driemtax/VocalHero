// Authors: Lars Beer, Jonas Rumpf
package audio;

import java.util.Arrays;

import utils.Helper;

public class PitchDetector {
    private final int frameSize, myCosSize2Pi;
  private final float[] mySin, myCos;
  private final double sampleRate;

  /**
   * Signalverarbeitungsobjekt mit Lookuptables für Sin und Cosinus
   * 
   * @param size Größe der FFT (muss eine Zweierpotenz sein)
   */
  public PitchDetector(int size, double sampleRate) {
    frameSize = 2048;
    this.sampleRate = sampleRate;
    myCosSize2Pi = size; // 0 - 2*PI
    myCos = new float[myCosSize2Pi]; // für FFT reicht size/2, als0 0 bis PI, nur DCT braucht size
    mySin = new float[myCosSize2Pi];
    for (int i = 0; i < size; i++) {
      myCos[i] = (float) Math.cos(2 * Math.PI * i / size);
      mySin[i] = (float) Math.sin(2 * Math.PI * i / size);
    }
  }

  public double[] getDominantFrequencies(byte[] audioData, double windowSizeSec) {
        int windowSize = (int)(sampleRate * windowSizeSec);
        int steps = audioData.length / windowSize;
        double[] frequencies = new double[steps];
        
        for(int i=0; i<steps; i++) {
            byte[] chunk = Arrays.copyOfRange(audioData, i*windowSize, (i+1)*windowSize);
            frequencies[i] = getDominantFrequency(chunk);
        }
        return frequencies;
    }

  public double getDominantFrequency(byte[] audioData) {
    float[] audioDataFloat = Helper.byteArrayToFloatArray(audioData);
    float[] spectrum = new float[frameSize / 2];

    double[] detectedFrequencies = new double[(audioDataFloat.length - frameSize) / frameSize + 1];
    int frameCount = 0;

    for (int offset = 0; offset + frameSize <= audioDataFloat.length; offset += frameSize) {
        float[] frame = Arrays.copyOfRange(audioDataFloat, offset, offset + frameSize);
        calculateSpectrum(frame, spectrum);

        // Peak-Detection: find strongest frequency in the spectrum
        int maxIndex = 0;
        float maxValue = 0;
        for (int i = 1; i < spectrum.length - 1; i++) {
            if (spectrum[i] > maxValue) {
                maxValue = spectrum[i];
                maxIndex = i;
            }
        }

        // Parabolic Interpolation for better frequency estimation
        double trueIndex = maxIndex;
        if (maxIndex > 0 && maxIndex < spectrum.length - 1) {
            float alpha = spectrum[maxIndex - 1];
            float beta = spectrum[maxIndex];
            float gamma = spectrum[maxIndex + 1];
            double p = 0.5 * (alpha - gamma) / (alpha - 2 * beta + gamma);
            trueIndex = maxIndex + p;
        }

        double frequency = trueIndex * sampleRate / frameSize;
        detectedFrequencies[frameCount++] = frequency;
    }

    // Moving Average Glättung
    double smoothed = 0;
    int window = Math.min(5, frameCount); // Fenstergröße 5 oder weniger
    for (int i = frameCount - window; i < frameCount; i++) {
        smoothed += detectedFrequencies[i];
    }
    smoothed /= window;

    return smoothed;
  }

  public double getNoteStability(double[] frequencies) {
        double avg = Arrays.stream(frequencies).average().orElse(0);
        double variance = Arrays.stream(frequencies)
                               .map(d -> Math.pow(d - avg, 2))
                               .average().orElse(0);
        return 1 / (1 + variance);
    }

  /**
   * FFT - Fast Fourier Transform (Cooley und Tukey / Radix-2-Algorithmus)
   * 
   * @param re   Real-Teil
   * @param im   Imaginary-Teil
   * @param size Anzahl Samples
   */
  private void calculateFFT(float[] re, float[] im, int size) {

    for (int span = size / 2; span > 0; span >>= 1) {
      for (int j = 0; j < span; j++) {
        int a = j * (myCosSize2Pi / 2) / span;
        float s = -mySin[a], c = myCos[a];
        for (int i = j; i < size; i += (span << 1)) {
          float tmpR = re[i] + re[i + span], tmpI = im[i] + im[i + span];
          float tmR = re[i] - re[i + span], tmI = im[i] - im[i + span];
          re[i + span] = tmR * c - tmI * s;
          re[i] = tmpR;
          im[i + span] = tmI * c + tmR * s;
          im[i] = tmpI;
        }
      }
    }
    // Permutation der Daten (bit reversal)
    for (int i = 1, j = 0; i < size - 1; i++) {
      int k;
      for (k = size >> 1; j >= k; k >>= 1)
        j -= k;
      j += k;
      if (i < j) { // swap result[i] und result[j]
        float tmpR = re[i], tmpI = im[i];
        re[i] = re[j];
        im[i] = im[j];
        re[j] = tmpR;
        im[j] = tmpI;
      }
    }
  }

  /**
   * Berechnung des Leistungsspektrum des Signals mit Hilfe der FFT (Phase
   * interessiert nicht)
   * 
   * @param signal    zeitabhängiges Signal
   * @param spectrums Spektrum (Beträge)
   */
  public void calculateSpectrum(float[] signal, float[] spectrum) {
    float[] re = new float[frameSize], im = new float[frameSize];

    // Hamming-Window auf Signal anwenden
    for (int i = 0; i < frameSize; i++) {
      re[i] = signal[i] * (0.54f - 0.46f * myCos[i * myCosSize2Pi / (frameSize - 1) % myCosSize2Pi]);
      im[i] = 0.0f;
    }

    calculateFFT(re, im, frameSize);

    // Betrag in Spectrum übertragen (Phase interessiert hier nicht)
    for (int i = 0; i < frameSize / 2; i++)
      spectrum[i] = (float) Math.sqrt(re[i] * re[i] + im[i] * im[i]);
  }

  /***
   * Berechnet den RMS-Wert (Root Mean Square) der Audiodaten.
   * Der RMS-Wert ist ein Maß für die durchschnittliche Leistung des Signals.
   * @param audioData Audiodaten als Byte-Array
   * @return der berechnete RMS-Wert
   */
  public double calculateRMS(byte[] audioData) {
      float[] audioDataFloat = utils.Helper.byteArrayToFloatArray(audioData);
      double sum = 0;
      for (float sample : audioDataFloat) {
          sum += sample * sample;
      }
      return Math.sqrt(sum / audioDataFloat.length);
  }

}
