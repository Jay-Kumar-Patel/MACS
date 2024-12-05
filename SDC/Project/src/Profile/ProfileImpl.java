package Profile;

import Dao.ProfileDao;
import Database.MysqlConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import Exception.*;

public class ProfileImpl implements Profile{

    /**
     * Method to Add new Profile.
     * @param profile : Profile Details like profile name and map in which mention each sector name and its percentage.
     * @return : If sector is successfully added than return ID of that sector, else return -1.
     */
    @Override
    public int defineProfile(ProfileDao profile)
    {
        try {

            int newProfileID = -1;

            //Open Connection
            Connection connection = (Connection) MysqlConnection.getInstance().StartConnection();

            if (connection == null){
                throw new SQLException();
            }

            //Check all the sector present in given map is valid or not
            Map<Integer, Integer> allSectorID = sectorValidation(profile);

            if (allSectorID == null){
                throw new CustomException("Given Sectors does not Exist!");
            }

            //Change autocommit to false.
            connection.setAutoCommit(false);

            //Create Query to add new Profile
            String insertQuery = "";
            if (profile.getSectorHoldings().containsKey("Cash")){
                insertQuery = String.format("INSERT INTO profile (Name, Cash) VALUES ('%s', '%d')", profile.getProfileName(), profile.getSectorHoldings().get("Cash"));
            }
            else{
                insertQuery = String.format("INSERT INTO profile (Name, Cash) VALUES ('%s', '%d')", profile.getProfileName(), profile.getSectorHoldings().get("cash"));
            }

            //Execute Query
            List<Integer> lastInsertedIdProfile = MysqlConnection.getInstance().ExecuteQueries(new ArrayList<String>(Arrays.asList(insertQuery)), new ArrayList<Boolean>(Arrays.asList(true)), "Creating New Profile: " + profile.getProfileName());

            //Result
            if (lastInsertedIdProfile != null && lastInsertedIdProfile.get(0) > 0){
                newProfileID = lastInsertedIdProfile.get(0);
            }
            else {
                connection.setAutoCommit(true);
                throw new CustomException("Failed to Create New Profile: " + profile.getProfileName());
            }

            List<String> quries = new ArrayList<>();
            List<Boolean> wantLastInsertedID = new ArrayList<>();

            //Create Query to insert each sector present in this profile.
            for (Map.Entry<Integer, Integer> entry : allSectorID.entrySet()){
                quries.add(String.format("INSERT INTO profile_sector_info (Profile_ID, Sector_ID, Percentage) VALUES ('%d', '%d', '%d')", newProfileID, entry.getKey(), entry.getValue()));
                wantLastInsertedID.add(false);
            }

            //Execute Queries.
            List<Integer> lastInsertedID = MysqlConnection.getInstance().ExecuteQueries(quries, wantLastInsertedID, "Insert Profile Sector Holdings for profile name: " + profile.getProfileName());

            if (lastInsertedID != null){
                for (Integer i : lastInsertedID){
                    if (i < 0){
                        newProfileID = -1;
                        break;
                    }
                }
            }

            //Result
            if (newProfileID > 0)
            {
                connection.commit();
                connection.setAutoCommit(true);
            }
            else
            {
                connection.rollback();
                connection.setAutoCommit(true);
                throw new CustomException("Failed to Create New Profile!");
            }

            //Close Connection
            MysqlConnection.getInstance().CloseConnection();

            return newProfileID;
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


    /**
     * Method to Validate each sector for new profile.
     * @param profile : Profile Details like profile name and map in which mention each sector name and its percentage.
     * @return : Return
     */
    private Map<Integer, Integer> sectorValidation(ProfileDao profile)
    {
        //Validation Query
        String readQuery = String.format("Select * from profile Where Name='%s'", profile.getProfileName());

        if (MysqlConnection.getInstance().isDataPresent(readQuery, "Check " + profile.getProfileName() + " profile is already exist or not to create a new profile.", false, new int[]{})){
            return null;
        }

        //Now Convert Sector Name to its Respective Sector ID.
        return getSectorID(profile.getSectorHoldings());
    }


    /**
     * Method to Change Sector Name to its ID.
     * @param sectorHoldings : Map in which sector name and its percentage in new Profile is available.
     * @return : Return map of Sector ID and its percentage. In case of any error return null.
     */
    private Map<Integer, Integer> getSectorID(Map<String, Integer> sectorHoldings)
    {
        try {
            Map<Integer, Integer> allSectorID = new HashMap<>();

            //Iterate through each sector and check it is present ot not. If yes than get ID of that sector.
            for(Map.Entry<String, Integer> entry : sectorHoldings.entrySet())
            {
                //If sector is cash, directly continue to next sector.
                if (entry.getKey().equals("Cash") || entry.getKey().equals("cash")){
                    continue;
                }

                //Create Query
                String readQuerySector = String.format("Select * from sector Where Name='%s'", entry.getKey().trim().toUpperCase());

                int[] sectorID = new int[1];

                //Execute Query
                if (!MysqlConnection.getInstance().isDataPresent(readQuerySector, "Check " + entry.getKey() + " sector is already exist or not for creating new profile", true, sectorID)){
                    throw new CustomException("Sector (" + entry.getKey() + ") does not exist!");
                }
                else {
                    allSectorID.put(sectorID[0], entry.getValue());
                }
            }

            return allSectorID;
        }
        catch (CustomException customException){
            System.out.println(customException.getMessage());
            return null;
        }
    }
}
