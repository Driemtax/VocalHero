package utils;

public class Helper {
    public static float[] byteArrayToFloatArray(byte[] audioData) {
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

  public static double frequencyToCentOffset(double frequency, double targetFrequency) {
      if (frequency <= 0 || targetFrequency <= 0) return 0;
      return 1200 * Math.log(frequency / targetFrequency) / Math.log(2);
  }
}
