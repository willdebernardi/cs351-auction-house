/**
 * Represents an endpoint for the server API that accepts a request and returns
 * a response.
 *
 * @author Christopher Medlin, Will Debernardi, Isaac Martell
 * @date 26 Apr 2021
 */
package server;

@FunctionalInterface
public interface Endpoint {
    Response call(Request request);
}
