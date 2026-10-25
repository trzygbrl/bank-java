/**
 * Interface implemented by views that need to synchronize their display
 * when account balance or transaction history changes.
 */
public interface Refreshable {

    /**
     * Refreshes view components with the latest account data.
     */
    void refresh();
}
