/**
 * Represents an endpoint for the server API that accepts a request and returns
 * a response.
 *
 * @author Christopher Medlin, Will Debernardi, Isaiah Martell
 * @date 26 Apr 2021
 */
package server;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class Endpoint {
    private Set<String> requiredParameters;
    private Function<Request, Response> rh;
    private String url;

    /**
     * Creates a new Endpoint for a server API
     * @param requiredParameters the parameters that any request to this
     *                           endpoint must specify
     * @param rh a request handler to determine an appropriate response for any
     *           request to this endpoint.
     * @param url the url of this endpoint
     */
    public Endpoint(String url, Function<Request, Response> rh,
                    String... requiredParameters) {
        this.requiredParameters = new HashSet<>();
        for (String s : requiredParameters) {
            this.requiredParameters.add(s);
        }
        this.rh = rh;
        this.url = url;
    }

    /**
     * Calls the endpoint, returning an appropriate response.
     * @param request the request sent to the endpoint
     * @return a response from the endpoint
     */
    public Response call(Request request) {
        // verify that all required parameters are in the response
        if (!request.getParameterNames().containsAll(requiredParameters)) {
            return new Response("Missing parameters.", null,
                                Response.Type.ERROR);
        } else if (!request.getEndpointUrl().equals(this.url)) {
            // this error should never occur.
            return new Response("Mismatched URL.", null,
                                Response.Type.ERROR);
        }
        // call the request handler
        return rh.apply(request);
    }

    /**
     * @return the url of the endpoint
     */
    public String getUrl() {
        return url;
    }
}
