package app.stockticker.com.stockticker.model;

import android.content.Context;
import android.database.Observable;

import com.jjoe64.graphview.series.DataPoint;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import app.stockticker.com.stockticker.gson.History;
import app.stockticker.com.stockticker.yahoofinance.GetYahooFinanceDataTask;
import app.stockticker.com.stockticker.yahoofinance.OperationCallback;
import app.stockticker.com.stockticker.yahoofinance.ParsingHelper;
import app.stockticker.com.stockticker.yahoofinance.RequestUrlHelper;
import app.stockticker.com.stockticker.yahoofinance.ResponseDetails;
import app.stockticker.com.stockticker.yahoofinance.RestRelatedWork;

/**
 * Created by nitinraj.arvind on 8/16/2015.
 */
public class HistoryProvider implements IHistoryProvider {

    private SharedPreferenceHelper sharedPreferenceHelper;
    private Context context;
    private RestRelatedWork restWork;
    private static HistoryProvider instance;

    private HistoryProvider(Context mContext){
        context = mContext;
        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(context);
        restWork = RestRelatedWork.getInstance();
    }

    public static HistoryProvider getInstance(Context context){
        if(instance==null){
            synchronized (HistoryProvider.class) {
                instance = new HistoryProvider(context);
            }
        }
        return instance;
    }


    @Override
    public Collection<History> getHistory(String ticker, Range range) {

        Date presentDate = new Date();
        Date startingDate = null;
        switch (range){
            case ONE_MONTH:
                startingDate = RequestUrlHelper.getInstance().getDateTimeSetToObject(new Date(), Calendar.MONTH, -1);
                break;
            case THREE_MONTH:
                startingDate = RequestUrlHelper.getInstance().getDateTimeSetToObject(new Date(), Calendar.MONTH, -3);
                break;
            case ONE_YEAR:
                startingDate = RequestUrlHelper.getInstance().getDateTimeSetToObject(new Date(), Calendar.YEAR, -1);
                break;
        }

        String graphQuery = RequestUrlHelper.getInstance().getSymbolDetailRequest(context, ticker, startingDate, presentDate);

        GetYahooFinanceDataTask task = new GetYahooFinanceDataTask(context,
                graphQuery, new OperationCallback() {
            @Override
            public void processException(Exception e) {

            }

            @Override
            public void onProgressStarted() {

            }

            @Override
            public void onProgressEnded() {

            }

            @Override
            public void onOperationCancelled() {

            }

            @Override
            public void onProgressUpdated(int progressPercent) {

            }

            @Override
            public void processResponseDetails(ResponseDetails responseDetails) {
//            List<History> historyList = ParsingHelper.getHistoryList(responseDetails.getReponseString());
                android.util.Log.d("","");

            }
        });

        task.execute();

        return null;
    }

    @Override
    public Observable<DataPoint[]> getDataPoints(String ticker, Range range) {

        Date presentDate = new Date();
        Date startingDate = null;
        switch (range){
            case ONE_MONTH:
                startingDate = RequestUrlHelper.getInstance().getDateTimeSetToObject(new Date(), Calendar.MONTH, -1);
                break;
            case THREE_MONTH:
                startingDate = RequestUrlHelper.getInstance().getDateTimeSetToObject(new Date(), Calendar.MONTH, -3);
                break;
            case ONE_YEAR:
                startingDate = RequestUrlHelper.getInstance().getDateTimeSetToObject(new Date(), Calendar.YEAR, -1);
                break;
        }

        String graphQuery = RequestUrlHelper.getInstance().getSymbolDetailRequest(context, ticker, startingDate, presentDate);

        GetYahooFinanceDataTask task = new GetYahooFinanceDataTask(context,
                graphQuery, new OperationCallback() {
            @Override
            public void processException(Exception e) {

            }

            @Override
            public void onProgressStarted() {

            }

            @Override
            public void onProgressEnded() {

            }

            @Override
            public void onOperationCancelled() {

            }

            @Override
            public void onProgressUpdated(int progressPercent) {

            }

            @Override
            public void processResponseDetails(ResponseDetails responseDetails) {
//            List<History> historyList = ParsingHelper.getHistoryList(responseDetails.getReponseString());
                android.util.Log.d("","");

            }
        });

        task.execute();

        return null;
    }
}
