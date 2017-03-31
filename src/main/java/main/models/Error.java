package main.models;

/**
 * Created by Dean on 31/03/2017.
 */
public class Error {

    private String type;
    private String subtype;
    private String message;

    public Error(String subtype,String message) {
        this.type ="error";
        this.setSubtype(subtype);
        this.setMessage(message);
    }

    public String getType() {
        return type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
