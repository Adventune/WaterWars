package me.vilmu.waterwars.utilities.exceptions;

@SuppressWarnings("serial")
public class SerializationException extends DataHandlingException {
    public SerializationException(Object o, Exception ex) {
        super("Error while serializing: " + String.valueOf(o), ex);
    }
}