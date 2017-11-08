package util;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class that helps centralize the message print-outs
 *
 * @author ramilmsh
 */
public class Console {
    public InputStream in;
    public PrintStream out;
    public PrintStream err;

    private ArrayList<Message> logs;
    private ArrayList<Message> errors;
    private ArrayList<Message> warnings;

    private class Message {
        String msg;
        MessageType type;
        double timestamp;

        /**
         * Creates new message
         *
         * @param msg: message content
         * @param type: message type
         */
        Message(String msg, MessageType type) {
            this.msg = msg;
            this.type = type;
            timestamp = System.currentTimeMillis();
        }

        @Override
        public String toString() {
            return type.toString() + ": " + msg;
        }
    }

    private enum MessageType {
        LOG, ERROR, WARNING
    }

    /**
     * Creates console object
     *
     * @param in: standard input
     * @param out: standard output
     * @param err: standard error
     */
    public Console(InputStream in, PrintStream out, PrintStream err) {
        this.in = in;
        this.out = out;
        this.err = err;

        logs = new ArrayList<>();
        errors = new ArrayList<>();
        warnings = new ArrayList<>();
    }

    /**
     * Print log
     *
     * @param msg: message content
     */
    public void log(String msg) {
        Message message = new Message(msg, MessageType.LOG);
        logs.add(message);
        out.println(message);
    }

    /**
     * Print error
     *
     * @param msg: message content
     */
    public void error(String msg) {
        Message message = new Message(msg, MessageType.ERROR);
        errors.add(message);
        err.println(message);
    }

    /**
     * Print warning
     *
     * @param msg: message content
     */
    public void warning(String msg) {
        Message message = new Message(msg, MessageType.WARNING);
        warnings.add(message);
        out.println(message);
    }

    /**
     * Get all messages in chronological order
     *
     * @return List of messages
     */
    public ArrayList<Message> flush() {
        ArrayList<Message> total = new ArrayList<>();
        total.addAll(logs);
        total.addAll(errors);
        total.addAll(warnings);
        total.sort((Message m1, Message m2) -> (int) (m1.timestamp - m2.timestamp));
        return total;
    }

    /**
     * Get all log messages
     *
     * @return iterator of messages
     */
    public Iterator<Message> getLogs() {
        return logs.iterator();
    }

    /**
     * Get all error messages
     *
     * @return iterator of messages
     */
    public Iterator<Message> getErrors() {
        return errors.iterator();
    }



    /**
     * Get all warning messages
     *
     * @return iterator of messages
     */
    public Iterator<Message> getWarnings() {
        return warnings.iterator();
    }
}
