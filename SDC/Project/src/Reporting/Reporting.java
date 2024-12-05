package Reporting;

import java.util.Map;
import java.util.Set;

public interface Reporting {

    double accountValue( int accountId );

    double advisorPortfolioValue( int advisorId );

    Map<Integer, Double> investorProfit( int clientId );

    Map<String, Integer> profileSectorWeights(int accountId );

    Set<Integer> divergentAccounts(int tolerance );
}
