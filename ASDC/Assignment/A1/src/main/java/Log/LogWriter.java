package Log;

import java.io.IOException;

public interface LogWriter {

    void setFilePath(String filePath);
    boolean Write(String type, String module, String logMessage);

}
