package app.stockticker.com.stockticker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.stockticker.com.stockticker.gson.History;
import app.stockticker.com.stockticker.gson.Stock;
import app.stockticker.com.stockticker.yahoofinance.GetYahooFinanceDataTask;
import app.stockticker.com.stockticker.yahoofinance.OperationCallback;
import app.stockticker.com.stockticker.yahoofinance.ParsingHelper;
import app.stockticker.com.stockticker.yahoofinance.RequestUrlHelper;
import app.stockticker.com.stockticker.yahoofinance.ResponseDetails;

/**
 * Created by nitinraj.arvind on 8/16/2015.
 */
public class DetailActivity extends AppCompatActivity{


    private FrameLayout textFrameLayout;
    private FrameLayout graphFrameLayout;
    private PortfolioDetailGraphFragment portfolioDetailGraphFragment;
    private PortfolioDetailTextFragment portfolioDetailTextFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        Intent itemIntent = getIntent();
        Bundle bundle = itemIntent.getBundleExtra(PortfolioFragment.ROOT_PAYLOAD);
        Stock stock = bundle.getParcelable(PortfolioFragment.PAYLOAD_CONTENTS);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        portfolioDetailGraphFragment = (PortfolioDetailGraphFragment) getSupportFragmentManager().findFragmentByTag(PortfolioDetailGraphFragment.TAG);
        portfolioDetailTextFragment = (PortfolioDetailTextFragment) getSupportFragmentManager().findFragmentByTag(PortfolioDetailTextFragment.TAG);

        textFrameLayout = (FrameLayout) findViewById(R.id.text_fragment);
        graphFrameLayout = (FrameLayout) findViewById(R.id.graph_fragment);

        if (savedInstanceState == null) {

            if(portfolioDetailGraphFragment==null) {
                portfolioDetailGraphFragment = new PortfolioDetailGraphFragment();
            }
            if(portfolioDetailTextFragment==null) {
                portfolioDetailTextFragment = new PortfolioDetailTextFragment();
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.graph_fragment, portfolioDetailGraphFragment, PortfolioDetailGraphFragment.TAG)
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.bounce_interpolator)
                    .commit();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.text_fragment, portfolioDetailTextFragment, PortfolioDetailTextFragment.TAG)
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.bounce_interpolator)
                    .commit();
        }else{
            portfolioDetailGraphFragment = (PortfolioDetailGraphFragment) getSupportFragmentManager().findFragmentByTag(PortfolioDetailGraphFragment.TAG);
            portfolioDetailTextFragment = (PortfolioDetailTextFragment) getSupportFragmentManager().findFragmentByTag(PortfolioDetailTextFragment.TAG);
        }



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
