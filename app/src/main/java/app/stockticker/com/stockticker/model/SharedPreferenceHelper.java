package app.stockticker.com.stockticker.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import app.stockticker.com.stockticker.yahoofinance.RequestUrlHelper;

/**
 * Created by nitinraj.arvind on 8/15/2015.
 */
public class SharedPreferenceHelper {

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String APP_SETTINGS = "APP_SETTINGS";

    private String STOCK_HOME_SET = "STOCK_HOME_SET";
    private String FIRST_RUN_COMPLETE = "FIRST_RUN_COMPLETE";
    private String LAST_FETCHED_TIME = "LAST_FETCHED_TIME";
    private String FIRST_TIME_GRIDVIEW_SETUP = "FIRST_TIME_GRIDVIEW_SETUP";
    private String FIRST_TIME_VIEWING_SWIPELAYOUT = "FIRST_TIME_VIEWING_SWIPELAYOUT";

    private static SharedPreferenceHelper instance = null;

    private SharedPreferenceHelper(Context mContext){
        context = mContext;
        sharedPreferences = context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPreferenceHelper getInstance(Context context){
        if(instance==null){
            synchronized (SharedPreferenceHelper.class){
                instance = new SharedPreferenceHelper(context);
            }
        }
        return instance;
    }


    public boolean firstTimeViewingSwipeLayout() {
        boolean firstTime = sharedPreferences.getBoolean(FIRST_TIME_VIEWING_SWIPELAYOUT, true);
        editor.putBoolean(FIRST_TIME_VIEWING_SWIPELAYOUT, false).apply();
        return firstTime;
    }

    public boolean isFirstTimeGridViewSetup(){
        boolean firstTime = sharedPreferences.getBoolean(FIRST_TIME_GRIDVIEW_SETUP, true);
        editor.putBoolean(FIRST_TIME_VIEWING_SWIPELAYOUT, false).apply();
        return firstTime;
    }


    public boolean isFirstRunComplete(){
        boolean isFirstRunCompleteStatus = false;
        isFirstRunCompleteStatus = sharedPreferences.getBoolean(FIRST_RUN_COMPLETE, false);
        editor.putBoolean(FIRST_RUN_COMPLETE, true).apply();
        return isFirstRunCompleteStatus;
    }

    public List<String> getStocksAtHome(){
        String stocksGson = sharedPreferences.getString(STOCK_HOME_SET, "");
        Gson gson = new GsonBuilder().create();
        Type collectionType = new TypeToken<List<String>>() {}.getType();
        List<String> stockList = gson.fromJson(stocksGson, collectionType);
        if(stockList==null)
            stockList = new ArrayList<String>();
        return stockList;
    }

    public void setLastFetchedTime(){
        editor.putLong(LAST_FETCHED_TIME,(new Date()).getTime());
        editor.commit();
    }

    public String getLastFetchedTime(){
        long time = sharedPreferences.getLong(LAST_FETCHED_TIME,0L);
        if(time==0L){
            return "-NA-";
        }else{
            Date lTime = new Date(time);
            String sTime = (new SimpleDateFormat("hh:mm")).format(lTime);
            return sTime;
        }
    }

    public void addStockAtHome(String stock){
        List<String> stockList = getStocksAtHome();
        if(!stockList.contains(stock)){
            stockList.add(stock);
        }
        setStocksAtHome(stockList);
    }

    public void removeStockAtHome(String stock){
        List<String> stockList = getStocksAtHome();
        stockList.remove(stock);
        setStocksAtHomeForce(stockList);
    }

    public void setStocksAtHome(List<String> stockList){

        //To prevent bulk duplication
        HashSet<String> hashSet = new HashSet<String>(stockList);
        List<String> stocksAtHome = getStocksAtHome();
        hashSet.addAll(stocksAtHome);

        //To proceed with easy storage
        stockList = new ArrayList<String>(hashSet);

        Gson gson = new GsonBuilder().create();
        String gsonStockList = gson.toJson(stockList);
        editor.putString(STOCK_HOME_SET, gsonStockList);
        editor.commit();
    }

    public void setStocksAtHomeForce(List<String> stockList){

        Gson gson = new GsonBuilder().create();
        String gsonStockList = gson.toJson(stockList);
        editor.putString(STOCK_HOME_SET, gsonStockList);
        editor.commit();
    }

}
