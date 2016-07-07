package io.lazluiz.challengesocialbasenyt.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.lazluiz.challengesocialbasenyt.model.NYTArticle;
import io.realm.Realm;

/**
 * Created by luiz on 06/07/16.
 * E-mail: lf.lazzarin@gmail.com
 * GitHub: github.com/luizfelippe
 */


public class FetchAPIData {

    private static final String LOG_TAG = FetchAPIData.class.getSimpleName();

    // I couldn't access the data from https://api.staging.onyo.com/v1/mobile/brand/1/company
    // so I put static JSON data at GitHub to simulate the API access.
    private static final String URL_DATA = "http://api.nytimes.com/svc/mostpopular/v2/mostviewed/arts/30.json?offset=40";

    // Time for not getting data
    private static final String SP_FETCH_TIME = LOG_TAG + ".FetchTime";
    private static final int TIME_OFFSET_IN_MS = (1000 * 60 * 60) * 3; // 3 hours

    private Context mContext;
    private NetworkQueue mNetworkQueue;

    public FetchAPIData(Context c) {
        mContext = c;
        mNetworkQueue = NetworkQueue.getInstance();
    }

    public void fetchData() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        long lastInsert = sp.getLong(SP_FETCH_TIME, 0);
        long diff = System.currentTimeMillis() - lastInsert;

        // Let's not update every time we launch our app
        if (diff > TIME_OFFSET_IN_MS) {
            mNetworkQueue.doGet(URL_DATA, LOG_TAG, new NetworkQueue.NetworkRequestCallback<JSONObject>() {
                @Override
                public void onRequestResponse(JSONObject response) {
                    persistData(response);
                }
                @Override
                public void onRequestError(Exception error) {
                    Log.e(LOG_TAG, error.getMessage());
                }
            });
        }
    }

    private void persistData(JSONObject data) {
        // API Serialize
        final String FIELD_ABSTRACT = "abstract";
        final String FIELD_ABSTRACT_SERIALIZED = "abstract_str";
        final String FIELD_METADATA = "media-metadata";
        final String FIELD_METADATA_SERIALIZED = "media_metadata";

        String dataStr = data.toString();
        dataStr = dataStr.replace("\"" + FIELD_ABSTRACT + "\":", "\"" + FIELD_ABSTRACT_SERIALIZED + "\":");
        dataStr = dataStr.replace("\"" + FIELD_METADATA + "\":", "\"" + FIELD_METADATA_SERIALIZED + "\":");

        try {
            final String NYT_ARTICLE = "results";
            final JSONObject serializedJSON = new JSONObject(dataStr);
            final JSONArray serializedArticles = serializedJSON.getJSONArray(NYT_ARTICLE);

            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.createOrUpdateAllFromJson(NYTArticle.class, serializedArticles);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}