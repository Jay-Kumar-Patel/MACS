package SOLID.good.s;

public class DriverClass {

    public static void main(String[] args) {
        SOLID.bad.s.authService authService = new SOLID.bad.s.authService();
        System.out.println(authService.logIn("kenil@gmail.com", 124741));
        System.out.println(authService.register("kenil@gmail.com", 124));
        System.out.println(authService.register("kenilgmail.com", 124741));
        System.out.println(authService.register("kenil@gmail.com", 124741));
        System.out.println(authService.logIn("kenil@gmail.com", 124741));
        System.out.println(authService.logOut("jay@gmail.com"));
        System.out.println(authService.logOut("kenil@gmail.com"));
        authService.updateLoginState("rutvik@gmail.com");
        authService.updateLoginState("shrey@gmail.com");
    }

}
