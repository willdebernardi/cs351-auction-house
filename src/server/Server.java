package server;

import java.util.HashMap;
import java.util.concurrent.Executor;

public class Server {
    private Executor exec;
    private HashMap<String, Endpoint> endpoints;

    /**
     * Creates a new server. By default registers an endpoint with the url
     * "listen", which registers a client as a listener for some Resource in the
     * DataStore.
     *
     * @param port the port for the Server to listen on
     */
    public Server(int port) {

    }

    /**
     * Starts the server, listening on the port specified in the constructor.
     * Creates new threads for each new connection.
     */
    public void run() {

    }

    /**
     * Registers a new endpoint for the server, to be specified by the endpoint
     * URL in a Request object when a Client makes a request.
     *
     * @param url the url of the endpoint
     * @param endpoint the endpoint
     */
    public void addEndpoint(String url, Endpoint endpoint) {

    }

    private void processRequest(Request r) {

    }
}
