package util.pubsub.messages;

public class StringMessage extends Message {

    private String myMessage;

    public StringMessage(String message) {
        myMessage = message;
    }

    public String readMessage() {
        return myMessage;
    }
}
