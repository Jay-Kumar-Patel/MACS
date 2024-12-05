package Sector;

import Dao.SectorDao;
import Database.MysqlConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Exception.*;

public class SectorImpl implements Sector{

    @Override
    public int defineSector(SectorDao sector) {

        try{

            //Start Connection
            if (MysqlConnection.getInstance().StartConnection() == null){
                throw new SQLException();
            }

            //Check Sector is already exist or not.
            String readQuery = String.format("Select * from sector Where Name='%s'", sector.getSectorName());

            if (MysqlConnection.getInstance().isDataPresent(readQuery, "Check " + sector.getSectorName() + " sector is already exist or not to create a new sector.", false, new int[]{})){
                throw new CustomException("Sector (" + sector + ") is already exist!");
            }

            //Create Query to create new sector.
            String insertQuery = String.format("INSERT INTO sector (name) VALUES ('%s')", sector.getSectorName());

            //Execute Query
            List<Integer> lastInsertedId = MysqlConnection.getInstance().ExecuteQueries(new ArrayList<String>(Arrays.asList(insertQuery)), new ArrayList<Boolean>(Arrays.asList(true)), "Creating New " + sector.getSectorName());

            //Close Connection
            MysqlConnection.getInstance().CloseConnection();

            //Result
            if (lastInsertedId != null && lastInsertedId.get(0) > 0){
                return lastInsertedId.get(0);
            }
            else {
                throw new CustomException("Unable to add new Sector: " + sector + "!");
            }
        }
        catch (SQLException sqlException)
        {
            MysqlConnection.getInstance().CloseConnection();
            System.out.println(sqlException.getMessage());
            return -1;
        }
        catch (CustomException customException){
            MysqlConnection.getInstance().CloseConnection();
            System.out.println(customException.getMessage());
            return -1;
        }
    }
}
