package memory.log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Log {

    private LogLevel logLevel;

    private final Writer writer;

    public Log() {
        this.logLevel = LogLevel.INFO;
        this.writer = new OutputStreamWriter(System.out);
    }

    public Log(Writer writer) {
        this.logLevel = LogLevel.INFO;
        this.writer = writer;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public void debug(Object msg) throws IOException {
        if (logLevel.severer(LogLevel.DEBUG)) {
            writer.write(msg.toString());
        }
    }

    public void info(Object msg) throws IOException {
        if (logLevel.severer(LogLevel.INFO)) {
            writer.write(msg.toString());
        }
    }

    public void warn(Object msg) throws IOException {
        if (logLevel.severer(LogLevel.WARN)) {
            writer.write(msg.toString());
        }
    }

    public void error(Object  msg) throws IOException {
        if (logLevel.severer(LogLevel.ERROR)) {
            writer.write(msg.toString());
        }
    }

}
