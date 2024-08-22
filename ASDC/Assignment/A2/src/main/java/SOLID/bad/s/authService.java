package SOLID.bad.s;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class authService {

    private Map<String, Integer> allUsers;
    private List<String> currentLoginUsers;
    private Validation validation;

    public authService(){
        allUsers = new HashMap<>();
        allUsers.put("jay@gmail.com", 123456);
        allUsers.put("jems@gmail.com", 789654);
        allUsers.put("rutvik@gmail.com", 458745);
        currentLoginUsers = new ArrayList<>();
        validation = new Validation();
    }

    public boolean logIn(String userEmail, int userPin){

        if (validation.isEmailValid(userEmail) && validation.isPinValid(userPin)){
            if (validation.isUserValid(userEmail, userPin, allUsers, currentLoginUsers)){
                if (!currentLoginUsers.contains(userEmail)){
                    currentLoginUsers.add(userEmail);
                    log("Authentication", "logIn Successfully");
                    return true;
                }
                else {
                    log("Authentication", "logIn Failed because user already logged In!");
                }
            }
            else {
                log("Authentication", "logIn Failed due to user is not Valid!");
            }
        }
        else{
            log("Authentication", "logIn Failed due to invalid input of email or pin!");
        }

        return false;
    }

    public boolean logOut(String userEmail){

        if (currentLoginUsers.contains(userEmail)){
            currentLoginUsers.remove(userEmail);
            log("Authentication", "logOut Successfully");
            return true;
        }
        else{
            log("Authentication", "logOut Failed because user is already logged out!");
        }

        return false;
    }

    public boolean register(String userEmail, int userPin){

        if (validation.isEmailValid(userEmail) && validation.isPinValid(userPin)){
            if (!validation.isEmailAlreadyExist(userEmail, allUsers) && !validation.isPINAlreadyExist(userPin, allUsers)){
                allUsers.put(userEmail, userPin);
                log("Authentication", "Register Successfully");
                return true;
            }
            else {
                log("Authentication", "Registration Failed due to email or pin already exist!");
            }
        }
        else {
            log("Authentication", "Registration Failed due to input email or pin invalid!");
        }

        return false;

    }

    public void updateLoginState(String userEmail) {

        if (validation.isEmailAlreadyExist(userEmail, allUsers)){
            boolean logOut = false;

            for (String str : currentLoginUsers){
                if (str.equals(userEmail)){
                    logOut = true;
                    currentLoginUsers.remove(userEmail);
                    System.out.println("Change from logIn to logOut!");
                    return;
                }
            }

            if (!logOut){
                currentLoginUsers.add(userEmail);
                System.out.println("Change from logOut to logIn!");
                return;
            }
        }
        else {
            System.out.println("Invalid User Email!");
        }

    }

    public boolean log(String moduleName, String message){
        try (FileWriter writer = new FileWriter(Paths.get(System.getProperty("user.home"), "Desktop", "log.txt").toString(), true)){

            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write("[" + String.valueOf(new Timestamp(System.currentTimeMillis())) + "] [" + moduleName + "] [" + message + "]");

            bufferedWriter.newLine();

            bufferedWriter.close();

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}
