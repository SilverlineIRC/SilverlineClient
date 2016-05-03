package gui.windows.loading;

/**
 * Callback listener for GUILoading.
 * @author Kulttuuri
 */
public interface LoadingCallBackListener {
    /**
     * Called when loading has started.
     */
    public abstract void loadingStarted();
    
    /**
     * Called when loading has completed.
     */
    public abstract void loadingCompleted();
}