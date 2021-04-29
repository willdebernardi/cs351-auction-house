/**
 * A request made to a server.
 *
 * @author Christopher Medlin, Will Debernardi, Isaiah Martell
 * @date 26 Apr 2021
 */
package server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Request {

    // url of the destination endpoint
    private String endpointURL;
    private HashMap<String, String> parameters;

    /**
     * Creates a new Request.
     *
     * @param endpointURL the url of the destination endpoint
     * @param parameters alternating key, value pairs for the parameters
     * @throws IllegalArgumentException if an odd number of strings for the
     *                                  parameters is given
     */
    public Request(String endpointURL, String... parameters)
            throws IllegalArgumentException {
        this.endpointURL = endpointURL;
        if (parameters.length % 2 == 0) {
            for (int i = 0; i < parameters.length; i = i + 2) {
                this.parameters.put(parameters[i], parameters[i + 1]);
            }
        }
        else {
            throw new IllegalArgumentException(
                    "Paramaters length must be even"
            );
        }
    }

    /**
     * Returns the parameter with the given name/key.
     *
     * @param key
     * @return
     */
    public String getParameter(String key) {
        return parameters.get(key);
    }

    /**
     * Returns the url of the endpoint
     *
     * @return
     */
    public String getEndpointUrl() {
        return endpointURL;
    }

    /**
     * Get the a list of all the parameter names
     * @return  List of parameters
     */
    public Set<String> getParameterNames() {
        return parameters.keySet();
    }
}
