package app.stockticker.com.stockticker.model;


import android.database.Observable;
import com.jjoe64.graphview.series.DataPoint;
import java.util.Collection;
import app.stockticker.com.stockticker.gson.History;

public interface IHistoryProvider {

    Collection<History> getHistory(String ticker, Range range);
    Observable<DataPoint[]> getDataPoints(String ticker, Range range);
}
