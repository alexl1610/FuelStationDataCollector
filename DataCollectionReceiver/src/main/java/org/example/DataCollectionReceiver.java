package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class DataCollectionReceiver {

    public DataCollectionReceiver() throws IOException, TimeoutException {
        Queue.handleQueue();
    }

    //taskData is a Map that stores the data for each customer
    // key is the customer ID
    // value is a list of integers representing the totals
    static Map<Integer, List<Integer>> taskData = new HashMap<>();

    public static void processTaskStart(int customerId) {
        // Initialize an empty list for the customer if it doesn't exist
        taskData.putIfAbsent(customerId, new ArrayList<>());
        System.out.println(" [*] Receiving the data from customer with ID: " + customerId);
    }

    public static void processTotal(int customerId, int total) {
        List<Integer> totals = taskData.get(customerId);
        if (totals != null) {
            // Add the total to the customer's list
            totals.add(total);
        }
    }

    public static int calculateOverallTotal(int customerId) {
        List<Integer> totals = taskData.get(customerId);
        if (totals != null && totals.size() == 3) {
            int overallTotal = 0;
            for (int t : totals) {
                overallTotal += t;
            }
            //System.out.println("Overall total for Customer ID " + customerId + ": " + overallTotal);

            taskData.remove(customerId);

            return overallTotal;
        }
        return 0;
    }
}