import org.example.Charge;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StationDataCollectorTest {

    @Test
    public void shouldReturnTotalKwhForCustomer() {
        // Arrange
        List<Charge> stationData = Arrays.asList(
                new Charge(1, 100, 1),
                new Charge(2, 200, 2),
                new Charge(3, 150, 1),
                new Charge(4, 300, 3)
        );

        int customer_id = 1;
        int expectedTotalKwh = 250;

        // Act
        int totalKwh = stationData.stream()
                .filter(data -> data.getCustomer_id() == customer_id)
                .mapToInt(Charge::getKwH)
                .sum();

        // Assert
        assertEquals(expectedTotalKwh, totalKwh);
    }
}

