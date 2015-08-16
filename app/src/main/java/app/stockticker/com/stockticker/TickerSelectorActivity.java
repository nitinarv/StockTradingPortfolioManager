package app.stockticker.com.stockticker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


import app.stockticker.com.stockticker.gson.Suggestion;
import app.stockticker.com.stockticker.model.StockProvider;
import app.stockticker.com.stockticker.model.Tools;
import app.stockticker.com.stockticker.yahoofinance.GetYahooFinanceDataTask;
import app.stockticker.com.stockticker.yahoofinance.ParsingHelper;
import app.stockticker.com.stockticker.yahoofinance.OperationCallback;
import app.stockticker.com.stockticker.yahoofinance.RequestUrlHelper;
import app.stockticker.com.stockticker.yahoofinance.ResponseDetails;

public class TickerSelectorActivity extends AppCompatActivity {

    StockProvider stocksProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stocksProvider = StockProvider.getInstance(this);
        setContentView(R.layout.stock_search_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText searchView = (EditText) findViewById(R.id.query);
        final ListView listView = (ListView) findViewById(R.id.resultList);


        final OperationCallback searchOperationCallback = new OperationCallback() {
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
                List<Suggestion> suggestionList = ParsingHelper.getSuggestionList(responseDetails.getReponseString());
                listView.setAdapter(new SuggestionsAdapter(suggestionList));
            }
        };


        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String query = s.toString().trim().replaceAll(" ", "");
                if (!query.isEmpty()) {
                    if (Tools.isNetworkOnline(getApplicationContext())) {
                        GetYahooFinanceDataTask task = new GetYahooFinanceDataTask(TickerSelectorActivity.this,
                                RequestUrlHelper.getInstance().getAutocompleteUri(TickerSelectorActivity.this, query),
                                searchOperationCallback);
                        task.execute();

                    } else {
                        Toast.makeText(TickerSelectorActivity.this, "No Network found", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final SuggestionsAdapter suggestionsAdapter = (SuggestionsAdapter) parent.getAdapter();
                final Suggestion suggestion = suggestionsAdapter.getItem(position);
                final String ticker = suggestion.symbol;
                if (!stocksProvider.getTickers().contains(ticker)) {
                    stocksProvider.addStock(ticker);
                    listView.setAdapter(new SuggestionsAdapter(new ArrayList<Suggestion>()));
                    setResult(Activity.RESULT_OK, getIntent());
                    finish();
                } else {
                    Toast.makeText(TickerSelectorActivity.this, "Company is already present in your portfolio", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

       switch (id){
            case android.R.id.home:
                super.onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

}
