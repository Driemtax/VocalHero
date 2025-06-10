//Author: David Herrmann

package utils;

import model.Interval;

public class IntervalUtil {
        /**
     * generate a random Interval in the range from and to
     * 
     * @param from lowest possible Interval (inclusive)
     * @param to   highest possible Interval (inclusive)
     * @return a random Interval in the range from and to
     */
    public static Interval getRandomIntervalInRange(Interval from, Interval to) {
        int start = from.getSemitones();
        int end = to.getSemitones();
        if (start > end) {
            throw new IllegalArgumentException("Start interval must be less than or equal to end interval");
        }
        int semitones = start + (int) (Math.random() * (end - start + 1));
        Interval result = Interval.getIntervalBySemitones(semitones);

        if (result == null) {
            throw new IllegalStateException("No interval found for semitones: " + semitones);
        }
        return result;
    }
}
