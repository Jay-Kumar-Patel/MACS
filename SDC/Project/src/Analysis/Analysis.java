package Analysis;

import java.util.Map;
import java.util.Set;

public interface Analysis {

    Map<String, Boolean> stockRecommendations(int accountId, int maxRecommendations, int
            numComparators );

    Set<Set<Integer>> advisorGroups(double tolerance, int maxGroups );
}
