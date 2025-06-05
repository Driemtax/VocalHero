package model;


/**
 * RecordingFinishedCallback is a functional interface that defines a callback method,
 * which can pass a boolean indicating whether the start of the recording was successful or not.
 * 
 * We need this callback to update the UI when the recording cannot start, because e.g. the audioFormat
 * is not supported by the mic of the device.
 */
@FunctionalInterface
public interface RecordingFinishedCallback {
    void onRecordingFinished(boolean success);
}
