import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataCollectionReceiverTest {

    @Test
    public void shouldReturnOverallTotalForCustomer() {
        // Arrange
        Map<Integer, List<Integer>> taskData = new HashMap<>();
        taskData.put(1, Arrays.asList(100, 200, 150));
        int customerId = 1;
        int expectedOverallTotal = 450;

        // Act
        List<Integer> totals = taskData.get(customerId);
        int overallTotal = 0;

        if (totals != null && totals.size() == 3) {
            for (int t : totals) {
                overallTotal += t;
            }
            taskData.remove(customerId);
        }

        // Assert
        assertEquals(expectedOverallTotal, overallTotal);
    }

    @Test
    public void shouldAddTotalToCustomerList() {
        // Arrange
        Map<Integer, List<Integer>> taskData = new HashMap<>();
        List<Integer> customerTotals = new ArrayList<>();
        taskData.put(1, customerTotals);
        int customerId = 1;
        int total = 100;

        // Act
        List<Integer> totals = taskData.get(customerId);

        if (totals != null) {
            totals.add(total);
        }

        // Assert
        assertEquals(1, customerTotals.size());
        assertEquals(total, customerTotals.get(0));
    }
}
