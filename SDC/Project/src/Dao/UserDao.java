package Dao;

public class UserDao {

    private int id;
    private String name;
    public enum Role{
        INVESTOR,
        ADVISOR
    }
    private Role role;

    public UserDao(){}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public String getRoleName(){
        return this.role.toString();
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
