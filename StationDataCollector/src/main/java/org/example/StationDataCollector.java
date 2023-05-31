package org.example;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class StationDataCollector {

    public StationDataCollector() throws IOException, TimeoutException {
        Queue.handleQueue();
    }

}
