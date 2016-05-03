package thread;

/**
 * Interface that you will need to implement in your class that can be used to listen for events happening in a thread.
 * @author kulttuuri
 */
public interface AbstractThreadCallbackListener {
    /**
     * When thread wants to inform something. Handle events received from thread here.
     * @param type Callback type name as a string.
     * @param object Object containing callback data.
     */
    public abstract void callbackReceivedFromThread(String type, Object object);
}