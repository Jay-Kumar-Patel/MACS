package SOLID.bad.s;

import java.util.List;
import java.util.Map;

public class Validation {

    public Validation(){}

    public boolean isEmailValid(String email){
        if (email == null || email.isBlank()){
            return false;
        }

        if (!email.contains("@")){
            System.out.println("Invalid Email Format!");
            return false;
        }

        if (!email.contains(".com") || !email.contains(".in")){
            System.out.println("Invalid Mail Provider!");
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

    public boolean isEmailAlreadyExist(String email, Map<String, Integer> allUsers){

        for (Map.Entry<String, Integer> currUser : allUsers.entrySet()){
            if (currUser.getKey().equals(email)){
                return true;
            }
        }

        return false;
    }

    public boolean isPINAlreadyExist(int PIN, Map<String, Integer> allUsers){

        for (Map.Entry<String, Integer> currUser : allUsers.entrySet()){
            if (currUser.getValue().equals(PIN)){
                return true;
            }
        }

        return false;
    }

    public boolean isUserValid(String email, int PIN, Map<String, Integer> allUsers, List<String> currentLoginUsers){

        for (Map.Entry<String, Integer> currUser : allUsers.entrySet()){
            if (currUser.getKey().equals(email) && currUser.getValue().equals(PIN)){
                return true;
            }
        }

        return false;
    }

}
