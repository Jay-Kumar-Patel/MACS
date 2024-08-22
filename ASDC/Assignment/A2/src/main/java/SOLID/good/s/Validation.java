package SOLID.good.s;

import java.util.List;
import java.util.Map;

public class Validation {

    public boolean isEmailValid(String email){
        if (email == null || email.isBlank()){
            return false;
        }

        if (!email.contains("@gmail.com")){
            return false;
        }

        return true;
    }

    public boolean isPinValid(int PIN){

        int tempPIN = PIN;
        int length = 0;

        if (tempPIN == 0){
            length = 1;
        }
        else {
            while (tempPIN != 0){
                if (tempPIN%10 == 0){
                    return false;
                }
                tempPIN = tempPIN / 10;
                length++;
            }
        }

        if (length != 6){
            return false;
        }

        return true;
    }

    public boolean isEmailAlreadyExist(String email, Map<String, Integer> allusers){

        for (Map.Entry<String, Integer> currUser : allusers.entrySet()){
            if (currUser.getKey().equals(email)){
                return true;
            }
        }

        return false;
    }

    public boolean isPINAlreadyExist(int PIN, Map<String, Integer> allusers){

        for (Map.Entry<String, Integer> currUser : allusers.entrySet()){
            if (currUser.getValue().equals(PIN)){
                return true;
            }
        }

        return false;
    }

    public boolean isUserValid(String email, int PIN, Map<String, Integer> allusers){

        for (Map.Entry<String, Integer> currUser : allusers.entrySet()){
            if (currUser.getKey().equals(email) && currUser.getValue().equals(PIN)){
                return true;
            }
        }

        return false;
    }

}
