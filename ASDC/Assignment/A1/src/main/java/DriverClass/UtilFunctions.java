package DriverClass;

import Database.MySqlConnection;
import Database.MysqlConnectionImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilFunctions {

    private MySqlConnection mySqlConnection;

    public UtilFunctions(){
        mySqlConnection = MysqlConnectionImpl.getInstance();
    }

    public boolean isMobileNumberValid(double mobileNumber){
        int count = 0;
        double tempNumber = mobileNumber;

        if (mobileNumber == 0) {
            count = 1;
        } else {
            while (tempNumber != 0) {
                tempNumber = (int) tempNumber / 10;
                count++;
            }
        }

        if (count == 10)
            return true;
        else
            return false;
    }

    public boolean isEmailValid(String email){
        // Regular expression for email validation
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

}
