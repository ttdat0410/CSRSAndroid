package vn.vnpt.ansv.csrsandroid.common.utils;

/**
 * Created by ANSV on 11/6/2017.
 */

public class ParsableAsType {
    public interface ParsableCallback {
        void callback(boolean isMatches, String ex);
    }
    public static void isParsableAsLong(final String inputString, ParsableCallback parsableCallback) {
        try {
            Long.valueOf(inputString);
            parsableCallback.callback(true, "");

        } catch (NumberFormatException numberFormatException) {
            parsableCallback.callback(false, numberFormatException.toString());
        }
    }
}
