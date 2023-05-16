package org.example;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DataCollectionDispatcher {

    public DataCollectionDispatcher() throws IOException, TimeoutException {
        Queue.handleQueue();
    }
}
