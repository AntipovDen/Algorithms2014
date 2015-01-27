package ru.itmo.antipov.coursework;

/**
 * Created by dantipov on 27.01.15.
 */
public class WrongVersionException extends Exception {
    public  WrongVersionException() {}

    public String getMessage() {
        return "Too old version for this node";
    }
}
