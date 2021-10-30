package me.vilmu.waterwars.utilities.exceptions;

@SuppressWarnings("serial")
public abstract class DataHandlingException extends Exception {
    /**
     * Creates a new instance of
     * <code>DataHandlingException</code>
     */
    public DataHandlingException(String message, Exception ex) {
        super(message, ex);
    }
}
