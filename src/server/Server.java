package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * The server itself.
 *
 * @author Isaiah Martell, Christopher Medlin, Will Debernardi
 * @date 26 Apr 2021
 */
public class Server {

    private HashMap<String, Endpoint> endpoints;
    private ServerSocket serverSocket;

    /**
     * Creates a new server. By default registers an endpoint with the url
     * "listen", which registers a client as a listener for some Resource in the
     * DataStore.
     *
     * @param port the port for the Server to listen on
     */
    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException ex) {
            System.out.println("Error creating server");
            ex.printStackTrace();
        }
    }

    /**
     * Starts the server, listening on the port specified in the constructor.
     * Creates new threads for each new connection.
     *
     */
    public void run() {
        //Create open ended thread pool
        ExecutorService threadPool = Executors.newCachedThreadPool();
        try {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Waiting for a connection to server");
                //Wait for client connection (blocking call)
                Socket clientSocket = serverSocket.accept();
                /* Create new client handler object for
                   socket and fork it in a background thread */
                threadPool.submit(new ClientHandler(clientSocket));
            }
        }
        catch(IOException ex) {
            System.out.println("Error starting server");
            ex.printStackTrace();
        }
        finally {
            threadPool.shutdown();
        }
    }

    /**
     * If server is running, stop it by killing the socket
     *
     */
    public void stop() {
        try {
            serverSocket.close();
        }
        catch (IOException e) {
            System.out.println("Error shutting down server");
            e.printStackTrace();
        }
    }

    /**
     * Registers a new endpoint for the server, to be specified by the endpoint
     * URL in a Request object when a Client makes a request.
     *
     * @param url the url of the endpoint
     * @param endpoint the endpoint
     */
    public void addEndpoint(String url, Endpoint endpoint) {
        endpoints.put(url, endpoint);
    }

    /**
     * Runnable that passes the request to the proper endpoint
     * and then returns a response to the client
     */
    private class ClientHandler implements Runnable {

        private Socket clientConnection;
        private ObjectInputStream io;
        private ObjectOutputStream os;

        /**
         * Creates a new client handler runnable
         * @param clientSocket  The client connection
         */
        public ClientHandler(Socket clientSocket) {
            try {
                this.clientConnection = clientSocket;
                this.io = new ObjectInputStream(
                        this.clientConnection.getInputStream()
                );
                this.os = new ObjectOutputStream(
                        this.clientConnection.getOutputStream()
                );
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (clientConnection.isClosed()) {
                try {
                    //Validate the object (check if request)
                    Object object = io.readObject();
                    if (object instanceof Request) {
                        errorMessage("Object not request");
                    }
                    //Convert object to a request object
                    Request request = (Request) object;
                    //Validate the endpoint
                    if (!endpoints.containsKey(request.getEndpointUrl())) {
                        errorMessage("Invalid endpoint");
                    }
                    //Pass the request to the proper endpoint and return
                    //response to client
                    Endpoint endpoint = endpoints
                            .get(request.getEndpointUrl());
                    os.writeObject(endpoint.call(request));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Utility method that gives an error response
         * @param msg  The message attached with the error
         * @throws IOException
         */
        private void errorMessage(String msg) throws IOException {
            os.writeObject(new Response(msg, null, Response.Type.ERROR));
        }
    }
}
