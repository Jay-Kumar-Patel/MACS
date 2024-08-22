package SOLID.good.s;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Users {

    private Map<String, Integer> allUsers;
    private List<String> currentLoginUsers;

    public Users(){
        allUsers = new HashMap<>();
        allUsers.put("jay@gmail.com", 123456);
        allUsers.put("jems@gmail.com", 789654);
        allUsers.put("rutvik@gmail.com", 458745);
        currentLoginUsers = new ArrayList<>();
    }

    public Map<String, Integer> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(Map<String, Integer> allUsers) {
        this.allUsers = allUsers;
    }

    public List<String> getCurrentLoginUsers() {
        return currentLoginUsers;
    }

    public void setCurrentLoginUsers(List<String> currentLoginUsers) {
        this.currentLoginUsers = currentLoginUsers;
    }
}
