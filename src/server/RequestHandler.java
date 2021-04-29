/**
 * Functional interface that accepts a Request and returns a Response.
 *
 * @author Christopher Medlin, Will Debernardi, Isaiah Martell
 */
package server;

@FunctionalInterface
public interface RequestHandler {
    Response handle(Request r);
}
