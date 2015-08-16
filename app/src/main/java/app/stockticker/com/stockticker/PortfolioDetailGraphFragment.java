package app.stockticker.com.stockticker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.stockticker.com.stockticker.gson.History;
import app.stockticker.com.stockticker.yahoofinance.GetYahooFinanceDataTask;
import app.stockticker.com.stockticker.yahoofinance.OperationCallback;
import app.stockticker.com.stockticker.yahoofinance.ParsingHelper;
import app.stockticker.com.stockticker.yahoofinance.RequestUrlHelper;
import app.stockticker.com.stockticker.yahoofinance.ResponseDetails;

/**
 * Created by nitinraj.arvind on 8/16/2015.
 */
public class PortfolioDetailGraphFragment extends Fragment {

    public static final String TAG = "app.stockticker.com.stockticker.PortfolioDetailGraphFragment";


//    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/YYYY");
//
//    private Stock ticker;
//    private SerializableDataPoint[] dataPoints;
//    private Range range = Range.THREE_MONTH;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.porfolio_graph_view, container, false);

//        final int viewId;
//        switch (range) {
//            case ONE_MONTH:
//                viewId = R.id.one_month;
//                break;
//            case THREE_MONTH:
//                viewId = R.id.three_month;
//                break;
//            default:
//            case ONE_YEAR:
//                viewId = R.id.one_year;
//                break;
//        }
//        findViewById(viewId).setEnabled(false);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (dataPoints == null) {
//            getData();
//        } else {
//            loadGraph();
//        }
    }


//    private void getData() {
//        if (Tools.isNetworkOnline(this)) {
//            findViewById(R.id.graph_holder).setVisibility(View.GONE);
//            findViewById(R.id.progress).setVisibility(View.VISIBLE);
//            final Observable<SerializableDataPoint[]> observable = historyProvider.getDataPoints(ticker.symbol, range);
//            bind(observable)
//                    .subscribe(new Subscriber<SerializableDataPoint[]>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            showDialog("Error loading datapoints", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    finish();
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onNext(SerializableDataPoint[] data) {
//                            dataPoints = data;
//                            loadGraph();
//                        }
//                    });
//        } else {
//            showDialog(getString(R.string.no_network_message), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    finish();
//                }
//            });
//        }
//    }



//    private void loadGraph() {
//        findViewById(R.id.graph_holder).setVisibility(View.VISIBLE);
//        findViewById(R.id.progress).setVisibility(View.GONE);
//        final GraphView graphView = (GraphView) findViewById(R.id.graph);
//        graphView.removeAllSeries();
//
//        final TextView tickerName = (TextView) findViewById(R.id.ticker);
//        final TextView desc = (TextView) findViewById(R.id.desc);
//        tickerName.setText(ticker.symbol);
//        desc.setText(ticker.Name);
//        final TextView dataPointValue = (TextView) findViewById(R.id.dataPointValue);
//        final LineGraphSeries<DataPoint> series = new LineGraphSeries(dataPoints);
//        graphView.addSeries(series);
//
//        final PointsGraphSeries disposableSeries = new PointsGraphSeries(new DataPointInterface[]{dataPoints[dataPoints.length - 1]});
//        graphView.addSeries(disposableSeries);
//        disposableSeries.setColor(getResources().getColor(R.color.spicy_salmon));
//        disposableSeries.setShape(PointsGraphSeries.Shape.POINT);
//        disposableSeries.setSize(10f);
//
//        series.setDrawBackground(true);
//        series.setBackgroundColor(getResources().getColor(R.color.color_accent));
//        series.setOnDataPointTapListener(new OnDataPointTapListener() {
//            @Override
//            public void onTap(Series series, DataPointInterface dataPointInterface) {
//                final StringBuilder dataPointText = new StringBuilder();
//                final DateTime dateTime = new DateTime((long) dataPointInterface.getX());
//                dataPointText.append(formatter.print(dateTime));
//                dataPointText.append(" // ");
//                dataPointText.append("$");
//                dataPointText.append(dataPointInterface.getY());
//                dataPointValue.setText(dataPointText.toString());
//                disposableSeries.resetData(new DataPointInterface[]{dataPointInterface});
//            }
//        });
//        final GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();
//        gridLabelRenderer.setLabelFormatter(new DateAsXAxisLabelFormatter(this));
//        gridLabelRenderer.setNumHorizontalLabels(5);
//
//        final Viewport viewport = graphView.getViewport();
//        viewport.setXAxisBoundsManual(true);
//        viewport.setYAxisBoundsManual(true);
//
//        viewport.setMinX(dataPoints[0].getX());
//        viewport.setMaxX(dataPoints[dataPoints.length - 1].getX());
//
//        double min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
//        for (int i = 0; i < dataPoints.length; i++) {
//            final DataPoint point = dataPoints[i];
//            final double val = point.getY();
//            if (val < min) {
//                min = val;
//            } else if (val > max) {
//                max = val;
//            }
//        }
//        if (min != Integer.MAX_VALUE && max != Integer.MIN_VALUE) {
//            min = min - Math.abs(0.1 * min);
//            viewport.setMinY(min <= 0 ? 0 : min);
//            viewport.setMaxY(max + Math.abs(0.1 * max));
//        }
//    }

    /**
     * xml OnClick
     *
     * @param v
     */
//    public void updateRange(View v) {
//        switch (v.getId()) {
//            case R.id.one_month:
//                range = Range.ONE_MONTH;
//                break;
//            case R.id.three_month:
//                range = Range.THREE_MONTH;
//                break;
//            default:
//            case R.id.one_year:
//                range = Range.ONE_YEAR;
//                break;
//        }
//        Analytics.trackUI("GraphUpdateRange", ticker.symbol + "-" + range.name());
//        final ViewGroup parent = (ViewGroup) v.getParent();
//        for (int i = 0; i < parent.getChildCount(); i++) {
//            final View view = parent.getChildAt(i);
//            if (view != v) {
//                view.setEnabled(true);
//            } else {
//                view.setEnabled(false);
//            }
//        }
//        getData();
//    }

}
