package util.pubsub;

import util.pubsub.messages.ExceptionMessage;
import util.pubsub.messages.Message;
import util.pubsub.messages.ThemeMessage;
import util.pubsub.messages.TransformMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Class that implements publish/subscribe pattern
 *
 * @author ramilmsh
 */
public class PubSub {
    private static PubSub instance;

    /**
     * Channel list
     */
    public enum Channel {
        THEME_MESSAGE(ThemeMessage.class),
        EXCEPTION_MESSAGE(ExceptionMessage.class),
        TRANSFORM_MESSAGE(TransformMessage.class);

        Class<? extends Message> clazz;

        Channel(Class<? extends Message> clazz) {
            this.clazz = clazz;
        }
    }

    private HashMap<Channel, ArrayList<Consumer<Message>>> callbacks;
    private HashMap<Channel, Function<Message, Object>> callbacksSync;

    /**
     * Create a new instance of PubSub
     */
    public PubSub() {
        callbacks = new HashMap<>();
        callbacksSync = new HashMap<>();
    }

    /**
     * Subscribe to a synchronous pubsub
     *
     * @param channel:  pubsub
     * @param callback: callback that processes message
     */
    public void subscribe(Channel channel, Consumer<Message> callback) {
        if (!callbacks.containsKey(channel))
            callbacks.put(channel, new ArrayList<>());

        callbacks.get(channel).add(callback);
    }

    /**
     * Subscribe to a synchronous pubsub
     *
     * @param channel:  pubsub
     * @param callback: callback that processes message
     */
    public void subscribeSync(Channel channel, Function<Message, Object> callback) {
        callbacksSync.put(channel, callback);
    }

    /**
     * Publish to an asynchronous pubsub
     *
     * @param channel: pubsub
     * @param msg:     message
     */
    public void publish(Channel channel, Message msg) {
        if (msg.getClass() != channel.clazz || callbacks == null || callbacks.get(channel) == null)
            return;

        for (Consumer<Message> callback : callbacks.get(channel))
            if (callback != null)
                callback.accept(msg);
    }

    /**
     * Publish to a synchronous pubsub
     *
     * @param channel: pubsub
     * @param msg:     message
     * @return result of processing
     */
    public Object publishSync(Channel channel, Message msg) {
        if (callbacksSync.containsKey(channel))
            return callbacksSync.get(channel).apply(msg);
        return null;
    }

    public static PubSub getInstance() {
        if (instance == null)
            instance = new PubSub();

        return instance;
    }
}
