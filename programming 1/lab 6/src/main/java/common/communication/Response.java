package common.communication;

import java.io.Serializable;

/**
 * Класс ответа, отправляемого от сервера клиенту.
 */
public class Response implements Serializable {
    String descr;
    ResponseStatus status;

    public Response() {};
    public Response(ResponseStatus status, String descr) {
        this.status = status;
        this.descr = descr;
    }
    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public String getDescr() {
        return descr;
    }
}


