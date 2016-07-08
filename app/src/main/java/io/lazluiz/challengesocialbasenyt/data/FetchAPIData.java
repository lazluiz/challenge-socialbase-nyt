package io.lazluiz.challengesocialbasenyt.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Window;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.lazluiz.challengesocialbasenyt.R;
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
    private static final String URL_DATA = "http://api.nytimes.com/svc/mostpopular/v2/mostviewed/%s/30.json?offset=40";

    // Time for not getting data
    private static final String SP_FETCH_TIME = LOG_TAG + ".FetchTime";
    private static final int TIME_OFFSET_IN_MS = (1000 * 60 * 60) * 1; // 1 hour

    private Context mContext;
    private NetworkQueue mNetworkQueue;
    private String[] mSections;

    public FetchAPIData(Context c, @NonNull String[] sections) {
        mContext = c;
        mNetworkQueue = NetworkQueue.getInstance();
        mSections = sections;
    }

    public void fetchData() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        long lastInsert = sp.getLong(SP_FETCH_TIME, 0);
        long diff = System.currentTimeMillis() - lastInsert;

        // Let's not update every time we launch our app
        if (diff > TIME_OFFSET_IN_MS) {
            final ProgressDialog loading = new ProgressDialog(mContext);
            loading.setCancelable(true);
            loading.setMessage(mContext.getString(R.string.message_fetching_data));
            loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loading.show();

            for (String section : mSections) {
                String url = String.format(URL_DATA, section);
                mNetworkQueue.doGet(url, LOG_TAG, new NetworkQueue.NetworkRequestCallback<JSONObject>() {
                    @Override
                    public void onRequestResponse(JSONObject response) {
                        persistData(response);
                        loading.dismiss();
                    }

                    @Override
                    public void onRequestError(Exception error) {
                        Log.e(LOG_TAG, error.getMessage());
                        loading.dismiss();
                    }
                });
            }
        }
    }

    private void persistData(JSONObject data) {
        final String FIELD_ABSTRACT = "abstract";
        final String FIELD_ABSTRACT_SERIALIZED = "abstract_str";
        final String FIELD_METADATA = "media-metadata";
        final String FIELD_METADATA_SERIALIZED = "media_metadata";
        final String FIELD_MEDIA = "media";

        String dataStr = data.toString();
        dataStr = serializeField(dataStr, FIELD_ABSTRACT, FIELD_ABSTRACT_SERIALIZED);
        dataStr = serializeField(dataStr, FIELD_METADATA, FIELD_METADATA_SERIALIZED);

        dataStr = serializeEmptyArrayField(dataStr, FIELD_MEDIA);
        dataStr = serializeEmptyArrayField(dataStr, FIELD_METADATA_SERIALIZED);

        try {
            final String FIELD_NYT_ARTICLE = "results";
            final JSONObject serializedJSON = new JSONObject(dataStr);
            final JSONArray serializedArticles = serializedJSON.getJSONArray(FIELD_NYT_ARTICLE);

            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Persist
                    realm.createOrUpdateAllFromJson(NYTArticle.class, serializedArticles);

                    // Save time
                    SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
                    spe.putLong(SP_FETCH_TIME, System.currentTimeMillis());
                    spe.apply();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // We need to serialize those fields whose names are equal to Java keywords, such as "abstract".
    private String serializeField(String str, String field, String srlField){
        return str.replace("\"" + field + "\":", "\"" + srlField + "\":");
    }

    // The NYT API returns "" for empty lists instead of [], and that would
    // throw an error on JSON parsing.
    private String serializeEmptyArrayField(String str, String field){
        return str.replace("\"" + field + "\":\"\"", "\"" + field + "\":[]");
    }
}