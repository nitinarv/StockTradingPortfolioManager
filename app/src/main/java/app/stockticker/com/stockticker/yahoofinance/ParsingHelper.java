package app.stockticker.com.stockticker.yahoofinance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

import app.stockticker.com.stockticker.gson.History;
import app.stockticker.com.stockticker.gson.Stock;
import app.stockticker.com.stockticker.gson.Suggestion;

/**
 * Created by nitinraj.arvind on 8/15/2015.
 */
public class ParsingHelper {

    public static List<Stock> getStockList(String responseString){
        List<Stock> stockList = null;

        try {
            JSONObject jsonObject = new JSONObject(responseString);
            jsonObject = jsonObject.getJSONObject("query");
            jsonObject = jsonObject.getJSONObject("results");
            JSONArray jsonArray = jsonObject.getJSONArray("quote");
            Gson gson = new GsonBuilder().create();
            Type collectionType = new TypeToken<List<Stock>>() {}.getType();
            stockList = gson.fromJson(jsonArray.toString(), collectionType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stockList;
    }

    public static List<History> getHistoryList(String responseString){
        List<History> historyList = null;

        try {
            JSONObject jsonObject = new JSONObject(responseString);
            jsonObject = jsonObject.getJSONObject("query");
            jsonObject = jsonObject.getJSONObject("results");
            JSONArray jsonArray = jsonObject.getJSONArray("quote");
            Gson gson = new GsonBuilder().create();
            Type collectionType = new TypeToken<List<History>>() {}.getType();
            historyList = gson.fromJson(jsonArray.toString(), collectionType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return historyList;
    }


    public static List<Suggestion> getSuggestionList(String responseString){
        List<Suggestion> suggestionList = null;

        responseString = responseString.replace("YAHOO.Finance.SymbolSuggest.ssCallback","");
        responseString = responseString.substring(1,responseString.length());
        responseString = responseString.substring(0, responseString.length()-1);

        try {
            JSONObject jsonObject = new JSONObject(responseString);
            jsonObject = jsonObject.getJSONObject("ResultSet");
            JSONArray jsonArray = jsonObject.getJSONArray("Result");
            Gson gson = new GsonBuilder().create();
            Type collectionType = new TypeToken<List<Suggestion>>() {}.getType();
            suggestionList = gson.fromJson(jsonArray.toString(), collectionType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return suggestionList;
    }

}
