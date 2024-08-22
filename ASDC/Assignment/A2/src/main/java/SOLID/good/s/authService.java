package SOLID.good.s;

public class authService {

    private Users users;
    public Log log;
    public Validation validation;

    public authService(){
        users = new Users();
        log = new TxtLog();
        validation = new Validation();
    }


    public boolean logIn(String userEmail, int userPin){

        if (validation.isEmailValid(userEmail) && validation.isPinValid(userPin)){
            if (validation.isUserValid(userEmail, userPin, users.getAllUsers())){
                if (!users.getCurrentLoginUsers().contains(userEmail)){
                    users.getCurrentLoginUsers().add(userEmail);
                    log.writeLog("Authentication", "logIn Successfully");
                    return true;
                }
                else {
                    log.writeLog("Authentication", "logIn Failed because user already logged In!");
                }
            }
            else {
                log.writeLog("Authentication", "logIn Failed due to user is not Valid!");
            }
        }
        else{
            log.writeLog("Authentication", "logIn Failed due to invalid input of email or pin!");
        }

        return false;
    }

    public boolean logOut(String userEmail){

        if (users.getCurrentLoginUsers().contains(userEmail)){
            users.getCurrentLoginUsers().remove(userEmail);
            log.writeLog("Authentication", "logOut Successfully");
            return true;
        }
        else{
            log.writeLog("Authentication", "logOut Failed because user is already logged out!");
        }

        return false;
    }

    public boolean register(String userEmail, int userPin){

        if (validation.isEmailValid(userEmail) && validation.isPinValid(userPin)){
            if (!validation.isEmailAlreadyExist(userEmail, users.getAllUsers()) && !validation.isPINAlreadyExist(userPin, users.getAllUsers())){
                users.getAllUsers().put(userEmail, userPin);
                log.writeLog("Authentication", "Register Successfully");
                return true;
            }
            else {
                log.writeLog("Authentication", "Registration Failed due to email or pin already exist!");
            }
        }
        else {
            log.writeLog("Authentication", "Registration Failed due to input email or pin invalid!");
        }

        return false;

    }


    public void updateLoginState(String userEmail) {

        if (validation.isEmailAlreadyExist(userEmail, users.getAllUsers())){
            boolean logOut = false;

            for (String str : users.getCurrentLoginUsers()){
                if (str.equals(userEmail)){
                    logOut = true;
                    users.getCurrentLoginUsers().remove(userEmail);
                    System.out.println("Change from logIn to logOut!");
                    return;
                }
            }

            if (!logOut){
                users.getCurrentLoginUsers().add(userEmail);
                System.out.println("Change from logOut to logIn!");
                return;
            }
        }
        else {
            System.out.println("Invalid User Email!");
        }

    }


}
