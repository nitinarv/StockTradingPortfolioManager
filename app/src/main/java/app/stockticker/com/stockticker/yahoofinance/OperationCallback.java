package app.stockticker.com.stockticker.yahoofinance;

/**
 * Created by  nitinraj.arvind on 06/07/15.
 */
public abstract class OperationCallback {


    public abstract void processException(Exception e);
    public abstract void onProgressStarted();
    public abstract void onProgressEnded();
    public abstract void onOperationCancelled();
    public abstract void onProgressUpdated(int progressPercent);
    public abstract void processResponseDetails(ResponseDetails responseDetails);

}
