import Dao.User;
import Database.MysqlConnectionImpl;
import Repositories.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserRepoTest {
    @Mock
    private MysqlConnectionImpl mysqlConnection;

    @InjectMocks
    private UserRepo userRepo;

    @InjectMocks
    User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user.setName("User");
        user.setMobileNumber(1234567891);
        user.setDateOfEnroll(LocalDate.now());
        user.setAddress("Address");
        user.setUserID("1");
        user.setEmail("user@gmail.com");
        user.setBorrowedBooks(null);
    }

    @Test
    public void createUser_Success() {
        when(mysqlConnection.CheckConnection()).thenReturn(true);
        when(mysqlConnection.StartConnection()).thenReturn(mysqlConnection);
        when(mysqlConnection.CloseConnection()).thenReturn(true);

        assertTrue(userRepo.createUser(user));
    }

    @Test
    public void createUser_FailureByDatabaseError() {
        when(mysqlConnection.CheckConnection()).thenReturn(false);

        assertFalse(userRepo.createUser(user));
    }

    @Test
    public void updateUser_Success() {
        when(mysqlConnection.CheckConnection()).thenReturn(true);
        when(mysqlConnection.StartConnection()).thenReturn(mysqlConnection);
        when(mysqlConnection.CloseConnection()).thenReturn(true);

        userRepo.createUser(user);

        assertTrue(userRepo.updateUser(user));
    }

    @Test
    public void updateUser_Failure() {
        when(mysqlConnection.CheckConnection()).thenReturn(false);

        assertFalse(userRepo.updateUser(user));
    }

    @Test
    public void deleteUser_Success() {
        when(mysqlConnection.CheckConnection()).thenReturn(true);
        when(mysqlConnection.StartConnection()).thenReturn(mysqlConnection);
        when(mysqlConnection.CloseConnection()).thenReturn(true);

        userRepo.createUser(user);

        assertTrue(userRepo.deleteUser(user.getEmail()));
    }

    @Test
    public void deleteUser_Failure() {
        when(mysqlConnection.CheckConnection()).thenReturn(false);

        assertFalse(userRepo.deleteUser(user.getEmail()));
    }

    @Test
    public void getUser_Success() {
        when(mysqlConnection.CheckConnection()).thenReturn(true);
        when(mysqlConnection.StartConnection()).thenReturn(mysqlConnection);
        when(mysqlConnection.CloseConnection()).thenReturn(true);

        userRepo.createUser(user);

        assertNotNull(userRepo.getUser(user.getEmail()));
    }

    @Test
    public void getUser_Failure() {
        when(mysqlConnection.CheckConnection()).thenReturn(false);

        assertNull(userRepo.getUser(user.getEmail()));
    }

    @Test
    public void isUserExist_Success() {
        when(mysqlConnection.CheckConnection()).thenReturn(true);
        when(mysqlConnection.StartConnection()).thenReturn(mysqlConnection);
        when(mysqlConnection.CloseConnection()).thenReturn(true);

        userRepo.createUser(user);

        assertTrue(userRepo.isUserExist(user.getEmail()));
    }

    @Test
    public void isUserExist_Failure() {
        when(mysqlConnection.CheckConnection()).thenReturn(false);

        assertFalse(userRepo.isUserExist(user.getEmail()));
    }

    @Test
    public void checkUserPendingBooks_Success() {
        when(mysqlConnection.CheckConnection()).thenReturn(true);
        when(mysqlConnection.StartConnection()).thenReturn(mysqlConnection);
        when(mysqlConnection.CloseConnection()).thenReturn(true);

        List<Map<String, LocalDate>> result = userRepo.checkUserPendingBooks(user.getUserID());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void checkUserPendingBooks_Failure() {
        when(mysqlConnection.CheckConnection()).thenReturn(false);

        assertNull(userRepo.checkUserPendingBooks(user.getUserID()));
    }

    @Test
    public void isEmailAlreadyExist_Success() {
        when(mysqlConnection.CheckConnection()).thenReturn(true);
        when(mysqlConnection.StartConnection()).thenReturn(mysqlConnection);
        when(mysqlConnection.CloseConnection()).thenReturn(true);

        userRepo.createUser(user);

        assertTrue(userRepo.isEmailAlreadyExist(user.getEmail()));
    }

    @Test
    public void isEmailAlreadyExist_Failure() {
        when(mysqlConnection.CheckConnection()).thenReturn(false);

        assertFalse(userRepo.isEmailAlreadyExist(user.getEmail()));
    }
}
