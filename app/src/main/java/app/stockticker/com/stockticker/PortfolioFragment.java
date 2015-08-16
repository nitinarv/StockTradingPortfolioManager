package app.stockticker.com.stockticker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

import app.stockticker.com.stockticker.model.IStocksProvider;
import app.stockticker.com.stockticker.model.SharedPreferenceHelper;
import app.stockticker.com.stockticker.model.StockProvider;
import app.stockticker.com.stockticker.model.Tools;
import app.stockticker.com.stockticker.ui.ScrollDetector;
import app.stockticker.com.stockticker.yahoofinance.ParsingHelper;
import app.stockticker.com.stockticker.yahoofinance.OperationCallback;
import app.stockticker.com.stockticker.yahoofinance.ResponseDetails;
import app.stockticker.com.stockticker.gson.Stock;


public class PortfolioFragment extends Fragment {


    public static final int ADD_REQUEST_CODE = 2131;
    public static final String ROOT_PAYLOAD = "ROOT_PAYLOAD";
    public static final String PAYLOAD_CONTENTS = "PAYLOAD_CONTENTS";
    public static final String TAG_PORTFOLIO_FRAGMENT = "app.stockticker.com.stockticker.PortfolioFragment";
    public static final String TITLE = "Custom Stock Portfolio Manager";

    OperationCallback latestDataOperation;

    IStocksProvider stocksProvider;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Parcelable listViewState;
    private StocksAdapter stocksAdapter;
    private final ScrollDetector scrollListener = new ScrollDetector() {
        @Override
        public void onScrollUp() {
            animateButton(false);
        }

        @Override
        public void onScrollDown() {
            animateButton(true);
        }
    };

    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stocksProvider = StockProvider.getInstance(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        update();

        GridView adapterView = (GridView) rootView.findViewById(R.id.stockList);
        if(adapterView.getCount()==0 && SharedPreferenceHelper.getInstance(getActivity()).isFirstTimeGridViewSetup()){
            stocksProvider.fetch(latestDataOperation);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context context = inflater.getContext();
        final View view = inflater.inflate(R.layout.portfolio_fragment, null);
        rootView = view.findViewById(R.id.fragment_root);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeResources(R.color.color_secondary, R.color.spicy_salmon, R.color.sea);

        latestDataOperation = new OperationCallback() {
            @Override
            public void processException(Exception e) {

            }

            @Override
            public void onProgressStarted() {
                if(!swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onProgressEnded() {
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onOperationCancelled() {

            }

            @Override
            public void onProgressUpdated(int progressPercent) {

            }

            @Override
            public void processResponseDetails(ResponseDetails responseDetails) {
                List<Stock> stockList = new ArrayList<Stock>();
                if(responseDetails!=null && responseDetails.getReponseString()!=null && !responseDetails.getReponseString().isEmpty()){
                    stockList = ParsingHelper.getStockList(responseDetails.getReponseString());
                }
                SharedPreferenceHelper.getInstance(getActivity()).setLastFetchedTime();
                StockProvider.getInstance(getActivity()).mainStockList = stockList;
                update();
            }
        };
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!Tools.isNetworkOnline(getActivity().getApplicationContext())) {
                    noNetwork();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    stocksProvider.fetch(latestDataOperation);
                }
            }
        });

        if (!Tools.isNetworkOnline(context.getApplicationContext())) {
            noNetwork();
        }

        final View addButton = view.findViewById(R.id.add_ticker_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TickerSelectorActivity.class);
                startActivityForResult(intent, ADD_REQUEST_CODE);
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PortfolioFragment.ADD_REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK){
                    stocksProvider.fetch(latestDataOperation);
                }
                break;

            default:
                break;
        }
    }


    public void update() {
        final FragmentActivity activity = getActivity();
        if (activity != null) {

            final TextView lastUpdatedTextView = (TextView) rootView.findViewById(R.id.last_updated);
            if (lastUpdatedTextView != null) {
                lastUpdatedTextView.setText("Last updated: " + stocksProvider.lastFetched());
            }

            final GridView adapterView = (GridView) rootView.findViewById(R.id.stockList);
            if (adapterView != null) {
                scrollListener.setListView(adapterView);
                adapterView.setOnScrollListener(scrollListener);

                if (stocksAdapter == null) {
                    stocksAdapter = new StocksAdapter(stocksProvider,
                            new StocksAdapter.OnRemoveClickListener() {
                                @Override
                                public void onRemoveClick(View view, Stock stock, int position) {
                                    promptRemove(stock);
                                }
                            });
                } else {
                    stocksAdapter.refresh(stocksProvider);
                }

                adapterView.setAdapter(stocksAdapter);

                if (listViewState != null) {
                    adapterView.onRestoreInstanceState(listViewState);
                }

                adapterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        StocksAdapter stocksAdapter = (StocksAdapter) parent.getAdapter();
                        Stock stock = stocksAdapter.getItem(position);

                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        Bundle payloadBundle = new Bundle();
                        payloadBundle.putParcelable(PAYLOAD_CONTENTS,stock);
                        intent.putExtra(ROOT_PAYLOAD, payloadBundle);
                        startActivity(intent);
                    }
                });

                if (stocksAdapter.getCount() > 1) {
                    if (SharedPreferenceHelper.getInstance(getActivity()).firstTimeViewingSwipeLayout()) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                final SwipeLayout layout = (SwipeLayout) adapterView.getChildAt(0);
                                if (layout != null) {
                                    layout.open(true);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (layout != null) layout.close();
                                            final SwipeLayout secondLayout = (SwipeLayout) adapterView.getChildAt(1);
                                            if (secondLayout != null) {
                                                secondLayout.open(true);
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (secondLayout != null)
                                                            secondLayout.close();
                                                    }
                                                }, 600);
                                            }
                                        }
                                    }, 600);
                                }
                            }
                        }, 1000);
                    }
                }
            }
        }
    }

    private final Interpolator interp = new OvershootInterpolator();

    private void animateButton(boolean show) {
        final View button = rootView.findViewById(R.id.add_ticker_button);
        final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) button.getLayoutParams();
        final int translationY = show ? 0 : (button.getHeight() + layoutParams.topMargin);
        button.animate().setInterpolator(interp)
                .setDuration(400)
                .translationY(translationY);
    }

    private void promptRemove(final Stock stock) {
        stocksProvider.removeStock(stock.symbol);
        if (stocksAdapter.remove(stock)) {
            stocksAdapter.notifyDataSetChanged();
        }
    }


    private void noNetwork() {
        Toast.makeText(getActivity(), "No internet available", Toast.LENGTH_LONG).show();
    }

}
