package Util;

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
}
