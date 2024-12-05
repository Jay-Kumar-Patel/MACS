package Database;

import java.util.List;

public interface DBConnection {
    Object StartConnection();
    void CloseConnection();
}
