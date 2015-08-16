package app.stockticker.com.stockticker.yahoofinance;

import android.content.Context;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.stockticker.com.stockticker.R;
import app.stockticker.com.stockticker.model.SharedPreferenceHelper;

/**
 * Created by nitinraj.arvind on 8/14/2015.
 * Help construct all necessary URLS for Yahoo fi
 */
public class RequestUrlHelper {

    private static RequestUrlHelper instance;

    /**
     * QUERY 1
     * The information in the opening page will be obtained by executing the query below.
     * */
    // "select * from yahoo.finance.quotes where symbol in (\"YHOO\",\"AAPL\",\"GOOG\",\"MSFT\") ";
    // (\"YHOO\",\"AAPL\",\"GOOG\",\"MSFT\") ;
    private String OpeningPageQuery = "select * from yahoo.finance.quotes where symbol in ";


    /**
     * QUERY 2
     * The information in the Details page will be obtained by executing the query below
     * */
    //"select * from yahoo.finance.historicaldata where symbol = \"YHOO\" and startDate = \"2009-09-11\" and endDate = \"2010-03-10\""
    //"YHOO"
    private String DetailPageQuery1 = "select * from yahoo.finance.historicaldata where symbol = ";
    //"2009-09-11" -> "yyyy-MM-dd"
    private String DetailPageQuery2 = " and startDate = ";
    //"2010-03-10" -> "yyyy-MM-dd"
    private String DetailPageQuery3 = " and endDate = ";


    public static SimpleDateFormat dateFormatter = getSimpleDateFormatter("yyyy-MM-dd");


    private String main_url = "query.yahooapis.com";
    private String requested_format = "json";
    private String diagnostics = Boolean.toString(true);

    private String ajax_main_url = "autoc.finance.yahoo.com";
    private String ajax_callback_type = "YAHOO.Finance.SymbolSuggest.ssCallback";

    private String store_environment = "store://datatables.org/alltableswithkeys";
    private String http_environment = "http://datatables.org/alltables.env";
    private String environment = http_environment;


    public static RequestUrlHelper getInstance(){
        if(instance==null){
            synchronized (RequestUrlHelper.class) {
                instance = new RequestUrlHelper();
            }
        }
        return instance;
    }


    public String getOpeningPageRequest(Context context){
        Uri requestUri = getFinanceRequestUri(getOpeningPageYqlQuery(context));
        return requestUri.toString();
    }

    public String getSymbolDetailRequest(Context context, String symbolName, Date startTime, Date endTime){
        Uri requestUri = getFinanceRequestUri(getDetailPageYqlQuery(symbolName, startTime, endTime));
        return requestUri.toString();
    }

    /**
     * URL FOR AUTOCOMPLETE
     * */
//    http://autoc.finance.yahoo.com/autoc?query=yhoo&callback=YAHOO.Finance.SymbolSuggest.ssCallback
    public String getAutocompleteUri(Context context, String text){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority(ajax_main_url)
                .appendPath("autoc")
                .appendQueryParameter("query", text)
                .appendQueryParameter("callback", ajax_callback_type);


        Uri uri = builder.build();
        return uri.toString();
    }

    /**
     * Adds the given amount to a {@code Calendar} field. For a Date object
     * @param date
     *            the {@code Date} field to base on.
     * @param field
     *            the {@code Calendar} field to modify.
     * @param value
     *            the amount to add to the field.
     * @throws IllegalArgumentException
     *                if {@code field} is {@code DST_OFFSET} or {@code
     *                ZONE_OFFSET}.
     */
    public Date getDateTimeSetToObject(Date date, int field, int value){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, value);
        date = calendar.getTime();

        return date;
    }


    private String getDetailPageYqlQuery(String symbolName, Date startTime, Date endTime){


        String totalQuery = DetailPageQuery1 + "\"" +symbolName+ "\""
                + DetailPageQuery2 +"\"" +dateFormatter.format(startTime)+ "\""
                + DetailPageQuery3 +"\"" +dateFormatter.format(endTime)+ "\"";

        return totalQuery;
    }



    private String getOpeningPageYqlQuery(Context context){
        boolean isFirstRun = SharedPreferenceHelper.getInstance(context).isFirstRunComplete();
        List<Symbol> symbolList;
        List<String> symbolStringList;

        String[] observing_symbols_string_array = null;
        if(!isFirstRun){
            //On First run
            observing_symbols_string_array = context.getResources().getStringArray(R.array.first_symbols);
            SharedPreferenceHelper.getInstance(context).setStocksAtHome(Arrays.asList(observing_symbols_string_array));
        }

        //Check our own persistance, using Gson to reverse back
        List<String> stringPrefList = SharedPreferenceHelper.getInstance(context).getStocksAtHome();


        symbolList = asSymbolList(stringPrefList);

        String symbolParams = symbolList.toString().replace("[", "").replace("]", "");
        String totalQuery = OpeningPageQuery + "(" +symbolParams+ ")" ;
        return totalQuery;
    }


    private Uri getFinanceRequestUri(String yqlQuery){
        Uri.Builder builder = getBaseYqlUriStartingBuilder()
                .appendQueryParameter("q", yqlQuery);
        builder = setYqlUriEndBuilding(builder);
        return builder.build();
    }


    /**
     * Convert normal String array to a symbol list
     * */
    private List<Symbol> asSymbolList(String[] symbol_string_array){
        List<String> stringList = Arrays.asList(symbol_string_array);
        return asSymbolList(stringList);
    }

    private List<Symbol> asSymbolList(List<String> stringList){
        List<Symbol> symbolList = new ArrayList<Symbol>();
        if(stringList!=null){
            for(String item: stringList){
                symbolList.add(new Symbol(item));
            }
            return symbolList;
        }else{
            return null;
        }
    }


    public synchronized static SimpleDateFormat getSimpleDateFormatter(String format){
        if(dateFormatter==null){
            dateFormatter = new SimpleDateFormat(format);
        }
        return dateFormatter;
    }




    private Uri.Builder getBaseYqlUriStartingBuilder(){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority(main_url)
                .appendPath("v1")
                .appendPath("public")
                .appendPath("yql");

        return builder;
    }

    private Uri.Builder setYqlUriEndBuilding(Uri.Builder builder){
        builder.appendQueryParameter("format", requested_format)
                .appendQueryParameter("diagnostics", diagnostics)
                .appendQueryParameter("env", environment)
                .appendQueryParameter("callback", "");
        return builder;
    }


}
