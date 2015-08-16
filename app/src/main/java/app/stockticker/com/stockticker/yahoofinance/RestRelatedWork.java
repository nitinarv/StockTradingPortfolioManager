package app.stockticker.com.stockticker.yahoofinance;

import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by nitinraj.arvind on 7/6/2015.
 * All methods in this class would run on the thread that calls it
 */
public class RestRelatedWork {

    /**The url given in the test*/
    private static RestRelatedWork instance = null;

    private RestRelatedWork() {

    }

    public static RestRelatedWork getInstance(){
        if(instance==null){
            synchronized (RestRelatedWork.class){
                instance = new RestRelatedWork();
            }
        }
        return instance;
    }


    /**
     * The url we have to get
     * */
    public ResponseDetails getWebPage(Context context, String url) throws ClientProtocolException, IOException{
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        // replace with your url
        ResponseDetails responseDetails = null;
        HttpResponse response;
        response = client.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = null;
        HttpEntity entity = response.getEntity();
        if(entity != null) {
            responseBody = EntityUtils.toString(entity);
        }


        return new ResponseDetails(statusCode, responseBody);
    }

}
