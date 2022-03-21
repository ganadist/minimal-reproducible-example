package com.example.myapplication;

public class Log {
    public static final int VERBOSE = android.util.Log.VERBOSE;
    public static final int DEBUG = android.util.Log.DEBUG;

    private static int mLoggingLevel = DEBUG;

    private Log() {

    }

    public static void setLoggingLevel(int LoggingLevel) {
        mLoggingLevel = LoggingLevel;
    }

    public static boolean isLoggable(String tag, int level) {
        return level >= mLoggingLevel;
    }

    public static int v(String tag, String msg) {
        return logMsg(VERBOSE, tag, msg, null);
    }

    public static int v(String tag, String msg, Throwable tr) {
        return logMsg(VERBOSE, tag, msg, tr);
    }

    public static int d(String tag, String msg) {
        return logMsg(DEBUG, tag, msg, null);
    }

    public static int d(String tag, String msg, Throwable tr) {
        return logMsg(DEBUG, tag, msg, tr);
    }

    private static String getLocation() {
        final int index = 3;
        Throwable tr = new Throwable();
        try {
            StackTraceElement ste = tr.getStackTrace()[index];
            return "[" + ste.getMethodName() + ":" + ste.getLineNumber() + "] ";
        } catch (Exception e) {
            return ":";
        }
    }

    private static int logMsg(int loggingLevel, String tag, String msg, Throwable tr) {
        if (!isLoggable(tag, loggingLevel)) {
            return -1;
        }

        final String location = getLocation();
        final String logMsg = location + msg;

        switch (loggingLevel) {
            case VERBOSE:
                return tr == null ? android.util.Log.v(tag, logMsg) : android.util.Log.v(tag, logMsg, tr);
            case DEBUG:
                return tr == null ? android.util.Log.d(tag, logMsg) : android.util.Log.d(tag, logMsg, tr);
            default:
                return -1;
        }
    }
}
