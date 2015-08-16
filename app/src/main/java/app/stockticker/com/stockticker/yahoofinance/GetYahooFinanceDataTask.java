package app.stockticker.com.stockticker.yahoofinance;

import android.content.Context;
import android.os.AsyncTask;


/**
 * Created by nitinraj.arvind on 7/6/2015.
 */
public class GetYahooFinanceDataTask extends AsyncTask<Void,Object,Void> {


    OperationCallback operationCallback = null;
    ResponseDetails responseDetails;
    RestRelatedWork jobs = null;
    Context context = null;
    String url = null;

    private static final String STAGEUPDATE = "stage_update:";

    private int indexOfInterest = 10;

    private int progressCount = 0;

    public GetYahooFinanceDataTask(Context mContext,String mUrl, OperationCallback mOperationCallback) {
        this.context = mContext;
        this.operationCallback = mOperationCallback;
        this.url = mUrl;
        this.jobs = RestRelatedWork.getInstance();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        operationCallback.onProgressStarted();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            publishProgress(new Integer(progressCount+=5));
            responseDetails = jobs.getWebPage(context,url);
            publishProgress(new Integer(progressCount+=95));
        }catch (Exception e){
            publishProgress(e);
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        operationCallback.onProgressEnded();
        operationCallback.processResponseDetails(responseDetails);
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
        if(operationCallback!=null){
            switch (progressUpdateObject(values[0])){
                case EXCEPTION:
                    Exception e = (Exception)values[0];
                    operationCallback.processException(e);
                    break;
                case PROGRESSUPDATE:
                    Integer progressCount = (Integer) values[0];
                    operationCallback.onProgressUpdated(progressCount);
                    break;
                case STAGEUPDATE:
                    String data = (String) values[0];
                    data = data.replaceAll(STAGEUPDATE,"");
                    break;
                case UNDEFINED:
                    break;
            }

        }

    }

    /**
     *
     * */
    private Types progressUpdateObject(Object value){
        if(value instanceof Exception){
            return Types.EXCEPTION;
        }else if(value instanceof  Integer){
            return Types.PROGRESSUPDATE;
        }else if(value instanceof String){
            String data = (String) value;
            if(data.contains(STAGEUPDATE)){
                return Types.STAGEUPDATE;
            }else{
                return Types.UNDEFINED;
            }
        }else{
            return Types.UNDEFINED;
        }
    }

}
