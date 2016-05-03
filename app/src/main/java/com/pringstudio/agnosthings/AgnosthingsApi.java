package com.pringstudio.agnosthings;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.pringstudio.agnosthings.model.Saklar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by sucipto on 5/1/16.
 */
public class AgnosthingsApi {

    Realm realm;
    Context context;

    // HTTP CLient
    AsyncHttpClient httpClient = new AsyncHttpClient();

    // Get saklar agnosting api channel
    String saklar_channel = "http://agnosthings.com/6fb658cc-05fe-11e6-8001-005056805279/";

    // Listenner
    private SaklarValueUpdateListener saklarValueUpdateListener;
    private SaklarHistoryLoadedListener saklarHistoryLoadedListener;

    // Saklar value proccessed data
    int processedData = 0;

    public AgnosthingsApi(Context context) {

        this.context = context;

        saklarValueUpdateListener = null;

        // Init Realm
        RealmConfiguration config = new RealmConfiguration.Builder(context)
                .name("agnosthings.realm")
                .schemaVersion(1)
                .build();

        realm = Realm.getInstance(config);
    }

    // Send data to agnosthing api server
    public void pushDataSaklar() {

        Log.d("pushDataSaklar()","Fire..!!");

        // Get All Saklar
        RealmResults<Saklar> saklars = realm.where(Saklar.class).findAll();


        // Get saklar item
        List<String> saklar_item = new ArrayList<>();
        for (Saklar saklar : saklars) {
            saklar_item.add(saklar.getId() + "=" + saklar.getValue());
        }

        String saklarku = TextUtils.join(",", saklar_item);

        String api_url = saklar_channel +"feed?push="+ saklarku;
        Log.d("AgnosthingApi", "Saklarku : " + api_url);

        // Cancel all pending request
        httpClient.cancelAllRequests(true);
        httpClient.cancelRequests(context,true);

        // Call new api request
        httpClient.get(context, api_url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);

                try {
                    if (response.getString("code").equals("200") && response.getString("value").equals("Data has been successfully added")) {
                        Toast.makeText(context, "Data updated ", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Failed to Update data", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "Api Cal Failed", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(context, "API Call Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(context, "API Call Failed", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }

            @Override
            public void onCancel() {
                Log.d("AgnosthingApi","Request Canceled");
            }
        });

    }

    // Retreive Data from api server
    public void retreiveSaklarValue(){
        // Get All Saklar
        final RealmResults<Saklar> saklars = realm.where(Saklar.class).findAll();



        for (final Saklar saklar : saklars){
            final String saklar_url = saklar_channel+"field/last/feed/174/"+saklar.getId();
            Log.d("Saklar URL",saklar_url);

            httpClient.setTimeout(30);
            httpClient.get(context,saklar_url, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //super.onSuccess(statusCode, headers, response);
                    try {
                        int oldValue = saklar.getValue();
                        int newValue = response.getInt("value");
                        realm.beginTransaction();
                        saklar.setValue(newValue);
                        realm.commitTransaction();

                        processedData++;

                        if(saklars.size() == processedData){
                            if(saklarValueUpdateListener != null){
                                // Send Signal to listenner
                                saklarValueUpdateListener.onValueLoaded();
                            }
                        }

                        Log.d("Update Skalar Value","UPdating saklar value "+saklar.getId()+" From: "+oldValue+" to "+newValue);
                    }catch (Exception e){
                        Toast.makeText(context,"Get data "+saklar.getId()+" fail!",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    //super.onFailure(statusCode, headers, responseString, throwable);
                    if(saklarValueUpdateListener != null){
                        // Send Signal to listenner
                        saklarValueUpdateListener.onFail();
                    }
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    if(saklarValueUpdateListener != null){
                        // Send Signal to listenner
                        saklarValueUpdateListener.onFail();
                    }
                    throwable.printStackTrace();
                }
            });


        }
    }

    // Retreive Saklar History
    public void getSaklarHistory(String id){

        final List<Map<String,String>> historyList = new ArrayList<>();


        httpClient.setTimeout(30);
        httpClient.cancelRequests(context,true);
        httpClient.cancelAllRequests(true);

        String api_url = saklar_channel+"field/week/feed/174/"+id;

        httpClient.get(context,api_url, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(statusCode == 200){
                    try {
                        JSONArray dataList = response.getJSONArray("cValue");
                        for(int x = 0; x < dataList.length(); x++){
                            String data = dataList.get(x).toString();
                            String value = data.split(",")[0];
                            String date = data.split(",")[1];

                            Map<String,String> history = new HashMap<>();
                            history.put("value",value);
                            history.put("date",date);
                            historyList.add(history);
                            Log.d("API",history.toString());
                        }

                        if(saklarHistoryLoadedListener != null){
                            saklarHistoryLoadedListener.onDataLoaded(historyList);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

        });

    }

    // Listener for Saklar update value
    public interface SaklarValueUpdateListener{
        public void onValueLoaded();
        public void onFail();
    }

    public interface SaklarHistoryLoadedListener{
        public void onDataLoaded(List<Map<String,String>> data);
    }

    public void setSaklarHistoryLoadedListener(SaklarHistoryLoadedListener listener){
        this.saklarHistoryLoadedListener = listener;
    }

    public void setSaklarValueUpdateListener(SaklarValueUpdateListener listener){
        this.saklarValueUpdateListener = listener;
    }



}
