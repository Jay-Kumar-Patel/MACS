package Database;

import com.mysql.cj.MysqlConnection;

public class MysqlConnectionImpl implements MySqlConnection{
    private static MysqlConnectionImpl mysqlDatabase;

    public static MysqlConnectionImpl getInstance()
    {
        if (mysqlDatabase == null) {
            //Use Synchronized, so that in case of multithreading or multiple user create object at the same time
            // only one thread goes inside this block.
            synchronized (MysqlConnection.class) {
                if (mysqlDatabase == null){
                    //Create one time object.
                    mysqlDatabase = new MysqlConnectionImpl();
                }
            }
        }

        return mysqlDatabase;
    }

    @Override
    public MySqlConnection StartConnection() {
        return null;
    }

    @Override
    public boolean CheckConnection() {
        return true;
    }

    @Override
    public boolean CloseConnection() {
        return true;
    }
}
