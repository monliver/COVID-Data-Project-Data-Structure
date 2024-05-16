package edu.upenn.cit594.logging;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class Logger {
    private static Logger instance = new Logger();
    private PrintWriter printOut = null;
    private boolean isFileLoggingEnabled = false;

    // Private constructor to prevent instantiation from outside the class (Singleton Pattern)
    private Logger() {}

    // Static method to get the single instance of the Logger class (Singleton Pattern)
    public static Logger getInstance() {
        return instance;
    }

    /**
     * Sets the output destination for the logger.
     * If the filename is not specified or is empty, log to System.err.
     * If the filename is specified, attempt to open or create the file and log to it.
     *
     * @param loggerFileLocation the file path for the log output
     */
    public void setOutputDestination(String loggerFileLocation) {
        if (loggerFileLocation == null || loggerFileLocation.isEmpty()) {
            // Disable file logging if filename is not specified
            isFileLoggingEnabled = false;
        } else {
            try {
                // Close the existing PrintWriter if it's already open
                if (printOut != null) printOut.close();

                // Initialize FileWriter in append mode and create PrintWriter
                FileWriter file = new FileWriter(loggerFileLocation, true);
                printOut = new PrintWriter(file, true);

                // Enable file logging
                isFileLoggingEnabled = true;
            } catch (IOException e) {
                // If unable to open the file, log the error to System.err and disable file logging
                isFileLoggingEnabled = false;
                System.err.println("Logger Error: Unable to open log file. Logging to System.err instead.");
            }
        }
    }

    /**
     * Logs the given message. If file logging is enabled and successful, writes to the file.
     * Otherwise, writes to System.err.
     *
     * @param message the message to be logged
     */
    public void log(String message) {
        String logMessage = System.currentTimeMillis() + " " + message;

        if (isFileLoggingEnabled && printOut != null) {
            // Log to file if file logging is enabled
            printOut.println(logMessage);
        } else {
            // Log to System.err if file logging is not enabled or fails
            System.err.println(logMessage);
        }
    }
}