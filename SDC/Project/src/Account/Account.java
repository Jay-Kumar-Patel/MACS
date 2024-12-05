package Account;

import Dao.AccountDao;

public interface Account {
    int createAccount(AccountDao account);
    boolean changeAdvisor(AccountDao account);
}
