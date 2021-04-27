/**
 * A request made to a server.
 *
 * @author Christopher Medlin, Will Debernardi, Isaiah Martell
 * @date 26 Apr 2021
 */
package server;

import java.util.HashMap;
import java.util.Set;

public class Request {
    // url of the destination endpoint
    private String endpoint;
    private HashMap<String, String> parameters;

    /**
     * Creates a new Request.
     *
     * @param endpoint the url of the destination endpoint
     * @param parameters alternating key, value pairs for the parameters
     * @throws IllegalArgumentException if an odd number of strings for the
     *                                  parameters is given
     */
    public Request(Endpoint endpoint, String... parameters) {

    }

    /**
     * Returns the parameter with the given name/key.
     *
     * @param key
     * @return
     */
    public String getParameter(String key) {

        return "";
    }

    public Set<String> getParameterNames() {
        return parameters.keySet();
    }

    public String getEndpointUrl() {

        return "";
    }
}
