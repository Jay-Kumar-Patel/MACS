package Database;

public interface MySqlConnection {

    MySqlConnection StartConnection();
    boolean CheckConnection();
    boolean CloseConnection();

}
