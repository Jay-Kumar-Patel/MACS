import Database.MysqlConnectionImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class MysqlConnectionImplTest {

    private MysqlConnectionImpl mysqlConnection;

    @BeforeEach
    public void setUp() {
        mysqlConnection = MysqlConnectionImpl.getInstance();
    }

    @Test
    public void getInstance_ShouldReturnSameInstance() {
        MysqlConnectionImpl instance1 = MysqlConnectionImpl.getInstance();
        MysqlConnectionImpl instance2 = MysqlConnectionImpl.getInstance();

        assertSame(instance1, instance2);
    }
}
