import Dao.User;
import DriverClass.LibraryManagementSystem;
import DriverClass.UtilFunctions;
import Repositories.UserRepo;
import Services.UserManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserManagementTest {

    @Mock
    private UserRepo userRepo;
    @InjectMocks
    private UserManagement userManagement;
    @InjectMocks
    private UtilFunctions utilFunctions;
    @InjectMocks
    private User user;
    @InjectMocks
    LibraryManagementSystem libraryManagementSystem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        utilFunctions = new UtilFunctions();
        userManagement = new UserManagement(userRepo, utilFunctions);

        libraryManagementSystem = new LibraryManagementSystem();
        libraryManagementSystem.SetUserManagement(userManagement);

        user = new User();
        user.setUserID("1");
        user.setEmail("jay@gmail.com");
        user.setName("John Doe");
        user.setMobileNumber(1234567890);
        user.setAddress("123, Example St.");
        user.setDateOfEnroll(LocalDate.now());
        user.setBorrowedBooks(null);
    }

    //Junit test cases to create new user or update user details.
    @Test
    void nullEmptyUser() {

        User testUser = null;
        assertFalse( libraryManagementSystem.registerUser(testUser), "null user" );
        assertFalse( libraryManagementSystem.updateUserDetails(testUser), "null user" );


        testUser = new User();
        assertFalse( libraryManagementSystem.registerUser(testUser), "empty user" );
        assertFalse( libraryManagementSystem.updateUserDetails(testUser), "empty user" );
    }

    @Test
    void nullEmptyUserName() {
        User testUser = new User();
        user.setEmail("jay@gmail.com");
        user.setMobileNumber(1234567890);
        user.setAddress("123, Example St.");
        assertFalse( libraryManagementSystem.registerUser(testUser), "empty username" );
        assertFalse( libraryManagementSystem.updateUserDetails(testUser), "empty username" );

        user.setName(null);
        assertFalse( libraryManagementSystem.registerUser(testUser), "null username" );
        assertFalse( libraryManagementSystem.updateUserDetails(testUser), "null username" );
    }

    @Test
    void nullEmptyUserEmail() {
        User testUser = new User();
        user.setMobileNumber(1234567890);
        user.setName("John Doe");
        user.setAddress("123, Example St.");
        assertFalse( libraryManagementSystem.registerUser(testUser), "empty useremail" );
        assertFalse( libraryManagementSystem.updateUserDetails(testUser), "empty useremail" );

        user.setEmail(null);
        assertFalse( libraryManagementSystem.registerUser(testUser), "null useremail" );
        assertFalse( libraryManagementSystem.updateUserDetails(testUser), "null useremail" );

        user.setEmail("jay");
        assertFalse( libraryManagementSystem.registerUser(testUser), "format of email is incorrect" );
        assertFalse( libraryManagementSystem.updateUserDetails(testUser), "format of email is incorrect" );
    }

    @Test
    void nullEmptyUserAddress() {
        User testUser = new User();
        user.setEmail("jay@gmail.com");
        user.setName("John Doe");
        user.setMobileNumber(1234567890);
        assertFalse( libraryManagementSystem.registerUser(testUser), "empty userAddress" );
        assertFalse( libraryManagementSystem.updateUserDetails(testUser), "empty userAddress" );

        user.setAddress(null);
        assertFalse( libraryManagementSystem.registerUser(testUser), "null userAddress" );
        assertFalse( libraryManagementSystem.updateUserDetails(testUser), "null userAddress" );
    }

    @Test
    void nullEmptyUserMobileNumber() {
        User testUser = new User();
        testUser.setName("John Doe");
        testUser.setAddress("123, Example St.");
        testUser.setEmail("jay@gmail.com");
        assertFalse( libraryManagementSystem.registerUser(testUser), "user mobile number is null while creating new user" );
        assertFalse( libraryManagementSystem.updateUserDetails(testUser), "user mobile number is null while updating user details" );

        user.setMobileNumber(0);
        assertFalse( libraryManagementSystem.registerUser(testUser), "user mobile number was set as 0 while creating new user" );
        assertFalse( libraryManagementSystem.updateUserDetails(testUser), "user mobile number is null while updating user details" );

        user.setMobileNumber(123);
        assertFalse( libraryManagementSystem.registerUser(testUser), "user mobile number was not of 10 digits while creating new user" );
        assertFalse( libraryManagementSystem.updateUserDetails(testUser), "user mobile number is null while updating user details" );
    }


    //Mockito test cases to create new user.
    @Test
    void registerUser_Success() {

        when(userRepo.isEmailAlreadyExist(anyString())).thenReturn(false);
        when(userRepo.createUser(any())).thenReturn(true);

        assertTrue(libraryManagementSystem.registerUser(user));
    }

    @Test
    void registerUser_FailureByEmail() {

        when(userRepo.isEmailAlreadyExist(anyString())).thenReturn(true);

        assertFalse(libraryManagementSystem.registerUser(user));
    }

    @Test
    void registerUser_FailureByDatabaseError() {

        System.out.println(user.getDateOfEnroll());

        when(userRepo.isEmailAlreadyExist(anyString())).thenReturn(false);
        when(userRepo.createUser(any())).thenReturn(false);

        assertFalse(libraryManagementSystem.registerUser(user));
    }


    //Mockito test cases to update user details.
    @Test
    void updateUser_Success() {

        when(userRepo.isEmailAlreadyExist(anyString())).thenReturn(true);
        when(userRepo.updateUser(any())).thenReturn(true);

        assertTrue(libraryManagementSystem.updateUserDetails(user));
    }

    @Test
    void updateUser_FailureByEmail() {

        when(userRepo.isEmailAlreadyExist(anyString())).thenReturn(false);

        assertFalse(libraryManagementSystem.updateUserDetails(user));
    }

    @Test
    void updateUser_FailureByDatabaseError() {

        when(userRepo.isEmailAlreadyExist(anyString())).thenReturn(true);
        when(userRepo.createUser(any())).thenReturn(false);

        assertFalse(libraryManagementSystem.updateUserDetails(user));
    }


    //Junit test cases to delete user.
    @Test
    void nullEmptyUserIDDeletion() {
        User testUser = new User();
        user.setName("John Doe");
        user.setAddress("123, Example St.");
        user.setEmail("jay@gmail.com");
        user.setMobileNumber(123);
        assertFalse( libraryManagementSystem.deleteUser(testUser.getUserID()), "empty userID" );

        user.setUserID(null);
        assertFalse( libraryManagementSystem.deleteUser(testUser.getUserID()), "null userID" );
    }


    //Mockito test cases to delete user.
    @Test
    void deleteUser_Success() {

        user.setUserID("1");
        when(userRepo.isUserExist(anyString())).thenReturn(true);
        when(userRepo.checkUserPendingBooks(anyString())).thenReturn(new ArrayList<>());
        when(userRepo.deleteUser(anyString())).thenReturn(true);

        assertTrue(libraryManagementSystem.deleteUser(user.getUserID()));
    }

    @Test
    void deleteUser_FailureByUserExist() {

        when(userRepo.isUserExist(anyString())).thenReturn(false);

        assertFalse(libraryManagementSystem.deleteUser(user.getUserID()));
    }

    @Test
    void deleteUser_FailureByUserPandingBooks() {

        when(userRepo.isUserExist(anyString())).thenReturn(true);

        List<Map<String, LocalDate>> arr = new ArrayList<>();
        Map<String, LocalDate> map = new HashMap<>();
        map.put("A", LocalDate.now());
        arr.add(map);
        when(userRepo.checkUserPendingBooks(anyString())).thenReturn(arr);

        System.out.println(user.getBorrowedBooks());

        assertFalse(libraryManagementSystem.deleteUser(user.getUserID()));
    }

    @Test
    void deleteUser_FailureByDatabaseError() {

        when(userRepo.isUserExist(anyString())).thenReturn(true);
        when(userRepo.checkUserPendingBooks(anyString())).thenReturn(new ArrayList<>());
        when(userRepo.deleteUser(anyString())).thenReturn(false);

        assertFalse(libraryManagementSystem.deleteUser(user.getUserID()));
    }


    //Junit test cases to fetch user details.
    @Test
    void nullEmptyUserIDFetchData() {

        user.setUserID("");
        assertEquals(null ,libraryManagementSystem.getUser(user.getUserID()));

        user.setUserID(null);
        assertEquals( null, libraryManagementSystem.getUser(user.getUserID()));
    }


    //Mockito test cases to fetch user details.
    @Test
    void getUser_Success() {

        when(userRepo.isUserExist(anyString())).thenReturn(true);
        when(userRepo.getUser(anyString())).thenReturn(user);

        assertEquals(user, libraryManagementSystem.getUser(user.getUserID()));
    }

    @Test
    void getUser_FailureByUserExist() {

        when(userRepo.isUserExist(anyString())).thenReturn(false);

        assertEquals( null, libraryManagementSystem.getUser(user.getUserID()));
    }

    @Test
    void getUser_FailureByDatabaseError() {

        when(userRepo.isUserExist(anyString())).thenReturn(true);
        when(userRepo.getUser(anyString())).thenReturn(null);

        assertEquals( null, libraryManagementSystem.getUser(user.getUserID()));
    }

}
