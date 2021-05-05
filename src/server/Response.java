/**
 * Immutable response from a server to a client.
 *
 * @author Christopher Medlin, Will Debernardi, Isaiah Martell
 * @date 26 Apr 2021
 */
package server;

import java.io.Serializable;

public class Response implements Serializable {
    public enum Type {
        OK, ERROR, NOTFOUND
    }

    private String message;
    private Object data;
    private Type type;

    public Response(String message, Object data, Type type) {
        this.message = message;
        this.type = type;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Response{" +
                "message='" + message + '\'' +
                ", data=" + data +
                ", type=" + type +
                '}';
    }
}
