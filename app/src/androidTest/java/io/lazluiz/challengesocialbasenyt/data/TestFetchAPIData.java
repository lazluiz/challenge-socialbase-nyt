package io.lazluiz.challengesocialbasenyt.data;

import android.test.InstrumentationTestCase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.lazluiz.challengesocialbasenyt.model.NYTArticle;
import io.lazluiz.challengesocialbasenyt.model.NYTMedia;
import io.lazluiz.challengesocialbasenyt.model.NYTMediaMetadata;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by luiz on 06/07/16.
 * E-mail: lf.lazzarin@gmail.com
 * GitHub: github.com/luizfelippe
 */


public class TestFetchAPIData extends InstrumentationTestCase {

    private static final String LOG_TAG = TestFetchAPIData.class.getSimpleName();
    private static final String URL_API = "http://api.nytimes.com/svc/mostpopular/v2/mostviewed/arts/30.json?offset=40";


    public void testFetchData() throws Throwable {
        // Criaremos um sinal para avisar quando o teste for finalizado
        // já que o teste é assíncrono
        final CountDownLatch signal = new CountDownLatch(1);

        final NetworkQueue networkQueue = NetworkQueue.getInstance();

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                networkQueue.doGet(URL_API, LOG_TAG, new NetworkQueue.NetworkRequestCallback<JSONObject>() {
                    @Override
                    public void onRequestResponse(JSONObject response) {
                        assertNotNull("The API should give us at least a bracket", response);
                        try {
                            Log.i(LOG_TAG, "JSON coming!!!\n" + response.toString(2));
                            persistRealmData(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        signal.countDown();
                    }

                    @Override
                    public void onRequestError(Exception error) {
                        Log.e(LOG_TAG, "Error: " + error.getMessage());
                        signal.countDown();
                    }
                });
            }
        });

        // Timeout
        signal.await(15, TimeUnit.SECONDS);
    }

    private void persistRealmData(JSONObject data) {
        try {
            final String NYT_ARTICLE = "results";

            // That's some fine serialization right here
            String dataStr = data.toString();
            dataStr = dataStr.replace("\"abstract\":","\"abstract_str\":");
            dataStr = dataStr.replace("\"media-metadata\":","\"media_metadata\":");

            final JSONObject serializedJSON = new JSONObject(dataStr);
            final JSONArray serializedArticles = serializedJSON.getJSONArray(NYT_ARTICLE);

            Realm realm =Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                        // Empty database
                        realm.deleteAll();
                        RealmResults<NYTArticle> resultArticles = realm.where(NYTArticle.class).findAll();
                        assertFalse("This list should be empty", resultArticles.size() > 0);

                        // Persist
                        realm.createOrUpdateAllFromJson(NYTArticle.class, serializedArticles);
                        testPersistedData(realm);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void testPersistedData(Realm realm) throws InterruptedException {
        RealmResults<NYTArticle> nytArticles = realm.where(NYTArticle.class).findAll();
        assertTrue("Now this list should contain something!", nytArticles.size() > 0);

        for(NYTArticle article : nytArticles){
            assertNotNull(article.getAdx_keywords());
            assertNotNull(article.getByline());
            assertNotNull(article.getId());
            assertNotNull(article.getPublished_date());
            assertNotNull(article.getSection());
            assertNotNull(article.getTitle());
            assertNotNull(article.getUrl());
            assertNotNull(article.getViews());
            assertNotNull(article.getAbstract_str());
            assertNotNull(article.getMedia());
            assertTrue("It should contain some media", article.getMedia().size() > 0);

            for(NYTMedia media : article.getMedia()){
                assertNotNull(media.getCaption());
                assertNotNull(media.getCopyright());
                assertNotNull(media.getMedia_metadata());
                assertTrue("It should contain meta-data", media.getMedia_metadata().size() > 0);

                for(NYTMediaMetadata mediaMetadata : media.getMedia_metadata()){
                    assertNotNull(mediaMetadata.getUrl());
                    assertNotNull(mediaMetadata.getFormat());
                    assertNotNull(mediaMetadata.getHeight());
                    assertNotNull(mediaMetadata.getWidth());
                }
            }
        }
    }
}
