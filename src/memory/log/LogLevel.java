package memory.log;

public enum LogLevel {

    DEBUG, INFO, WARN, ERROR;

    public boolean severer(LogLevel logLevel) {
        return ordinal() >= logLevel.ordinal();
    }

}
