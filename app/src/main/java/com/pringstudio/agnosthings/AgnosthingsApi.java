package com.pringstudio.agnosthings;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.pringstudio.agnosthings.model.Saklar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public AgnosthingsApi(Context context) {

        this.context = context;

        // Init Realm
        RealmConfiguration config = new RealmConfiguration.Builder(context)
                .name("agnosthings.realm")
                .schemaVersion(1)
                .build();

        realm = Realm.getInstance(config);
    }

    public void pushDataSaklar() {

        // Get All Saklar
        RealmResults<Saklar> saklars = realm.where(Saklar.class).findAll();
        // Get saklar agnosting api channel
        String saklar_channel = "http://agnosthings.com/6fb658cc-05fe-11e6-8001-005056805279/feed?push=";

        // Get saklar item
        List<String> saklar_item = new ArrayList<>();
        for (Saklar saklar : saklars) {
            saklar_item.add(saklar.getId() + "=" + saklar.getValue());
        }

        String saklarku = TextUtils.join(",", saklar_item);

        String api_url = saklar_channel + saklarku;
        Log.d("AgnosthingApi", "Saklarku : " + saklar_channel + saklarku);

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


}
