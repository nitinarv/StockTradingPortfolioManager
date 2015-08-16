package app.stockticker.com.stockticker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.stockticker.com.stockticker.gson.History;
import app.stockticker.com.stockticker.yahoofinance.GetYahooFinanceDataTask;
import app.stockticker.com.stockticker.yahoofinance.OperationCallback;
import app.stockticker.com.stockticker.yahoofinance.ParsingHelper;
import app.stockticker.com.stockticker.yahoofinance.RequestUrlHelper;
import app.stockticker.com.stockticker.yahoofinance.ResponseDetails;


public class HomeActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private PortfolioFragment portfolioFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        portfolioFragment = (PortfolioFragment) getSupportFragmentManager().findFragmentByTag(PortfolioFragment.TAG_PORTFOLIO_FRAGMENT);

        frameLayout = (FrameLayout) findViewById(R.id.symbols_list_fragment);


        if (savedInstanceState == null) {
            if(portfolioFragment==null) {
                portfolioFragment = new PortfolioFragment();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.symbols_list_fragment, portfolioFragment, PortfolioFragment.TAG_PORTFOLIO_FRAGMENT)
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.bounce_interpolator)
                    .commit();
        }else{
            portfolioFragment = (PortfolioFragment) getSupportFragmentManager().findFragmentByTag(PortfolioFragment.TAG_PORTFOLIO_FRAGMENT);
        }

    }





}
