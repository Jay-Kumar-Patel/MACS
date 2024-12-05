package Log;

public interface LogWriter {
    void Write(String type, String module, String logMessage);
}
