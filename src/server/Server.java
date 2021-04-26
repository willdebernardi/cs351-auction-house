package server;

import java.util.HashMap;
import java.util.concurrent.Executor;

public class Server {
    private Executor exec;
    private HashMap<String, Endpoint> endpoints;

    /**
     * @param port the port for the Server to listen on
     */
    public Server(int port) {

    }

    /**
     * Starts the server, listening on the port specified in the constructor.
     * Creates new threads for each request given.
     */
    public void run() {

    }
}
