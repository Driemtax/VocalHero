import java.util.Arrays;

public class FrequencyAnalyzer {
  private final int frameSize, myCosSize2Pi;
  private final float[] mySin, myCos;

  /**
   * Signalverarbeitungsobjekt mit Lookuptables für Sin und Cosinus
   * 
   * @param size Größe der FFT (muss eine Zweierpotenz sein)
   */
  public FrequencyAnalyzer(int size) {
    frameSize = 2048;
    myCosSize2Pi = size; // 0 - 2*PI
    myCos = new float[myCosSize2Pi]; // für FFT reicht size/2, als0 0 bis PI, nur DCT braucht size
    mySin = new float[myCosSize2Pi];
    for (int i = 0; i < size; i++) {
      myCos[i] = (float) Math.cos(2 * Math.PI * i / size);
      mySin[i] = (float) Math.sin(2 * Math.PI * i / size);
    }
  }

  public double getDominantFrequency(byte[] audioData) {
    // Konvertiere die Byte-Daten in ein Float-Array
    float[] audioDataFloat = byteArrayToFloatArray(audioData);
    float[] spectrum = new float[frameSize / 2];

    double sampleRate = 22050.0;
    double[] detectedFrequencies = new double[(audioDataFloat.length - frameSize) / frameSize + 1];
    int frameCount = 0;

    for (int offset = 0; offset + frameSize <= audioDataFloat.length; offset += frameSize) {
      float[] frame = Arrays.copyOfRange(audioDataFloat, offset, offset + frameSize);
      calculateSpectrum(frame, spectrum);

      // Peak-Detection: stärkste Frequenz im Spektrum finden
      int maxIndex = 0;
      float maxValue = 0;
      for (int i = 1; i < spectrum.length; i++) {
        if (spectrum[i] > maxValue) {
          maxValue = spectrum[i];
          maxIndex = i;
        }
      }
      // Frequenz berechnen
      double frequency = maxIndex * sampleRate / frameSize;
      detectedFrequencies[frameCount++] = frequency;
    }
    // Nur die tatsächlich gefüllten Werte betrachten
    double[] validFrequencies = Arrays.copyOf(detectedFrequencies, frameCount);
    Arrays.sort(validFrequencies);
    double medianFrequency = validFrequencies[validFrequencies.length / 2];

    return medianFrequency;
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

  private float[] byteArrayToFloatArray(byte[] audioData) {
    int numSamples = audioData.length / 2;
    float[] signal = new float[numSamples];

    for (int i = 0; i < numSamples; i++) {
      int low = audioData[2 * i] & 0xFF;
      int high = audioData[2 * i + 1]; // signed
      int sample = (high << 8) | low;
      // Normierung auf -1.0 bis +1.0
      signal[i] = sample / 32768f;
    }
    return signal;
  }
}
