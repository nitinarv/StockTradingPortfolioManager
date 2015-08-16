package app.stockticker.com.stockticker.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import app.stockticker.com.stockticker.yahoofinance.GetYahooFinanceDataTask;
import app.stockticker.com.stockticker.yahoofinance.OperationCallback;
import app.stockticker.com.stockticker.yahoofinance.RequestUrlHelper;
import app.stockticker.com.stockticker.yahoofinance.RestRelatedWork;
import app.stockticker.com.stockticker.gson.Stock;

/**
 * Created by nitinraj.arvind on 8/15/2015.
 */
public class StockProvider implements IStocksProvider {

    private SharedPreferenceHelper sharedPreferenceHelper;
    private Context context;
    private RestRelatedWork restWork;
    private static StockProvider instance;
    public List<Stock> mainStockList = new ArrayList<Stock>();


    private StockProvider(Context mContext){
        context = mContext;
        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(context);
        restWork = RestRelatedWork.getInstance();
    }

    public static StockProvider getInstance(Context context){
        if(instance==null){
            synchronized (StockProvider.class) {
                instance = new StockProvider(context);
            }
        }
        return instance;
    }

    @Override
    public Collection<String> removeStock(String ticker) {
        sharedPreferenceHelper.removeStockAtHome(ticker);
        return sharedPreferenceHelper.getStocksAtHome();
    }

    @Override
    public Collection<String> addStock(String ticker) {
        sharedPreferenceHelper.addStockAtHome(ticker);
        return sharedPreferenceHelper.getStocksAtHome();
    }

    @Override
    public void addPosition(String ticker, float shares, float price) {
        addStock(ticker);
    }

    @Override
    public void removePosition(String ticker) {
        removeStock(ticker);
    }

    @Override
    public Collection<String> addStocks(Collection<String> tickers) {
        sharedPreferenceHelper.setStocksAtHome(new ArrayList<String>(tickers));
        return sharedPreferenceHelper.getStocksAtHome();
    }

    @Override
    public Collection<Stock> getStocks() {
        return mainStockList;
    }

    @Override
    public Collection<Stock> rearrange(List<String> tickers) {
        return getStocks();
    }

    @Override
    public Stock getStock(String ticker) {
        for(Stock item : getStocks()){
            if(item.symbol.equalsIgnoreCase(ticker))
                return item;
        }
        return null;
    }


    @Override
    public List<String> getTickers() {
        return sharedPreferenceHelper.getStocksAtHome();
    }

    @Override
    public String lastFetched() {
        return SharedPreferenceHelper.getInstance(context).getLastFetchedTime();

    }

    @Override
    public void fetch(OperationCallback operationCallback) {

        GetYahooFinanceDataTask task = new GetYahooFinanceDataTask(context,
                RequestUrlHelper.getInstance().getOpeningPageRequest(context),
                operationCallback);


        task.execute();
    }
}
