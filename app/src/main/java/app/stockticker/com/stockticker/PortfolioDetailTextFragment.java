package app.stockticker.com.stockticker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by nitinraj.arvind on 8/16/2015.
 */
public class PortfolioDetailTextFragment extends Fragment {

    public static final String TAG = "app.stockticker.com.stockticker.PortfolioDetailTextFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.portfolio_text_view, container, false);
        return rootView;
    }
}
