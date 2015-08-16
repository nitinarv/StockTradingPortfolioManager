package app.stockticker.com.stockticker.model;

import java.util.Collection;
import java.util.List;

import app.stockticker.com.stockticker.yahoofinance.OperationCallback;
import app.stockticker.com.stockticker.gson.Stock;

public interface IStocksProvider {

    Collection<String> removeStock(String ticker);

    Collection<String> addStock(String ticker);

    void addPosition(String ticker, float shares, float price);

    void removePosition(String ticker);

    Collection<String> addStocks(Collection<String> tickers);

    Collection<Stock> getStocks();

    Collection<Stock> rearrange(List<String> tickers);

    Stock getStock(String ticker);

    List<String> getTickers();

    String lastFetched();

    void fetch(OperationCallback operationCallback);
}
