package ru.itmo.antipov.coursework;

/**
 * Created by dantipov on 27.01.15.
 */
public class FullNodeException extends Exception {
    public FullNodeException() {}

    public String getMessage() {
        return "The node can't contain more versions";
    }
}
